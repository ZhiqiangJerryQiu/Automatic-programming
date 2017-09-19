/**
* Copyright (c) 2010-2011 Yaroslav Geryatovich, Alexander Yakushev
* Distributed under the GNU GPL v3. For full terms see the file docs/COPYING.
*/
package com.bytopia.abalone.mechanics;

import java.util.Random;

/**
 * Interface that describes how the AI classes should work.
 * 
 * @author Bytopia
 */
public abstract class ArtificialIntilligence implements Player {

	/**
	 * Identifies the current move as clever.
	 */
	protected static int CLEVER = 1;

	/**
	 * Identifies the current move as stupid.
	 */
	protected static int STUPID = 2;

	/**
	 * Coefficient that is used by AI to determine how important is for marbles
	 * to be together.
	 */
	private static double NEIGHBOUR_IMPORTANCE_COEFFICIENT = 0.1;
	private static float BDW = (float)10;
	private static float EDC = (float)0.33;

	/**
	 * Directions that are primary for an AI. Used for performance reasons.
	 */
	private static byte[] PRIMARY_DIRECTIONS = { Direction.East,
			Direction.South, Direction.SouthEast };

	/**
	 * Directions that are secondary for an AI. Used for performance reasons.
	 */
	private static byte[] SECONDARY_DIRECTIONS = { Direction.West,
			Direction.North, Direction.NorthWest };

	/**
	 * All the directions excluding north and south. Used for performance
	 * reasons.
	 */
	private static byte[] NOT_NORTH_SOUTH_DIRECTIONS = {
			Direction.NorthWest, Direction.SouthEast, Direction.West,
			Direction.East };

	/**
	 * All the directions excluding north-west and south-east. Used for
	 * performance reasons.
	 */
	private static byte[] NOT_NW_SE_DIRECTIONS = { Direction.North,
			Direction.South, Direction.West, Direction.East };

	/**
	 * All the directions excluding west and east. Used for performance reasons.
	 */
	private static byte[] NOT_WEST_EAST_DIRECTIONS = {
			Direction.NorthWest, Direction.SouthEast, Direction.North,
			Direction.South };

	/**
	 * Costs in points for AI for each quantity of marbles captured.
	 */
	private static byte[] MARBLE_COSTS = { 0, 4, 6, 8, 12, 18, 100 };

	/**
	 * Best move that is found by an AI.
	 */
	private Move bestMove;

	/**
	 * Returns the collection of directions in which given direction does not
	 * appear. Used for performance reasons.
	 * 
	 * @param d
	 *            arbitrary direction
	 * @return one of the static collections of directions
	 */
	private static byte[] getNotDirection(byte d) {
		if (d == Direction.North || d == Direction.South)
			return NOT_NORTH_SOUTH_DIRECTIONS;
		else if (d == Direction.NorthWest || d == Direction.SouthEast)
			return NOT_NW_SE_DIRECTIONS;
		else
			return NOT_WEST_EAST_DIRECTIONS;
	}

