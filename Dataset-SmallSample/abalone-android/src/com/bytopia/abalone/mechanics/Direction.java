/**
* Copyright (c) 2010-2011 Yaroslav Geryatovich, Alexander Yakushev
* Distributed under the GNU GPL v3. For full terms see the file docs/COPYING.
*/
package com.bytopia.abalone.mechanics;

/**
 * Pseudo-enumeration that represents possible all directions of movement.
 * 
 * @author Bytopia
 */
public class Direction {

	public final static byte NorthWest = 0;
	public final static byte North = 1;
	public final static byte East = 2;
	public final static byte West = 3;
	public final static byte South = 4;
	public final static byte SouthEast = 5;

	public final static byte[] storage = { NorthWest, North, East, West, South,
			SouthEast };
	public final static byte[] opposite = { SouthEast, South, West, East, North,
			NorthWest };

	/**
	 * Private constructor to ensure that class is service-only.
	 */
	private Direction() {
	}

}
