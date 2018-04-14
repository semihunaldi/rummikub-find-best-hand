package com.semihunaldi.rummikub.model;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

/**
 * Created by semihunaldi on 13.04.2018
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Player {

	private int playerNumber = 1;
	private List<Tile> hand = Lists.newArrayList();

	private Set<Set<Tile>> sameNumbersDifferentColorsHavingCountGreaterThanTwo = Sets.newHashSet();
	private Set<Set<Tile>> possibleSameNumbersDifferentColorsGroupingsWithJokers = Sets.newHashSet();

	private Set<Set<Tile>> differentNumbersSameColorsHavingCountGreaterThanTwo = Sets.newHashSet();
	private Set<Set<Tile>> possibleDifferentNumbersSameColorsHavingCountGreaterThanTwo = Sets.newHashSet();

	private Set<Tile> duplicates = Sets.newHashSet();

	public Player(int playerNumber) {
		this.playerNumber = playerNumber;
	}

	private int bestHandScore = 0;

	public void calculateScore() {
		bestHandScore = sameNumbersDifferentColorsHavingCountGreaterThanTwo.size()
				+ possibleSameNumbersDifferentColorsGroupingsWithJokers.size()
				+ differentNumbersSameColorsHavingCountGreaterThanTwo.size()
				+ possibleDifferentNumbersSameColorsHavingCountGreaterThanTwo.size();
	}
}
