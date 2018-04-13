package com.semihunaldi.rummikub;

import lombok.Data;

/**
 * Created by semihunaldi on 13.04.2018
 */

@SuppressWarnings({"unused", "WeakerAccess"})
@Data
public class HandDistributionFactory {

	private int maxNum = 13;
	private int minNum = 1;

	private int totalTileCountWithoutFakes = 104;
	private int numberOfCopiesOfTiles = 2;

	public HandDistribution distributeHand() {
		return new HandDistribution(maxNum, minNum, totalTileCountWithoutFakes, numberOfCopiesOfTiles);
	}
}
