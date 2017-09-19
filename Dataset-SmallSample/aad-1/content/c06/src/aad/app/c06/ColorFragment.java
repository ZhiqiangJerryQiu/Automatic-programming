package aad.app.c06;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ColorFragment extends Fragment {

    private int mColor = 0; // Default to black
    
    private static final String KEY_INDEX = "index";

    public int getCurrentIndex() {
        
        Bundle b = getArguments();
        if (b != null)
            return b.getInt(KEY_INDEX, -1);
        
        return -2;
    }
    
    public static ColorFragment getInstance(int index) {

        ColorFragment fragment = new ColorFragment();

        Bundle args = new Bundle();
        args.putInt(KEY_INDEX, index);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment and set the color
        View v = inflater.inflate(R.layout.color_layout, container, false);

        v.setBackgroundColor(mColor);

        return v;
    }
    
    public void setColor(int color) {        
        
        mColor = color;
    }

}
