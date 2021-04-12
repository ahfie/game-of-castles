package game;

import java.io.PrintWriter;
import java.util.Date;

/**
 * Diese Klasse stellt einen Eintrag in der Bestenliste dar. Sie enthält den
 * Namen des Spielers, das Datum, die erreichte Punktzahl sowie den Spieltypen.
 */
public class ScoreEntry implements Comparable<ScoreEntry> {

	private String name;
	private Date date;
	private int score;
	private String gameType;

	/**
	 * Erzeugt ein neues ScoreEntry-Objekt
	 * 
	 * @param name     der Name des Spielers
	 * @param score    die erreichte Punktzahl
	 * @param date     das Datum
	 * @param gameGoal der Spieltyp
	 */
	private ScoreEntry(String name, int score, Date date, String gameGoal) {
		this.name = name;
		this.score = score;
		this.date = date;
		this.gameType = gameGoal;
	}

	/**
	 * Erzeugt ein neues ScoreEntry-Objekt
	 * 
	 * @param player   der Spieler
	 * @param gameGoal der Spieltyp
	 */
	public ScoreEntry(Player player, Goal gameGoal) {
		this.name = player.getName();
		this.score = player.getPoints();
		this.date = new Date();
		this.gameType = gameGoal.getName();
	}

	@Override
	public int compareTo(ScoreEntry scoreEntry) {
		return Integer.compare(this.score, scoreEntry.score);
	}

	/**
	 * Schreibt den Eintrag als neue Zeile mit dem gegebenen {@link PrintWriter}
	 * Der Eintrag sollte im richtigen Format gespeichert werden.
	 * @see #read(String)
	 * @see Date#getTime()
	 * 
	 * @author Liang Zhao
	 * @param printWriter der PrintWriter, mit dem der Eintrag geschrieben wird
	 */
	public void write(PrintWriter printWriter) {
		// TODO: ScoreEntry#write(PrintWriter)
		printWriter.println();
		printWriter.write(this.name);

		printWriter.append(";");
		long dateLong = this.date.getTime();
		String strLong = Long.toString(dateLong);

		printWriter.append(strLong);

		printWriter.append(";");

		String scoreString = Integer.toString(this.score);
		printWriter.append(scoreString);

		printWriter.append(";");
		printWriter.append(this.gameType);

		printWriter.flush();

	}

	/**
	 * List eine gegebene Zeile ein und wandelt dies in ein ScoreEntry-Objekt um.
	 * Ist das Format der Zeile ungültig oder enthält es ungültige Daten, wird null
	 * zurückgegeben. Eine gültige Zeile enthält in der Reihenfolge durch Semikolon
	 * getrennt: den Namen, das Datum als Unix-Timestamp (in Millisekunden), die
	 * erreichte Punktzahl, den Spieltypen Gültig wäre beispielsweise:
	 * "Florian;1546947397000;100;Eroberung"
	 *
	 *
	 * @see String#split(String)
	 * @see Long#parseLong(String)
	 * @see Integer#parseInt(String)
	 * @see Date#Date(long)
	 * 
	 * @author Liang Zhao
	 * @param line Die zu lesende Zeile
	 * @return Ein ScoreEntry-Objekt oder null
	 */
	public static ScoreEntry read(String line) {
		// TODO: ScoreEntry#read(String)
		String[] buff = line.split(";");
		if (buff.length != 4)
			return null;
		try {
			String name = buff[0];
			long dateLong = Long.parseLong(buff[1]);
			Date date = new Date(dateLong);
			int scoreInt = Integer.parseInt(buff[2]);
			String gameGoal = buff[3];
			ScoreEntry newScoreEntry = new ScoreEntry(name, scoreInt, date, gameGoal);
			return newScoreEntry;
		} catch (Exception e) {
			System.out.println(String.format("'%s' is not a valid format!", line));
			e.printStackTrace();
			return null;
		}
	}

	public Date getDate() {
		return date;
	}

	public String getName() {
		return this.name;
	}

	public int getScore() {
		return this.score;
	}

	public String getMode() {
		return this.gameType;
	}
}
