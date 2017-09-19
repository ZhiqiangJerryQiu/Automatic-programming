/**
* Copyright (c) 2010-2011 Yaroslav Geryatovich, Alexander Yakushev
* Distributed under the GNU GPL v3. For full terms see the file docs/COPYING.
*/
package com.bytopia.abalone.mechanics;

/**
 * Normal configuration of AI. Uses two-step depth analysis. Level "Hard".
 * 
 * @author Bytopia
 */
public class AiCharlotte extends ArtificialIntilligence {

	public Move requestMove(Game g) {
		return findNextMove(g.getBoard(), g.getSide(), 2, CLEVER, false);
	}
}
