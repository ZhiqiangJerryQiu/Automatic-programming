/**
* Copyright (c) 2010-2011 Yaroslav Geryatovich, Alexander Yakushev
* Distributed under the GNU GPL v3. For full terms see the file docs/COPYING.
*/
package com.bytopia.abalone.mechanics;

/**
 * Class that represents the game process, encapsulating the board, players, the
 * current side to move and some more service details.
 * 
 * @author Bytopia
 */
public class Game {

	/**
	 * Represents a vs type "Human vs Human"
	 */
	public static final byte HUMAN = 1;
	
	/**
	 * Represents a vs type "Human vs CPU"
	 */
	public static final byte CPU = 2;
	
	/**
	 * Board on which the game is run.
	 */
	private Board board;

	/**
	 * Object that watches the game and somehow shows game's progress. View
	 * entity in MVC pattern.
	 */
	private Watcher watcher;

	/**
	 * Player that plays for the black side.
	 */
	private Player blacksPlayer;

	/**
	 * Player that plays for the white side.
	 */
	private Player whitesPlayer;

	/**
	 * Current side to move.
	 */
	private byte currentSide = Side.BLACK;
	
	/**
	 * The type of game by players - "Human vs CPU" or "Human vs Human".
	 */
	private byte vsType;

	/**
	 * Constructs game instance from a given layout.
	 * 
	 * @param l
	 *            starting layout
	 * @param playerSide
	 *            side that will be on the bottom
	 * @param blacks
	 *            player that will play for the black side
	 * @param whites
	 *            player that will play for the white side
	 * @param watcher
	 *            object that watches the game and somehow shows game's progress
	 * @param vsType
	 *            type of game by players - "Human vs CPU" or "Human vs Human".
	 */
	public Game(Layout l, byte playerSide, Player blacks, Player whites,
			Watcher watcher, byte vsType) {
		board = new Board(l, playerSide);
		blacksPlayer = blacks;
		whitesPlayer = whites;
		this.watcher = watcher;
		this.vsType = vsType;
	}

	/**
	 * Constructs game instance from a given board.
	 * 
	 * @param board
	 *            board that will be the starting position of the game
	 * @param currentSide
	 *            side whose turn is next
	 * @param blacks
	 *            player that will play for the black side
	 * @param whites
	 *            player that will play for the white side
	 * @param watcher
	 *            object that watches the game and somehow shows game's progress
	 * @param vsType
	 *            type of game by players - "Human vs CPU" or "Human vs Human".
	 */
	public Game(Board board, byte currentSide, Player blacks, Player whites,
			Watcher watcher, byte vsType) {
		this.board = board;
		blacksPlayer = blacks;
		whitesPlayer = whites;
		this.watcher = watcher;
		this.currentSide = currentSide;
		this.vsType = vsType;
	}

	/**
	 * Starts the game process.
	 */
	public void start() {
//		Cell.init();
		Move move = null;
		int capturedBlack = board.getMarblesCaptured(Side.BLACK);
		int capturedWhite=board.getMarblesCaptured(Side.WHITE);
		while (board.getMarblesCaptured(Side.WHITE) < 6
				&& board.getMarblesCaptured(Side.BLACK) < 6) {
			if (currentSide == Side.BLACK) {
				move = blacksPlayer.requestMove(this);
			} else {
				move = whitesPlayer.requestMove(this);
			}
			if (watcher != null) {
				watcher.doAnimation(board.getMoveType(move),
						move.getDirection());
			}

			board.makeMove(move);
			if (watcher != null) {
				watcher.updateView();
			}

			if(board.getMarblesCaptured(Side.WHITE)!=capturedWhite){
				capturedWhite++;
				watcher.marbleCaptured(Side.WHITE);
			}
			
			if(board.getMarblesCaptured(Side.BLACK)!=capturedBlack){
				capturedBlack++;
				watcher.marbleCaptured(Side.BLACK);
			}
			
			currentSide = Side.opposite(currentSide);

		}
		if (board.getMarblesCaptured(Side.WHITE) >= 6) {
			watcher.win(Side.BLACK);
		} else {
			watcher.win(Side.WHITE);
		}
	}

	/**
	 * Returns the board on which game is run.
	 * 
	 * @return current board
	 */
	public Board getBoard() {
		return board;
	}

	/**
	 * Returns the current side whose turn is next.
	 * 
	 * @return side that will move next
	 */
	public byte getSide() {
		return currentSide;
	}

	/**
	 * Returns the type of game - "Human vs CPU" or "Human vs Human".
	 * 
	 * @return game type by players
	 */
	public byte getVsType() {
		return vsType;
	}
}
