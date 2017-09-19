package aad.app.c27.test;

import android.test.AndroidTestCase;

import java.io.File;

import junit.framework.Assert;


public class TestDatabase extends AndroidTestCase {
    
    private File mDatabaseFile;
    
    @Override
    protected void setUp() throws Exception {
      super.setUp();
   
      mDatabaseFile = this.getContext().getDatabasePath("my-data");
    }

    public void testDatabaseExists() throws Throwable {
        Assert.assertTrue(mDatabaseFile.exists());
     }
     
}
