package aad.app.c18;

import android.nfc.Tag;
import android.nfc.tech.MifareUltralight;
import android.util.Log;
import java.io.IOException;
import java.nio.charset.Charset;

public class TagTool {

    private static final String TAG = TagTool.class.getSimpleName();

    /**
     * Write the tag
     * @param tag
     * @param isSmiling
     */
    public void writeTag(Tag tag, boolean isSmiling) {

        Log.i(TAG, "writeTag() ");
        
        MifareUltralight mul = MifareUltralight.get(tag);
        try {
            mul.connect();

            Log.i(TAG, "connect() ");
            
            if (isSmiling)
                mul.writePage(4, "smyl".getBytes(Charset.forName("US-ASCII")));
            else
                mul.writePage(4, "frwn".getBytes(Charset.forName("US-ASCII")));
        }
        catch (IOException e) {
            Log.e(TAG, "writeTag() ", e);
        }
        finally {
            try {
                mul.close();
                Log.i(TAG, "close() ");
            }
            catch (IOException e) {
                Log.e(TAG, "writeTag() ", e);
            }
        }
    }

    /**
     * Read the tag
     * @param tag
     * @return
     */
    public String readTag(Tag tag) {

        MifareUltralight mul = MifareUltralight.get(tag);
        try {
            mul.connect();
            byte[] payload = mul.readPages(4);
            return new String(payload, Charset.forName("US-ASCII"));
        }
        catch (IOException e) {
            Log.e(TAG, "readTag() ", e);
        }
        finally {
            if (mul != null) {
                try {
                    mul.close();
                }
                catch (IOException e) {
                    Log.e(TAG, "readTag() ", e);
                }
            }
        }
        return null;
    }
}