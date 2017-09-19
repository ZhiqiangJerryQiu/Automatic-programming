/**
* Copyright (c) 2010-2011 Yaroslav Geryatovich, Alexander Yakushev
* Distributed under the GNU GPL v3. For full terms see the file docs/COPYING.
*/
package com.bytopia.abalone.mechanics;

/**
 * Class that constructs a "German daisy" Abalone starting position.
 * 
 * @author Bytopia
 */

public class GermanLayout extends Layout {

	/**
	 * Array that contains the cell description for the starting position.
	 */
	private byte[][] start = 
		  { { N, N, N, N, N, N, N, N, N, N, N },
			{ N, E, E, E, E, E, N, N, N, N, N }, // A
			{ N, W, W, E, E, B, B, N, N, N, N }, // W
			{ N, W, W, W, E, B, B, B, N, N, N }, // C
			{ N, E, W, W, E, E, B, B, E, N, N }, // D
			{ N, E, E, E, E, E, E, E, E, E, N }, // E
			{ N, N, E, B, B, E, E, W, W, E, N }, // F
			{ N, N, N, B, B, B, E, W, W, W, N }, // G
			{ N, N, N, N, B, B, E, E, W, W, N }, // H
			{ N, N, N, N, N, E, E, E, E, E, N }, // I
			{ N, N, N, N, N, N, N, N, N, N, N } };

	/**
	 * Returns the starting position.
	 */
	public byte[][] getBlackStartField() {
		return start;
	}
	
}
