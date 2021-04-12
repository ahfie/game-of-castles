package game.goals;

import game.Game;
import game.Goal;
import game.Player;
import game.map.Castle;

/**
 * @author Liang Zhao
 */
public class CapitalGoal extends Goal {

	public static final int[] capitalTroops = { 20, 40, 60 };
	public static final int[] maxRounds = { 20, 30, 40 };
	private Player winnerCapital;
	private Player winnerConquer;

	public CapitalGoal() {
		super("Capital Modus",
				"Nachdem der Spieler eine bestimmte Anzahl von Truppen in einem Burgen innerhalb von 20 Runde, können Sie gewinnen.");
		winnerCapital = null;
		winnerConquer = null;
	}

	/**
	 * check if the game is completed if the game has run more than maxRound rounds,
	 * then the game is completed if one player has conquered all the castles, the
	 * game is completed if one player has finished the goal of capital mode, the
	 * game is completed
	 */
	@Override
	public boolean isCompleted() {
		Game game = this.getGame();
		winnerCapital = getCapitalWinner(game);
		winnerConquer = getConquerWinner(game);

		if (game.getRound() >= maxRounds[game.getMapSize().ordinal()]) {
			return true;
		}

		if (winnerCapital != null || winnerConquer != null)
			return true;
		return false;
	}

	/**
	 * if the player has reached the necessary number of troops in one castle within
	 * maxRounds rounds, then he will become the winner
	 * 
	 * @return get the winner of the game
	 */
	@Override
	public Player getWinner() {
		if (winnerCapital != null)
			return winnerCapital;
		if (winnerConquer != null)
			return null;
		return null;
	}

	private Player getCapitalWinner(Game game) {
		if (game.getRound() < 2)
			return null;
		// check kill modus winner
		for (Player p : game.getPlayers()) {
			if (p.getNumCapitalTroops() >= capitalTroops[game.getMapSize().ordinal()]) {
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
		winnerCapital = null;
		winnerConquer = null;
	}

	@Override
	public String getExplain() {
		int n = getGame().getMapSize().ordinal();
		return String.format("Goal: %s\n%s\nGezielte eingesetzte Truppenanzahl: %d\nRundelimit : %d\n", getName(),
				getDescription(), capitalTroops[n], maxRounds[n]);
	}

	@Override
	public String getEndMessage(Player winner) {
		if (winner != null)
			return "\n%PLAYER%" + String.format(" hat %d Truppen in seiner Hauptstadt gesammelt!",
					capitalTroops[getGame().getMapSize().ordinal()]);
		if (getCapitalWinner(getGame()) != null)
			return "\nAlle Burgen sind erobert und niemand hat das Ziel schon erreicht.";
		return "\nNiemand hat das Ziel vor der Rundelimit erreicht.";
	}
}
