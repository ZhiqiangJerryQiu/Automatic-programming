/**
* Copyright (c) 2010-2011 Yaroslav Geryatovich, Alexander Yakushev
* Distributed under the GNU GPL v3. For full terms see the file docs/COPYING.
*/
package com.bytopia.abalone.mechanics;

/**
 * Class that represents a cell - coordinates of X and Y on the game board.
 * 
 * @author Bytopia
 * 
 */
public final class Cell {

	/**
	 * X coordinate of the cell.
	 */
	public int row;

	/**
	 * Y coordinate of the cell.
	 */
	public int column;

	/**
	 * An array that stores all possible cells on the board. Done in this way
	 * because of performance issues.
	 */
	public static Cell[][] storage = new Cell[12][12];

	/**
	 * An array that stores neighbouring cells for every cell in every
	 * direction.
	 */
	private static Cell[][][] shift = new Cell[12][12][6];

	/**
	 * And array that stores distances between every two cells.
	 */
	private static int[][][][] distances = new int[10][10][10][10];
	/**
	 * An array that stores minimal column value for each row.
	 */
	public final static int[] minColumn = { 1, 1, 1, 1, 1, 1, 2, 3, 4, 5, 6, 6 };

	/**
	 * An array that stores maximal column value for each row.
	 */
	public final static int[] maxColumn = { 5, 5, 6, 7, 8, 9, 9, 9, 9, 9, 10, 10 };

	/**
	 * Initializes cell, shift and distance arrays.
	 */
	static {
		// Initialize storage array
		for (int i = 0; i < 12; i++)
			for (int j = 0; j < 12; j++)
				storage[i][j] = new Cell(i, j);
		// Initialize shift array
		for (int i = 0; i < 12; i++)
			for (int j = 0; j < 12; j++)
				for (byte k = 0; k < 6; k++)
					shift[i][j][k] = shiftInit(i, j, k);
		// Initialized distance array
		for (int i = 1; i < 10; i++)
			for (int j = 1; j < 10; j++)
				for (int k = 1; k < 10; k++)
					for (int l = 1; l < 10; l++)
						distances[i][j][k][l] = distanceInit(i, j, k, l);
	}

	/**
	 * Constructs a new cell.
	 * 
	 * @param newRow
	 *            X coordinate of the cell
	 * @param newColumn
	 *            Y coordinate of the cell
	 */
	private Cell(int newRow, int newColumn) {
		row = newRow;
		column = newColumn;
	}

	/**
	 * Returns a cell that is next to given cell in a given direction. Used for initial array filling.
	 * 
	 * @param d
	 *            direction in which to search next cell
	 * @param row
	 *            row of the cell
	 * @param column
	 *            column of the cell
	 * @return cell instance
	 */
	private static Cell shiftInit(int row, int column, byte d) {
		switch (d) {
		case Direction.NorthWest:
			if (row >= 1 && column >= Cell.minColumn[row])
				return storage[row - 1][column - 1];
			break;
		case Direction.North:
			if (row >= 1)
				return storage[row - 1][column];
			break;
		case Direction.East:
			if (column <= Cell.maxColumn[row])
				return storage[row][column + 1];
			break;
		case Direction.SouthEast:
			if (row <= 9 && column <= Cell.maxColumn[row])
				return storage[row + 1][column + 1];
			break;
		case Direction.South:
			if (row <= 9)
				return storage[row + 1][column];
			break;
		case Direction.West:
			if (column >= Cell.minColumn[row])
				return storage[row][column - 1];
			break;
		}
		return storage[row][column];
	}

	/**
	 * Returns a cell that is next to this cell in a given direction.
	 * @param d direction in which to search next cell
	 * @return cell instance
	 */
	public Cell shift(byte d) {
		return shift[row][column][d];
	}
	
	/**
	 * Tests if this cell is on any line with a given cell.
	 * 
	 * @param c
	 *            another cell
	 * @return true if two cells are on the same line, false otherwise
	 */
	public boolean isOnAnyLine(Cell c) {
		for (byte d : Direction.storage) {
			if (isOnLine(c, d))
				return true;
		}
		return false;
	}

	/**
	 * Tests if this cell is on line of a given direction with a given cell.
	 * 
	 * @param c
	 *            another cell
	 * @param d
	 *            direction of the line
	 * @return true if two cells are on this line, false otherwise
	 */
	public boolean isOnLine(Cell c, byte d) {
		if (d == Direction.NorthWest || d == Direction.SouthEast) {
			return Math.abs(row - c.row) == Math.abs(column - c.column);
		} else if (d == Direction.North || d == Direction.South) {
			return Math.abs(column - c.column) == 0;
		} else
			return Math.abs(row - c.row) == 0;
	}

	/**
	 * Computes the Manhattan distance between this cell and a given cell.
	 * 
	 * @param c
	 *            another cell
	 * @return Manhattan distance between two cells
	 */
	private static int distanceInit(int arow, int acolumn, int brow, int bcolumn) {
		int cols = acolumn - bcolumn;
		int rows = arow - brow;
		if ((cols < 0) ^ (rows < 0))
			return Math.abs(cols) + Math.abs(rows);
		else
			return Math.max(Math.abs(cols), Math.abs(rows));

	}
	
	public int findDistance(Cell c) {
		return distances[this.row][this.column][c.row][c.column];
	}

	/**
	 * Returns a string representing the cell, e.g. "A5", "E7".
	 */
	@Override
	public String toString() {
		return Character.toString((char) ((int) 'A' + row - 1))
				+ Integer.toString(column);
	}

	/**
	 * Tests if a current cell is equal to a given cell.
	 * 
	 * @param c
	 *            another cell
	 * @return true if two cells are equal, false otherwise
	 */
	public boolean equals(Cell c) {
		return (row == c.row && column == c.column);
	}

}
