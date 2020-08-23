package io.github.iridis.internal.merge;

public interface IAnvilScreenHandlerAttach {
	/**
	 * @return the number of the items the anvil will take from the second slot when the output is taken
	 */
	int getRepairItemUsage();

	/**
	 * @param repairItemUsage the number of the items the anvil should take from the second slot when the output is taken
	 */
	void setRepairItemUsage(int repairItemUsage);

	/**
	 * @return the string in the text box of the anvil
	 */
	String getItemNameText();

	/**
	 * @param text the new string in the text box of the anvil
	 */
	void setItemNameText(String text);

	/**
	 * @return the amount of levels the anvil recipe will cost
	 */
	int getLevelCost();

	/**
	 * @param cost the amount of levels the anvil recipe should cost
	 */
	void setLevelCost(int cost);
}