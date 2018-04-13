package com.semihunaldi.rummikub.tiles;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Created by semihunaldi on 13.04.2018
 */

@Data
@EqualsAndHashCode(of = {"tileColor", "number"})
@NoArgsConstructor
public class Tile implements Comparable<Tile> {

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

	@Override
	public String toString() {
		String joker = isJoker() ? "-JOKER" : "";
		String fake = isFake() ? "-FAKE" : "";
		return tileColor.name().toLowerCase() + "-" + number + joker + fake;
	}

	@Override
	public int compareTo(Tile o) {
		return Integer.compare(this.getNumber(), o.getNumber());
	}
}
