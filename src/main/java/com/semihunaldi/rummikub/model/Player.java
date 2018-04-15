package com.semihunaldi.rummikub.model;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.semihunaldi.rummikub.Util;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.apache.commons.collections4.IterableUtils;

import java.io.Serializable;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by semihunaldi on 13.04.2018
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "playerNumber")
public class Player implements Serializable {

	private static double WEIGHT_HAS_JOKER = 1.2d;
	private static double WEIGHT_HAS_DOUBLE_JOKER = 1.4d;
	private static double WEIGHT_UN_GROUPED_TILE_LEFT = 2d;
	private static double WEIGHT_DUPLICATE_ELIGIBLE = 2.2d;

	private int playerNumber = 1;
	private List<Tile> hand = Lists.newArrayList();

	private Set<List<Tile>> sameNumbersDifferentColorsHavingCountGreaterThanTwo = Sets.newHashSet();
	private Set<List<Tile>> possibleSameNumbersDifferentColorsGroupingsWithJokers = Sets.newHashSet();

	private Set<List<Tile>> differentNumbersSameColorsHavingCountGreaterThanTwo = Sets.newHashSet();
	private Set<List<Tile>> possibleDifferentNumbersSameColorsHavingCountGreaterThanTwo = Sets.newHashSet();

	private Set<Tile> duplicates = Sets.newHashSet();

	private double bestHandScore = 1;

	private List<List<Tile>> bestOrderedHand = Lists.newArrayList();

	private int unGroupedCount = 0;

	public Player(int playerNumber) {
		this.playerNumber = playerNumber;
	}

	public void calculateScore(int minNum, int maxNum) {
		prepareBestHand();
		bestHandScore = 1;
		int jokerCount = Util.playerJokerCount(this);
		if(jokerCount == 1){
			bestHandScore = bestHandScore * WEIGHT_HAS_JOKER;
		} else if(jokerCount == 2){
			bestHandScore = bestHandScore * WEIGHT_HAS_DOUBLE_JOKER;
		}
		int subtractor = maxNum - minNum + 1;
		int point = subtractor - unGroupedCount;
		bestHandScore = point * WEIGHT_UN_GROUPED_TILE_LEFT;
		if(duplicates.size() > 4){
			bestHandScore = bestHandScore * WEIGHT_DUPLICATE_ELIGIBLE;
		}
	}

	private void prepareBestHand() {
		prepareBestActualTileOrder(this, null);
		prepareBestPossibleTileOrder(this, null);
		prepareBestDoublesHand(this);
		List<Tile> allOrderedTiles = Lists.newArrayList();
		for(List<Tile> tiles : this.getBestOrderedHand()){
			allOrderedTiles.addAll(tiles);
		}
		List<Tile> remaining = getHand().stream().filter(tile -> !allOrderedTiles.contains(tile)).collect(Collectors.toList());
		if(!remaining.isEmpty()){
			this.getBestOrderedHand().add(remaining);
			unGroupedCount = remaining.size();
		}
	}

	private void prepareBestActualTileOrder(Player player, List<Tile> remaining) {
		Set<List<Tile>> actualConsecutive = adjustPropertyListsWithRemainingData(player.getDifferentNumbersSameColorsHavingCountGreaterThanTwo(), remaining);
		Set<List<Tile>> actualColors = adjustPropertyListsWithRemainingData(player.getSameNumbersDifferentColorsHavingCountGreaterThanTwo(), remaining);
		List<Tile> bestTileSetBetweenLists = findBestTileSetBetweenLists(actualConsecutive, actualColors, getUsedTiles());
		if(!bestTileSetBetweenLists.isEmpty() && bestTileSetBetweenLists.stream().filter(tile -> isTileAvailable(tile, this)).count() == bestTileSetBetweenLists.size()){
			List<Tile> remainingList = removeUsedTiles(player, bestTileSetBetweenLists);
			this.getBestOrderedHand().add(bestTileSetBetweenLists);
			prepareBestActualTileOrder(player, remainingList);
		}
	}

	private void prepareBestPossibleTileOrder(Player player, List<Tile> remaining) {
		Set<List<Tile>> possibleConsecutive = adjustPropertyListsWithRemainingData(player.getPossibleDifferentNumbersSameColorsHavingCountGreaterThanTwo(), remaining);
		Set<List<Tile>> possibleColors = adjustPropertyListsWithRemainingData(player.getPossibleSameNumbersDifferentColorsGroupingsWithJokers(), remaining);
		List<Tile> bestTileSetBetweenLists = findBestTileSetBetweenLists(possibleConsecutive, possibleColors, getUsedTiles());
		if(!bestTileSetBetweenLists.isEmpty() && bestTileSetBetweenLists.stream().filter(tile -> isTileAvailable(tile, this)).count() == bestTileSetBetweenLists.size()){
			List<Tile> remainingList = removeUsedTiles(player, bestTileSetBetweenLists);
			this.getBestOrderedHand().add(bestTileSetBetweenLists);
			prepareBestPossibleTileOrder(player, remainingList);
		}
	}

