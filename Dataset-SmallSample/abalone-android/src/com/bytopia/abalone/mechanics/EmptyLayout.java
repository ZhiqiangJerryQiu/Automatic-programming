/**
* Copyright (c) 2010-2011 Yaroslav Geryatovich, Alexander Yakushev
* Distributed under the GNU GPL v3. For full terms see the file docs/COPYING.
*/
package com.bytopia.abalone.mechanics;

/**
 * Class that constructs a empty Abalone board.
 * 
 * @author Bytopia
 */
public class EmptyLayout extends Layout {

	/**
	 * Array that contains the cell description for the starting position.
	 */
	private byte[][] start = { 
			{ N, N, N, N, N, N, N, N, N, N, N },
			{ N, E, E, E, E, E, N, N, N, N, N }, // A
			{ N, E, E, E, E, E, E, N, N, N, N }, // W
			{ N, E, E, E, E, E, E, E, N, N, N }, // C
			{ N, E, E, E, E, E, E, E, E, N, N }, // D
			{ N, E, E, E, E, E, E, E, E, E, N }, // E
			{ N, N, E, E, E, E, E, E, E, E, N }, // F
			{ N, N, N, E, E, E, E, E, E, E, N }, // G
			{ N, N, N, N, E, E, E, E, E, E, N }, // H
			{ N, N, N, N, N, E, E, E, E, E, N }, // I
			{ N, N, N, N, N, N, N, N, N, N, N } };

	/**
	 * Returns the starting position.
	 */
	public byte[][] getBlackStartField() {
		return start;
	}

}
