import org.gradle.api.logging.Logger
import java.io.File
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL
import java.nio.charset.StandardCharsets

object DownloadUtil {
    /**
     * Download from the given [URL] to the given [File] so long as there are differences between them.
     *
     * @param from The URL of the file to be downloaded
     * @param to The destination to be saved to, and compared against if it exists
     * @param logger The logger to print information to, typically from [Project.getLogger]
     * @param quiet Whether to only print warnings (when `true`) or everything
     * @throws IOException If an exception occurs during the process
     */
    /**
     * Download from the given [URL] to the given [File] so long as there are differences between them.
     *
     * @param from The URL of the file to be downloaded
     * @param to The destination to be saved to, and compared against if it exists
     * @param logger The logger to print everything to, typically from [Project.getLogger]
     * @throws IOException If an exception occurs during the process
     */
    @JvmOverloads
    @Throws(IOException::class)
    fun downloadIfChanged(
        from: URL,
        to: File,
        logger: Logger,
        quiet: Boolean = false
    ) {
        val connection = from.openConnection() as HttpURLConnection

        //If the output already exists we'll use it's last modified time
        if (to.exists()) {
            connection.ifModifiedSince = to.lastModified()
        }

        //Try use the ETag if there's one for the file we're downloading
        val etag = loadETag(to, logger)
        if (etag != null) {
            connection.setRequestProperty("If-None-Match", etag)
        }

        //We want to download gzip compressed stuff
        connection.setRequestProperty("Accept-Encoding", "gzip")

        //We shouldn't need to set a user agent, but it's here just in case
        //connection.setRequestProperty("User-Agent", null);

        //Try make the connection, it will hang here if the connection is bad
        connection.connect()
        val code = connection.responseCode
        if ((code < 200 || code > 299) && code != HttpURLConnection.HTTP_NOT_MODIFIED) {
            //Didn't get what we expected
            throw IOException(connection.responseMessage + " for " + from)
        }
        val modifyTime = connection.getHeaderFieldDate("Last-Modified", -1)
        if (to.exists() && (code == HttpURLConnection.HTTP_NOT_MODIFIED || modifyTime > 0 && to.lastModified() >= modifyTime)) {
            if (!quiet) {
                logger.info("'{}' Not Modified, skipping.", to)
            }
            return  //What we've got is already fine
        }
        val contentLength = connection.contentLengthLong
        if (!quiet && contentLength >= 0) {
            logger.info("'{}' Changed, downloading {}", to, toNiceSize(contentLength))
        }
        try { //Try download to the output
           to.writeBytes(connection.inputStream.readBytes())
        } catch (e: IOException) {
            to.delete() //Probably isn't good if it fails to copy/save
            throw e
        }

        //Set the modify time to match the server's (if we know it)
        if (modifyTime > 0) {
            to.setLastModified(modifyTime)
        }

        //Save the ETag (if we know it)
        val eTag = connection.getHeaderField("ETag")
        if (eTag != null) {
            //Log if we get a weak ETag and we're not on quiet
            if (!quiet && eTag.startsWith("W/")) {
                logger.warn("Weak ETag found.")
            }
            saveETag(to, eTag, logger)
        }
    }

    /**
     * Creates a new file in the same directory as the given file with `.etag` on the end of the name.
     *
     * @param file The file to produce the ETag for
     * @return The (uncreated) ETag file for the given file
     */
    private fun getETagFile(file: File): File {
        return File(file.absoluteFile.parentFile, file.name + ".etag")
    }

    /**
     * Attempt to load an ETag for the given file, if it exists.
     *
     * @param to The file to load an ETag for
     * @param logger The logger to print errors to if it goes wrong
     * @return The ETag for the given file, or `null` if it doesn't exist
     */
    private fun loadETag(to: File, logger: Logger): String? {
        val eTagFile = getETagFile(to)
        return if (!eTagFile.exists()) {
            null
        } else try {
            eTagFile.readText()
        } catch (e: IOException) {
            logger.warn("Error reading ETag file '{}'.", eTagFile)
            null
        }
    }

    /**
     * Saves the given ETag for the given file, replacing it if it already exists.
     *
     * @param to The file to save the ETag for
     * @param eTag The ETag to be saved
     * @param logger The logger to print errors to if it goes wrong
     */
    private fun saveETag(to: File, eTag: String, logger: Logger) {
        val eTagFile = getETagFile(to)
        try {
            if (!eTagFile.exists()) {
                eTagFile.createNewFile()
            }
            eTagFile.writeText(eTag)
        } catch (e: IOException) {
            logger.warn("Error saving ETag file '{}'.", eTagFile, e)
        }
    }

    /**
     * Format the given number of bytes as a more human readable string.
     *
     * @param bytes The number of bytes
     * @return The given number of bytes formatted to kilobytes, megabytes or gigabytes if appropriate
     */
    private fun toNiceSize(bytes: Long): String {
        return if (bytes < 1024) {
            "$bytes B"
        } else if (bytes < 1024 * 1024) {
            (bytes / 1024).toString() + " KB"
        } else if (bytes < 1024 * 1024 * 1024) {
            String.format("%.2f MB", bytes / (1024.0 * 1024.0))
        } else {
            String.format("%.2f GB", bytes / (1024.0 * 1024.0 * 1024.0))
        }
    }

    /**
     * Delete the file along with the corresponding ETag, if it exists.
     *
     * @param file The file to delete.
     */
    fun delete(file: File) {
        if (file.exists()) {
            file.delete()
        }
        val etagFile = getETagFile(file)
        if (etagFile.exists()) {
            etagFile.delete()
        }
    }
}