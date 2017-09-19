/**
* Copyright (c) 2010-2011 Yaroslav Geryatovich, Alexander Yakushev
* Distributed under the GNU GPL v3. For full terms see the file docs/COPYING.
*/
package com.bytopia.abalone.mechanics;

/**
 * The most weak configuration of AI. Uses one-step depth analysis. Level
 * "Easy".
 * 
 * @author Bytopia
 */
public class AiAnn extends ArtificialIntilligence {

	public Move requestMove(Game g) {
		return findNextMove(g.getBoard(), g.getSide(), 1, CLEVER, false);
	}
}
