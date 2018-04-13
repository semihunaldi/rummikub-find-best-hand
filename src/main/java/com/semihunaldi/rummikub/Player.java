package com.semihunaldi.rummikub;

import com.google.common.collect.Lists;
import com.semihunaldi.rummikub.tiles.Tile;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Created by semihunaldi on 13.04.2018
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Player {

	private int playerNumber = 1;
	private List<Tile> hand = Lists.newArrayList();

	public Player(int playerNumber) {
		this.playerNumber = playerNumber;
	}
}
