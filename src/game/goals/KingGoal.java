package game.goals;

import java.awt.Color;
import java.util.Random;
import game.Goal;
import game.Player;
import game.map.Castle;
import game.players.Human;

/**
 * Control a random chosen castle by the end of 20th (or 30th or 40th) round to
 * win. The castle starts as neutral and has 5 troops to defend itself. The
 * castle can be identified with a black marker and starts with a gray color.
 * 
 * @author Jonathan Geraldi Joewono
 */
public class KingGoal extends Goal {

	private static final int[] roundLimit = { 20, 30, 40 };
	private static final Player defaultOwner = new Human("Keine", Color.GRAY);
	private Castle hill;

	public KingGoal() {
		super("King of the Hill",
				"Eine Burg wird zufällig ausgewählt. Derjenige Spieler, der diese Burg am Ende des Spiels kontrolliert, gewinnt.");
	}

	@Override
	public boolean isCompleted() {
		if (getGame().getRound() >= roundLimit[getGame().getMapSize().ordinal()]) {
			// play until all players in last round have a turn
			if (!getGame().getPlayerQueue().iterator().hasNext())
				return true;
			else if (getGame().getPlayerQueue().iterator().next() == getGame().getStartingPlayer())
				return true;
			return false;
		} else {
			// check if conquered before
			Player p = null;
			for (Castle c : getGame().getMap().getCastles()) {
				if (c.getOwner() == null)
					return false;
				else if (p == null)
					p = c.getOwner();
				else if (p != c.getOwner())
					return false;
			}
			return true;
		}
	}

	@Override
	public Player getWinner() {
		Player winner = hill.getOwner();
		if (winner != defaultOwner)
			return winner;
		else
			return null;
	}

	@Override
	public boolean hasLost(Player player) {
		if (getGame().getRound() < 2)
			return false;
		return player.getNumRegions(getGame()) == 0;
	}

	public void generateHill() {
		if (hill == null) {
			int n = new Random().nextInt(getGame().getMap().getCastles().size());
			hill = getGame().getMap().getCastles().get(n);
			hill.setOwner(defaultOwner);
			hill.addTroops(5);
		}
	}

	public Castle getHill() {
		return hill;
	}

	public void reset() {
		hill = null;
	}

	@Override
	public String getExplain() {
		generateHill();
		return String.format("Goal: %s\n%s\nEroberungsziel: %s\nRundelimit: %d\n", getName(), getDescription(),
				getHill().getName(), roundLimit[getGame().getMapSize().ordinal()]);
	}

	@Override
	public String getEndMessage(Player winner) {
		if (winner != null)
			return "\n%PLAYER% ist der Besitzer der Hill!";
		return "\nRundelimit wird erreicht!\nNiemand hat den Hill erobert.";
	}

}