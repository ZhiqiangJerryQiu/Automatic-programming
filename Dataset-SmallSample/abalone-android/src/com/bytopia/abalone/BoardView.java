/**
* Copyright (c) 2010-2011 Yaroslav Geryatovich, Alexander Yakushev
* Distributed under the GNU GPL v3. For full terms see the file docs/COPYING.
*/
package com.bytopia.abalone;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.bytopia.abalone.mechanics.Board;
import com.bytopia.abalone.mechanics.Cell;
import com.bytopia.abalone.mechanics.Direction;
import com.bytopia.abalone.mechanics.Game;
import com.bytopia.abalone.mechanics.Group;
import com.bytopia.abalone.mechanics.Move;
import com.bytopia.abalone.mechanics.MoveType;
import com.bytopia.abalone.mechanics.Player;
import com.bytopia.abalone.mechanics.Side;
import com.bytopia.abalone.mechanics.Watcher;

public class BoardView extends View implements Player, Watcher {

	int size = 0;
	private Activity parent;

	public void setParent(Activity parent) {
		this.parent = parent;
	}

	private Board board;

	boolean selected = false, selectionStarted = false, moveRequested = false;
	Cell startCell;
	Group selectedGroup;

	// getMove
	Object monitor;
	Move resultMove;

	private Game game;
	private MoveType currentMoveType;
	private Group currentGroup;
	private Resources r;
	private BoardRenderer renderer;

	public BoardView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public BoardView(Context context) {
		super(context);
		init();
	}

	private void init() {
		setFocusable(true);
		monitor = new Object();
		r = getResources();
		renderer = new BoardRenderer(this);
		renderer.rescale(getWidth(), getWidth());
	}

	private int measure(int measureSpec) {
		int result = 0;

		// Decode the measurement specifications.
		int specMode = MeasureSpec.getMode(measureSpec);
		int specSize = MeasureSpec.getSize(measureSpec);

		if (specMode == MeasureSpec.UNSPECIFIED) {
			// Return a default size of 200 if no bounds are specified.
			result = 200;
		} else {
			// As you want to fill the available space
			// always return the full available bounds.
			result = specSize;
		}
		Log.d("screen", "result=" + result);
		return result;
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int measuredWidth = measure(widthMeasureSpec);
		int measuredHeight = measure(heightMeasureSpec);
		int borderSize = renderer.getBorderSize();
		float PROP = BoardRenderer.PROP;

		if ((measuredWidth - 2 * borderSize) * PROP + 2 * borderSize < measuredHeight) {
			measuredHeight = (int) (((measuredWidth - 2 * borderSize) * PROP) + 2 * borderSize);

		} else {
			measuredWidth = (int) (((measuredHeight - 2 * borderSize) / PROP) + 2 * borderSize);
		}
		Log.d("screen", measuredWidth + "x" + measuredHeight);
		setMeasuredDimension(measuredWidth, measuredHeight);
		size = measuredWidth;
		renderer.rescale(getWidth(), size);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		renderer.renderBoard(canvas);
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		measure(MeasureSpec.AT_MOST);
		renderer.rescale(w, size);
		updateView();
		Log.d("screen", "screen changed " + h + " " + w);
	}

	@Override
	public boolean onTouchEvent(MotionEvent e) {

		postInvalidate();
		if (moveRequested) {
			if (!selected) {
				if (e.getAction() == MotionEvent.ACTION_DOWN) {
					startCell = getCell(e.getX(), e.getY());
					Log.d("input", "startCell " + startCell.toString());
					selectionStarted = true;
				} else if (e.getAction() == MotionEvent.ACTION_MOVE) {

					currentGroup = board
							.getUsableGroup(
									startCell,
									getCell(e.getX(), e.getY()),
									getDirectionFromCell(e.getX(), e.getY(),
											startCell), game.getSide());
					renderer.setSelected(currentGroup);
					Log.d("input", getCell(e.getX(), e.getY()).toString()
							+ " curG " + currentGroup);
				} else if (e.getAction() == MotionEvent.ACTION_UP
						&& selectionStarted) {
					selectedGroup = board
							.getUsableGroup(
									startCell,
									getCell(e.getX(), e.getY()),
									getDirectionFromCell(e.getX(), e.getY(),
											startCell), game.getSide());

					selectionStarted = false;
					currentGroup = null;

					renderer.setSelected(selectedGroup);
					if (selectedGroup != null) {
						selected = true;
						Log.d("group", "group is valid");

					} else {
						Log.d("group", "group is not valid");
						// TODO notification
					}
				}
				// if selected
			} else {
				if (e.getAction() == MotionEvent.ACTION_DOWN) {

					currentMoveType = board.getMoveType(new Move(selectedGroup,
							getDirection(e.getX(), e.getY()), game.getSide()));
					Group highlitedCells = currentMoveType.getHighlightedCells();
					if (highlitedCells != null) {
						renderer.setHighlighted(highlitedCells);
					} else {
						renderer.setHighlighted(selectedGroup);
					}

				} else if (e.getAction() == MotionEvent.ACTION_MOVE) {

					currentMoveType = board.getMoveType(new Move(selectedGroup,
							getDirection(e.getX(), e.getY()), game.getSide()));
					Group highlitedCells = currentMoveType.getHighlightedCells();
					if (highlitedCells != null) {
						renderer.setHighlighted(highlitedCells);
					} else {
						renderer.setHighlighted(selectedGroup);
					}

				} else if (e.getAction() == MotionEvent.ACTION_UP) {
					renderer.setHighlighted(null);
					if (selectedGroup
							.isCellInGroup(getCell(e.getX(), e.getY()))) {
						selected = false;
						selectionStarted = false;
						renderer.setSelected(null);
					} else {
						Move move = new Move(selectedGroup, getDirection(
								e.getX(), e.getY()), game.getSide());

						MoveType moveType = board.getMoveType(move);
						if (moveType.getResult() != MoveType.NOMOVE) {
							resultMove = move;
							synchronized (monitor) {
								monitor.notify();
							}
							Log.d("group", "moved");
							// board.makeMove(move);
							// Log.d("input", "move");
							// selected = false;
							// drawBoard(board);
							moveRequested = false;
							selected = false;
							selectionStarted = false;
							renderer.setSelected(null);
						} else {
							// moveRequested = true;
							// TODO notification
							Log.d("group", "NOMOVE");

						}
					}
				}
			}
		} else {
		}
		return true;
	}

