package com.semihunaldi.rummikub;

import org.assertj.core.api.Assertions;
import org.junit.Test;

/**
 * Created by semihunaldi on 13.04.2018
 */
public class BestHandFinderTest {

	private BestHandFinder bestHandFinder = new BestHandFinder();
	@Test
	public void testFindBestHand() {
		HandDistributionFactory handDistributionFactory = new HandDistributionFactory();
		HandDistribution handDistribution = handDistributionFactory.distributeHand();
		Player bestHand = bestHandFinder.findBestHand(handDistribution);
		Assertions.assertThat(bestHand).isNotNull();
	}
}
