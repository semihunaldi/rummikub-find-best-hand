package com.semihunaldi.rummikub;


/**
 * Created by semihunaldi on 13.04.2018
 */
public class HandDistributionFactory {

	private int maxNum = 13;
	private int minNum = 1;

	private int totalTileCount = 106;
	private int numberOfCopiesOfTiles = 2;
	private int numberOfFakes = 2;

	public HandDistribution distributeHand() {
		return new HandDistribution(maxNum,minNum,totalTileCount,numberOfCopiesOfTiles,numberOfFakes);
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

	public int getTotalTileCount() {
		return totalTileCount;
	}

	public void setTotalTileCount(int totalTileCount) {
		this.totalTileCount = totalTileCount;
	}

	public int getNumberOfCopiesOfTiles() {
		return numberOfCopiesOfTiles;
	}

	public void setNumberOfCopiesOfTiles(int numberOfCopiesOfTiles) {
		this.numberOfCopiesOfTiles = numberOfCopiesOfTiles;
	}

	public int getNumberOfFakes() {
		return numberOfFakes;
	}

	public void setNumberOfFakes(int numberOfFakes) {
		this.numberOfFakes = numberOfFakes;
	}
}
