package game.players;

import java.awt.Color;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import base.Edge;
import base.Graph;
import base.Node;
import game.AI;
import game.Game;
import game.map.Castle;
import game.map.Kingdom;
import gui.AttackThread;

/**
 * @author Wu Fei Yang
 */
public class ErweitertAI extends AI {
	public ErweitertAI(String name, Color color) {
		super(name, color);
	}

	private Castle getCastleWithFewestTroops(List<Castle> castles) {
		Castle fewestTroops = castles.get(0);
		for (Castle castle : castles) {
			if (castle.getTroopCount() < fewestTroops.getTroopCount()) {
				fewestTroops = castle;
			}
		}

		return fewestTroops;
	}

	@Override
	protected void actions(Game game) throws InterruptedException {
		// AI soll am Anfang alle Burgen,die dem selben Koenigreich gehoeren, auswaehlen
		if (game.getRound() == 1) {
			List<Castle> availableCastles = game.getMap().getCastles().stream().filter(c -> c.getOwner() == null)
					.collect(Collectors.toList());

			List<Castle> myCastle = this.getCastles(game);
			if (myCastle.size() == 0) {
				Castle randomCastle = availableCastles.remove(this.getRandom().nextInt(availableCastles.size()));

				List<Castle> castleHasOwner = game.getMap().getCastles().stream().filter(c -> c.getOwner() != null)
						.collect(Collectors.toList());
				castleHasOwner = castleHasOwner.stream().filter(c -> c.getOwner() != this).collect(Collectors.toList());

				List<Kingdom> listOfKingdom = new ArrayList<>();

				if (castleHasOwner.size() == 0) {
					game.chooseCastle(randomCastle, this);
				} else {
					for (Castle c : castleHasOwner) {
						if (listOfKingdom.size() == 0) {
							listOfKingdom.add(c.getKingdom());
						} else {
							if (!c.getKingdom().equals(listOfKingdom.get(listOfKingdom.size() - 1))) {
								listOfKingdom.add(c.getKingdom());
							}
						}
					}
					while (listOfKingdom.contains(randomCastle.getKingdom())) {
						randomCastle = availableCastles.remove(this.getRandom().nextInt(availableCastles.size()));
					}
					game.chooseCastle(randomCastle, this);
				}

				while (availableCastles.size() > 0 && getRemainingTroops() > 0) {

					sleep(1000);

					for (Castle c : availableCastles) {
						if (c.getKingdom().equals(randomCastle.getKingdom())) {
							availableCastles.remove(c);
							game.chooseCastle(c, this);
							break;
						}
					}
				}
			} else {
				int b = 0;
				for (Castle c : availableCastles) {
					if (c.getKingdom().equals(myCastle.get(myCastle.size() - 1).getKingdom())) {
						b++;
					}
				}
				while (availableCastles.size() > 0 && getRemainingTroops() > 0) {

					sleep(1000);

					if (b != 0) {
						for (Castle ca : availableCastles) {
							if (ca.getKingdom().equals(myCastle.get(myCastle.size() - 1).getKingdom())) {

								availableCastles.remove(ca);
								game.chooseCastle(ca, this);
								b--;
								break;
							}
						}
					} else {
						game.chooseCastle(availableCastles.remove(this.getRandom().nextInt(availableCastles.size())),
								this);
					}
				}
			}
		} else {
			// AI soll die Trupen an denjenigen Burg uebergeben,der wenige Finde hat.
			// 1. Distribute remaining troops
			Graph<Castle> graph = game.getMap().getGraph();

			List<Castle> castleNearEnemy = new ArrayList<>();
			// new
			Map<Integer, Castle> castleNearEnemyMap = new HashMap<>();

			for (Castle castle : this.getCastles(game)) {

				Node<Castle> node = graph.getNode(castle);

				int enemySize = 0;// new
				boolean hasEnemy = false;
				for (Edge<Castle> edge : graph.getEdges(node)) {
					Castle otherCastle = edge.getOtherNode(node).getValue();
					if (otherCastle.getOwner() != this) {
						enemySize++; // new
						if (hasEnemy == false) {
							hasEnemy = true;
						}
						/*
						 * castleNearEnemy.add(castle); break;
						 */
					}
				}
				if (hasEnemy == true) {
					castleNearEnemyMap.put(enemySize, castle);// new
				}
			}
			// new
			List<Integer> enemySizeList = new ArrayList<>();
			for (Entry<Integer, Castle> e : castleNearEnemyMap.entrySet()) {
				enemySizeList.add(e.getKey());
				castleNearEnemy.add(e.getValue());
			}

			Collections.sort(enemySizeList);// new
			Castle castleNeedAttck = castleNearEnemyMap.get(enemySizeList.get(0));// new
			castleNearEnemy.remove(castleNeedAttck);
			castleNearEnemy.add(0, castleNeedAttck);

			while (this.getRemainingTroops() > 0) {
				Castle fewestTroops = getCastleWithFewestTroops(castleNearEnemy);
				sleep(500);
				game.addTroops(this, fewestTroops, 1);
			}

			boolean attackWon;

			do {
				// 2. Move troops from inside to border
				for (Castle castle : this.getCastles(game)) {
					if (!castleNearEnemy.contains(castle) && castle.getTroopCount() > 1) {
						Castle fewestTroops = getCastleWithFewestTroops(castleNearEnemy);
						game.moveTroops(castle, fewestTroops, castle.getTroopCount() - 1);
					}
				}

				// 3. attack!
				attackWon = false;
				for (Castle castle : castleNearEnemy) {
					if (castle.getTroopCount() < 2)
						continue;

					Node<Castle> node = graph.getNode(castle);
					for (Edge<Castle> edge : graph.getEdges(node)) {
						Castle otherCastle = edge.getOtherNode(node).getValue();
						if (otherCastle.getOwner() != this && castle.getTroopCount() >= otherCastle.getTroopCount()) {
							AttackThread attackThread = game.startAttack(castle, otherCastle, castle.getTroopCount());
							if (fastForward)
								attackThread.fastForward();

							attackThread.join();
							attackWon = attackThread.getWinner() == this;
							break;
						}
					}

					if (attackWon)
						break;
				}
			} while (attackWon);
		}
	}
}