	private void prepareBestDoublesHand(Player player) {
		if(player.getDuplicates().size() > 4){
			player.setBestOrderedHand(Lists.newArrayList());
			for(Tile tile : player.getDuplicates()){
				List<Tile> doubles = Lists.newArrayList();
				doubles.add(tile);
				doubles.add(tile);
				player.getBestOrderedHand().add(doubles);
			}
		}
	}

	private Set<List<Tile>> adjustPropertyListsWithRemainingData(Set<List<Tile>> listSet, List<Tile> remaining) {
		if(remaining == null){
			return Sets.newHashSet(listSet);
		}
		return listSet.stream().filter(tiles -> tiles.stream().filter(t -> !remaining.contains(t)).count() <= 0).collect(Collectors.toSet());
	}

	private List<Tile> removeUsedTiles(Player player, List<Tile> used) {
		List<Tile> tiles = Lists.newArrayList(player.getHand());
		for(Tile tile : used){
			int index = tiles.indexOf(tile);
			tiles.remove(index);
		}
		return tiles;
	}

	private List<Tile> getUsedTiles() {
		Set<Tile> tiles = Sets.newHashSet();
		for(List<Tile> used : this.getBestOrderedHand()){
			tiles.addAll(used);
		}
		return Lists.newArrayList(tiles);
	}

	private boolean isTileAvailable(Tile tile, Player player) {
		List<List<Tile>> bestOrderedHand = player.getBestOrderedHand();
		if(player.getDuplicates().size() < 4){
			for(List<Tile> tiles : bestOrderedHand){
				if(tiles.contains(tile)){
					return false;
				}
			}
			return true;
		} else{ //If player owns more than 4 duplicate, it is a good choice to go doubles.
			long count = bestOrderedHand.stream()
					.filter(tiles -> IterableUtils.countMatches(tiles, object -> object.equals(tile)) < IterableUtils.countMatches(player.getDuplicates(), object -> object.equals(tile)))
					.count();
			return count > 0;
		}
	}

	private List<Tile> findBestTileSetBetweenLists(Set<List<Tile>> list1, Set<List<Tile>> list2, List<Tile> excludeList) {
		list1 = list1.stream().filter(tiles -> tiles.stream().filter(excludeList::contains).count() <= 0).collect(Collectors.toSet());
		list2 = list2.stream().filter(tiles -> tiles.stream().filter(excludeList::contains).count() <= 0).collect(Collectors.toSet());
		if(list1.isEmpty()){
			return list2.stream().max(Comparator.comparingInt(List::size)).orElse(Lists.newArrayList());
		}
		if(list2.isEmpty()){
			return list1.stream().max(Comparator.comparingInt(List::size)).orElse(Lists.newArrayList());
		}
		int bestSize = 0;
		List<Tile> bestTileSet;
		List<Tile> bestTileSet1 = Lists.newArrayList();
		List<Tile> bestTileSet2 = Lists.newArrayList();
		bestTileSet1 = getTiles(list2, list1, bestSize, bestTileSet1);
		bestTileSet2 = getTiles(list1, list2, bestSize, bestTileSet2);
		if(bestTileSet1.size() > bestTileSet2.size()){
			bestTileSet = bestTileSet1;
		} else{
			bestTileSet = bestTileSet2;
		}
		if(bestTileSet.isEmpty()){
			bestTileSet = list1.stream().max(Comparator.comparingInt(List::size)).orElse(Lists.newArrayList());
		}
		return bestTileSet;
	}

	private List<Tile> getTiles(Set<List<Tile>> list1, Set<List<Tile>> list2, int bestSize, List<Tile> bestTileSet2) {
		for(List<Tile> tiles : list2){
			for(Tile tile : tiles){
				Optional<List<Tile>> intersection = list1.stream().filter(tiles1 -> tiles1.contains(tile)).max(Comparator.comparingInt(List::size));
				if(intersection.isPresent() && intersection.get().size() > bestSize){
					bestTileSet2 = intersection.get();
				}
			}
		}
		return bestTileSet2;
	}
}
