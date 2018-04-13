package com.semihunaldi.rummikub.tiles;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Created by semihunaldi on 13.04.2018
 */

@Data
@EqualsAndHashCode(of = {"tileColor", "number"})
@ToString(of = {"tileColor", "number", "id"})
@NoArgsConstructor
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
}
