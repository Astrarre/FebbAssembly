import abstractor.MemberAbstractionType;
import metautils.api.ClassApi;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.regex.Pattern;

public class JAbstractionMetadata {
    private static final List<Predicate<String>> INTERFACE_REGEX = new ArrayList<>(), BASE_REGEX = new ArrayList<>();
    public static void populate(Path interfaces, Path bases) {
        try {
            Files.lines(interfaces).map(Pattern::compile).map(Pattern::asPredicate).forEach(INTERFACE_REGEX::add);
            Files.lines(bases).map(Pattern::compile).map(Pattern::asPredicate).forEach(BASE_REGEX::add);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static MemberAbstractionType methods(ClassApi api, ClassApi.Method method) {
        return abstraction(api.getName() + ";" + method);
    }

    public static MemberAbstractionType fields(ClassApi api, ClassApi.Field field) {
        return abstraction(api.getName() + ";" + field);
    }

    private static MemberAbstractionType abstraction(String desc) {
        boolean i = INTERFACE_REGEX.stream().noneMatch(p -> p.test(desc)), b = BASE_REGEX.stream().noneMatch(p -> p.test(desc));
        if(i & b) {
            return MemberAbstractionType.BaseclassAndInterface;
        } else if(i) {
            return MemberAbstractionType.Interface;
        } else if(b) {
            return MemberAbstractionType.Baseclass;
        } else {
            return MemberAbstractionType.None;
        }
    }
}
