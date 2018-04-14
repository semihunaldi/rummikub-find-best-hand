package com.semihunaldi.rummikub;

import com.google.common.collect.Lists;
import com.semihunaldi.rummikub.tiles.Tile;
import com.semihunaldi.rummikub.tiles.TileColor;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by semihunaldi on 13.04.2018
 */
@SuppressWarnings("WeakerAccess")
@Setter(AccessLevel.NONE)
@Data
public class HandDistribution {

	private static final String JOKER_NAME = "JOKER";
	private static final String FAKE_PREFIX = "fake-";
	@Getter(AccessLevel.NONE)
	private final int maxNum;
	@Getter(AccessLevel.NONE)
	private final int minNum;
	@Getter(AccessLevel.NONE)
	private final int totalTileCountWithoutFakes;
	@Getter(AccessLevel.NONE)
	private final int numberOfCopiesOfTiles;
	@Getter(AccessLevel.NONE)
	private int id = -1;

	private List<Tile> yellowList = Lists.newArrayList();
	private List<Tile> blueList = Lists.newArrayList();
	private List<Tile> blackList = Lists.newArrayList();
	private List<Tile> redList = Lists.newArrayList();
	private List<Tile> fakeList = Lists.newArrayList();

	private List<Tile> allTiles = Lists.newArrayList();
	private List<Tile> remainingTiles = Lists.newArrayList();
	private Tile joker;
	private Player player1 = new Player(1);
	private Player player2 = new Player(2);
	private Player player3 = new Player(3);
	private Player player4 = new Player(4);

	public HandDistribution(int maxNum, int minNum, int totalTileCountWithoutFakes, int numberOfCopiesOfTiles) {
		this.maxNum = maxNum;
		this.minNum = minNum;
		this.totalTileCountWithoutFakes = totalTileCountWithoutFakes;
		this.numberOfCopiesOfTiles = numberOfCopiesOfTiles;
		distribute();
	}

	public void distribute() {
		clear();
		createTiles();
		Collections.shuffle(allTiles);
		pickJoker();
		adjustFakeTiles();
		assignTilesToPlayers();
	}

	private void createTiles() {
		int numberOfCycle = totalTileCountWithoutFakes / 2 / (maxNum - minNum + 1) / numberOfCopiesOfTiles;
		for(int i = 1; i <= numberOfCycle; i++){
			yellowList.addAll(createColorTiles(TileColor.YELLOW));
			blueList.addAll(createColorTiles(TileColor.BLUE));
			blackList.addAll(createColorTiles(TileColor.BLACK));
			redList.addAll(createColorTiles(TileColor.RED));
			fakeList.add(createFakeTile());
		}
		allTiles.addAll(yellowList);
		allTiles.addAll(blueList);
		allTiles.addAll(blackList);
		allTiles.addAll(redList);
		allTiles.addAll(fakeList);
	}

	private void assignTilesToPlayers() {
		Collections.shuffle(allTiles);
		switch(Util.getRandomNumberInRange(1, 4)){
			case 1:
				player1.setHand(allTiles.subList(0, 15));
				player2.setHand(allTiles.subList(15, 29));
				player3.setHand(allTiles.subList(29, 43));
				player4.setHand(allTiles.subList(43, 57));
				break;
			case 2:
				player2.setHand(allTiles.subList(0, 15));
				player1.setHand(allTiles.subList(15, 29));
				player3.setHand(allTiles.subList(29, 43));
				player4.setHand(allTiles.subList(43, 57));
				break;
			case 3:
				player3.setHand(allTiles.subList(0, 15));
				player2.setHand(allTiles.subList(15, 29));
				player1.setHand(allTiles.subList(29, 43));
				player4.setHand(allTiles.subList(43, 57));
				break;
			case 4:
				player4.setHand(allTiles.subList(0, 15));
				player2.setHand(allTiles.subList(15, 29));
				player3.setHand(allTiles.subList(29, 43));
				player1.setHand(allTiles.subList(43, 57));
				break;
			default:
				break;
		}
		remainingTiles = allTiles.subList(57, allTiles.size());
	}

	private void pickJoker() {
		int index = Util.getRandomNumberInRange(0, totalTileCountWithoutFakes - 1);
		List<Tile> tilesExcludingFakes = allTiles.stream().filter(tile -> !tile.isFake()).collect(Collectors.toList());
		Tile randomTile = tilesExcludingFakes.get(index);
		int jokerNumber = findJokerNumber(randomTile);
		final TileColor tileColor = randomTile.getTileColor();
		List<Tile> jokers = allTiles.stream().filter(tile -> tile.getNumber().equals(jokerNumber) && tile.getTileColor().equals(tileColor)).collect(Collectors.toList());
		for(Tile joker : jokers){
			joker.setJoker(true);
			joker.setName(JOKER_NAME);
			this.joker = joker;
		}
	}

	private void adjustFakeTiles() {
		List<Tile> fakeTiles = allTiles.stream().filter(Tile::isFake).collect(Collectors.toList());
		for(Tile fakeTile : fakeTiles){
			fakeTile.setNumber(joker.getNumber());
			fakeTile.setTileColor(joker.getTileColor());
			fakeTile.setFake(true);
			fakeTile.setName(FAKE_PREFIX.concat(joker.getTileColor().name()).concat("-").concat(joker.getNumber().toString()));
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
			id++;
			tile.setId(id);
			tiles.add(tile);
		}
		return tiles;
	}

	private Tile createFakeTile() {
		Tile tile = new Tile();
		tile.setFake(true);
		id++;
		tile.setId(id);
		return tile;
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
		player1.setHand(Lists.newArrayList());
		player2.setHand(Lists.newArrayList());
		player3.setHand(Lists.newArrayList());
		player4.setHand(Lists.newArrayList());
	}

	public int getFakeCount() {
		return allTiles.size() - totalTileCountWithoutFakes;
	}

	public int getCountOfEachColor() {
		return numberOfCopiesOfTiles * (maxNum - minNum + 1);
	}

	public List<Player> getPlayers() {
		return Lists.newArrayList(player1, player2, player3, player4);
	}
}
