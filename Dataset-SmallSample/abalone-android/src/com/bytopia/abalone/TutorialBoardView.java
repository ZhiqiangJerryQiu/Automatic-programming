/**
* Copyright (c) 2010-2011 Yaroslav Geryatovich, Alexander Yakushev
* Distributed under the GNU GPL v3. For full terms see the file docs/COPYING.
*/
package com.bytopia.abalone;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.bytopia.abalone.mechanics.Board;
import com.bytopia.abalone.mechanics.Cell;
import com.bytopia.abalone.mechanics.Group;
import com.bytopia.abalone.mechanics.Move;
import com.bytopia.abalone.mechanics.MoveType;

public class TutorialBoardView extends View {

	int size = 0;
	public Activity parent;

	public void setParent(Activity parent) {
		this.parent = parent;
	}

	private Board board;

	private BoardRenderer renderer;

	public TutorialBoardView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public TutorialBoardView(Context context) {
		super(context);
		init();
	}
	
	public TutorialBoardView(Context context, Board initialBoard) {
		super(context);
		board = initialBoard;
		init();
	}

	private void init() {
		setFocusable(false);
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
		renderer.updateBoard();
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
	
	public void setBoard(Board b) {
		board = b;
		updateView();
	}
	
	public void setSelected(Group g) {
		renderer.setSelected(g);
	}
	
	public void setHighlighted(Group g) {
		renderer.setHighlighted(g);
	}
	
	public void updateView() {
		if (board != null) {
			renderer.updateBoard(board);
		}
	}
	
	public void makeMove(Move move) {
		MoveType moveType = board.getMoveType(move);
		doAnimation(moveType, move.getDirection());
		board.makeMove(move);
		updateView();
	}

	public void doAnimation(MoveType moveType, byte direction) {
		renderer.animate(moveType, direction);
	}
	
}
