package game.goals;

import game.Game;
import game.Goal;
import game.Player;
import game.map.Castle;

public class ConquerGoalJoker extends Goal {

	public ConquerGoalJoker() {
		super("Eroberung with Joker", "Derjenige Spieler gewinnt, der als erstes alle Gebiete erobert hat.");
	}

	@Override
	public boolean isCompleted() {
		return this.getWinner() != null;
	}

	@Override
	public Player getWinner() {
		Game game = this.getGame();
		if (game.getRound() < 2)
			return null;

		Player p = null;
		/*
		 * check if all the castles belongs to the same player
		 */
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
		return player.getNumRegions(getGame()) == 0;
	}

	@Override
	public void reset() {
		// do nothing
	}

	@Override
	public String getExplain() {
		return String.format(
				"Goal: %s\n%s\nJe weniger Burge wird besitzt, desto mehr Truppen wird mit dem Joker erhalten!\n"
						+ "Um die Jokertruppen einzusetzen, klicken Sie das Icon auf einer Burg! Die Joker darf nur ab 7. Runde genutzt werden.",
				getName(), getDescription());
	}

	@Override
	public String getEndMessage(Player winner) {
		if (winner != null)
			return "\n%PLAYER% hat die letzte Burg erobert!";
		return "\n";
	}
}
