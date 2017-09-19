/**
* Copyright (c) 2010-2011 Yaroslav Geryatovich, Alexander Yakushev
* Distributed under the GNU GPL v3. For full terms see the file docs/COPYING.
*/
package com.bytopia.abalone.mechanics;

/**
 * Class that represents an arbitrary group of cells.
 * 
 * @author Bytopia
 */
public class Group {

	/**
	 * Array of cell instances.
	 */
	private Cell[] cells;

	/**
	 * Length of a group for convenience purposes.
	 */
	private int size;

	/**
	 * Constructs a group of one cell.
	 * 
	 * @param cell
	 *            cell instance
	 */
	public Group(Cell cell) {
		cells = new Cell[1];
		cells[0] = cell;
		size = 1;
	}

	/**
	 * Constructs a group from the interval of cells.
	 * 
	 * @param start
	 *            beginning cell of the interval
	 * @param end
	 *            ending cell of the interval
	 */
	public Group(Cell start, Cell end) {
		size = cellsInRange(start, end) + 1;
		cells = new Cell[size];
		cells[0] = start;
		cells[size - 1] = end;
		if (size >= 3 && start.isOnAnyLine(end)) {
			int rowStep = (end.row - start.row) / (size - 1);
			int columnStep = (end.column - start.column) / (size - 1);
			for (int i = 1; i < size - 1; i++) {
				cells[i] = Cell.storage[start.row + (i * rowStep)][start.column
						+ (i * columnStep)];
			}
		}
	}

	/**
	 * Returns the first cell in the group.
	 * 
	 * @return cell instance
	 */
	public Cell getFirstEnd() {
		return cells[0];
	}

	/**
	 * Returns the last cell in the group.
	 * 
	 * @return cell instance
	 */
	public Cell getSecondEnd() {
		return cells[size - 1];
	}

	/**
	 * Returns the cells of group in the form of array.
	 * 
	 * @return array containing cells from the group
	 */
	public Cell[] getCells() {
		return cells;
	}

	/**
	 * Tests if the group consists only of one cell.
	 * 
	 * @return true if the group has only one cell, false otherwise
	 */
	public boolean isAtom() {
		return size == 1;
	}

	/**
	 * Returns the length - the number of cells - of the group.
	 * 
	 * @return amount of cells
	 */
	public int getSize() {
		return size;
	}

	/**
	 * Returns the number of cells in a given interval.
	 * 
	 * @param start
	 *            beginning cell of the interval
	 * @param end
	 *            ending cell of the interval
	 * @return amount of cells in the interval
	 */
	private static int cellsInRange(Cell start, Cell end) {
		return Math.max(Math.abs(start.row - end.row), Math.abs(start.column
				- end.column));
	}

	/**
	 * Returns a new group such as if a current group was moved (shifted) in a
	 * given direction.
	 * 
	 * @param d
	 *            direction of movement
	 * @return shifted group
	 */
	public Group shift(byte d) {
		if (isAtom())
			return new Group(cells[0].shift(d));
		else
			return new Group(cells[0].shift(d), cells[size - 1].shift(d));
	}

	/**
	 * Tests if the group has the same direction as the given direction.
	 * 
	 * @param d
	 *            another direction
	 * @return true if their directions are the same, false otherwise
	 */
	public boolean isLineDirected(byte d) {
		if (isAtom())
			return false;
		return cells[0].isOnLine(cells[size - 1], d);
	}

	/**
	 * Returns a string representing the group in a form of "start-end", e.g.
	 * "A1-C3"
	 */
	@Override
	public String toString() {
		if (isAtom()) {
			return cells[0].toString();
		} else {
			return cells[0].toString() + "-" + cells[size - 1].toString();
		}
	}

	/**
	 * Returns the first element of the group for the given direction of moving.
	 * 
	 * @param d
	 *            direction in which group should be moved
	 * @return first cell of the group in this direction
	 */
	public Cell getPeak(byte d) {
		Cell cell;
		switch (d) {
		case Direction.NorthWest:
		case Direction.North:
			cell = cells[0].row < cells[1].row ? cells[0] : cells[1];
			if (size == 3)
				cell = cell.row < cells[2].row ? cell : cells[2];
			break;
		case Direction.East:
			cell = cells[0].column > cells[1].column ? cells[0] : cells[1];
			if (size == 3)
				cell = cell.column > cells[2].column ? cell : cells[2];
			break;
		case Direction.SouthEast:
		case Direction.South:
			cell = cells[0].row > cells[1].row ? cells[0] : cells[1];
			if (size == 3)
				cell = cell.row > cells[2].row ? cell : cells[2];
			break;
		case Direction.West:
			cell = cells[0].column < cells[1].column ? cells[0] : cells[1];
			if (size == 3)
				cell = cell.column < cells[2].column ? cell : cells[2];
			break;
		default:
			cell = null;
		}
		return cell;
	}

	/**
	 * Tests if the group contains the given cell.
	 * 
	 * @param c
	 *            arbitrary cell
	 * @return true if the group contains the cell, false otherwise
	 */
	public boolean isCellInGroup(Cell c) {
		for (int i = 0; i < size; i++) {
			if (cells[i].equals(c))
				return true;
		}
		return false;
	}

}
