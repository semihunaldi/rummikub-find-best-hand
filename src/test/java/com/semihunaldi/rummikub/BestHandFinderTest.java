package com.semihunaldi.rummikub;

import com.google.common.collect.Lists;
import com.semihunaldi.rummikub.model.Player;
import com.semihunaldi.rummikub.model.Tile;
import com.semihunaldi.rummikub.model.TileColor;
import org.assertj.core.api.Assertions;
import org.junit.Test;

import java.util.List;

/**
 * Created by semihunaldi on 13.04.2018
 */
public class BestHandFinderTest {

	private final BestHandFinder bestHandFinder = new BestHandFinder();

	@Test
	public void testFindBestHand() {
		System.out.println("\n\n======================BEST HAND FINDING STARTED======================");
		HandDistributionFactory handDistributionFactory = new HandDistributionFactory();
		HandDistribution handDistribution = handDistributionFactory.distributeHand();
		Player bestHand = bestHandFinder.findBestHand(handDistribution);
		Assertions.assertThat(bestHand).isNotNull();
		System.out.println("======================BEST HAND FINDING FINISHED======================\n\n");
	}

	@Test
	public void testRandom() {
		for(int i = 0; i < 100_000; i++){
			int randomNumberInRange = Util.getRandomNumberInRange(50, 100);
			Assertions.assertThat(randomNumberInRange).isGreaterThanOrEqualTo(50).isLessThanOrEqualTo(100);
		}
	}

	@Test
	public void testFindBestHandWithPreBuiltHand1() {
		List<Tile> handForTest1 = createHandForTest("[yellow-7, blue-1, red-3, red-9, blue-7, yellow-10, red-7-FAKE, blue-7, red-13, black-2, yellow-12, red-2, blue-2, red-3]");
		List<Tile> handForTest2 = createHandForTest("[red-1, yellow-5, black-2, blue-3, black-6, yellow-13, blue-6, blue-8, red-10, yellow-13, yellow-2, red-4, yellow-1, yellow-4, black-8]");
		List<Tile> handForTest3 = createHandForTest("[red-12, black-5, black-13, blue-11, black-9, black-4, yellow-8, yellow-2, blue-2, red-6, red-13, red-11, blue-10, red-8]");
		List<Tile> handForTest4 = createHandForTest("[black-12, blue-9, blue-6, blue-4, black-3, blue-4, red-2, yellow-7, yellow-6, red-6, black-10, red-1, black-11, yellow-5]");
		preBuiltHandTest(1, handForTest1, handForTest2, handForTest3, handForTest4);
	}

	@Test
	public void testFindBestHandWithPreBuiltHand2() {
		List<Tile> handForTest1 = createHandForTest("[blue-6, black-4, yellow-9, black-2, blue-2, black-3, yellow-3, blue-6, red-10, red-4, yellow-1, black-13, red-10, red-2]");
		List<Tile> handForTest2 = createHandForTest("[yellow-2, blue-9, red-12, black-6, blue-13, blue-7, blue-3, blue-2, black-4, black-10, black-5, black-12-JOKER, black-8, red-7]");
		List<Tile> handForTest3 = createHandForTest("[blue-8, blue-5, red-1, black-1, red-5, blue-3, blue-5, black-6, yellow-7, blue-11, red-6, yellow-9, red-12, black-1, red-2]");
		List<Tile> handForTest4 = createHandForTest("[yellow-13, blue-4, red-9, red-8, blue-10, red-3, black-9, red-9, black-8, blue-12, yellow-4, yellow-12, black-11, black-7]");
		preBuiltHandTest(2, handForTest1, handForTest2, handForTest3, handForTest4);
	}

	@Test
	public void testFindBestHandWithPreBuiltHand3() {
		List<Tile> handForTest1 = createHandForTest("[yellow-2, black-12, black-13, yellow-11, black-3, yellow-1, blue-7, red-4, black-4, yellow-7, blue-11, black-5, blue-11, blue-12]");
		List<Tile> handForTest2 = createHandForTest("[black-13, black-11, yellow-2, black-10, red-11, yellow-7, yellow-6, red-12, black-9, red-9, blue-1, yellow-4, red-10, blue-5]");
		List<Tile> handForTest3 = createHandForTest("[blue-2, yellow-3, yellow-10, black-6, red-8, yellow-4, red-6, yellow-13, blue-8-JOKER, yellow-9, red-8, blue-3, yellow-3, yellow-5]");
		List<Tile> handForTest4 = createHandForTest("[yellow-12, blue-4, red-9, black-2, blue-1, red-13, blue-13, black-7, red-7, red-3, red-12, black-4, red-10, red-1, red-6]");
		preBuiltHandTest(2, handForTest1, handForTest2, handForTest3, handForTest4);
	}

