package com.semihunaldi.rummikub;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.semihunaldi.rummikub.model.Player;
import com.semihunaldi.rummikub.model.Tile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by semihunaldi on 13.04.2018
 */
public class Util {

	public static boolean playerHasJoker(Player player) {
		Optional<Tile> any = player.getHand().stream().filter(Tile::isJoker).findAny();
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
}
