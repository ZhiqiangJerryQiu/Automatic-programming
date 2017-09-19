package aad.app.c08;

import java.util.ArrayList;

import android.app.ListActivity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class CustomListViewActivity extends ListActivity {

    public enum CAR_TYPE { CAR, TRUCK };
    
    public class Car {
        
        public Car(String make, String model, String year, CAR_TYPE type) {
            this.make = make;
            this.model = model;
            this.year = year;            
            this.type = type;
        }
        
        public String make;
        public String model;
        public String year;
        public CAR_TYPE type;
    }

    private class CarAdapter extends ArrayAdapter<Car> {

        private ArrayList<Car> mCars;

        public CarAdapter(Context context, int textViewResourceId, ArrayList<Car> cars) {

            super(context, textViewResourceId, cars);
            this.mCars = cars;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View v = convertView;
            if (v == null) {
                LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = vi.inflate(R.layout.custom_list_view_item, null);
            }

            Car c = mCars.get(position);
            if (c != null) {

                
                ImageView vehicleImageView = (ImageView) v.findViewById(R.id.vehicleImageView);
                if (c.type == CAR_TYPE.TRUCK) 
                    vehicleImageView.setImageResource(R.drawable.truck);                    
                else
                    vehicleImageView.setImageResource(R.drawable.car);   
                
                TextView makeTextView = (TextView) v.findViewById(R.id.makeTextView);
                if (makeTextView != null)
                    makeTextView.setText(c.make);

                TextView modelTextView = (TextView) v.findViewById(R.id.modelTextView);
                if (modelTextView != null)
                    modelTextView.setText(c.year + " " + c.model);

            }
            return v;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        ArrayList<Car> myCars = new ArrayList<Car>();
        
        myCars.add(new Car("Fiat", "X1/9", "1982", CAR_TYPE.CAR));
        myCars.add(new Car("GMC", "Sierra Grande", "1984", CAR_TYPE.TRUCK));
        myCars.add(new Car("Toyota", "Celica", "1988", CAR_TYPE.CAR));
        myCars.add(new Car("Toyota", "Tacoma", "1999", CAR_TYPE.TRUCK));
        myCars.add(new Car("Volkswagen", "GLI", "2007", CAR_TYPE.CAR));
        

        CarAdapter carAdapter = new CarAdapter(this, R.layout.custom_list_view_item, myCars);
        this.setListAdapter(carAdapter);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {

        TextView tv = (TextView) v.findViewById(R.id.makeTextView);
        String carSelected = tv.getText().toString();
        Toast.makeText(this, carSelected, Toast.LENGTH_SHORT).show();
    }
}
