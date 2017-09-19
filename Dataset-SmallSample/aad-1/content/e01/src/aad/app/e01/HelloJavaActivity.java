package aad.app.e01;

import java.util.ArrayList;

import aad.app.e01.R;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

public class HelloJavaActivity extends Activity {

    private static final String TAG = "E01_HelloJava";

    /** Interface for a sellable object - one that has a name and a price */
    public interface ISellable {

        public long getPrice();

        public void setPrice(long price);

        public String getName();

        public void setName(String title);
    }

    /** A sellable Toy */
    public class Toy implements ISellable {

        private long mPrice = 50;
        private String mName;

        public Toy(String name) {

            mName = name;
        }
        
        @Override
        public long getPrice() {

            return mPrice;
        }

        @Override
        public void setPrice(long price) {

            mPrice = price;
        }

        public String getName() {

            return mName;
        }

        public void setName(String title) {

            mName = title;
        }

    }

    /** A sellable Book */
    public class Book implements ISellable {

        private long mPrice = 10;
        private String mTitle;

        public Book(String title) {

            mTitle = title;
        }

        public String getName() {

            return mTitle;
        }

        public void setName(String title) {

            mTitle = title;
        }

        @Override
        public long getPrice() {

            return mPrice;
        }

        @Override
        public void setPrice(long price) {

            mPrice = price;
        }

    }

    /** A sellable Comic Book */
    public class ComicBook extends Book {

        public ComicBook(String title) {

            super(title);
        }

        private int mIssue;

        public int getIssue() {

            return mIssue;
        }

        public void setIssue(int issue) {

            mIssue = issue;
        }
    }

    public static class Utility {
        public static float getPI() { return 3.14159265f; }
    }

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {

        Log.d(TAG, "onCreate()");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        // Call through the example code blocks
        presentationJavaRefresher_Access();
        presentationJavaRefresher_Block();
        presentationJavaRefresher_Classes();
        presentationJavaRefresher_Control();
        presentationJavaRefresher_Interfaces();
    }
    
    /** Examples of access methods */
    private void presentationJavaRefresher_Access() {

        Log.d(TAG, "presentationJavaRefresher_Access()");

        float pi = Utility.getPI();
        Log.d(TAG, "The value of PI is " + pi); // String conversion on concatenation
        Log.d(TAG, "The value of PI is " + String.valueOf(pi)); // Explicit String conversion
    }
    
    /** Examples of Blocks */
    private void presentationJavaRefresher_Block() {

        Log.d(TAG, "presentationJavaRefresher_Block()");

        {;}
        
    }    
    
    /** Examples of classes and their usage */
    private void presentationJavaRefresher_Classes() {

        Log.d(TAG, "presentationJavaRefresher_Classes()");

        // Button example
        findViewById(R.id.testButton).setOnClickListener(new OnClickListener(){
        @Override
        public void onClick(View v) {
            Toast.makeText(getApplicationContext(), "Button Clicked", Toast.LENGTH_LONG).show();
        }});
        
    }

    private void presentationJavaRefresher_Control() {

        Log.d(TAG, "presentationJavaRefresher_Control()");

        for (int index = 10; index > 0; index--) {
            Log.i(TAG, String.valueOf(index));
        }
        
        // $LEARN Two different ways to log out - check logcat to see the difference
        Log.i(TAG, "Blast Off!");
        System.out.println("Blast Off!");
    }
    
    private void presentationJavaRefresher_Interfaces() {

        Log.d(TAG, "presentationJavaRefresher_Interfaces()");

        Book newBook = new Book("The Art of War");

        ComicBook newComicBook = new ComicBook("Green Lantern");
        newComicBook.setIssue(20);

        ArrayList<Book> books = new ArrayList<Book>();
        books.add(newBook);
        books.add(newComicBook);

        for (Book book : books) {
            Log.i(TAG, "Book in collection: " + book.getName());
        }

        ArrayList<ISellable> sellableItems = new ArrayList<ISellable>();
        sellableItems.add(newBook);
        sellableItems.add(newComicBook);
        sellableItems.add(new Toy("Jack in the Box"));
        
        for (ISellable item : sellableItems) {
            Log.i(TAG, "Item in collection: " + item.getName() + " : " + item.getPrice() + "c");
        }

    }

}