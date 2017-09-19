/**
* Copyright (c) 2010-2011 Yaroslav Geryatovich, Alexander Yakushev
* Distributed under the GNU GPL v3. For full terms see the file docs/COPYING.
*/
package com.bytopia.abalone.mechanics;

public class TestLayout extends Layout {

	private static byte[][] start = { 
			{ N, N, N, N, N, N, N, N, N, N, N },
			{ N, E, E, E, E, E, N, N, N, N, N }, // A
			{ N, E, E, E, W, W, E, N, N, N, N }, // B
			{ N, E, E, W, E, W, W, W, N, N, N }, // C
			{ N, E, E, W, W, W, W, B, E, N, N }, // D
			{ N, E, E, W, B, B, W, B, E, E, N }, // E
			{ N, N, E, E, E, B, B, W, E, E, N }, // F
			{ N, N, N, E, E, B, B, B, B, E, N }, // G
			{ N, N, N, N, E, E, E, B, B, E, N }, // H
			{ N, N, N, N, N, E, E, E, E, B, N }, // I
			{ N, N, N, N, N, N, N, N, N, N, N } };

	public byte[][] getBlackStartField() {
		return start;
	}

}
