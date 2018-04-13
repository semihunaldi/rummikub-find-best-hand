package com.semihunaldi.rummikub;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.semihunaldi.rummikub.tiles.Tile;
import com.semihunaldi.rummikub.tiles.TileColor;
import org.assertj.core.api.Assertions;
import org.junit.Test;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by semihunaldi on 13.04.2018
 */
public class HandDistributionFactoryTest {

	private final HandDistributionFactory handDistributionFactory = new HandDistributionFactory();

	@Test
	public void testHandDistribution() {
		HandDistribution handDistribution = handDistributionFactory.distributeHand();
		int fakeCount = joinAll(handDistribution).size() - handDistributionFactory.getTotalTileCountWithoutFakes();
		Assertions.assertThat(handDistribution.getPlayer1().size()).isEqualTo(15);
		Assertions.assertThat(handDistribution.getPlayer2().size()).isEqualTo(14);
		Assertions.assertThat(handDistribution.getPlayer3().size()).isEqualTo(14);
		Assertions.assertThat(handDistribution.getPlayer4().size()).isEqualTo(14);
		Assertions.assertThat(handDistribution.getRemainingTiles().size()).isEqualTo(handDistributionFactory.getTotalTileCountWithoutFakes() - 15 - 14 - 14 - 14 + fakeCount);
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
		List<Tile> tiles = joinAll(handDistribution);
		for(Tile tile : tiles){
			Assertions.assertThat(tile.getNumber()).isGreaterThanOrEqualTo(handDistributionFactory.getMinNum());
			Assertions.assertThat(tile.getNumber()).isLessThanOrEqualTo(handDistributionFactory.getMaxNum());
		}
	}

	@Test
	public void testNumberOfFakes() {
		HandDistribution handDistribution = handDistributionFactory.distributeHand();
		List<Tile> tiles = joinAll(handDistribution);
		List<Tile> fakes = tiles.stream().filter(Tile::isFake).collect(Collectors.toList());
		Assertions.assertThat(fakes.size()).isEqualTo(tiles.size() - handDistributionFactory.getTotalTileCountWithoutFakes());
	}

	@Test
	public void testNumberOfJokers() {
		HandDistribution handDistribution = handDistributionFactory.distributeHand();
		List<Tile> tiles = joinAll(handDistribution);
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

	@Test
	public void testLoad() {
		HandDistribution handDistribution = handDistributionFactory.distributeHand();
		for(int i = 0; i < 1_000_000; i++){
			handDistribution.distribute();
		}
	}

	private void testColorCounts(TileColor color) {
		HandDistribution handDistribution = handDistributionFactory.distributeHand();
		List<Tile> tiles = joinAll(handDistribution);
		List<Tile> colors = tiles.stream().filter(tile -> tile.getTileColor().equals(color) && !tile.isFake()).collect(Collectors.toList());
		Assertions.assertThat(colors.size()).isEqualTo(26);
	}

	private List<Tile> joinAll(HandDistribution handDistribution) {
		return Lists.newArrayList(Iterables.concat(
				handDistribution.getPlayer1(),
				handDistribution.getPlayer2(),
				handDistribution.getPlayer3(),
				handDistribution.getPlayer4(),
				handDistribution.getRemainingTiles())
		);
	}

	private void testTiles(List<Tile> tileList) {
		for(Tile tile : tileList){
			Assertions.assertThat(tile.getTileColor()).isNotNull();
			Assertions.assertThat(tile.getName()).isNotNull();
			Assertions.assertThat(tile.getNumber()).isNotNull();
		}
	}
}
