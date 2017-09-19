/**
* Copyright (c) 2010-2011 Yaroslav Geryatovich, Alexander Yakushev
* Distributed under the GNU GPL v3. For full terms see the file docs/COPYING.
*/
package com.bytopia.abalone;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.bytopia.abalone.mechanics.Board;
import com.bytopia.abalone.mechanics.Cell;
import com.bytopia.abalone.mechanics.Direction;
import com.bytopia.abalone.mechanics.Group;
import com.bytopia.abalone.mechanics.Layout;
import com.bytopia.abalone.mechanics.MoveType;

/**
 * Class that manages drawing and updating the board on the screen.
 * 
 * @author Bytopia
 * 
 */
public class BoardRenderer {

	/**
	 * Default size of game border in pixels.
	 */
	static final int DEFAULT_BORDERSIZE = 10;

	/**
	 * Values precomputed for optimization.
	 */
	static final float SQRT3_2 = (float) Math.sqrt(3) / 2f,
			PROP = (8 * SQRT3_2 + 1f) / 9f;

	/**
	 * Values that define animation time.
	 */
	private final static int T = 20, TIME = 400, N = TIME / T;

	/**
	 * Utility class that encapsulates position and state of the marble on the
	 * screen.
	 */
	class RenderBall {
		float x, y;
		int state;

		public RenderBall(float x, float y, int state) {
			this.x = x;
			this.y = y;
			this.state = state;
		}
	}

	/**
	 * Variables that define sizes of the drawn board.
	 */
	private int borderSize = DEFAULT_BORDERSIZE;
	private float ballSize;
	private RectF boardRect;
	private Rect dst;

	/**
	 * Variables that store logical information about the board and marbles.
	 */
	private Board board;
	private ArrayList<RenderBall> balls;
	private Group highlightedGroup;
	private Group selectedGroup;
	private List<RenderBall> emptyBalls, animBalls;
	private boolean animation = false;

	/**
	 * Variables that define graphical properties of the board.
	 */
	private Paint defaultPaint, blackP, whiteP, emptyP, highlightedP,
			selectedP, boardP;
	private Drawable blackBallPicture, whiteBallPicture, emptyBallPicture;

	/**
	 * Variables to interact with calling views.
	 */
	private View view;

	/**
	 * Constructor that initializes brushes and images from resources.
	 * 
	 * @param view
	 *            parent view to render the board onto
	 */
	public BoardRenderer(View view) {
		this.view = view;
		Resources r = view.getResources();
		blackBallPicture = r.getDrawable(R.drawable.black_ball);
		whiteBallPicture = r.getDrawable(R.drawable.white_ball);
		emptyBallPicture = r.getDrawable(R.drawable.hole);
		// TODO move to XML
		defaultPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		defaultPaint.setColor(r.getColor(R.color.defaultColor));
		blackP = new Paint(Paint.ANTI_ALIAS_FLAG);
		blackP.setColor(Color.BLACK);
		whiteP = new Paint(Paint.ANTI_ALIAS_FLAG);
		whiteP.setColor(Color.WHITE);
		emptyP = new Paint(Paint.ANTI_ALIAS_FLAG);
		emptyP.setColor(Color.GRAY);
		highlightedP = new Paint(Paint.ANTI_ALIAS_FLAG);
		highlightedP.setColor(Color.RED);
		highlightedP.setAlpha(100);

		selectedP = new Paint(Paint.ANTI_ALIAS_FLAG);
		selectedP.setColor(Color.GREEN);
		selectedP.setAlpha(100);

		boardP = new Paint(Paint.ANTI_ALIAS_FLAG);
		boardP.setColor(r.getColor(R.color.board));

		dst = new Rect();
	}

	/**
	 * Notifies the renderer that the board was updated.
	 * 
	 * @param b
	 *            board that was updated
	 */
	public void updateBoard(Board b) {
		this.board = b;

		Log.d("update", "BoardDrawer.UpdateBoard at " + System.nanoTime());

		// synchronized (balls) {
		balls = new ArrayList<RenderBall>();
		for (int i = 1; i <= 9; i++) {
			float shift = (5f - i) * ballSize / 2f;
			float x, y;
			for (int j = 1; j <= 9; j++) {
				int state = b.getState(i, j);
				if (state != Layout.N) {
					x = borderSize + shift + (j - 1) * ballSize + ballSize / 2f;
					y = (float) (borderSize + (i - 1) * ballSize * SQRT3_2)
							+ ballSize / 2f;
					balls.add(new RenderBall(x, y, state));
				}
			}
		}
		// }

		if (animBalls != null) {
			animBalls = null;
		}

		if (emptyBalls != null) {
			emptyBalls = null;
		}

		animation = false;

		view.postInvalidate();
	}
	
	public void updateBoard() {
		updateBoard(board);
	}

	/**
	 * Updates sizes according to the given board diameter.
	 * 
	 * @param boardDiameter
	 *            distance between two opposite corners of the board
	 */
	public void rescale(int boardDiameter, int size) {
		Log.d("rescale", "Rescaling the board");
		boardRect = new RectF(boardDiameter / 4f - 1, 1,
				3f * boardDiameter / 4f + 1, boardDiameter / 2);
		balls = new ArrayList<RenderBall>();
		ballSize = ((float) size - 2 * borderSize) / 9f;
		view.postInvalidate();
	}

	/**
	 * Set the group of user-selected marbles.
	 * 
	 * @param selected
	 *            group of selected marbles
	 */
	public void setSelected(Group selected) {
		selectedGroup = selected;
		view.postInvalidate();
	}

