/*
 * Copyright (C) 2010 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package aad.app.c27.test;

import aad.app.c27.HelloTestActivity;

import android.app.Instrumentation;
import android.os.SystemClock;
import android.test.ActivityInstrumentationTestCase2;
import android.test.TouchUtils;
import android.test.UiThreadTest;
import android.view.MotionEvent;
import android.widget.Button;
import android.widget.EditText;

public class HelloTestActivityTest extends ActivityInstrumentationTestCase2<HelloTestActivity> {

    private HelloTestActivity mActivity;
    private String mButtonSelection;

    private Button b1;
    private Button b2;
    private Button b3;
    private Button b4;
    private EditText mEditText;

    // Constructor
    public HelloTestActivityTest() {

        super("aad.app.c27.HelloTestActivity", HelloTestActivity.class);
    }

    /**
     * Setup the test.
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();

        // Turn on touch mode
        setActivityInitialTouchMode(true);

        mActivity = getActivity();

        b1 = (Button) mActivity.findViewById(aad.app.c27.R.id.btn1);
        b2 = (Button) mActivity.findViewById(aad.app.c27.R.id.btn2);
        b3 = (Button) mActivity.findViewById(aad.app.c27.R.id.btn3);
        b4 = (Button) mActivity.findViewById(aad.app.c27.R.id.btn4);

        mEditText = (EditText) mActivity.findViewById(aad.app.c27.R.id.mainEditText);
    }

    public void testButton1() {

//        // Look at the javadoc for this call
//        MotionEvent event = MotionEvent.obtain(
//                SystemClock.uptimeMillis(),
//                SystemClock.uptimeMillis(),
//                MotionEvent.ACTION_DOWN,
//                80, 260, // TODO Show where I got these from
//                1, // Normal pressure
//                1, // Normal size
//                0, 0, 0, 0, 0);
//
//        getInstrumentation().sendPointerSync(event);
//
//        event = MotionEvent.obtain(
//                SystemClock.uptimeMillis(),
//                SystemClock.uptimeMillis(),
//                MotionEvent.ACTION_UP,
//                80, 260, // TODO Show where I got these from
//                1, // Normal pressure
//                1, // Normal size
//                0, 0, 0, 0, 0);
//
//        getInstrumentation().sendPointerSync(event);

        // .. Well S***
        TouchUtils.clickView(this, b1);

        String editText = mEditText.getText().toString();

        // See if we got what we are looking for
        assertEquals("1", editText);
    }

    public void testButton2() {

        TouchUtils.clickView(this, b1);
        TouchUtils.clickView(this, b2);

        String editText = mEditText.getText().toString();
        
        // Look at this... and understand
        assertNotSame("2", editText);
    }

    public void testButton3() {

        TouchUtils.clickView(this, b3);

        String editText = mEditText.getText().toString();
        
        assertEquals("3", editText);
    }

    public void testButton4() {

        TouchUtils.clickView(this, b4);

        String editText = mEditText.getText().toString();
        
        assertEquals("4", editText);
    }
    
    @UiThreadTest
    public void testStatePause() {

        // TODO Set our state
        getInstrumentation().callActivityOnPause(mActivity);

        getInstrumentation().callActivityOnResume(mActivity);
        // TODO Test to see if our state is valid
        // assertEquals(resultText, mSelection);

    }

}