	/**
	 * Evaluates the position on the board for the given side and other
	 * parameters. This method is fairly big, contains a lot of duplicate code
	 * and terrible programming techniques. All this was done to save each
	 * millisecond of computing time.
	 * 
	 * @param b
	 *            board to be computed
	 * @param side
	 *            side that AI plays for
	 * @param steps
	 *            how many steps an AI must scan through
	 * @param alphabeta
	 *            alpha-beta coefficient of the reduction algorithm
	 * @param aiState
	 *            state in which an AI is at the moment - clever or stupid
	 * @param analyzeNeighbours
	 *            if true, tells an AI to count neighbouring marbles for every
	 *            marble
	 * @return numeric representation of quality of the position
	 */
	private double evaluatePosition(Board b, byte side, int steps,
			double alphabeta, int aiState, boolean analyzeNeighbours) {
		Cell center = Cell.storage[5][5];
		byte[][] f = b.getField();
		byte oppSide = Side.opposite(side);
		if (steps == 0) {
			if (aiState == CLEVER) {
				double sum = MARBLE_COSTS[b.getMarblesCaptured(oppSide)]
						- MARBLE_COSTS[b.getMarblesCaptured(side)]
						+ Math.random() * 0.000001;
				double edt = BDW + EDC * MARBLE_COSTS[b.getMarblesCaptured(oppSide)];
				for (int i = 1; i <= 9; i++)
					for (int j = Cell.minColumn[i]; j <= Cell.maxColumn[i]; j++) {
						if (f[i][j] == side)
							sum += 1 / (Cell.storage[i][j].findDistance(center) + 1.0);
						else if (f[i][j] == oppSide)
							sum -= edt / (Cell.storage[i][j].findDistance(center) + 1.0);
					}
				if (analyzeNeighbours) {
					for (int i = 1; i <= 9; i++)
						for (int j = Cell.minColumn[i]; j <= Cell.maxColumn[i]; j++) {
							if (f[i][j] == side) {
								if (f[i - 1][j] == side)
									sum += NEIGHBOUR_IMPORTANCE_COEFFICIENT;
								if (f[i + 1][j] == side)
									sum += NEIGHBOUR_IMPORTANCE_COEFFICIENT;
								if (f[i][j - 1] == side)
									sum += NEIGHBOUR_IMPORTANCE_COEFFICIENT;
								if (f[i][j + 1] == side)
									sum += NEIGHBOUR_IMPORTANCE_COEFFICIENT;
								if (f[i - 1][j - 1] == side)
									sum += NEIGHBOUR_IMPORTANCE_COEFFICIENT;
								if (f[i + 1][j + 1] == side)
									sum += NEIGHBOUR_IMPORTANCE_COEFFICIENT;
							}
						}
				}
				return sum;
			} else {
				double sum = 0;
				for (int i = 1; i <= 9; i++)
					for (int j = Cell.minColumn[i]; j <= Cell.maxColumn[i]; j++) {
						if (f[i][j] == side) {
							if (f[i - 1][j] == side)
								sum--;
							if (f[i + 1][j] == side)
								sum--;
							if (f[i][j - 1] == side)
								sum--;
							if (f[i][j + 1] == side)
								sum--;
							if (f[i - 1][j - 1] == side)
								sum--;
							if (f[i + 1][j + 1] == side)
								sum--;
						}
					}
				return sum;
			}
		} else {
			Board futureBoard;
			Move bestMove = null, m = null;
			double currValue, bestValue = Double.POSITIVE_INFINITY, ab = alphabeta;
			Cell original, shifted1, shifted2, shifted3, shifted4;
			byte state1, state2, state3, state4, state5;
			flag: for (int i = 1; i <= 9; i++)
				for (int j = Cell.minColumn[i]; j <= Cell.maxColumn[i]; j++) {
					if (f[i][j] == side) {
						for (byte d : SECONDARY_DIRECTIONS) {
							original = Cell.storage[i][j];
							shifted1 = original.shift(d);
							state1 = b.getState(shifted1);
							if (state1 == Layout.E) {
								futureBoard = b.clone();
								m = new Move(new Group(original), d, side);
								futureBoard.makeMove(m);
								currValue = evaluatePosition(futureBoard,
										oppSide, steps - 1,
										ab == Double.NEGATIVE_INFINITY ? ab
												: -ab, aiState,
										analyzeNeighbours);
								if (currValue < bestValue) {
									bestValue = currValue;
									bestMove = m;
									if (alphabeta > bestValue)
										break flag;
									ab = bestValue;
								}
							} else if (state1 == side) {
								shifted2 = shifted1.shift(d);
								state2 = b.getState(shifted2);
								shifted3 = shifted2.shift(d);
								state3 = b.getState(shifted3);
								if (state2 == Layout.E
										|| (state2 == oppSide && (state3 == Layout.N || state3 == Layout.E))) {
									futureBoard = b.clone();
									m = new Move(new Group(original, shifted1),
											d, side);
									futureBoard.makeMove(m);
									currValue = evaluatePosition(futureBoard,
											oppSide, steps - 1,
											ab == Double.NEGATIVE_INFINITY ? ab
													: -ab, aiState,
											analyzeNeighbours);
									if (currValue < bestValue) {
										bestValue = currValue;
										bestMove = m;
										if (alphabeta > bestValue)
											break flag;
										ab = bestValue;
									}
								} else if (state2 == side) {
									shifted4 = shifted3.shift(d);
									state4 = b.getState(shifted4);
									state5 = b.getState(shifted4.shift(d));
									if (state3 == Layout.E
											|| (state3 == oppSide
													&& state4 == oppSide && (state5 == Layout.N || state5 == Layout.E))) {
										futureBoard = b.clone();
										m = new Move(new Group(original,
												shifted2), d, side);
										futureBoard.makeMove(m);
										currValue = evaluatePosition(
												futureBoard,
												oppSide,
												steps - 1,
												ab == Double.NEGATIVE_INFINITY ? ab
														: -ab, aiState,
												analyzeNeighbours);
										if (currValue < bestValue) {
											bestValue = currValue;
											bestMove = m;
											if (alphabeta > bestValue)
												break flag;
											ab = bestValue;
										}
									}
								}
							}
						}
						for (byte d : PRIMARY_DIRECTIONS) {
							original = Cell.storage[i][j];
							shifted1 = original.shift(d);
							state1 = b.getState(shifted1);
							if (state1 == Layout.E) {
								futureBoard = b.clone();
								m = new Move(new Group(original), d, side);
								futureBoard.makeMove(m);
								currValue = evaluatePosition(futureBoard,
										oppSide, steps - 1,
										ab == Double.NEGATIVE_INFINITY ? ab
												: -ab, aiState,
										analyzeNeighbours);
								if (currValue < bestValue) {
									bestValue = currValue;
									bestMove = m;
									if (alphabeta > bestValue)
										break flag;
									ab = bestValue;
								}
							} else if (state1 == side) {
								shifted2 = shifted1.shift(d);
								state2 = b.getState(shifted2);
								shifted3 = shifted2.shift(d);
								state3 = b.getState(shifted3);
								if (state2 == Layout.E
										|| (state2 == oppSide && (state3 == Layout.N || state3 == Layout.E))) {
									futureBoard = b.clone();
									m = new Move(new Group(original, shifted1),
											d, side);
									futureBoard.makeMove(m);
									currValue = evaluatePosition(futureBoard,
											oppSide, steps - 1,
											ab == Double.NEGATIVE_INFINITY ? ab
													: -ab, aiState,
											analyzeNeighbours);
									if (currValue < bestValue) {
										bestValue = currValue;
										bestMove = m;
										if (alphabeta > bestValue)
											break flag;
										ab = bestValue;
									}
								} else if (state2 == side) {
									shifted4 = shifted3.shift(d);
									state4 = b.getState(shifted4);
									state5 = b.getState(shifted4.shift(d));
									if (state3 == Layout.E
											|| (state3 == oppSide
													&& state4 == oppSide && (state5 == Layout.N || state5 == Layout.E))) {
										futureBoard = b.clone();
										m = new Move(new Group(original,
												shifted2), d, side);
										futureBoard.makeMove(m);
										currValue = evaluatePosition(
												futureBoard,
												oppSide,
												steps - 1,
												ab == Double.NEGATIVE_INFINITY ? ab
														: -ab, aiState,
												analyzeNeighbours);
										if (currValue < bestValue) {
											bestValue = currValue;
											bestMove = m;
											if (alphabeta > bestValue)
												break flag;
											ab = bestValue;
										}
									}
									for (byte md : getNotDirection(d)) {
										if (b.getState(original.shift(md)) == Layout.E
												&& b.getState(shifted1
														.shift(md)) == Layout.E
												&& b.getState(shifted2
														.shift(md)) == Layout.E) {
											futureBoard = b.clone();
											m = new Move(new Group(original,
													shifted2), md, side);
											futureBoard.makeMove(m);
											currValue = evaluatePosition(
													futureBoard,
													oppSide,
													steps - 1,
													ab == Double.NEGATIVE_INFINITY ? ab
															: -ab, aiState,
													analyzeNeighbours);
											if (currValue < bestValue) {
												bestValue = currValue;
												bestMove = m;
												if (alphabeta > bestValue)
													break flag;
												ab = bestValue;
											}
										}
									}
								}
								for (byte md : getNotDirection(d)) {
									if (b.getState(original.shift(md)) == Layout.E
											&& b.getState(shifted1.shift(md)) == Layout.E) {
										futureBoard = b.clone();
										m = new Move(new Group(original,
												shifted1), md, side);
										futureBoard.makeMove(m);
										currValue = evaluatePosition(
												futureBoard,
												oppSide,
												steps - 1,
												ab == Double.NEGATIVE_INFINITY ? ab
														: -ab, aiState,
												analyzeNeighbours);
										if (currValue < bestValue) {
											bestValue = currValue;
											bestMove = m;
											if (alphabeta > bestValue)
												break flag;
											ab = bestValue;
										}
									}
								}
							}
						}
					}
				}

			this.bestMove = bestMove;
			return -bestValue;
		}
	}

	/**
	 * Passes the given arguments and parameters to the evaluation method and
	 * returns the best move that an AI have found.
	 * 
	 * @param b
	 *            board to find the move on
	 * @param side
	 *            side to find a move for
	 * @param steps
	 *            number of steps (depth of analysis)
	 * @param aiType
	 *            clever or stupid
	 * @param countNeighbours
	 *            if true, takes into account neighbouring marbles of every
	 *            marble
	 * @return best move
	 */
	public Move findNextMove(Board b, byte side, int steps, int aiType,
			boolean countNeighbours) {
		Random r = new Random();
		if (r.nextDouble() > 0.25 || aiType == CLEVER)
			evaluatePosition(b, side, steps, Double.NEGATIVE_INFINITY, CLEVER,
					countNeighbours);
		else
			evaluatePosition(b, side, steps, Double.NEGATIVE_INFINITY, STUPID,
					countNeighbours);
		return bestMove;
	}

}
