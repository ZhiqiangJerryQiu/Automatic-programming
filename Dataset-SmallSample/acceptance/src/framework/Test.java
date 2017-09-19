package framework;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;

import framework.cards.Card;
import framework.interfaces.GameState;
import framework.interfaces.MoveMaker;

/**
 * Provides a template for writing tests for Roma.
 *
 * <h1>How to Write a Test for Roma</h1>
 *
 * <p>
 * Writing a test for Roma is as simple as implementing this class.
 * When the tester runs, it will call your test's run method. The
 * parameters of this method contain a GameState, and a MoveMaker. You can
 * manipulate these to be in any legal state, and check it changes
 * correctly when you make a move. The GameState given will be be in
 * the initial Roma Game condition.
 * </p>
 *
 * <p>
 * The tester will take care of printing a lot of information for you.
 * This includes your short description, so make sure it gives enough
 * information to show what the test is about. Also name your class
 * something appropriate, as this will also be printed. Any method you
 * call in the MoveMaker interface will also be printed, along with its
 * arguments.<br/>
 * If you do need to print miscellaneous information, <b> do not print
 * to {@code System.out}! </b> Instead, print to {@code this.out}. The
 * tester can then handle the printed text correctly.
 * </p>
 *
 * <p>
 * When you have written your test, either add it to the git repository
 * with a pull request, or email your tutorial acceptance
 * representative.
 * </p>
 *
 * @author Benjamin James Wright (ben.wright)
 * @author Damon Stacey (damon.stacey)
 * @author Matthew Moss (matthew.moss)
 * @author Lasath Fernando (lasath.fernando)
 */
public abstract class Test {

    /**
     * Returns a single line description of this test.
     *
     * @return a one-line string of no more than 60 characters
     */
    public abstract String getShortDescription();

    /**
     * Runs this test against the given gameState and MoveMaker.
     *
     * <p>
     * Information about the current test should be written to this.out.
     * It is the responsibility of the test author to ensure all moves
     * made by their tester are valid for the current gameState.
     * </p>
     *
     * <p>
     * This test should check conditions in the gameState using
     * assertions. The testing framework will handle the generated
     * AssertionError exceptions. A test is considered successful if
     * no exceptions are thrown when this method is called.
     * </p>
     *
     * @param gameState an initial GameState to change and check
     * @param move a MoveMaker interface for you to call
     * @throws AssertionError on test failure
     * @throws UnsupportedOperationException on unimplemented features
     * @throws IllegalArgumentException on invalid tests
     */
    public abstract void run (GameState gameState, MoveMaker move)
                                                  throws AssertionError,
                                          UnsupportedOperationException,
                                               IllegalArgumentException;

    /******************************************************************/
    /*   Ignore everything below this line. This is for the tester!   */
    /******************************************************************/

    protected final PrintStream   out;
    private ByteArrayOutputStream buffer;

    public Test() {
        buffer = new ByteArrayOutputStream();
        this.out = new PrintStream(buffer);
    }

    String getOutputSteam() {
        return buffer.toString();
    }

    protected int getIndexFromPile (Card toFind, List<Card> pile) {
       int index = -1;
       boolean found = false;
       for (int i = 0; i < pile.size() && !found; i++) {
          //System.out.println("Finding: " + toFind + " Found: " + pile.get(i));
          if (pile.get(i).equals(toFind)) {
             found = true;
             index = i;
          }
       }
       return index;
    }
}
