package aad.app.l01;

import java.util.ArrayList;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

// A library of bad things to do
public class Fail  {

    /** This is pretty dumb but it is to illustrate a point... */
    public void exampleAccessNull1() {

        Object object = new Object();
        object = null;
        int hashCode = object.hashCode();
        System.out.print(hashCode);
    }

    /** The compiler can only help you so much. */
    public void exampleAccessNull2() {

        Object object = newObject();
        int hashCode = object.hashCode();
        System.out.print(hashCode);
    }

    /** The indirection of resources comes at a cost. */
    public void exampleBadId(View v, int id) {

        Button myButton = (Button) v.findViewById(id);
        myButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View view) {

                System.out.print("Clickaroo");
            }
        });
    }
    
    /** A nastier example - concurrency issues are never this neat */
    ArrayList<Integer> mIntegers = new ArrayList<Integer>();
    public void exampleConcurrency() {

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