/**
* Copyright (c) 2010-2011 Yaroslav Geryatovich, Alexander Yakushev
* Distributed under the GNU GPL v3. For full terms see the file docs/COPYING.
*/
package com.bytopia.abalone.mechanics;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.util.Log;

/**
 * Class that represents a playing board with all cells and marbles in them.
 * 
 * @author Bytopia
 */
public class Board implements Cloneable, Serializable {

	/**
	 * Array of cell descriptions.
	 */
	private byte[][] field;

	/**
	 * Number of white marbles captured.
	 */
	private int whiteCaptured = 0;

	/**
	 * Number of black marbles captured.
	 */
	private int blackCaptured = 0;

	/**
	 * Constructs a board with a default classic layout and black marbles
	 * downside.
	 */
	public Board() {
		this(new ClassicLayout(), Side.BLACK);
	}

	/**
	 * Constructs a board with an arbitrary layout and a given side on the
	 * bottom.
	 * 
	 * @param l
	 *            layout that will set the starting position
	 * @param downSide
	 *            side whose position will be on the bottom
	 */
	public Board(Layout l, byte downSide) {
		if (downSide == Side.BLACK)
			field = l.getBlackStartField();
		else
			field = l.getWhiteStartField();
	}

	/**
	 * Returns the array that describes every cell.
	 * 
	 * @return array of cell descriptions
	 */
	public byte[][] getField() {
		return field;
	}

	/**
	 * Returns the state of the given cell - non-existing, empty, white or black
	 * marble.
	 * 
	 * @param c
	 *            cell on the board
	 * @return numeric representation of cell state. All states are listed in
	 *         the Layout class
	 */
	public byte getState(Cell c) {
		return field[c.row][c.column];
	}

	/**
	 * Returns the state of the given cell by its coordinates - non-existing,
	 * empty, white or black marble.
	 * 
	 * @param row
	 *            X coordinate of the cell
	 * @param column
	 *            Y coordinate of the cell
	 * @return numeric representation of cell state. All states are listed in
	 *         the Layout class
	 */
	public byte getState(int row, int column) {
		return field[row][column];
	}

	/**
	 * Sets the given state to the cell on the board.
	 * 
	 * @param c
	 *            given cell
	 * @param state
	 *            numeric representation of cell state
	 */
	public void setState(Cell c, byte state) {
		field[c.row][c.column] = state;
	}

	/**
	 * Returns what the type would the move have if performed on this board.
	 * 
	 * @param m
	 *            move to be performed
	 * @return move type instance
	 */
	public MoveType getMoveType(Move m) {
		Group destination = m.getDestination();
		// If move is a leap
		boolean result = true;
		for (Cell c : destination.getCells()) {
			if (getState(c) != Layout.E)
				result = false;
		}
		if (result == true)
			return new MoveType(MoveType.LEAP, m.getSource(), destination);
		// If move is a pushing move...
		if (m.isPushing()) {
			byte d = m.getDirection();
			// ...check if it is a silent pushing move
			if (getState(m.getPeak().shift(d)) == Layout.E)
				return new MoveType(MoveType.SILENTPUSH, m.getSource(),
						destination);
			// Otherwise it's a enemy pushing move
			int nCell = getState(m.getPeak().shift(d));
			if (nCell == Layout.N)
				return new MoveType(MoveType.NOMOVE);
			int nnCell = getState(m.getPeak().shift(d).shift(d));
			byte enemyMarble = Side.opposite(m.getSide());
			if (nCell == enemyMarble
					&& (nnCell == Layout.E || nnCell == Layout.N))
				return new MoveType(MoveType.ENEMYPUSH, new Group(m.getTail(),
						m.getPeak().shift(d)), destination);
			if (nnCell == Layout.N || m.getSource().getSize() == 2)
				return new MoveType(MoveType.NOMOVE);
			int nnnCell = getState(m.getPeak().shift(d).shift(d).shift(d));
			if (nCell == enemyMarble && nnCell == enemyMarble
					&& (nnnCell == Layout.E || nnnCell == Layout.N))
				return new MoveType(MoveType.ENEMYPUSH, new Group(m.getTail(),
						m.getPeak().shift(d).shift(d)), destination);
		}
		return new MoveType(MoveType.NOMOVE);
	}

	/**
	 * Perform a given move on the current board.
	 * 
	 * @param m
	 *            move to be performed
	 */
	public void makeMove(Move m) {
		int type = getMoveType(m).getResult();
		if (type == MoveType.LEAP || type == MoveType.SILENTPUSH) {
			for (Cell c : m.getSource().getCells())
				setState(c, Layout.E);
			for (Cell c : m.getDestination().getCells())
				setState(c, m.getSide());
		} else {
			byte src = Layout.E, dest;
			Cell c = m.getTail();

			do {
				dest = getState(c);
				if (dest == Layout.N)
					if (m.getSide() == Side.WHITE)
						blackCaptured++;
					else
						whiteCaptured++;
				else
					setState(c, src);
				src = dest;
				c = c.shift(m.getDirection());
			} while (dest != Layout.E && dest != Layout.N);
		}
	}

