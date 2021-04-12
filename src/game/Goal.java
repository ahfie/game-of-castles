package game;

public abstract class Goal {

	private Game game;
	private final String description;
	private final String name;

	public Goal(String name, String description) {
		this.name = name;
		this.description = description;
	}

	public void setGame(Game game) {
		this.game = game;
	}

	/**
	 * Check whether the game is completed.
	 * 
	 * @return whether game is complete or not
	 */
	public abstract boolean isCompleted();

	/**
	 * Check whether a player has achieved the goal.
	 * 
	 * @return winner of the game, otherwise null if there are no winners yet.
	 */
	public abstract Player getWinner();

	/**
	 * Check if player has lost the game. If yes, the player will be removed from
	 * the player queue.
	 * 
	 * @param player
	 * @return whether player has lost the game or not
	 */
	public abstract boolean hasLost(Player player);

	/**
	 * @author Jonathan Geraldi Joewono
	 * @return Text to show in the log at the beginning of the game, explaining
	 *         certain win conditions
	 */
	
	/**
	 * reset all parameters
	 */
	public abstract void reset();
	
	/**
	 * Print (optional) message in log when the game ends
	 * Useful when there are more than one goal conditions available.
	 * 
	 * @author Jonathan Geraldi Joewono
	 * @return winner message
	 */
	public abstract String getEndMessage(Player winner);
	
	/**
	 * Print explanation of goal conditions
	 * @return
	 */
	public String getExplain() {
		return String.format("Goal: %s\n%s\n", getName(), getDescription());
	}
	

	public final String getDescription() {
		return this.description;
	}

	public final String getName() {
		return this.name;
	}

	protected Game getGame() {
		return this.game;
	}
}
