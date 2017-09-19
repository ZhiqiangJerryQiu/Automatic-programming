package tests.verifiedChar;

import framework.Test;
import framework.interfaces.GameState;
import framework.interfaces.MoveMaker;

/**
 * An example test.
 *
 * @author Matthew Moss (matthew.moss)
 */
public class MoverEndTurnTest extends Test {

    @Override
    public String getShortDescription() {
        return "Checking if player advances at end of turn.";
    }

    @Override
    public void run(GameState gameState, MoveMaker move)
                                          throws AssertionError,
                                          UnsupportedOperationException,
                                          IllegalArgumentException {

        // You shouldn't need to print anything in your tests. The
        // tester will take care of printing:
        // - the class name and discription of the test
        // - whether the test succeeds or fails
        // - any methods called in the MoveMaker interface, with arguments
        // If you do need to print, never print to System.out!
        // Only print to this.out.
        out.println("This is just an example");

        // Set up the GameState for the test
        gameState.setWhoseTurn(0);

        // Do something in the game with the MoveMaker interface
        move.endTurn();

        // Check the GameState responded as expected
        // The tester will mark a test as failed if an assert is false
        assert (gameState.getWhoseTurn() == 1);
        move.endTurn();
        assert (gameState.getWhoseTurn() == 0);

        // The tester will automatically print the things every test
        // should have. These include a smiley, and "You Are Awesome!"
    }
}
