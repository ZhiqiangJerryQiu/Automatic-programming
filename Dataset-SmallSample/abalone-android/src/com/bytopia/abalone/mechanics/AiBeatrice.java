/**
* Copyright (c) 2010-2011 Yaroslav Geryatovich, Alexander Yakushev
* Distributed under the GNU GPL v3. For full terms see the file docs/COPYING.
*/
package com.bytopia.abalone.mechanics;

/**
 * Weak configuration of AI. Uses two-step depth analysis, 25% of the time
 * performs stupid moves. Level "Normal".
 * 
 * @author Bytopia
 */
public class AiBeatrice extends ArtificialIntilligence {

	public Move requestMove(Game g) {
		return findNextMove(g.getBoard(), g.getSide(), 2, STUPID, false);
	}
}
