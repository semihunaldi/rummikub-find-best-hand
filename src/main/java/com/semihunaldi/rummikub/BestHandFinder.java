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

	private List<Integer> SUPER_LIST = Lists.newArrayList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 1);

	public Player findBestHand(HandDistribution handDistribution) {
		printHands(handDistribution);
		for(Player player : handDistribution.getPlayers()){
			groupHandBySameNumbersDifferentColors(player);
			groupHandByDifferentNumbersSameColors(player);
			groupByGoingDoubles(player);
			player.calculateScore();
		}
		Optional<Player> max = handDistribution.getPlayers().stream().max(Comparator.comparingInt(Player::getBestHandScore));
		if(max.isPresent()){
			Player player = max.get();
			List<Tile> hand = player.getHand().stream().sorted().collect(Collectors.toList());
			System.out.println("\nBest Hand Player Number : " + player.getPlayerNumber());
			System.out.println(hand);
		}
		return max.orElse(null);
	}

	private void groupByGoingDoubles(Player player) {
		Set<Tile> duplicates = Util.findDuplicates(player.getHand());
		if(Util.playerHasJoker(player)){
			List<Tile> playerJokers = Util.getPlayerJokers(player);
			if(playerJokers.size() == 1){
				if(duplicates.size() * 2 < player.getHand().size()){
					duplicates.add(playerJokers.get(0));
				}
			} else{
				if(duplicates.size() * 2 < player.getHand().size() - 1){
					duplicates.add(playerJokers.get(0));
					duplicates.add(playerJokers.get(1));
				}
			}
		} else{
			player.setDuplicates(duplicates);
		}
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
						player.getDifferentNumbersSameColorsHavingCountGreaterThanTwo().add(Sets.newHashSet(found));
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
						player.getPossibleDifferentNumbersSameColorsHavingCountGreaterThanTwo().add(Sets.newHashSet(found));
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
						player.getPossibleDifferentNumbersSameColorsHavingCountGreaterThanTwo().add(Sets.newHashSet(found));
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
				Set<Set<Tile>> sameNumbersDifferentColorsHavingCountGreaterThanOne = sameNumbersDifferentColors.values()
						.stream()
						.map((Function<List<Tile>, Set<Tile>>) Sets::newHashSet)
						.filter(tiles -> tiles.size() > 1)
						.collect(Collectors.toSet());
				sameNumbersDifferentColorsHavingCountGreaterThanOne.forEach(tiles -> {
					if(tiles.size() < 4){
						tiles.add(playerJokers.get(0));
					}
				});
				player.getPossibleSameNumbersDifferentColorsGroupingsWithJokers().addAll(sameNumbersDifferentColorsHavingCountGreaterThanOne);
			} else{
				Set<Set<Tile>> sameNumbersDifferentColorsHavingCountEqualsOne = sameNumbersDifferentColors.values()
						.stream()
						.map((Function<List<Tile>, Set<Tile>>) Sets::newHashSet)
						.filter(tiles -> tiles.size() == 1)
						.collect(Collectors.toSet());
				sameNumbersDifferentColorsHavingCountEqualsOne.forEach(tiles -> {
					if(tiles.size() < 4){
						tiles.add(playerJokers.get(1));
						tiles.add(playerJokers.get(0));
					}
				});
				player.getPossibleSameNumbersDifferentColorsGroupingsWithJokers().addAll(sameNumbersDifferentColorsHavingCountEqualsOne);
			}
		} else{
			Map<Integer, List<Tile>> sameNumbersDifferentColors = player.getHand().stream().collect(Collectors.groupingBy(Tile::getNumber));
			player.setSameNumbersDifferentColorsHavingCountGreaterThanTwo(sameNumbersDifferentColors.values()
					.stream()
					.map((Function<List<Tile>, Set<Tile>>) Sets::newHashSet)
					.filter(tiles -> tiles.size() > 2)
					.collect(Collectors.toSet()));
		}
	}

	private void printHands(HandDistribution handDistribution) {
		for(Player player : handDistribution.getPlayers()){
			List<Tile> hand = player.getHand();
			System.out.println("Player Number : " + player.getPlayerNumber());
			System.out.println(hand);
		}
	}
}
