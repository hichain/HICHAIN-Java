package jp.hichain.prototype.basic;

import java.util.ArrayList;
import java.util.List;

import jp.hichain.prototype.concept.CardColor;

/**
 * プレイヤー
 * @author NT
 *
 */
public class Player {
	private static List<Player> players;

	private String name;
	private CardColor cardColor;
	private Move lastMove;

	public Player(String _name, CardColor _color) {
		name = _name;
		cardColor = _color;
	}

	static {
		players = new ArrayList<>();
	}

	public static void add(Player player) {
		players.add(player);
	}

	public static Player get(String name) {
		for (Player player : players) {
			if (player.name.equals(name)) {
				return player;
			}
		}
		return null;
	}

	public static Player get(CardColor color) {
		for (Player player : players) {
			if (player.cardColor == color) {
				return player;
			}
		}
		return null;
	}

	public String getName() {
		return name;
	}

	public CardColor getCardColor() {
		return cardColor;
	}

	public Move getLastMove() {
		return lastMove;
	}

	@Override
	public String toString() {
		return name;
	}
}
