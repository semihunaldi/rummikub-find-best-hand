package com.semihunaldi.rummikub;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.semihunaldi.rummikub.model.Player;
import com.semihunaldi.rummikub.model.Tile;
import com.semihunaldi.rummikub.model.TileColor;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Created by semihunaldi on 13.04.2018
 */

@NoArgsConstructor
public class BestHandFinder {

	public static final List<Integer> SUPER_LIST = Lists.newArrayList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 1);

	public Player findBestHand(HandDistribution handDistribution) {
		for(Player player : handDistribution.getPlayers()){
			groupHandBySameNumbersDifferentColors(player);
			groupHandByDifferentNumbersSameColors(player);
			groupByGoingDoubles(player);
			player.calculateScore();
		}
		Optional<Player> max = handDistribution.getPlayers().stream().max(Comparator.comparingDouble(Player::getBestHandScore));
		max.ifPresent(player -> {
			Util.printHands(handDistribution);
			Util.generateBestHandsReport(player, handDistribution);
		});
		return max.orElse(null);
	}

	private void groupByGoingDoubles(Player player) {
		Set<Tile> duplicates = Util.findDuplicates(player.getHand());
		player.setDuplicates(duplicates);
	}

	private void groupHandByDifferentNumbersSameColors(Player player) {
		if(Util.playerHasJoker(player.getHand())){
			preparePossibleGroupHandByDifferentNumbersSameColors(player);
		}
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
					player.getDifferentNumbersSameColorsHavingCountGreaterThanTwo().add(Lists.newArrayList(Sets.newHashSet(found)));
				}
			}
		}
	}

	private void preparePossibleGroupHandByDifferentNumbersSameColors(Player player) {
		List<Tile> playerJokers = Util.getPlayerJokers(player);
		Map<TileColor, List<Tile>> groupedByColor = player.getHand().stream().filter(tile -> !tile.isJoker()).sorted().collect(Collectors.groupingBy(Tile::getTileColor));
		List<List<Tile>> group = new ArrayList<>(groupedByColor.values());
		for(List<Tile> sorted : group){
			List<List<Integer>> collect = Util.getSubLists(sorted)
					.stream()
					.filter(tiles -> tiles.size() >= 1)
					.map(tiles -> tiles.stream().map(Tile::getNumber).collect(Collectors.toList()))
					.collect(Collectors.toList());
			for(List<Integer> subList : collect){
				int index = Collections.indexOfSubList(SUPER_LIST, subList);
				if(index >= 0){
					List<Tile> found = sorted.stream().filter(tile -> subList.contains(tile.getNumber())).collect(Collectors.toList());
					if(playerJokers.size() == 1 && found.size() > 1){
						found.addAll(playerJokers);
						player.getPossibleDifferentNumbersSameColorsHavingCountGreaterThanTwo().add(found);
					} else if(playerJokers.size() > 1 && found.size() >= 1){
						found.addAll(playerJokers);
						player.getPossibleDifferentNumbersSameColorsHavingCountGreaterThanTwo().add(found);
					}
				}
			}
		}
	}

	private void groupHandBySameNumbersDifferentColors(Player player) {
		if(Util.playerHasJoker(player.getHand())){
			List<Tile> playerJokers = Util.getPlayerJokers(player);
			Map<Integer, List<Tile>> sameNumbersDifferentColors = player.getHand().stream().filter(tile -> !tile.isJoker()).collect(Collectors.groupingBy(Tile::getNumber));
			List<List<Tile>> sameNumbersDifferentColorsHavingCountGreaterThanOne = sameNumbersDifferentColors.values()
					.stream()
					.filter(tiles -> tiles.size() >= 1)
					.collect(Collectors.toList());
			sameNumbersDifferentColorsHavingCountGreaterThanOne.forEach(tiles -> {
				if(playerJokers.size() == 1 && tiles.size() >= 2){
					tiles.addAll(playerJokers);
					List<Tile> removed = removeDuplicates(tiles);
					if(removed.size() > 2)
						player.getPossibleSameNumbersDifferentColorsGroupingsWithJokers().add(removed);
				} else if(playerJokers.size() > 1 && tiles.size() >= 3){
					tiles.add(playerJokers.get(0));
					List<Tile> removed = removeDuplicates(tiles);
					if(removed.size() > 2)
						player.getPossibleSameNumbersDifferentColorsGroupingsWithJokers().add(removed);
				}
			});
		}
		Map<Integer, List<Tile>> sameNumbersDifferentColors = player.getHand().stream().collect(Collectors.groupingBy(Tile::getNumber));
		player.setSameNumbersDifferentColorsHavingCountGreaterThanTwo(sameNumbersDifferentColors.values()
				.stream()
				.map((Function<List<Tile>, Set<Tile>>) Sets::newHashSet)
				.filter(tiles -> tiles.size() > 2)
				.map((Function<Set<Tile>, List<Tile>>) Lists::newArrayList)
				.collect(Collectors.toSet()));
	}

	private List<Tile> removeDuplicates(List<Tile> tiles) {
		Set<Tile> duplicates = Util.findDuplicates(tiles);
		Set<Tile> collect = duplicates.stream().filter(tile -> !tile.isJoker()).collect(Collectors.toSet());
		for(Tile tile : collect){
			int i = tiles.indexOf(tile);
			tiles.remove(i);
		}
		return tiles;
	}
}
