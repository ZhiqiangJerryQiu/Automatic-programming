/**
* Copyright (c) 2010-2011 Yaroslav Geryatovich, Alexander Yakushev
* Distributed under the GNU GPL v3. For full terms see the file docs/COPYING.
*/
package com.bytopia.abalone;

import com.bytopia.abalone.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;

public class SplashAcitvity extends Activity {
	long m_dwSplashTime = 2000;
	boolean m_bPaused = false;

	boolean m_bSplashActive = true;

	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		// Draw the splash screen

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.splash);
		// Very simple timer thread
		Thread splashTimer = new Thread() {
			public void run() {
				try {
					// Wait loop
					long ms = 0;
					while (m_bSplashActive && ms < m_dwSplashTime) {
						sleep(100);
						// Advance the timer only if we're running.
						if (!m_bPaused)
							ms += 100;
					}
					// Advance to the next screen.
					startActivity(new Intent("com.bytopia.abalone.MAINMENU"));

				} catch (Exception e) {
					Log.e("Splash", e.toString());
				} finally {
					finish();
				}
			}
		};
		splashTimer.start();
	}

	protected void onPause() {
		super.onPause();
		m_bPaused = true;
	}

	protected void onResume() {
		super.onResume();
		m_bPaused = false;
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// if we get any key, clear the splash screen
		super.onKeyDown(keyCode, event);
		m_bSplashActive = false;
		return true;
	}
}
