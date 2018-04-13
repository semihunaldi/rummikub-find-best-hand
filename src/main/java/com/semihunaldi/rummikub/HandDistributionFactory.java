package com.semihunaldi.rummikub;

/**
 * Created by semihunaldi on 13.04.2018
 */

@SuppressWarnings({"unused", "WeakerAccess"})
public class HandDistributionFactory {

	private int maxNum = 13;
	private int minNum = 1;

	private int totalTileCountWithoutFakes = 104;
	private int numberOfCopiesOfTiles = 2;

	public HandDistribution distributeHand() {
		return new HandDistribution(maxNum, minNum, totalTileCountWithoutFakes, numberOfCopiesOfTiles);
	}

	public int getMaxNum() {
		return maxNum;
	}

	public void setMaxNum(int maxNum) {
		this.maxNum = maxNum;
	}

	public int getMinNum() {
		return minNum;
	}

	public void setMinNum(int minNum) {
		this.minNum = minNum;
	}

	public int getTotalTileCountWithoutFakes() {
		return totalTileCountWithoutFakes;
	}

	public void setTotalTileCountWithoutFakes(int totalTileCountWithoutFakes) {
		this.totalTileCountWithoutFakes = totalTileCountWithoutFakes;
	}

	public int getNumberOfCopiesOfTiles() {
		return numberOfCopiesOfTiles;
	}

	public void setNumberOfCopiesOfTiles(int numberOfCopiesOfTiles) {
		this.numberOfCopiesOfTiles = numberOfCopiesOfTiles;
	}
}
