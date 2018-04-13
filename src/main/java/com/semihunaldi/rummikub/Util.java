package com.semihunaldi.rummikub;

import com.google.common.collect.Lists;
import com.semihunaldi.rummikub.tiles.Tile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
}
