/**
* Copyright (c) 2010-2011 Yaroslav Geryatovich, Alexander Yakushev
* Distributed under the GNU GPL v3. For full terms see the file docs/COPYING.
*/
package com.bytopia.abalone;

import android.util.Log;

import com.bytopia.abalone.mechanics.Board;
import com.bytopia.abalone.mechanics.Group;
import com.bytopia.abalone.mechanics.Move;

public class Scenario {

	final static int PAUSE = 1000;
	final static int HALF_PAUSE = 500;
	final static int NONE = 0;

	static Object monitor = new Object();

	private Action[] actions;
	private boolean active = false;
	private Thread thread;

	TutorialBoardView boardView;
	private Board initial;

	public class Action {

		protected Object parameters;
		public int pause;

		public Action() {
		}

		public Action construct(Object parameters, int pause) {
			this.parameters = parameters;
			this.pause = pause;
			return this;
		}

		public void perform() {
		}
	}

	public class RestoreBoard extends Action {

		@Override
		public void perform() {
			Log.d("restore", "Board" + initial);
			boardView.setBoard(initial.clone());
		}
	}

	public class MakeMove extends Action {

		@Override
		public void perform() {
			boardView.setSelected(null);
			boardView.setHighlighted(null);
			boardView.makeMove((Move) parameters);
		}
	}

	public class SelectGroup extends Action {

		@Override
		public void perform() {
			boardView.setSelected((Group) parameters);
			boardView.postInvalidate();
		}
	}
	
	public class HighlightGroup extends Action {

		@Override
		public void perform() {
			boardView.setHighlighted((Group) parameters);
			boardView.postInvalidate();
		}
	}

	public Scenario(TutorialBoardView boardView, Board initialPosition) {
		this.boardView = boardView;
		initial = initialPosition;
	}

	public void SetActions(final Action... actions) {
		this.actions = actions;
	}

	public void playScenario(final Action[] actions) {
		final Scenario scenario = this;
		thread = new Thread() {
			@Override
			public void run() {
				synchronized (monitor) {
					try {
						synchronized (scenario) {
							scenario.notify();
						}
						boardView.setHighlighted(null);
						boardView.setSelected(null);
						Log.d("thread", "Called setBoard from scenarion thread, board" + initial);
						boardView.setBoard(initial.clone());
						Thread.sleep(PAUSE);
						while (active && actions != null) {
							for (Action a : actions) {
								if (!active) {
									return;
								}
								a.perform();
								Thread.sleep(a.pause);
							}
						}
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		};
		thread.start();
	}

	public void start() {
		active = true;
		synchronized (this) {
			playScenario(actions);
			try {
				this.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public void stop() {
		active = false;
	}
}