	/**
	 * Returns a string representing the board in a form of ASCII symbols
	 */
	@Override
	public String toString() {
		StringBuilder s = new StringBuilder();
		s.append("       1 2 3 4 5");
		int j = 1;
		for (byte[] row : field) {
			for (int i = 0; i < Math.abs(6 - j); i++) {
				s.append(" ");
			}
			if (j != 1 && j != 11) {
				s.append((char) ((int) 'A' + j - 2) + " ");
			}
			for (int cell : row)
				if (cell == Layout.E)
					s.append("- ");
				else if (cell == Layout.W)
					s.append("o ");
				else if (cell == Layout.B)
					s.append("@ ");
			if (j != 1 && j <= 5)
				s.append(Integer.toString(j + 4));
			s.append("\n");
			j++;
		}
		return s.toString();
	}

	/**
	 * Returns how many marbles are captured for the given side.
	 * 
	 * @param side
	 *            black or white side
	 * @return number of marbles captured for (not by) this side
	 */
	public int getMarblesCaptured(byte side) {
		return side == Side.WHITE ? whiteCaptured : blackCaptured;
	}

	/**
	 * Returns the list of all marbles, i.e. list of cells with any marbles
	 * inside.
	 * 
	 * @return list of cells with marbles
	 */
	public List<Cell> getAllMarbles() {
		List<Cell> list = new ArrayList<Cell>();
		for (int i = 1; i <= 9; i++)
			for (int j = Cell.minColumn[i]; j <= Cell.maxColumn[i]; j++)
				if (getState(i, j) == Side.WHITE
						|| getState(i, j) == Side.BLACK)
					list.add(Cell.storage[i][j]);
		return list;
	}

	/**
	 * Returns the list of marbles of a given side, i.e. list of cells with
	 * marbles of this side inside.
	 * 
	 * @param side
	 *            black or white side
	 * @return list of cells with marbles of this side
	 */
	public List<Cell> getSideMarbles(byte side) {
		List<Cell> list = new ArrayList<Cell>();
		for (int i = 1; i <= 9; i++)
			for (int j = Cell.minColumn[i]; j <= Cell.maxColumn[i]; j++)
				if (getState(i, j) == side)
					list.add(Cell.storage[i][j]);
		return list;
	}

	/**
	 * Clones the board returning the same board.
	 */
	@Override
	public Board clone() {
		Board b = new Board();
		byte[][] f = new byte[11][11];
		for (int i = 0; i < 11; i++)
			System.arraycopy(field[i], 0, f[i], 0, 11);
		b.field = f;
		b.whiteCaptured = whiteCaptured;
		b.blackCaptured = blackCaptured;
		return b;
	}

	/**
	 * Returns the possible group to be moved on this board by given starting
	 * cell, ending cell, side of player and presumably direction of selection.
	 * Used by Player class.
	 * 
	 * @param start
	 *            starting cell of the group
	 * @param end
	 *            possible ending cell of the group (if the cell is the same
	 *            color as the player's side and group is legal)
	 * @param d
	 *            direction of cell selection
	 * @param side
	 *            side of the player
	 * @return the group of interval start-end, if it is legal for this player.
	 *         Otherwise the largest possible interval from start to the last
	 *         legal cell in a given direction taken from start cell is returned
	 */
	public Group getUsableGroup(Cell start, Cell end, byte d, byte side) {
		if (getState(start) == side)
			if (getState(start.shift(d)) == side
					&& start.findDistance(end) >= 1)
				if (getState(start.shift(d).shift(d)) == side
						&& start.findDistance(end) >= 2)
					return new Group(start, start.shift(d).shift(d));
				else
					return new Group(start, start.shift(d));
			else
				return new Group(start);
		else
			return null;
	}

	/**
	 * Sets the number of captured white marbles
	 * 
	 * @param whiteCaptured
	 *            Quantity of white marbles captured by the enemy
	 */
	public void setWhiteCaptured(int whiteCaptured) {
		this.whiteCaptured = whiteCaptured;
	}

	/**
	 * Sets the number of captured black marbles
	 * 
	 * @param whiteCaptured
	 *            Quantity of black marbles captured by the enemy
	 */
	public void setBlackCaptured(int blackCaptured) {
		this.blackCaptured = blackCaptured;
	}

}
