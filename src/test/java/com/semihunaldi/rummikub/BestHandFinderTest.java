package com.semihunaldi.rummikub;

import org.assertj.core.api.Assertions;
import org.junit.Test;

/**
 * Created by semihunaldi on 13.04.2018
 */
public class BestHandFinderTest {

	@Test
	public void testFindBestHand() {
		HandDistributionFactory handDistributionFactory = new HandDistributionFactory();
		HandDistribution handDistribution = handDistributionFactory.distributeHand();
		Player bestHand = BestHandFinder.findBestHand(handDistribution);
		Assertions.assertThat(bestHand).isNull(); //TODO
	}
}