	/**
	 * Set the group of marbles highlighted during the interaction with user.
	 * 
	 * @param highlighted
	 *            group of highlighted marbles
	 */
	public void setHighlighted(Group highlighted) {
		highlightedGroup = highlighted;
		view.postInvalidate();
	}

	/**
	 * Computes a point on the screen that is the center of the given cell.
	 * 
	 * @param cell
	 *            cell on the board
	 * @return point on the screen
	 */
	public PointF getPointByCell(Cell cell) {
		float shift = (5f - cell.row) * ballSize / 2f;
		float x, y;

		x = borderSize + shift + (cell.column - 1) * ballSize + ballSize / 2f;
		y = (float) (borderSize + (cell.row - 1) * ballSize * SQRT3_2)
				+ ballSize / 2f;

		return new PointF(x, y);
	}

	/**
	 * Renders the specified RenderBall on the Canvas.
	 * 
	 * @param canvas
	 *            canvas to render the ball onto
	 * @param ball
	 *            ball to render
	 */
	private void renderBall(Canvas canvas, RenderBall ball) {
		dst.set((int) (ball.x - ballSize / 2), (int) (ball.y - ballSize / 2),
				(int) (ball.x + ballSize / 2), (int) (ball.y + ballSize / 2));

		switch (ball.state) {
		case Layout.B:
			blackBallPicture.setBounds(dst);
			blackBallPicture.draw(canvas);
			break;
		case Layout.W:
			whiteBallPicture.setBounds(dst);
			whiteBallPicture.draw(canvas);
			break;
		case Layout.E:
			emptyBallPicture.setBounds(dst);
			emptyBallPicture.draw(canvas);
			break;
		}
	}

	/**
	 * Render the overlay on the group of highlighted marbles.
	 * 
	 * @param canvas
	 *            canvas to draw onto
	 */
	private void renderHighlighted(Canvas canvas) {
		PointF p;
		for (Cell cell : highlightedGroup.getCells()) {
			p = getPointByCell(cell);
			canvas.drawCircle(p.x, p.y, 0.5f * ballSize, highlightedP);
		}
	}

	/**
	 * Render the overlay on the group of marbles selected by user.
	 * 
	 * @param canvas
	 *            canvas to draw onto
	 */
	private void renderSelected(Canvas canvas) {
		PointF p;
		for (Cell cell : selectedGroup.getCells()) {
			p = getPointByCell(cell);
			canvas.drawCircle(p.x, p.y, 0.5f * ballSize, selectedP);
		}
	}

	/**
	 * Render the whole board.
	 * 
	 * @param canvas
	 *            canvas to draw onto
	 */
	public void renderBoard(Canvas canvas) {
		// TODO boar edges

		canvas.save();

		for (int i = 1; i <= 6; i++) {
			canvas.drawRect(boardRect, boardP);
			canvas.rotate(60, view.getWidth() / 2, view.getHeight() / 2);
		}

		canvas.restore();
		// TODO cells

		if (balls != null) {
			// synchronized (balls) {
			for (RenderBall ball : balls) {
				renderBall(canvas, ball);
			}
			// }
		}

		if (animation) {
			if (emptyBalls != null) {
				// synchronized (emptyBalls) {
				for (RenderBall ball : emptyBalls) {
					renderBall(canvas, ball);
					// }
				}
			}
			if (animBalls != null) {
				synchronized (animBalls) {
					for (RenderBall ball : animBalls) {
						renderBall(canvas, ball);
					}
				}
			}
		}

		if (selectedGroup != null) {
			renderSelected(canvas);
		}

		if (highlightedGroup != null) {
			renderHighlighted(canvas);
		}
	}

	/**
	 * Initiate animation of moved marbles.
	 * 
	 * @param moveType
	 *            type of the move performed
	 * @param direction
	 *            direction of the move performed
	 */
	public void animate(MoveType moveType, byte direction) {
		Log.d("animate", "BoardDrawer.Animate at " + System.nanoTime());
		double angle = 0;
		double PI = Math.PI;
		if (direction == Direction.East) {
			angle = 0;
		} else if (direction == Direction.SouthEast) {
			angle = PI / 3d;
		} else if (direction == Direction.South) {
			angle = 2d * PI / 3d;
		} else if (direction == Direction.West) {
			angle = PI;
		} else if (direction == Direction.NorthWest) {
			angle = -2d * PI / 3d;
		} else if (direction == Direction.North) {
			angle = -PI / 3d;
		}

		emptyBalls = new LinkedList<RenderBall>();
		animBalls = new LinkedList<RenderBall>();
		PointF point;
		for (Cell cell : moveType.getMovedCells().getCells()) {
			float x, y;
			point = getPointByCell(cell);
			x = point.x;
			y = point.y;
			emptyBalls.add(new RenderBall(x, y, Layout.E));
			animBalls.add(new RenderBall(x, y, board.getState(cell)));
		}

		animation = true;

		for (int i = 0; i < N; i++) {
			for (RenderBall ball : animBalls) {
				ball.x += (1d / (double) N) * Math.cos(angle) * ballSize;
				ball.y += (1d / (double) N) * Math.sin(angle) * ballSize;
			}

			try {
				Thread.sleep(T);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			view.postInvalidate();
		}
		try {
			Thread.sleep(200);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Returns the size of the marble in pixels.
	 * 
	 * @return size of the marble
	 */
	public float getBallSize() {
		return ballSize;
	}

	/**
	 * Returns the diameter of the board in pixels.
	 * 
	 * @return size of the board
	 */
	public int getBorderSize() {
		return borderSize;
	}
}
