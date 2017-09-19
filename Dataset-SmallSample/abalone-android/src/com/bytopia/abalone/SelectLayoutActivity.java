/**
* Copyright (c) 2010-2011 Yaroslav Geryatovich, Alexander Yakushev
* Distributed under the GNU GPL v3. For full terms see the file docs/COPYING.
*/
package com.bytopia.abalone;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bytopia.abalone.mechanics.Board;
import com.bytopia.abalone.mechanics.Layout;
import com.bytopia.abalone.mechanics.Side;

public class SelectLayoutActivity extends Activity {

	List<Layout> layouts;
	private int index = 0;
	private BoardView boardView;
	private Button prev;
	private Button next;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		layouts = new ArrayList<Layout>();
		for (String layout : getResources()
				.getStringArray(R.array.game_layouts)) {
			String tempAi = "com.bytopia.abalone.mechanics." + layout;
			try {
				Class layoutClass = Class.forName(tempAi);
				Layout l = (Layout) layoutClass.newInstance();
				layouts.add(l);

			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}

		setContentView(R.layout.layout_selecting);
		boardView = new BoardView(getApplicationContext()) {
			@Override
			public boolean onTouchEvent(MotionEvent e) {
				if (e.getAction() == MotionEvent.ACTION_UP) {
					String name = layouts.get(index).getClass().getName();
					SharedPreferences preferences = PreferenceManager
							.getDefaultSharedPreferences(getApplicationContext());
					preferences.edit().putString("layout", name).commit();
					finish();
				}
				return true;
			}
		};

		prev = (Button) findViewById(R.id.slect_layout_prev);
		next = (Button) findViewById(R.id.slect_layout_next);
		refrashLayout();

		LinearLayout linearLayout = (LinearLayout) findViewById(R.id.layout_selecting_layout);
		linearLayout.addView(boardView);
	}

	private void refrashLayout() {
		//boardView.drawBoard(new Board(layouts.get(index), Side.BLACK));
		if (index == 0) {
			prev.setEnabled(false);
		} else {
			prev.setEnabled(true);
		}

		if (index == layouts.size() - 1) {
			next.setEnabled(false);
		} else {
			next.setEnabled(true);
		}
	}

	public void prev(View view) {
		index--;
		refrashLayout();
	}

	public void next(View view) {
		index++;
		refrashLayout();
	}
}
