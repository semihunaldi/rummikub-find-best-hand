package com.semihunaldi.rummikub;

import com.google.common.collect.Lists;
import com.semihunaldi.rummikub.tiles.Tile;
import com.semihunaldi.rummikub.tiles.TileColor;

import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * Created by semihunaldi on 13.04.2018
 */
public class HandDistributionFactory {

	public static int maxNum = 13;
	public static int minNum = 1;

	public static final int totalTileCount = 106;
	public static final int numberOfCopiesOfTiles = 2;
	public static final int numberOfFakes = 2;

	private List<Tile> yellowList = Lists.newArrayList();
	private List<Tile> blueList = Lists.newArrayList();
	private List<Tile> blackList = Lists.newArrayList();
	private List<Tile> redList = Lists.newArrayList();
	private List<Tile> fakeList = Lists.newArrayList();

	private List<Tile> allTiles = Lists.newArrayList();
	private List<Tile> remainingTiles = Lists.newArrayList();

	private Tile joker;

	private List<Tile> player1;
	private List<Tile> player2;
	private List<Tile> player3;
	private List<Tile> player4;

	public void distribute() {
		clear();
		createTiles();
		Collections.shuffle(allTiles);
		pickJoker();
		adjustFakeTiles();
		assignTilesToPlayers();
	}

	private void createTiles() {
		int numberOfCycle = (totalTileCount - numberOfFakes) / 2 / (maxNum - minNum + 1) / numberOfCopiesOfTiles;
		for(int i = 1; i <= numberOfCycle; i++){
			yellowList.addAll(createColorTiles(TileColor.YELLOW));
			blueList.addAll(createColorTiles(TileColor.BLUE));
			blackList.addAll(createColorTiles(TileColor.BLACK));
			redList.addAll(createColorTiles(TileColor.RED));
		}
		fakeList.addAll(createFakeTiles());
		allTiles.addAll(yellowList);
		allTiles.addAll(blueList);
		allTiles.addAll(blackList);
		allTiles.addAll(redList);
		allTiles.addAll(fakeList);
	}

	private void assignTilesToPlayers() {
		Collections.shuffle(allTiles);
		player1 = allTiles.subList(0, 15);
		player2 = allTiles.subList(15, 29);
		player3 = allTiles.subList(29, 43);
		player4 = allTiles.subList(43, 57);
		remainingTiles = allTiles.subList(57, allTiles.size());
	}

	private void pickJoker() {
		Random r = new Random();
		int low = 0;
		int high = 104; //fakes excluded
		int random = r.nextInt(high - low) + low;
		List<Tile> tilesExcludingFakes = allTiles.stream().filter(tile -> !tile.isFake()).collect(Collectors.toList());
		Tile randomTile = tilesExcludingFakes.get(random);
		int jokerNumber = findJokerNumber(randomTile);
		final TileColor tileColor = randomTile.getTileColor();
		List<Tile> jokers = allTiles.stream().filter(tile -> tile.getNumber().equals(jokerNumber) && tile.getTileColor().equals(tileColor)).collect(Collectors.toList());
		if(jokers.isEmpty()) {
			throw new RuntimeException("Joker finding failed");
		}
		for(Tile joker : jokers){
			joker.setJoker(true);
			joker.setName("JOKER");
			this.joker = joker;
		}
	}

	private void adjustFakeTiles() {
		List<Tile> fakeTiles = allTiles.stream().filter(tile -> tile.getTileColor().equals(TileColor.FAKE)).collect(Collectors.toList());
		for(Tile fakeTile : fakeTiles){
			fakeTile.setNumber(joker.getNumber());
			fakeTile.setTileColor(joker.getTileColor());
			fakeTile.setFake(true);
			fakeTile.setName("fake-".concat(joker.getTileColor().name()).concat("-").concat(joker.getNumber().toString()));
		}
	}

	private int findJokerNumber(Tile tile) {
		Integer number = tile.getNumber();
		if(number.equals(maxNum)){
			return minNum;
		}
		return number + 1;
	}

	private List<Tile> createColorTiles(TileColor tileColor) {
		List<Tile> tiles = Lists.newArrayList();
		for(int i = minNum; i <= maxNum; i++){
			Tile tile = new Tile(tileColor, i, tileColor.name().toLowerCase().concat("-").concat(String.valueOf(i)));
			tiles.add(tile);
		}
		return tiles;
	}

	private List<Tile> createFakeTiles() {
		List<Tile> fakeTiles = Lists.newArrayList();
		for(int i = 1; i <= numberOfFakes; i++){
			Tile tile = new Tile();
			tile.setTileColor(TileColor.FAKE);
			tile.setFake(true);
			fakeTiles.add(tile);
		}
		return fakeTiles;
	}

	private void clear() {
		yellowList = Lists.newArrayList();
		blueList = Lists.newArrayList();
		blackList = Lists.newArrayList();
		redList = Lists.newArrayList();
		fakeList = Lists.newArrayList();
		allTiles = Lists.newArrayList();
		remainingTiles = Lists.newArrayList();
		joker = null;
		player1 = Lists.newArrayList();
		player2 = Lists.newArrayList();
		player3 = Lists.newArrayList();
		player4 = Lists.newArrayList();
	}

	public List<Tile> getPlayer1() {
		return player1;
	}

	public List<Tile> getPlayer2() {
		return player2;
	}

	public List<Tile> getPlayer3() {
		return player3;
	}

	public List<Tile> getPlayer4() {
		return player4;
	}

	public List<Tile> getRemainingTiles() {
		return remainingTiles;
	}
}
