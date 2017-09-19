/**
* Copyright (c) 2010-2011 Yaroslav Geryatovich, Alexander Yakushev
* Distributed under the GNU GPL v3. For full terms see the file docs/COPYING.
*/
package com.bytopia.abalone.mechanics;

/**
 * Service class for debugging from the PC.
 * 
 * @author Bytopia
 */
public class ConsoleWatcher implements Watcher {

	/**
	 * Game instance being watched.
	 */
	private Game game;
	private int moveCount = 0; 

	/**
	 * Sets the game to watch.
	 * 
	 * @param g
	 *            Abalone game
	 */
	public void setGame(Game g) {
		game = g;
	}

	/**
	 * Updates the information about the game.
	 */
	public void updateView() {
		System.out.println("" + ++moveCount + ". Blacks: " + game.getBoard().getMarblesCaptured(Side.WHITE)
				+ " Whites: " + game.getBoard().getMarblesCaptured(Side.BLACK));
		System.out.println(game.getBoard());
	}

	/**
	 * Shows animation. Not used.
	 */
	public void doAnimation(MoveType moveType, byte direction) {
	}

	/**
	 * Triggers when one of the sides wins.
	 */
	public void win(byte side) {
		System.exit(0);
	}

	/**
	 * Triggers when one of the side captures enemy's marble. Not used.
	 */
	public void marbleCaptured(byte side) {
	}

}