	byte getDirection(float x, float y) {
		PointF tempPoint = getCentrPointOfSelectedGroup();
		double angle = Math.atan((y - tempPoint.y) / (x - tempPoint.x));

		if (x - tempPoint.x < 0) {

			angle = Math.PI + angle;

		} else if ((y - tempPoint.y < 0)) {
			angle += Math.PI * 2;
		}
		double tAngle = angle + Math.PI / 6d;
		if (tAngle >= 2 * Math.PI) {
			tAngle -= 2 * Math.PI;
		}
		int t = (int) (tAngle / (Math.PI / 3d));
		byte d = 0;
		switch (t) {
		case 0:
			d = Direction.East;
			break;
		case 1:
			d = Direction.SouthEast;
			break;
		case 2:
			d = Direction.South;
			break;
		case 3:
			d = Direction.West;
			break;
		case 4:
			d = Direction.NorthWest;
			break;
		case 5:
			d = Direction.North;
			break;
		}
		Log.d("input", "angle = " + angle / Math.PI * 180 + " t=" + t + " " + d);
		return d;
	}

	byte getDirectionFromCell(float x, float y, Cell c) {
		PointF tempPoint = renderer.getPointByCell(c);
		double angle = Math.atan((y - tempPoint.y) / (x - tempPoint.x));

		if (x - tempPoint.x < 0) {

			angle = Math.PI + angle;

		} else if ((y - tempPoint.y < 0)) {
			angle += Math.PI * 2;
		}
		double tAngle = angle + Math.PI / 6d;
		if (tAngle >= 2 * Math.PI) {
			tAngle -= 2 * Math.PI;
		}
		int t = (int) (tAngle / (Math.PI / 3d));
		byte d = 0;
		switch (t) {
		case 0:
			d = Direction.East;
			break;
		case 1:
			d = Direction.SouthEast;
			break;
		case 2:
			d = Direction.South;
			break;
		case 3:
			d = Direction.West;
			break;
		case 4:
			d = Direction.NorthWest;
			break;
		case 5:
			d = Direction.North;
			break;
		}
		// Log.d("input", "angle = " + angle / Math.PI * 180 + " t=" + t + " " +
		// d);
		return d;
	}

	public Cell getCell(float x, float y) {
		int borderSize = renderer.getBorderSize();
		float ballSize = renderer.getBallSize();
		float SQRT3_2 = BoardRenderer.SQRT3_2;
		int row = (int) ((y - borderSize - (1 - SQRT3_2) * ballSize) / ((size
				- 2 * borderSize - 2 * (1 - SQRT3_2) * ballSize)
				* SQRT3_2 / 9f)) + 1;
		if (row > 9) {
			row = 9;
		} else if (row < 1) {
			row = 1;
		}
		int column = (int) ((x - borderSize - (5 - row) * ballSize / 2f)
				/ ballSize + 1);
		if (row <= 5) {
			if (column < 1)
				column = 1;
			else if (column > 4 + row) {
				column = 4 + row;
			}
		} else {
			if (column > 9) {
				column = 9;
			} else if (column < (row - 4)) {
				column = row - 4;
			}
		}
		Log.d("draw", "row = " + row);
		Log.d("draw", "column = " + column);
		return Cell.storage[row][column];
	}

	public Move requestMove(Game g) {
		moveRequested = true;
		synchronized (monitor) {
			try {
				monitor.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return resultMove;
	}

	@Override
	public void updateView() {
		if (board != null) {
			renderer.updateBoard(board);
		}
	}

	@Override
	public void doAnimation(MoveType moveType, byte direction) {
		renderer.animate(moveType, direction);
	}

	public PointF getCentrPointOfSelectedGroup() {
		PointF t = renderer.getPointByCell(selectedGroup.getFirstEnd());
		float x1 = t.x;
		float y1 = t.y;

		t = renderer.getPointByCell(selectedGroup.getSecondEnd());
		float x2 = t.x;
		float y2 = t.y;

		t = new PointF((x1 + x2) / 2f, (y1 + y2) / 2f);

		return t;
	}

	public void setGame(Game game) {
		this.game = game;
		this.board = game.getBoard();
		updateView();
	}

	public void screenChanged() {
		updateView();
	}

	@Override
	public void win(byte side) {

		final String sideString = (side == Side.BLACK) ? r
				.getString(R.string.black) : r.getString(R.string.white);

		parent.runOnUiThread(new Runnable() {

			@Override
			public void run() {

				new AlertDialog.Builder(getContext())
						.setMessage(
								sideString + " " + r.getString(R.string.wins))
						.setTitle(r.getString(R.string.game_over))
						.setCancelable(false).setNeutralButton("Ok", null)
						.show();

			}
		});

	}

	@Override
	public void marbleCaptured(byte side) {
		((GameActivity) parent).ballCaptured(side);

	}

}
