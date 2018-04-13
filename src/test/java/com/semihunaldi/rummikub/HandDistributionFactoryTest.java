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

	@Test
	public void testHandDistribution() {
		HandDistributionFactory handDistributionFactory = new HandDistributionFactory();
		handDistributionFactory.distribute();
		Assertions.assertThat(handDistributionFactory.getPlayer1().size()).isEqualTo(15);
		Assertions.assertThat(handDistributionFactory.getPlayer2().size()).isEqualTo(14);
		Assertions.assertThat(handDistributionFactory.getPlayer3().size()).isEqualTo(14);
		Assertions.assertThat(handDistributionFactory.getPlayer4().size()).isEqualTo(14);
		Assertions.assertThat(handDistributionFactory.getRemainingTiles().size()).isEqualTo(HandDistributionFactory.totalTileCount - 15 - 14 - 14 - 14);
	}

	@Test
	public void testTilesForNulls() {
		HandDistributionFactory handDistributionFactory = new HandDistributionFactory();
		handDistributionFactory.distribute();
		testTiles(handDistributionFactory.getPlayer1());
		testTiles(handDistributionFactory.getPlayer2());
		testTiles(handDistributionFactory.getPlayer3());
		testTiles(handDistributionFactory.getPlayer4());
	}

	@Test
	public void testTileNumberRanges() {
		HandDistributionFactory handDistributionFactory = new HandDistributionFactory();
		handDistributionFactory.distribute();
		List<Tile> tiles = joinAll(handDistributionFactory);
		for(Tile tile : tiles){
			Assertions.assertThat(tile.getNumber()).isGreaterThanOrEqualTo(HandDistributionFactory.minNum);
			Assertions.assertThat(tile.getNumber()).isLessThanOrEqualTo(HandDistributionFactory.maxNum);
		}
	}

	@Test
	public void testNumberOfFakes() {
		HandDistributionFactory handDistributionFactory = new HandDistributionFactory();
		handDistributionFactory.distribute();
		List<Tile> tiles = joinAll(handDistributionFactory);
		List<Tile> fakes = tiles.stream().filter(Tile::isFake).collect(Collectors.toList());
		Assertions.assertThat(fakes.size()).isEqualTo(HandDistributionFactory.numberOfFakes);
	}

	@Test
	public void testNumberOfJokers() {
		HandDistributionFactory handDistributionFactory = new HandDistributionFactory();
		handDistributionFactory.distribute();
		List<Tile> tiles = joinAll(handDistributionFactory);
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
	public void loadTest() {
		for(int i = 0; i < 100_000; i++){
			HandDistributionFactory handDistributionFactory = new HandDistributionFactory();
			handDistributionFactory.distribute();
		}
	}

	private void testColorCounts(TileColor color) {
		HandDistributionFactory handDistributionFactory = new HandDistributionFactory();
		handDistributionFactory.distribute();
		List<Tile> tiles = joinAll(handDistributionFactory);
		List<Tile> colors = tiles.stream().filter(tile -> tile.getTileColor().equals(color) && !tile.isFake()).collect(Collectors.toList());
		Assertions.assertThat(colors.size()).isEqualTo(26);
	}

	private List<Tile> joinAll(HandDistributionFactory handDistributionFactory) {
		List<Tile> all = Lists.newArrayList();
		all.addAll(handDistributionFactory.getPlayer1());
		all.addAll(handDistributionFactory.getPlayer2());
		all.addAll(handDistributionFactory.getPlayer3());
		all.addAll(handDistributionFactory.getPlayer4());
		all.addAll(handDistributionFactory.getRemainingTiles());
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
