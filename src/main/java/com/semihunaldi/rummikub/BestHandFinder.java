package com.semihunaldi.rummikub;

import com.semihunaldi.rummikub.tiles.Tile;

import java.util.List;

/**
 * Created by semihunaldi on 13.04.2018
 */
public class BestHandFinder {

	public static Player findBestHand(HandDistribution handDistribution) {
		printHands(handDistribution);
		Player player1 = handDistribution.getPlayer1();
		Player player2 = handDistribution.getPlayer2();
		Player player3 = handDistribution.getPlayer3();
		Player player4 = handDistribution.getPlayer4();
		//TODO
		return null;
	}

	private static void printHands(HandDistribution handDistribution) {
		for(Player player : handDistribution.getPlayers()){
			List<Tile> hand = player.getHand();
			System.out.println("Player Number : " + player.getPlayerNumber());
			System.out.println(hand);
			System.out.println();
		}
	}
}
