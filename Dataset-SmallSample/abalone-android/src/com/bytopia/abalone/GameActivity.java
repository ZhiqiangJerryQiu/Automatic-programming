/**
* Copyright (c) 2010-2011 Yaroslav Geryatovich, Alexander Yakushev
* Distributed under the GNU GPL v3. For full terms see the file docs/COPYING.
*/
package com.bytopia.abalone;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;

import com.bytopia.abalone.mechanics.AiAnn;
import com.bytopia.abalone.mechanics.ArtificialIntilligence;
import com.bytopia.abalone.mechanics.Board;
import com.bytopia.abalone.mechanics.ClassicLayout;
import com.bytopia.abalone.mechanics.Game;
import com.bytopia.abalone.mechanics.Layout;
import com.bytopia.abalone.mechanics.Player;
import com.bytopia.abalone.mechanics.Side;

public class GameActivity extends Activity {
	private BoardView bw;
	private Game game;
	private String cpuType;
	private static final String FILE_NAME = "gamedump.bin";
	
	private static final int CAPTURED_BALL_SIZE = 34;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		Intent intent = getIntent();

		setContentView(R.layout.main);
		bw = (BoardView) findViewById(R.id.boardView);
		bw.setParent(this);
		
		LinearLayout topBalls = (LinearLayout) findViewById(R.id.top_balls);
		topBalls.setMinimumHeight(CAPTURED_BALL_SIZE);
		LinearLayout bottomBalls = (LinearLayout) findViewById(R.id.bottom_balls);
		bottomBalls.setMinimumHeight(CAPTURED_BALL_SIZE);

		if (intent.getAction().equals("com.bytopia.abalone.GAME")) {

			String sp = intent.getExtras().getString("vs");

			Player secondPlayer;

			if (sp.equals("cpu")) {

				cpuType = intent.getExtras().getString("cpu_type");
				if (cpuType == null) {
					cpuType = "AiAnn";
				}
				Log.d("state", cpuType);
				secondPlayer = getAi(cpuType);

			} else {
				secondPlayer = bw;
			}
			Layout layout = null;
			Log.d("layout", intent.getExtras().getString("layout"));
			try {
				layout = (Layout) Class.forName(
						intent.getExtras().getString("layout")).newInstance();
				if (layout == null) {
					layout = new ClassicLayout();
				}
			} catch (Exception e) {
				Log.d("error", "Error finding layout " + layout + ". Using ClassicLayout");
				layout = new ClassicLayout();
			}

			game = new Game(layout, Side.BLACK, bw, secondPlayer, bw, sp
					.equals("cpu") ? Game.CPU : Game.HUMAN);
			startGame();
		} else if (intent.getAction().equals("com.bytopia.abalone.RESUMEGAME")) {
			Log.d("state", "resumeing");
			try {
				FileInputStream fis = openFileInput(FILE_NAME);
				ObjectInputStream ois = new ObjectInputStream(fis);
				Board board = (Board) ois.readObject();
				byte side = ois.readByte();
				byte vsType = ois.readByte();
				Player secondPlayer;

				if (vsType == Game.HUMAN) {
					secondPlayer = bw;
				} else {
					secondPlayer = getAi((String) ois.readObject());
				}

				game = new Game(board, side, bw, secondPlayer, bw, vsType);

				byte n = ois.readByte();

				Log.d("resume", "read!!!");
				// bw.ballSizeRecalc();
				game.getBoard().setBlackCaptured(n);

				for (int i = 1; i <= n; i++) {
					ballCaptured(Side.BLACK);
				}

				n = ois.readByte();
				game.getBoard().setWhiteCaptured(n);
				for (int i = 1; i <= n; i++) {
					ballCaptured(Side.WHITE);
				}

				startGame();

			} catch (IOException e) {
				Log.d("state", "IOException");
				finish();
			} catch (ClassNotFoundException e) {
				Log.d("state", "ClassNotFoundException");
				finish();
			}
		}

		super.onCreate(savedInstanceState);

	}

	private ArtificialIntilligence getAi(String cpuValue) {
		final String prefix = "com.bytopia.abalone.mechanics.";
		ArtificialIntilligence secondPlayer = null;
		try {
			secondPlayer = (ArtificialIntilligence) Class.forName(
					prefix + cpuValue).newInstance();
			Log.d("cpu", secondPlayer.getClass().getSimpleName() + " used.");
		} catch (Exception e) {
			Log.d("cpu", "NotSuchCpuException. AiAnn used.");
			secondPlayer = new AiAnn();
		}

		return secondPlayer;
	}

	private void startGame() {
		bw.setGame(game);
		(new Thread(new Runnable() {

			@Override
			public void run() {

				game.start();
			}
		}, "Game Thread")).start();
	}

	@Override
	public Object onRetainNonConfigurationInstance() {
		bw.screenChanged();
		return super.onRetainNonConfigurationInstance();
	}

	@Override
	protected void onPause() {
		Log.d("state", "paused");

		// Create a new output file stream that’s private to this application.
		try {
			FileOutputStream fos = openFileOutput(FILE_NAME,
					Context.MODE_PRIVATE);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(game.getBoard());
			oos.writeByte(game.getSide());
			oos.writeByte(game.getVsType());
			if (cpuType != null) {
				oos.writeObject(cpuType);
			}
			oos.writeByte((byte)game.getBoard().getMarblesCaptured(Side.BLACK));
			oos.writeByte((byte)game.getBoard().getMarblesCaptured(Side.WHITE));
			oos.close();

		} catch (FileNotFoundException e) {
			Log.d("state", "FileNotFound");
		} catch (IOException e) {
			Log.d("state", "IO Exception");
		}

		super.onPause();
	}

	public void ballCaptured(final byte side) {
		Runnable runnable = new Runnable() {

			@Override
			public void run() {
				LinearLayout ll1 = (LinearLayout) findViewById(R.id.top_balls);
				LinearLayout ll2 = (LinearLayout) findViewById(R.id.bottom_balls);
				ImageView iw = new ImageView(bw.getContext());
				iw
						.setImageResource((side == Side.BLACK) ? R.drawable.black_ball
								: R.drawable.white_ball);
				iw.setAdjustViewBounds(true);
				iw.setMaxHeight(CAPTURED_BALL_SIZE);
				iw.setMaxWidth(CAPTURED_BALL_SIZE);
				iw.setScaleType(ScaleType.CENTER_INSIDE);
				if (side == Side.BLACK) {
					ll1.addView(iw);
				} else {
					ll2.addView(iw);
				}

			}
		};

		runOnUiThread(runnable);

	}

}
