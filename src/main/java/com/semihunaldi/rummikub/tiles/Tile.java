package com.semihunaldi.rummikub.tiles;

/**
 * Created by semihunaldi on 13.04.2018
 */
public class Tile {

	private int id = 0;
	private TileColor tileColor;
	private Integer number = -99;
	private String name;

	private boolean joker;
	private boolean fake;

	public Tile(TileColor tileColor, Integer number, String name) {
		this.tileColor = tileColor;
		this.number = number;
		this.name = name;
	}

	public Tile() {
	}

	public boolean isFake() {
		return fake;
	}

	public void setFake(boolean fake) {
		this.fake = fake;
	}

	public boolean isJoker() {
		return joker;
	}

	public void setJoker(boolean joker) {
		this.joker = joker;
	}

	public TileColor getTileColor() {
		return tileColor;
	}

	public void setTileColor(TileColor tileColor) {
		this.tileColor = tileColor;
	}

	public Integer getNumber() {
		return number;
	}

	public void setNumber(Integer number) {
		this.number = number;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Override
	public String toString() {
		String joker = isJoker() ? " , JOKER" : "";
		String fake = isFake() ? " , FAKE" : "";
		return "Tile{" + tileColor.name().toLowerCase() + "-" + number + joker + fake + ", id=" + id + "}";
	}
}
