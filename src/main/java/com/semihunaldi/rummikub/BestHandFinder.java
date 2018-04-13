package com.semihunaldi.rummikub;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.semihunaldi.rummikub.tiles.Tile;
import com.semihunaldi.rummikub.tiles.TileColor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by semihunaldi on 13.04.2018
 */
public class BestHandFinder {

	private List<Integer> SUPER_LIST = Lists.newArrayList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 1);

	private List<List<Tile>> sameNumbersDifferentColorsHavingCountGreaterThanTwo = Lists.newArrayList();
	private List<List<Tile>> possibleSameNumbersDifferentColorsGroupingsWithJokers = Lists.newArrayList();

	private Set<Set<Tile>> differentNumbersSameColorsHavingCountGreaterThanTwo = Sets.newHashSet();
	private Set<Set<Tile>> possibleDifferentNumbersSameColorsHavingCountGreaterThanTwo = Sets.newHashSet();

	public Player findBestHand(HandDistribution handDistribution) {
		printHands(handDistribution);
		Player player = handDistribution.getPlayer1();
		//		for(Player player : handDistribution.getPlayers()){
		groupHandBySameNumbersDifferentColors(player);
		groupHandByDifferentNumbersSameColors(player);
		//		}
		return null;
	}

	private void groupHandByDifferentNumbersSameColors(Player player) {
		if(Util.playerHasJoker(player)){
			preparePossibleGroupHandByDifferentNumbersSameColors(player);
		} else{
			Map<TileColor, List<Tile>> groupedByColor = player.getHand().stream().sorted().collect(Collectors.groupingBy(Tile::getTileColor));
			List<List<Tile>> group = new ArrayList<>(groupedByColor.values());
			for(List<Tile> sorted : group){
				List<List<Integer>> collect = Util.getSubLists(sorted)
						.stream()
						.filter(tiles -> tiles.size() > 2)
						.map(tiles -> tiles.stream().map(Tile::getNumber).collect(Collectors.toList()))
						.collect(Collectors.toList());
				for(List<Integer> subList : collect){
					int index = Collections.indexOfSubList(SUPER_LIST, subList);
					if(index >= 0){
						List<Tile> found = sorted.stream().filter(tile -> subList.contains(tile.getNumber())).collect(Collectors.toList());
						differentNumbersSameColorsHavingCountGreaterThanTwo.add(Sets.newHashSet(found));
					}
				}
			}
		}
	}

	private void preparePossibleGroupHandByDifferentNumbersSameColors(Player player) {
		List<Tile> playerJokers = Util.getPlayerJokers(player);
		if(playerJokers.size() == 1){
			Map<TileColor, List<Tile>> groupedByColor = player.getHand().stream().filter(tile -> !tile.isJoker()).sorted().collect(Collectors.groupingBy(Tile::getTileColor));
			List<List<Tile>> group = new ArrayList<>(groupedByColor.values());
			for(List<Tile> sorted : group){
				List<List<Integer>> collect = Util.getSubLists(sorted)
						.stream()
						.filter(tiles -> tiles.size() > 1)
						.map(tiles -> tiles.stream().map(Tile::getNumber).collect(Collectors.toList()))
						.collect(Collectors.toList());
				for(List<Integer> subList : collect){
					int index = Collections.indexOfSubList(SUPER_LIST, subList);
					if(index >= 0){
						List<Tile> found = sorted.stream().filter(tile -> subList.contains(tile.getNumber())).collect(Collectors.toList());
						found.add(playerJokers.get(0));
						possibleDifferentNumbersSameColorsHavingCountGreaterThanTwo.add(Sets.newHashSet(found));
					}
				}
			}
		} else{
			Map<TileColor, List<Tile>> groupedByColor = player.getHand().stream().filter(tile -> !tile.isJoker()).sorted().collect(Collectors.groupingBy(Tile::getTileColor));
			List<List<Tile>> group = new ArrayList<>(groupedByColor.values());
			for(List<Tile> sorted : group){
				List<List<Integer>> collect = Util.getSubLists(sorted)
						.stream()
						.filter(tiles -> tiles.size() == 1)
						.map(tiles -> tiles.stream().map(Tile::getNumber).collect(Collectors.toList()))
						.collect(Collectors.toList());
				for(List<Integer> subList : collect){
					int index = Collections.indexOfSubList(SUPER_LIST, subList);
					if(index >= 0){
						List<Tile> found = sorted.stream().filter(tile -> subList.contains(tile.getNumber())).collect(Collectors.toList());
						found.add(playerJokers.get(0));
						found.add(playerJokers.get(1));
						possibleDifferentNumbersSameColorsHavingCountGreaterThanTwo.add(Sets.newHashSet(found));
					}
				}
			}
		}
	}

	private void groupHandBySameNumbersDifferentColors(Player player) {
		if(Util.playerHasJoker(player)){
			List<Tile> playerJokers = Util.getPlayerJokers(player);
			Map<Integer, List<Tile>> sameNumbersDifferentColors = player.getHand().stream().filter(tile -> !tile.isJoker()).collect(Collectors.groupingBy(Tile::getNumber));
			if(playerJokers.size() == 1){
				List<List<Tile>> sameNumbersDifferentColorsHavingCountGreaterThanOne = sameNumbersDifferentColors.values()
						.stream()
						.filter(tiles -> tiles.size() > 1)
						.collect(Collectors.toList());
				sameNumbersDifferentColorsHavingCountGreaterThanOne.forEach(tiles -> {
					if(tiles.size() < 4){
						tiles.add(playerJokers.get(0));
					}
				});
				possibleSameNumbersDifferentColorsGroupingsWithJokers.addAll(sameNumbersDifferentColorsHavingCountGreaterThanOne);
			} else{
				List<List<Tile>> sameNumbersDifferentColorsHavingCountEqualsOne = sameNumbersDifferentColors.values()
						.stream()
						.filter(tiles -> tiles.size() == 1)
						.collect(Collectors.toList());
				sameNumbersDifferentColorsHavingCountEqualsOne.forEach(tiles -> {
					if(tiles.size() < 4){
						tiles.add(playerJokers.get(1));
						tiles.add(playerJokers.get(0));
					}
				});
				possibleSameNumbersDifferentColorsGroupingsWithJokers.addAll(sameNumbersDifferentColorsHavingCountEqualsOne);
			}
		} else{
			Map<Integer, List<Tile>> sameNumbersDifferentColors = player.getHand().stream().collect(Collectors.groupingBy(Tile::getNumber));
			sameNumbersDifferentColorsHavingCountGreaterThanTwo = sameNumbersDifferentColors.values().stream().filter(tiles -> tiles.size() > 2).collect(Collectors.toList());
		}
	}

	private void printHands(HandDistribution handDistribution) {
		for(Player player : handDistribution.getPlayers()){
			List<Tile> hand = player.getHand();
			System.out.println("Player Number : " + player.getPlayerNumber());
			System.out.println(hand);
			System.out.println();
		}
	}
}
