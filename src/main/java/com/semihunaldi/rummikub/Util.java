package com.semihunaldi.rummikub;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.semihunaldi.rummikub.model.Player;
import com.semihunaldi.rummikub.model.Tile;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by semihunaldi on 13.04.2018
 */
public class Util {

	public static boolean playerHasJoker(List<Tile> hand) {
		Optional<Tile> any = hand.stream().filter(Tile::isJoker).findAny();
		return any.isPresent();
	}

	public static int playerJokerCount(Player player) {
		return (int) player.getHand().stream().filter(Tile::isJoker).count();
	}

	public static List<Tile> getPlayerJokers(Player player) {
		return player.getHand().stream().filter(Tile::isJoker).collect(Collectors.toList());
	}

	public static <T> List<List<T>> getSubLists(List<T> input) {
		int allMasks = 1 << input.size();
		List<List<T>> output = new ArrayList<>();
		for(int i = 0; i < allMasks; i++){
			List<T> sub = Lists.newArrayList();
			for(int j = 0; j < input.size(); j++){
				if((i & (1 << j)) > 0){
					sub.add(input.get(j));
				}
			}
			output.add(sub);
		}
		return output;
	}

	public static <T> Set<T> findDuplicates(List<T> listContainingDuplicates) {
		final Set<T> resultSet = Sets.newHashSet();
		final Set<T> set1 = Sets.newHashSet();
		for(T t : listContainingDuplicates){
			if(!set1.add(t)){
				resultSet.add(t);
			}
		}
		return resultSet;
	}

	public static int getRandomNumberInRange(int min, int max) {
		if(min >= max){
			throw new IllegalArgumentException("max must be greater than min");
		}
		Random r = new Random();
		return r.nextInt((max - min) + 1) + min;
	}

	public static void printHands(HandDistribution handDistribution) {
		for(Player player : handDistribution.getPlayers()){
			List<Tile> hand = player.getHand();
			System.out.println("Player Number : " + player.getPlayerNumber() + "  (Hand Score : " + player.getBestHandScore() + ")");
			System.out.println(hand);
		}
	}

	public static void generateBestHandsReport(Player bestHandPlayer, HandDistribution handDistribution) {
		System.out.println();
		System.out.println("Best Hand Player : " + bestHandPlayer.getPlayerNumber() + " with possible pair counts ;");
		System.out.println("    Consecutive count : " + bestHandPlayer.getDifferentNumbersSameColorsHavingCountGreaterThanTwo().size());
		System.out.println("    Color count : " + bestHandPlayer.getSameNumbersDifferentColorsHavingCountGreaterThanTwo().size());
		System.out.println("    Possible Consecutive Count : " + bestHandPlayer.getPossibleDifferentNumbersSameColorsHavingCountGreaterThanTwo().size());
		System.out.println("    Possible Color Count : " + bestHandPlayer.getPossibleSameNumbersDifferentColorsGroupingsWithJokers().size());
		System.out.println("    Duplicate Count : " + bestHandPlayer.getDuplicates().size());
		System.out.println("    Best Ordered Hand : " + bestHandPlayer.getBestOrderedHand());
		generateSecondBestHandReport(bestHandPlayer, handDistribution);
	}

	private static void generateSecondBestHandReport(Player bestHandPlayer, HandDistribution handDistribution) {
		Optional<Player> secondBestHandPlayer = handDistribution.getPlayers()
				.stream()
				.filter(player -> !player.equals(bestHandPlayer))
				.max(Comparator.comparingDouble(Player::getBestHandScore));
		secondBestHandPlayer.ifPresent(player -> {
			System.out.println();
			System.out.println("Second Best Hand Player : " + player.getPlayerNumber() + " with possible pair counts ;");
			System.out.println("    Consecutive count : " + player.getDifferentNumbersSameColorsHavingCountGreaterThanTwo().size());
			System.out.println("    Color count : " + player.getSameNumbersDifferentColorsHavingCountGreaterThanTwo().size());
			System.out.println("    Possible Consecutive Count : " + player.getPossibleDifferentNumbersSameColorsHavingCountGreaterThanTwo().size());
			System.out.println("    Possible Color Count : " + player.getPossibleDifferentNumbersSameColorsHavingCountGreaterThanTwo().size());
			System.out.println("    Duplicate Count : " + player.getDuplicates().size());
			System.out.println("    Best Ordered Hand : " + player.getBestOrderedHand());
		});
	}
}
