/**
* Copyright (c) 2010-2011 Yaroslav Geryatovich, Alexander Yakushev
* Distributed under the GNU GPL v3. For full terms see the file docs/COPYING.
*/
package com.bytopia.abalone;

import java.util.ArrayList;
import java.util.List;

import com.bytopia.abalone.mechanics.Board;
import com.bytopia.abalone.mechanics.Layout;
import com.bytopia.abalone.mechanics.Side;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class GameOptionsActivity extends Activity {

	private Context context;
	private GameOptionsActivity self;
	private List<Layout> layouts;
	private TextView tvAiLevel;
	private TutorialBoardView boardView;
	SharedPreferences prefs;
	int current_ai_level;
	int current_layout;
	String vsType;

	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		context = getApplicationContext();
		self = this;
		vsType = getIntent().getExtras().getString("vs");

		prefs = PreferenceManager.getDefaultSharedPreferences(context);

		current_ai_level = prefs.getInt("ai_level", 0);
		current_layout = prefs.getInt("game_layout", 0);

		layouts = new ArrayList<Layout>();
		for (String layout : getResources()
				.getStringArray(R.array.game_layouts)) {
			String tempAi = "com.bytopia.abalone.mechanics." + layout;
			try {
				Class layoutClass = Class.forName(tempAi);
				Layout l = (Layout) layoutClass.newInstance();
				layouts.add(l);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}

		setContentView(R.layout.game_options);

		tvAiLevel = (TextView) findViewById(R.id.tv_ai_difficulty);
		tvAiLevel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				new AlertDialog.Builder(GameOptionsActivity.this).setTitle(
						getResources().getString(R.string.cpu_type)).setItems(
						R.array.bot_names,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								current_ai_level = which;
								self.refresh();
							}
						}).create().show();
			}
		});

		boardView = new TutorialBoardView(getApplicationContext());

		ImageView layout_prev = (ImageView) findViewById(R.id.btn_prev_layout);
		layout_prev.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				current_layout--;
				if (current_layout < 0) {
					current_layout = layouts.size() - 1;
				}
				refresh();
			}
		});

		ImageView layout_next = (ImageView) findViewById(R.id.btn_next_layout);
		layout_next.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				current_layout++;
				if (current_layout >= layouts.size()) {
					current_layout = 0;
				}
				refresh();
			}
		});

		LinearLayout brdPanel = (LinearLayout) findViewById(R.id.choose_layout);
		brdPanel.addView(boardView);

		Button startGame = (Button) findViewById(R.id.btn_start_game);
		startGame.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent("com.bytopia.abalone.GAME");
				intent.putExtra("vs", vsType);
				if (vsType.equals("cpu")) {
					intent.putExtra("cpu_type", getResources().getStringArray(
							R.array.bot_values)[current_ai_level]);
				}
				intent.putExtra("layout", layouts.get(current_layout).getClass().getName());
				
				prefs.edit().putInt("game_layout", current_layout);
				prefs.edit().putInt("ai_level", current_ai_level);
				startActivity(intent);
				finish();
			}
		});
		refresh();
	}

	public void refresh() {
		tvAiLevel
				.setText("Difficulty: "
						+ getResources().getStringArray(R.array.bot_names)[current_ai_level]);

		boardView.setBoard(new Board(layouts.get(current_layout), Side.BLACK));
	}

}
