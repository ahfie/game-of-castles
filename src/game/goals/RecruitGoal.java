package game.goals;

import game.Game;
import game.Goal;
import game.Player;
import game.map.Castle;

public class RecruitGoal extends Goal {

	public static final int[] recruitedTroops = { 30, 60, 100 };
	public static final int[] maxRounds = { 20, 30, 40 };
	private Player winnerRecruit;
	private Player winnerConquer;

	public RecruitGoal() {
		super("Recruit Modus",
				"Gewinnt der Spieler, nachdem der Spieler eine bestimmte Anzahl von Truppen innerhalb von 20 Runde eingestellt hat.");
		winnerRecruit = null;
		winnerConquer = null;
	}

	/**
	 * check if the game is completed if the game has run more than maxRound rounds,
	 * then the game is completed if one player has conquered all the castles, the
	 * game is completed if one player has finished the goal of recruit mode, the
	 * game is completed
	 */
	@Override
	public boolean isCompleted() {
		Game game = this.getGame();
		winnerRecruit = getRecruitWinner(game);
		winnerConquer = getConquerWinner(game);

		if (game.getRound() >= maxRounds[game.getMapSize().ordinal()]) {
			return true;
		}
		if (winnerRecruit != null || winnerConquer != null)
			return true;
		return false;
	}

	/**
	 * if the player has recruited the necessary number of troops within maxRounds
	 * rounds, then he will become the winner
	 * 
	 * @return get the winner of the game
	 */
	@Override
	public Player getWinner() {
		if (winnerRecruit != null)
			return winnerRecruit;
		if (winnerConquer != null)
			return null;
		return null;
	}

	private Player getRecruitWinner(Game game) {
		if (game.getRound() < 2)
			return null;
		// check kill modus winner
		for (Player p : game.getPlayers()) {
			if (p.getNumDefeatedTroops() >= recruitedTroops[game.getMapSize().ordinal()]) {
				return p;
			}
		}
		return null;
	}

	private Player getConquerWinner(Game game) {
		// check conquer winner
		if (game.getRound() < 2)
			return null;
		Player p = null;
		for (Castle c : game.getMap().getCastles()) {
			if (c.getOwner() == null)
				return null;
			else if (p == null)
				p = c.getOwner();
			else if (p != c.getOwner())
				return null;
		}
		return p;
	}

	/**
	 * @return true if the given player is not the winner, else false
	 */
	@Override
	public boolean hasLost(Player player) {
		if (getGame().getRound() < 2)
			return false;
		if (player.getNumRegions(getGame()) == 0)
			return true;
		return false;
	}

	@Override
	public void reset() {
		winnerRecruit = null;
		winnerConquer = null;
	}

	@Override
	public String getExplain() {
		int n = getGame().getMapSize().ordinal();
		return String.format("Goal: %s\n%s\nGezielte eingesetzte Truppenanzahl: %d\nRundelimit: %d\n", getName(),
				getDescription(), recruitedTroops[n], maxRounds[n]);
	}

	@Override
	public String getEndMessage(Player winner) {
		if (winner != null)
			return "\n%PLAYER%"
					+ String.format(" hat %d Truppen eingesetzt!", recruitedTroops[getGame().getMapSize().ordinal()]);
		if (getRecruitWinner(getGame()) != null)
			return "\nAlle Burgen sind erobert und niemand hat das Ziel schon erreicht.";
		return "\nNiemand hat das Ziel vor der Eroberung/Rundelimit erreicht.";
	}
}