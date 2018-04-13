package com.semihunaldi.rummikub;

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

	private HandDistributionFactory handDistributionFactory = new HandDistributionFactory();

	@Test
	public void testHandDistribution() {
		HandDistribution handDistribution = handDistributionFactory.distributeHand();
		Assertions.assertThat(handDistribution.getPlayer1().size()).isEqualTo(15);
		Assertions.assertThat(handDistribution.getPlayer2().size()).isEqualTo(14);
		Assertions.assertThat(handDistribution.getPlayer3().size()).isEqualTo(14);
		Assertions.assertThat(handDistribution.getPlayer4().size()).isEqualTo(14);
		Assertions.assertThat(handDistribution.getRemainingTiles().size()).isEqualTo(handDistributionFactory.getTotalTileCount() - 15 - 14 - 14 - 14);
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
		Assertions.assertThat(fakes.size()).isEqualTo(handDistributionFactory.getNumberOfFakes());
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
		for(int i = 0; i < 100_000; i++){
			handDistributionFactory.distributeHand();
		}
	}

	private void testColorCounts(TileColor color) {
		HandDistribution handDistribution = handDistributionFactory.distributeHand();
		List<Tile> tiles = joinAll(handDistribution);
		List<Tile> colors = tiles.stream().filter(tile -> tile.getTileColor().equals(color) && !tile.isFake()).collect(Collectors.toList());
		Assertions.assertThat(colors.size()).isEqualTo(26);
	}

	private List<Tile> joinAll(HandDistribution handDistribution) {
		List<Tile> all = Lists.newArrayList();
		all.addAll(handDistribution.getPlayer1());
		all.addAll(handDistribution.getPlayer2());
		all.addAll(handDistribution.getPlayer3());
		all.addAll(handDistribution.getPlayer4());
		all.addAll(handDistribution.getRemainingTiles());
		return all;
	}

	private void testTiles(List<Tile> tileList) {
		for(Tile tile : tileList){
			Assertions.assertThat(tile.getTileColor()).isNotNull();
			Assertions.assertThat(tile.getName()).isNotNull();
			Assertions.assertThat(tile.getNumber()).isNotNull();
		}
	}
}
