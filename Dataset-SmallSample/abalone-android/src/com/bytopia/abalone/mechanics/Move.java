/**
* Copyright (c) 2010-2011 Yaroslav Geryatovich, Alexander Yakushev
* Distributed under the GNU GPL v3. For full terms see the file docs/COPYING.
*/
package com.bytopia.abalone.mechanics;

/**
 * Class that represents a process of moving - the group which is moved, new
 * position of this group and so on.
 * 
 * @author Bytopia
 */
public class Move {

	/**
	 * Source group that is moved.
	 */
	private Group source;

	/**
	 * New position of the moved group.
	 */
	private Group destination;

	/**
	 * Direction in which the group was moved.
	 */
	private byte direction;

	/**
	 * Side that performs a move.
	 */
	private byte side;

	/**
	 * Constructs a move of the given group in a given destination made by
	 * player of a given side.
	 * 
	 * @param group
	 *            group that is moved
	 * @param d
	 *            direction in which it is moved
	 * @param side
	 *            side that moves the group
	 */
	public Move(Group group, byte d, byte side) {
		source = group;
		destination = group.shift(d);
		direction = d;
		this.side = side;
	}

	/**
	 * Returns the group that is moved.
	 * 
	 * @return group being moved
	 */
	public Group getSource() {
		return source;
	}

	/**
	 * Returns the new position of the moved group.
	 * 
	 * @return group after the movement.
	 */
	public Group getDestination() {
		return destination;
	}

	/**
	 * Returns the direction in which group is moved.
	 * 
	 * @return direction of movement
	 */
	public byte getDirection() {
		return direction;
	}

	/**
	 * Returns the side that moves the group.
	 * 
	 * @return player side
	 */
	public byte getSide() {
		return side;
	}

	/**
	 * Tests if the move performed is a pushing move.
	 * 
	 * @return true if the move is pushing, false if the move is leaping
	 */
	public boolean isPushing() {
		return source.isLineDirected(direction);
	}

	/**
	 * Returns the peak of the moved group.
	 * 
	 * @return peak cell of the move
	 */
	public Cell getPeak() {
		return source.getPeak(direction);
	}

	/**
	 * Returns the tail of the moved group.
	 * 
	 * @return tail cell of the move
	 */
	public Cell getTail() {
		return source.getPeak(Direction.opposite[direction]);
	}

	/**
	 * Returns a string representing the movement in a form of
	 * "source direction", e.g. "A1-C3 SouthEast"
	 */
	@Override
	public String toString() {
		return source.toString() + " " + direction;
	}
	
}
