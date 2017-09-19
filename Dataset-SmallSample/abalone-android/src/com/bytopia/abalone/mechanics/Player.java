/**
* Copyright (c) 2010-2011 Yaroslav Geryatovich, Alexander Yakushev
* Distributed under the GNU GPL v3. For full terms see the file docs/COPYING.
*/
package com.bytopia.abalone.mechanics;

/**
 * Interface that describes how Player objects (Controller entities in MVC
 * pattern) should work.
 * 
 * @author Bytopia
 */
public interface Player {

	/**
	 * Returns the move chosen by player for the given game.
	 * 
	 * @param g
	 *            game currently played
	 * @return move that player wants to make
	 */
	public Move requestMove(Game g);

}