	@Test
	public void testFindBestHandWithPreBuiltHand4() {
		List<Tile> handForTest1 = createHandForTest("[black-13, black-3, red-5, blue-11, yellow-9, yellow-3, red-11, black-4, red-8, red-12, black-11, black-3, blue-8, black-10]");
		List<Tile> handForTest2 = createHandForTest("[blue-12, blue-2, yellow-5, blue-6, yellow-6, red-2, yellow-2, yellow-4, red-7, blue-10, black-13, black-2, yellow-2, yellow-8]");
		List<Tile> handForTest3 = createHandForTest("[blue-3, yellow-12, black-2, red-8, black-11, red-10, red-13, red-3, red-1, black-7, blue-10, blue-4, yellow-8, red-9]");
		List<Tile> handForTest4 = createHandForTest("[blue-3, yellow-7, black-12, blue-9, yellow-1, blue-2, black-7, red-3, black-5-JOKER, red-6, red-9, yellow-10, black-5-JOKER, yellow-13, blue-4]");
		preBuiltHandTest(2, handForTest1, handForTest2, handForTest3, handForTest4);
	}

	@Test
	public void testFindBestHandWithPreBuiltHand5() {
		List<Tile> handForTest1 = createHandForTest("[black-1, black-2, black-3, red-11, red-12, red-13, yellow-11, yellow-4, yellow-8, yellow-12, blue-11, black-3, blue-8, black-10]");
		List<Tile> handForTest2 = createHandForTest("[blue-12, blue-2, yellow-5, blue-6, yellow-6, red-2, yellow-2, yellow-4, red-7, blue-10, black-13, black-2, yellow-2, yellow-8]");
		List<Tile> handForTest3 = createHandForTest("[blue-3, yellow-12, black-2, red-8, black-11, red-10, red-13, red-3, red-1, black-7, blue-10, blue-4, yellow-8, red-9]");
		List<Tile> handForTest4 = createHandForTest("[blue-3, yellow-7, black-12, blue-9, yellow-1, blue-2, black-7, red-3, black-5-JOKER, red-6, red-9, yellow-10, black-5-JOKER, yellow-13, blue-4]");
		preBuiltHandTest(2, handForTest1, handForTest2, handForTest3, handForTest4);
	}

	private void preBuiltHandTest(int bestHandNum, List<Tile> handForTest1, List<Tile> handForTest2, List<Tile> handForTest3, List<Tile> handForTest4) {
		System.out.println("\n\n======================BEST HAND FINDING STARTED======================");
		HandDistributionFactory handDistributionFactory = new HandDistributionFactory();
		HandDistribution handDistribution = handDistributionFactory.distributeHand();
		handDistribution.getPlayer1().setHand(handForTest1);
		handDistribution.getPlayer2().setHand(handForTest2);
		handDistribution.getPlayer3().setHand(handForTest3);
		handDistribution.getPlayer4().setHand(handForTest4);
		Player bestHand = bestHandFinder.findBestHand(handDistribution);
		Assertions.assertThat(bestHand.getPlayerNumber()).isEqualTo(bestHandNum);
		System.out.println("======================BEST HAND FINDING FINISHED======================\n\n");
	}

	private List<Tile> createHandForTest(String hand) {
		List<Tile> handTile = Lists.newArrayList();
		hand = hand.replace(" ", "");
		hand = hand.replace("[", "");
		hand = hand.replace("]", "");
		String[] tiles = hand.split(",");
		for(String tile : tiles){
			Tile testTile = new Tile();
			String[] split = tile.split("-");
			String color = split[0];
			String number = split[1];
			String fakeOrJoker = "";
			if(split.length == 3){
				fakeOrJoker = split[2];
			}
			testTile.setTileColor(TileColor.valueOf(color.toUpperCase()));
			testTile.setNumber(Integer.valueOf(number));
			if(fakeOrJoker.contains("JOKER")){
				testTile.setJoker(true);
			}
			if(fakeOrJoker.contains("FAKE")){
				testTile.setFake(true);
			}
			handTile.add(testTile);
		}
		return handTile;
	}
}
