package aad.app.c03;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;

import aad.app.c03.R;
import android.app.Activity;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;

import android.widget.ImageView;

public class HelloAssetActivity extends Activity {

    public static final String TAG = "HelloAssetActivity";

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        // Get something from the applications Assets
        AssetManager assetManager = this.getAssets();
        Resources resources = this.getResources();

        // Load a Drawable
        ImageView iv0 = (ImageView) this.findViewById(R.id.imageView0);
        iv0.setImageDrawable(resources.getDrawable(R.drawable.pm5544_ntsc_2));

        try {

            // Get an image as an asset
            InputStream is = assetManager.open("smpte.png");
            Bitmap bm = BitmapFactory.decodeStream(is);

            ImageView iv1 = (ImageView) this.findViewById(R.id.imageView1);
            iv1.setImageBitmap(bm);

            // Get a list of assets
            String[] files = assetManager.list("html");
            for (String file : files) {
                Log.i(TAG, "file: " + file);
            }

            // Get something from the raw resources
            is = resources.openRawResource(R.raw.rca_indian_head);
            bm = BitmapFactory.decodeStream(is);

            ImageView iv2 = (ImageView) this.findViewById(R.id.imageView2);
            iv2.setImageBitmap(bm);
            
            // Write something to the app dir on the SD Card
            String externalFileName = this.getExternalFilesDir(null) + "/test.txt";
            File externalWriteFile = new File(externalFileName);
            FileWriter fw = new FileWriter(externalWriteFile);
            fw.write("This is a test");
            fw.close();

            // Get something from the app dir on the SD Card
            String currentLine = null;
            File externalReadFile = new File(externalFileName);
            BufferedReader br = new BufferedReader(new FileReader(externalReadFile));
            while ((currentLine = br.readLine()) != null) {
                Log.i(TAG, "Read line: " + currentLine);
            }
            br.close();

        }
        catch (IOException e) {
            e.printStackTrace();
        }

    }
}