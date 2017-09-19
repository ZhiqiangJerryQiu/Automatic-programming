/**
* Copyright (c) 2010-2011 Yaroslav Geryatovich, Alexander Yakushev
* Distributed under the GNU GPL v3. For full terms see the file docs/COPYING.
*/
package com.bytopia.abalone;

import com.bytopia.abalone.mechanics.Board;
import com.bytopia.abalone.mechanics.Cell;
import com.bytopia.abalone.mechanics.Direction;
import com.bytopia.abalone.mechanics.EmptyLayout;
import com.bytopia.abalone.mechanics.Group;
import com.bytopia.abalone.mechanics.Move;
import com.bytopia.abalone.mechanics.Side;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class TutorialActivity extends Activity {

	private TextView textView;
	private int currentActIndex = 0;
	private Scenario[] piece;
	private int[] scripts;
	private Button butNext, butPrev;
	private TutorialBoardView tutorialBoardView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.tutorial);

		textView = (TextView) findViewById(R.id.turorial_text);

		butPrev = (Button) findViewById(R.id.prev_step);
		butPrev.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				prevAct();
			}
		});

		butNext = (Button) findViewById(R.id.next_step);
		butNext.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				nextAct();
			}
		});

		LinearLayout linearLayout = (LinearLayout) findViewById(R.id.tutorial_board_layout);
		// Board board = new Board(new ClassicLayout(), Side.BLACK);
		tutorialBoardView = new TutorialBoardView(getApplicationContext());
		tutorialBoardView.setParent(this);

		initPiece();
		currentActIndex = 0;
		updateTutorial();

		linearLayout.addView(tutorialBoardView);
	}

	private void initPiece() {
		Board initialBoard = new Board();
		Scenario scIntro = new Scenario(tutorialBoardView, initialBoard);

		Scenario scWhatIs = new Scenario(tutorialBoardView, initialBoard);

		Board brdMoveSingle = new Board(new EmptyLayout(), Side.BLACK);
		brdMoveSingle.setState(Cell.storage[6][5], Side.BLACK);
		Scenario scMoveSingle = new Scenario(tutorialBoardView, brdMoveSingle);
		scMoveSingle.SetActions(scMoveSingle.new SelectGroup().construct(
				new Group(Cell.storage[6][5]), Scenario.HALF_PAUSE),
				scMoveSingle.new MakeMove().construct(new Move(new Group(
						Cell.storage[6][5]), Direction.NorthWest, Side.BLACK),
						Scenario.PAUSE),

				scMoveSingle.new SelectGroup().construct(new Group(
						Cell.storage[5][4]), Scenario.HALF_PAUSE),
				scMoveSingle.new MakeMove().construct(new Move(new Group(
						Cell.storage[5][4]), Direction.North, Side.BLACK),
						Scenario.PAUSE),

				scMoveSingle.new SelectGroup().construct(new Group(
						Cell.storage[4][4]), Scenario.HALF_PAUSE),
				scMoveSingle.new MakeMove().construct(new Move(new Group(
						Cell.storage[4][4]), Direction.East, Side.BLACK),
						Scenario.PAUSE),

				scMoveSingle.new SelectGroup().construct(new Group(
						Cell.storage[4][5]), Scenario.HALF_PAUSE),
				scMoveSingle.new MakeMove().construct(new Move(new Group(
						Cell.storage[4][5]), Direction.SouthEast, Side.BLACK),
						Scenario.PAUSE),

				scMoveSingle.new SelectGroup().construct(new Group(
						Cell.storage[5][6]), Scenario.HALF_PAUSE),
				scMoveSingle.new MakeMove().construct(new Move(new Group(
						Cell.storage[5][6]), Direction.South, Side.BLACK),
						Scenario.PAUSE),

				scMoveSingle.new SelectGroup().construct(new Group(
						Cell.storage[6][6]), Scenario.HALF_PAUSE),
				scMoveSingle.new MakeMove().construct(new Move(new Group(
						Cell.storage[6][6]), Direction.West, Side.BLACK),
						Scenario.PAUSE));

		Board brdSelGroup = new Board(new EmptyLayout(), Side.BLACK);
		putMarbles(brdSelGroup, new int[] { 3, 3, 4, 4, 3, 6, 3, 7, 4, 8, 7, 5,
				7, 6, 7, 7, 7, 8 }, Side.WHITE);
		Scenario scSelGroup = new Scenario(tutorialBoardView, brdSelGroup);
		scSelGroup.SetActions(scSelGroup.new SelectGroup().construct(new Group(
				Cell.storage[7][5], Cell.storage[7][7]), Scenario.PAUSE),
				scSelGroup.new SelectGroup().construct(null,
						Scenario.HALF_PAUSE),

				scSelGroup.new SelectGroup()
						.construct(new Group(Cell.storage[7][6],
								Cell.storage[7][8]), Scenario.PAUSE),
				scSelGroup.new SelectGroup().construct(null,
						Scenario.HALF_PAUSE),

				scSelGroup.new SelectGroup()
						.construct(new Group(Cell.storage[3][3],
								Cell.storage[4][4]), Scenario.PAUSE),
				scSelGroup.new SelectGroup().construct(null,
						Scenario.HALF_PAUSE),

				scSelGroup.new SelectGroup()
						.construct(new Group(Cell.storage[3][6],
								Cell.storage[3][7]), Scenario.PAUSE),
				scSelGroup.new SelectGroup().construct(null,
						Scenario.HALF_PAUSE),

				scSelGroup.new SelectGroup()
						.construct(new Group(Cell.storage[3][7],
								Cell.storage[4][8]), Scenario.PAUSE),
				scSelGroup.new SelectGroup().construct(null,
						Scenario.HALF_PAUSE));

		Board brdLeapMove = new Board(new EmptyLayout(), Side.BLACK);
		putMarbles(brdLeapMove, new int[] { 4, 5, 5, 5, 6, 5 }, Side.BLACK);
		Scenario scLeapMove = new Scenario(tutorialBoardView, brdLeapMove);

		scLeapMove.SetActions(scLeapMove.new SelectGroup().construct(new Group(
				Cell.storage[4][5], Cell.storage[6][5]), Scenario.HALF_PAUSE),
				scLeapMove.new MakeMove().construct(new Move(new Group(
						Cell.storage[4][5], Cell.storage[6][5]),
						Direction.West, Side.BLACK), Scenario.PAUSE),

				scLeapMove.new SelectGroup().construct(new Group(
						Cell.storage[4][4], Cell.storage[6][4]),
						Scenario.HALF_PAUSE), scLeapMove.new MakeMove()
						.construct(new Move(new Group(Cell.storage[4][4],
								Cell.storage[6][4]), Direction.NorthWest,
								Side.BLACK), Scenario.PAUSE),

				scLeapMove.new SelectGroup().construct(new Group(
						Cell.storage[4][3], Cell.storage[5][3]),
						Scenario.HALF_PAUSE), scLeapMove.new MakeMove()
						.construct(
								new Move(new Group(Cell.storage[4][3],
										Cell.storage[5][3]), Direction.East,
										Side.BLACK), Scenario.PAUSE),

				scLeapMove.new SelectGroup().construct(new Group(
						Cell.storage[4][4], Cell.storage[5][4]),
						Scenario.HALF_PAUSE), scLeapMove.new MakeMove()
						.construct(new Move(new Group(Cell.storage[4][4],
								Cell.storage[5][4]), Direction.SouthEast,
								Side.BLACK), Scenario.PAUSE),

				scLeapMove.new SelectGroup().construct(new Group(
						Cell.storage[5][5], Cell.storage[6][5]),
						Scenario.HALF_PAUSE), scLeapMove.new MakeMove()
						.construct(
								new Move(new Group(Cell.storage[5][5],
										Cell.storage[6][5]), Direction.West,
										Side.BLACK), Scenario.PAUSE),

				scLeapMove.new SelectGroup().construct(new Group(
						Cell.storage[5][4], Cell.storage[6][4]),
						Scenario.HALF_PAUSE), scLeapMove.new MakeMove()
						.construct(new Move(new Group(Cell.storage[5][4],
								Cell.storage[6][4]), Direction.NorthWest,
								Side.BLACK), Scenario.PAUSE),

				scLeapMove.new SelectGroup().construct(new Group(
						Cell.storage[3][3], Cell.storage[5][3]),
						Scenario.HALF_PAUSE), scLeapMove.new MakeMove()
						.construct(
								new Move(new Group(Cell.storage[3][3],
										Cell.storage[5][3]), Direction.East,
										Side.BLACK), Scenario.PAUSE),

				scLeapMove.new SelectGroup().construct(new Group(
						Cell.storage[3][4], Cell.storage[5][4]),
						Scenario.HALF_PAUSE), scLeapMove.new MakeMove()
						.construct(new Move(new Group(Cell.storage[3][4],
								Cell.storage[5][4]), Direction.SouthEast,
								Side.BLACK), Scenario.PAUSE));

		Board brdPushMove = new Board(new EmptyLayout(), Side.BLACK);
		putMarbles(brdPushMove,
				new int[] { 5, 5, 6, 5, 6, 6, 7, 5, 7, 6, 7, 7 }, Side.WHITE);
		Scenario scPushMove = new Scenario(tutorialBoardView, brdPushMove);
		scPushMove.SetActions(scPushMove.new SelectGroup().construct(new Group(
				Cell.storage[5][5], Cell.storage[7][5]), Scenario.HALF_PAUSE),
				scPushMove.new MakeMove().construct(new Move(new Group(
						Cell.storage[5][5], Cell.storage[7][5]),
						Direction.North, Side.WHITE), Scenario.PAUSE),

				scPushMove.new SelectGroup().construct(new Group(
						Cell.storage[5][5], Cell.storage[7][7]),
						Scenario.HALF_PAUSE), scPushMove.new MakeMove()
						.construct(new Move(new Group(Cell.storage[5][5],
								Cell.storage[7][7]), Direction.NorthWest,
								Side.WHITE), Scenario.PAUSE),

				scPushMove.new SelectGroup().construct(new Group(
						Cell.storage[6][5], Cell.storage[7][6]),
						Scenario.HALF_PAUSE), scPushMove.new MakeMove()
						.construct(new Move(new Group(Cell.storage[6][5],
								Cell.storage[7][6]), Direction.NorthWest,
								Side.WHITE), Scenario.PAUSE),

				scPushMove.new SelectGroup().construct(new Group(
						Cell.storage[4][5], Cell.storage[6][5]),
						Scenario.HALF_PAUSE), scPushMove.new MakeMove()
						.construct(new Move(new Group(Cell.storage[4][5],
								Cell.storage[6][5]), Direction.North,
								Side.WHITE), Scenario.PAUSE),

				scPushMove.new SelectGroup().construct(new Group(
						Cell.storage[4][4], Cell.storage[6][6]),
						Scenario.HALF_PAUSE), scPushMove.new MakeMove()
						.construct(new Move(new Group(Cell.storage[4][4],
								Cell.storage[6][6]), Direction.NorthWest,
								Side.WHITE), Scenario.PAUSE),

				scPushMove.new SelectGroup().construct(new Group(
						Cell.storage[4][4], Cell.storage[5][4]),
						Scenario.HALF_PAUSE), scPushMove.new MakeMove()
						.construct(new Move(new Group(Cell.storage[4][4],
								Cell.storage[5][4]), Direction.North,
								Side.WHITE), Scenario.PAUSE),

				scPushMove.new RestoreBoard().construct(null, Scenario.PAUSE));

		Board brdPushEnemy = new Board(new EmptyLayout(), Side.BLACK);
		putMarbles(brdPushEnemy, new int[] { 2, 4, 2, 5, 3, 4, 3, 5, 4, 4, 4,
				5, 4, 6 }, Side.WHITE);
		putMarbles(brdPushEnemy, new int[] { 5, 5, 6, 4, 6, 5, 6, 6, 7, 4, 7,
				6, 7, 7 }, Side.BLACK);
		Scenario scPushEnemy = new Scenario(tutorialBoardView, brdPushEnemy);

		scPushEnemy.SetActions(scPushEnemy.new SelectGroup().construct(
				new Group(Cell.storage[5][5], Cell.storage[7][7]),
				Scenario.HALF_PAUSE), scPushEnemy.new HighlightGroup()
				.construct(new Group(Cell.storage[4][4]), Scenario.HALF_PAUSE),
				scPushEnemy.new MakeMove().construct(new Move(new Group(
						Cell.storage[5][5], Cell.storage[7][7]),
						Direction.NorthWest, Side.BLACK), Scenario.PAUSE),

				scPushEnemy.new SelectGroup().construct(new Group(
						Cell.storage[2][4], Cell.storage[3][4]),
						Scenario.HALF_PAUSE), scPushEnemy.new HighlightGroup()
						.construct(new Group(Cell.storage[4][4]),
								Scenario.HALF_PAUSE),
				scPushEnemy.new MakeMove().construct(new Move(new Group(
						Cell.storage[2][4], Cell.storage[3][4]),
						Direction.South, Side.WHITE), Scenario.PAUSE),

				scPushEnemy.new SelectGroup().construct(new Group(
						Cell.storage[5][4], Cell.storage[7][4]),
						Scenario.HALF_PAUSE), scPushEnemy.new HighlightGroup()
						.construct(new Group(Cell.storage[3][4],
								Cell.storage[4][4]), Scenario.HALF_PAUSE),
				scPushEnemy.new MakeMove().construct(new Move(new Group(
						Cell.storage[5][4], Cell.storage[7][4]),
						Direction.North, Side.BLACK), Scenario.PAUSE),

				scPushEnemy.new SelectGroup().construct(new Group(
						Cell.storage[2][5], Cell.storage[4][5]),
						Scenario.HALF_PAUSE), scPushEnemy.new HighlightGroup()
						.construct(new Group(Cell.storage[5][5],
								Cell.storage[6][5]), Scenario.HALF_PAUSE),
				scPushEnemy.new MakeMove().construct(new Move(new Group(
						Cell.storage[2][5], Cell.storage[4][5]),
						Direction.South, Side.WHITE), Scenario.PAUSE),

				scPushEnemy.new SelectGroup().construct(new Group(
						Cell.storage[4][4], Cell.storage[6][4]),
						Scenario.HALF_PAUSE), scPushEnemy.new HighlightGroup()
						.construct(new Group(Cell.storage[2][4],
								Cell.storage[3][4]), Scenario.HALF_PAUSE),
				scPushEnemy.new MakeMove().construct(new Move(new Group(
						Cell.storage[4][4], Cell.storage[6][4]),
						Direction.North, Side.BLACK), Scenario.PAUSE),

				scPushEnemy.new SelectGroup().construct(new Group(
						Cell.storage[4][5], Cell.storage[4][6]),
						Scenario.HALF_PAUSE), scPushEnemy.new HighlightGroup()
						.construct(new Group(Cell.storage[4][4]),
								Scenario.HALF_PAUSE),
				scPushEnemy.new MakeMove().construct(new Move(new Group(
						Cell.storage[4][5], Cell.storage[4][6]),
						Direction.West, Side.WHITE), Scenario.PAUSE),

				scPushEnemy.new RestoreBoard().construct(null, Scenario.PAUSE));

		Board brdNoPush = new Board(new EmptyLayout(), Side.BLACK);
		putMarbles(brdNoPush, new int[] { 5, 6, 5, 7, 5, 8 }, Side.WHITE);
		putMarbles(brdNoPush, new int[] { 5, 2, 5, 3, 5, 4, 5, 5 }, Side.BLACK);
		Scenario scNoPush = new Scenario(tutorialBoardView, brdNoPush);

		Board brdCapture = new Board(new EmptyLayout(), Side.BLACK);
		putMarbles(brdCapture, new int[] { 5, 7, 5, 8, 5, 9 }, Side.WHITE);
		putMarbles(brdCapture, new int[] { 6, 9, 7, 9 }, Side.BLACK);
		Scenario scCapture = new Scenario(tutorialBoardView, brdCapture);

		scCapture.SetActions(scCapture.new SelectGroup().construct(new Group(
				Cell.storage[6][9], Cell.storage[7][9]), Scenario.HALF_PAUSE),
				scCapture.new HighlightGroup().construct(new Group(
						Cell.storage[5][9]), Scenario.HALF_PAUSE),
				scCapture.new MakeMove().construct(new Move(new Group(
						Cell.storage[6][9], Cell.storage[7][9]),
						Direction.North, Side.BLACK), Scenario.PAUSE),

				scCapture.new SelectGroup().construct(new Group(
						Cell.storage[5][7], Cell.storage[5][8]),
						Scenario.HALF_PAUSE), scCapture.new HighlightGroup()
						.construct(new Group(Cell.storage[5][9]),
								Scenario.HALF_PAUSE), scCapture.new MakeMove()
						.construct(
								new Move(new Group(Cell.storage[5][7],
										Cell.storage[5][8]), Direction.East,
										Side.WHITE), Scenario.PAUSE),

				scCapture.new RestoreBoard().construct(null, Scenario.PAUSE));

		Board brdGameStart = new Board();
		Scenario scGameStart = new Scenario(tutorialBoardView, brdGameStart);
		scGameStart.SetActions(scGameStart.new SelectGroup().construct(
				new Group(Cell.storage[7][6], Cell.storage[9][6]),
				Scenario.HALF_PAUSE), scGameStart.new MakeMove().construct(
				new Move(new Group(Cell.storage[7][6], Cell.storage[9][6]),
						Direction.North, Side.BLACK), Scenario.PAUSE),

		scGameStart.new SelectGroup().construct(new Group(Cell.storage[3][3],
				Cell.storage[3][5]), Scenario.HALF_PAUSE),
				scGameStart.new MakeMove().construct(new Move(new Group(
						Cell.storage[3][3], Cell.storage[3][5]),
						Direction.SouthEast, Side.WHITE), Scenario.PAUSE),

				scGameStart.new SelectGroup().construct(new Group(
						Cell.storage[7][5], Cell.storage[9][5]),
						Scenario.HALF_PAUSE), scGameStart.new MakeMove()
						.construct(new Move(new Group(Cell.storage[7][5],
								Cell.storage[9][5]), Direction.North,
								Side.BLACK), Scenario.PAUSE),

				scGameStart.new SelectGroup().construct(new Group(
						Cell.storage[2][3], Cell.storage[2][5]),
						Scenario.HALF_PAUSE), scGameStart.new MakeMove()
						.construct(new Move(new Group(Cell.storage[2][3],
								Cell.storage[2][5]), Direction.South,
								Side.WHITE), Scenario.PAUSE),

				scGameStart.new SelectGroup().construct(new Group(
						Cell.storage[7][7], Cell.storage[8][8]),
						Scenario.HALF_PAUSE), scGameStart.new MakeMove()
						.construct(new Move(new Group(Cell.storage[7][7],
								Cell.storage[8][8]), Direction.North,
								Side.BLACK), Scenario.PAUSE),

				scGameStart.new SelectGroup().construct(new Group(
						Cell.storage[2][2], Cell.storage[4][4]),
						Scenario.HALF_PAUSE), scGameStart.new MakeMove()
						.construct(new Move(new Group(Cell.storage[2][2],
								Cell.storage[4][4]), Direction.SouthEast,
								Side.WHITE), Scenario.PAUSE),

				scGameStart.new RestoreBoard().construct(null, Scenario.PAUSE));

		Board brdFinish = new Board(new EmptyLayout(), Side.BLACK);
		putMarbles(brdFinish, new int[] { 3, 3, 5, 2, 5, 4, 5, 5, 6, 3, 6, 5,
				7, 4, 7, 5 }, Side.WHITE);
		putMarbles(brdFinish, new int[] { 3, 2, 4, 2, 3, 7, 4, 7, 5, 7, 6, 7,
				7, 7, 7, 8, 7, 9 }, Side.BLACK);
		Scenario scFinish = new Scenario(tutorialBoardView, brdFinish);
		scFinish.SetActions(scFinish.new MakeMove().construct(
				new Move(new Group(Cell.storage[4][7]), Direction.NorthWest,
						Side.BLACK), Scenario.PAUSE), scFinish.new MakeMove()
				.construct(new Move(new Group(Cell.storage[5][4],
						Cell.storage[5][5]), Direction.North, Side.WHITE),
						Scenario.PAUSE), scFinish.new MakeMove().construct(
				new Move(new Group(Cell.storage[3][6], Cell.storage[3][7]),
						Direction.West, Side.BLACK), Scenario.PAUSE),
				scFinish.new MakeMove().construct(new Move(new Group(
						Cell.storage[5][2], Cell.storage[6][3]),
						Direction.East, Side.WHITE), Scenario.PAUSE),
				scFinish.new MakeMove().construct(new Move(new Group(
						Cell.storage[5][7], Cell.storage[7][7]),
						Direction.North, Side.BLACK), Scenario.PAUSE),
				scFinish.new MakeMove().construct(new Move(new Group(
						Cell.storage[7][4], Cell.storage[7][5]),
						Direction.East, Side.WHITE), Scenario.PAUSE),
				scFinish.new MakeMove().construct(new Move(new Group(
						Cell.storage[7][8], Cell.storage[7][9]),
						Direction.West, Side.BLACK), Scenario.PAUSE),
				scFinish.new MakeMove().construct(new Move(new Group(
						Cell.storage[6][5], Cell.storage[7][6]),
						Direction.SouthEast, Side.WHITE), Scenario.PAUSE),
				scFinish.new MakeMove().construct(new Move(new Group(
						Cell.storage[6][7], Cell.storage[7][7]),
						Direction.South, Side.BLACK), Scenario.PAUSE),
				scFinish.new MakeMove().construct(new Move(new Group(
						Cell.storage[7][6]), Direction.South, Side.WHITE),
						Scenario.PAUSE), scFinish.new MakeMove().construct(
						new Move(new Group(Cell.storage[7][8]),
								Direction.NorthWest, Side.BLACK),
						Scenario.PAUSE),

				scFinish.new RestoreBoard().construct(null, Scenario.PAUSE));

		piece = new Scenario[] { scIntro, scWhatIs, scMoveSingle, scSelGroup,
				scLeapMove, scPushMove, scPushEnemy, scNoPush, scCapture,
				scGameStart, scFinish };
		scripts = new int[] { R.string.tutorial_step1_intro,
				R.string.tutorial_step2_whatis,
				R.string.tutorial_step3_movesingle,
				R.string.tutorial_step4_selectgroup,
				R.string.tutorial_step5_moveleap,
				R.string.tutorial_step6_movepush,
				R.string.tutorial_step7_enemypush,
				R.string.tutorial_step8_nopush,
				R.string.tutorial_step9_capture,
				R.string.tutorial_step10_gamestart,
				R.string.tutorial_step11_finish };
	}

	public void nextAct() {
		piece[currentActIndex].stop();
		currentActIndex++;
		updateTutorial();
	}

	public void prevAct() {
		piece[currentActIndex].stop();
		currentActIndex--;
		updateTutorial();
	}

	public void updateTutorial() {
		if (currentActIndex == piece.length) {
			startActivity(new Intent("com.bytopia.abalone.MAINMENU"));
			finish();
			return;
		}
		if (currentActIndex == piece.length - 1) {
			butNext.setText(R.string.finish);
		} else {
			butNext.setText(R.string.next);
			if (currentActIndex == 0) {
				butPrev.setEnabled(false);
			} else {
				butPrev.setEnabled(true);
			}
		}
		piece[currentActIndex].start();
		textView.setText(scripts[currentActIndex]);
	}

	@Override
	protected void onPause() {
		Log.d("state", "paused");
		if (currentActIndex != piece.length) {
			piece[currentActIndex].stop();
		}
		super.onPause();
	}

	public void putMarbles(Board b, int[] coords, byte side) {
		for (int i = 0; i < coords.length - 1; i += 2) {
			b.setState(Cell.storage[coords[i]][coords[i + 1]], side);
		}
	}

}
