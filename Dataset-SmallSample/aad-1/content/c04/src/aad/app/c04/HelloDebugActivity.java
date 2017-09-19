package aad.app.c04;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class HelloDebugActivity extends Activity {

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        exampleAccessNull1();
        exampleAccessNull2();
        exampleBadId();
        exampleConcurrency();

    }

    /** This is pretty dumb but it is to illustrate a point... */
    private void exampleAccessNull1() {

        Object object = new Object();
        object = null;
        int hashCode = object.hashCode();
        System.out.print(hashCode);
    }

    /** The compiler can only help you so much. */
    private void exampleAccessNull2() {

        Object object = newObject();
        int hashCode = object.hashCode();
        System.out.print(hashCode);
    }

    /** The indirection of resources comes at a cost. */
    private void exampleBadId() {

        Button myButton = (Button) this.findViewById(R.id.start);
        myButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View view) {

                System.out.print("Clickaroo");
            }
        });
    }
    
    /** A nastier example - concurrency issues are never this neat */
    ArrayList<Integer> mIntegers = new ArrayList<Integer>();
    private void exampleConcurrency() {

        // Thread to add
        Thread t0 = new Thread() {

            @Override
            public void run() {

                while (true) {
                    mIntegers.add(new Integer(1));
                    
                    // Try to do something with the values
                    for (Integer i : mIntegers) {
                        i++;
                    }
                }
            }
        };
        t0.setName("Add Integer Thread");
        t0.start();
        
        // Thread to clear
        Thread t1 = new Thread() {

            @Override
            public void run() {

                while (true)
                    mIntegers.clear();
            }
        };
        t1.setName("Clear Integer Thread");
        t1.start();
    }

    private Object newObject() {

        return null;
    }
}