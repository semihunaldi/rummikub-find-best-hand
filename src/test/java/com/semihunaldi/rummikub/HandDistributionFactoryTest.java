package com.semihunaldi.rummikub;

import com.google.common.collect.Lists;
import com.semihunaldi.rummikub.exception.JokerIsNotUniqueException;
import com.semihunaldi.rummikub.tiles.Tile;
import com.semihunaldi.rummikub.tiles.TileColor;
import org.assertj.core.api.Assertions;
import org.junit.Test;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by semihunaldi on 13.04.2018
 */
@SuppressWarnings("WeakerAccess")
public class HandDistributionFactoryTest {

	private final HandDistributionFactory handDistributionFactory = new HandDistributionFactory();

	@Test
	public void testHandDistribution() {
		HandDistribution handDistribution = handDistributionFactory.distributeHand();
		Assertions.assertThat(handDistribution.getPlayer1().size()).isEqualTo(15);
		Assertions.assertThat(handDistribution.getPlayer2().size()).isEqualTo(14);
		Assertions.assertThat(handDistribution.getPlayer3().size()).isEqualTo(14);
		Assertions.assertThat(handDistribution.getPlayer4().size()).isEqualTo(14);
		Assertions.assertThat(handDistribution.getRemainingTiles().size())
				.isEqualTo(handDistributionFactory.getTotalTileCountWithoutFakes() - 15 - 14 - 14 - 14 + handDistribution.getFakeCount());
	}

	@Test
	public void testTilesForNulls() {
		HandDistribution handDistribution = handDistributionFactory.distributeHand();
		testTiles(handDistribution.getPlayer1());
		testTiles(handDistribution.getPlayer2());
		testTiles(handDistribution.getPlayer3());
		testTiles(handDistribution.getPlayer4());
	}

	@Test
	public void testTileNumberRanges() {
		HandDistribution handDistribution = handDistributionFactory.distributeHand();
		List<Tile> tiles = handDistribution.getAllTiles();
		for(Tile tile : tiles){
			Assertions.assertThat(tile.getNumber()).isGreaterThanOrEqualTo(handDistributionFactory.getMinNum());
			Assertions.assertThat(tile.getNumber()).isLessThanOrEqualTo(handDistributionFactory.getMaxNum());
		}
	}

	@Test
	public void testNumberOfFakes() {
		HandDistribution handDistribution = handDistributionFactory.distributeHand();
		List<Tile> tiles = handDistribution.getAllTiles();
		List<Tile> fakes = tiles.stream().filter(Tile::isFake).collect(Collectors.toList());
		Assertions.assertThat(fakes.size()).isEqualTo(tiles.size() - handDistributionFactory.getTotalTileCountWithoutFakes());
	}

	@Test
	public void testNumberOfJokers() {
		HandDistribution handDistribution = handDistributionFactory.distributeHand();
		List<Tile> tiles = handDistribution.getAllTiles();
		List<Tile> jokers = tiles.stream().filter(Tile::isJoker).collect(Collectors.toList());
		Assertions.assertThat(jokers.size()).isEqualTo(2);
	}

	@Test
	public void testColorCounts() {
		testColorCounts(TileColor.YELLOW);
		testColorCounts(TileColor.BLUE);
		testColorCounts(TileColor.BLACK);
		testColorCounts(TileColor.RED);
	}

	@Test
	public void testJokerFound() {
		HandDistribution handDistribution = handDistributionFactory.distributeHand();
		Assertions.assertThat(handDistribution.getJoker()).isNotNull();
	}

	@Test(expected = JokerIsNotUniqueException.class)
	public void testJokerCanNotBeUniqueWithIterationOverTotalTileCount() {
		List<Tile> jokers = Lists.newArrayList();
		HandDistribution handDistribution = handDistributionFactory.distributeHand();
		jokers.add(handDistribution.getJoker());
		for(int i = 1; i <= handDistribution.getAllTiles().size() + 1; i++){
			handDistribution.distribute();
			if(jokers.contains(handDistribution.getJoker())){
				throw new JokerIsNotUniqueException("Joker is not unique at loop : " + i);
			}
			jokers.add(handDistribution.getJoker());
		}
	}

	@Test
	public void testLoad() {
		for(int i = 1; i <= 100_000; i++){
			if(i % 5000 == 0){
				System.out.println("Load Test Iteration : " + i);
			}
			testHandDistribution();
			testTilesForNulls();
			testTileNumberRanges();
			testNumberOfFakes();
			testNumberOfJokers();
			testColorCounts();
			testJokerFound();
		}
	}

	private void testColorCounts(TileColor color) {
		HandDistribution handDistribution = handDistributionFactory.distributeHand();
		List<Tile> tiles = handDistribution.getAllTiles();
		List<Tile> colors = tiles.stream().filter(tile -> tile.getTileColor().equals(color) && !tile.isFake()).collect(Collectors.toList());
		Assertions.assertThat(colors.size()).isEqualTo(handDistribution.getCountOfEachColor());
	}

	private void testTiles(List<Tile> tileList) {
		for(Tile tile : tileList){
			Assertions.assertThat(tile.getTileColor()).isNotNull();
			Assertions.assertThat(tile.getName()).isNotNull();
			Assertions.assertThat(tile.getNumber()).isNotNull();
		}
	}
}
