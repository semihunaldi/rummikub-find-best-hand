package com.semihunaldi.rummikub.tiles;

/**
 * Created by semihunaldi on 13.04.2018
 */
public class Tile {
	private TileColor tileColor;
	private Integer number = -99;
	private String name;

	private boolean joker;
	private boolean fake;

	public boolean isFake(){
		return fake;
	}

	public boolean isJoker() {
		return joker;
	}

	public Tile(TileColor tileColor, Integer number, String name) {
		this.tileColor = tileColor;
		this.number = number;
		this.name = name;
	}

	public Tile() {
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

	public void setJoker(boolean joker) {
		this.joker = joker;
	}

	public void setFake(boolean fake) {
		this.fake = fake;
	}

	@Override
	public String toString() {
		return "Tile{" +
				"name='" + name + '\'' +
				'}';
	}
}
