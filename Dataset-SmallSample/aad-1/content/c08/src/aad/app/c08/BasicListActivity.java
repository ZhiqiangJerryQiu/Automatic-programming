package aad.app.c08;

import java.util.ArrayList;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


public class BasicListActivity extends ListActivity {

    private ArrayList<String> mArrayList = new ArrayList<String>();
    
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        mArrayList.add("Item 1");
        mArrayList.add("Item 2");
        mArrayList.add("Item 3");
        mArrayList.add("Item 4");
        mArrayList.add("Item 5");
        mArrayList.add("Item 6");
        mArrayList.add("Item 7");
        mArrayList.add("Item 8");
        mArrayList.add("Item 9");
        mArrayList.add("Item 10");
        
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mArrayList); 
        this.setListAdapter(arrayAdapter);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {

        Toast.makeText(this, ((TextView) v).getText(), Toast.LENGTH_SHORT).show();
    }
    
    
}
