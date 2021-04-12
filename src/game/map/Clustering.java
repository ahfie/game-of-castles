package game.map;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.awt.Point;

/**
 * Diese Klasse teilt Burgen in Königreiche auf
 */
public class Clustering {

	private Random random;
	private final List<Castle> allCastles;
	private final int kingdomCount;

	/**
	 * Ein neues Clustering-Objekt erzeugen.
	 * 
	 * @param castles      Die Liste von Burgen, die aufgeteilt werden sollen
	 * @param kingdomCount Die Anzahl von Königreichen die generiert werden sollen
	 */
	public Clustering(List<Castle> castles, int kingdomCount) {
		if (kingdomCount < 2)
			throw new IllegalArgumentException("Ungültige Anzahl an Königreichen");

		this.random = new Random();
		this.kingdomCount = kingdomCount;
		this.allCastles = Collections.unmodifiableList(castles);
	}

	/**
	 * Gibt eine Liste von Königreichen zurück. Jedes Königreich sollte dabei einen
	 * Index im Bereich 0-5 bekommen, damit die Burg richtig angezeigt werden kann.
	 * Siehe auch {@link Kingdom#getType()}
	 * 
	 * @author Jonathan Geraldi Joewono
	 * @return list of kingdoms after clustered
	 */
	public List<Kingdom> getPointsClusters() {
		// TODO Clustering#getPointsClusters()
		ArrayList<List<Castle>> kingdoms = new ArrayList<>(kingdomCount);
		Map<Integer, Point> kingdomCenters = new HashMap<Integer, Point>();
		for (int i = 0; i < kingdomCount; i++) {
			kingdoms.add(new ArrayList<Castle>());
			kingdomCenters.put(i, new Point(allCastles.get(random.nextInt(allCastles.size())).getLocationOnMap().x,
					allCastles.get(random.nextInt(allCastles.size())).getLocationOnMap().y));
		}
		boolean finished = false;
		while (!finished) {
			finished = true;
			// Generate new center points for each kingdom
			for (List<Castle> kingdom : kingdoms) {
				if (!kingdom.isEmpty()) {
					// average of its castles
					double sumX = 0;
					double sumY = 0;
					for (Castle castle : kingdom) {
						sumX += castle.getLocationOnMap().x;
						sumY += castle.getLocationOnMap().y;
					}
					int avgX = (int) (sumX / kingdom.size());
					int avgY = (int) (sumY / kingdom.size());
					kingdomCenters.put(kingdoms.indexOf(kingdom), new Point(avgX, avgY));
				}
			}
			for (Castle castle : allCastles) {
				List<Castle> kingdomOfCastle = kingdoms.stream().filter(n -> n.contains(castle)).findFirst()
						.orElseGet(() -> null);
				double distanceToKingdom;
				if (kingdomOfCastle != null)
					distanceToKingdom = castle.distance(kingdomCenters.get(kingdoms.indexOf(kingdomOfCastle)));
				else
					distanceToKingdom = Double.MAX_VALUE;
				Integer nearest = -1;
				for (Integer i = 0; i < kingdomCount; i++) {
					double newDistance = castle.distance(kingdomCenters.get(i));
					if (newDistance < distanceToKingdom) {
						nearest = i;
						distanceToKingdom = newDistance;
					}
				}
				if (nearest != -1) {
					finished = false;
					if (kingdomOfCastle != null)
						kingdomOfCastle.remove(castle);
					kingdoms.get(nearest).add(castle);
				}
			}
		}
		List<Kingdom> result = new ArrayList<>(kingdomCount);
		for (int i = 0; i < kingdomCount; i++) {
			Kingdom newKingdom = new Kingdom(i);
			for (Castle castle : kingdoms.get(i)) {
				castle.setKingdom(newKingdom);
			}
			result.add(newKingdom);
		}
		return result;
	}
}
