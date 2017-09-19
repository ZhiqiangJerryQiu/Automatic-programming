package aad.app.c27.test;

import android.test.AndroidTestCase;
import android.test.AssertionFailedError;

import junit.framework.Assert;


public class TestExamples extends AndroidTestCase {
    
    public void testSomething() throws Throwable {
        Assert.assertTrue(1 + 1 == 2);
     }

     public void testSomethingElse() throws Throwable {
        Assert.assertTrue(1 + 1 == 3);
     }   
      
     public void testFail() throws Throwable {
         throw new Exception();
     }
}
