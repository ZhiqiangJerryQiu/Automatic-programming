package aad.app.c08;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.Spinner;

public class HelloListActivity extends Activity implements OnClickListener {

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        LinearLayout buttonLinearLayout = (LinearLayout) this.findViewById(R.id.buttonLinearLayout);

        // Add an onClickListener to all the children
        int childCount = buttonLinearLayout.getChildCount();
        for (int i = 0; i < childCount; i++) {

            buttonLinearLayout.getChildAt(i).setOnClickListener(this);
        }

        // Add the listener to the button outside the container
        this.findViewById(R.id.startButton).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        // Get the tag value and route accordingly

        Intent i = new Intent();

        // TEACH Just showing off some different string comparison options
        if (String.valueOf(view.getTag()).equalsIgnoreCase("basic")) {

            i.setClass(this, BasicListActivity.class);

        }
        else if (view.getTag().equals("custom")) {

            i.setClass(this, CustomListViewActivity.class);

        }
        else if ("sql".equals(view.getTag())) { // LEARN What is this wizardry!? A quoted string is an object?!

            i.setClass(this, DatabaseListActivity.class);

        }
        else if (view.getId() == R.id.contactListButton) {

            i.setClass(this, ContactListActivity.class);

        }
        else if (view.getId() == R.id.startButton) {

            Spinner spinner = (Spinner) this.findViewById(R.id.typeSpinner);

            int pos = spinner.getSelectedItemPosition(); // You could also do a string comparison
            switch (pos) {

            case 0:
                i.setClass(this, BasicListActivity.class);
                break;

            case 1:
                i.setClass(this, CustomListViewActivity.class);
                break;

            case 2:
                i.setClass(this, DatabaseListActivity.class);
                break;

            case 3:
                i.setClass(this, ContactListActivity.class);
                break;

            default:
                i.setClass(this, BasicListActivity.class);
                break;
            }

        }

        // Actually start the Activity
        startActivity(i);

    }
}