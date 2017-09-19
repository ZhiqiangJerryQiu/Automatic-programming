package framework;

import java.io.PrintStream;

import framework.cards.Card;
import framework.interfaces.GameState;
import framework.interfaces.MoveMaker;
import framework.interfaces.activators.CardActivator;

/**
 * Wrapper for a testee's MoveMaker, to ensure no invalid moves are
 * given.
 *
 * @author Lasath Fernando (lasath.fernando)
 * @author Matthew Moss (matthew.moss)
 */
class SanityChecker implements MoveMaker {
    private MoveMaker mover;
    private GameState gameState;
    PrintStream out;

    public SanityChecker(MoveMaker mover, GameState gameState, PrintStream out) {
        this.mover = mover;
        this.gameState = gameState;
        this.out = out;
    }

    @Override
    public CardActivator chooseCardToActivate(int disc) {
        // print out all the arguments
        // the format of this string will need to be agreed/decided soon
        out.println("Calling MoveMaker.activateCard(");
        out.println("\tdisc = " + disc);
        out.println(")");

        // do whatever checks are necessary to ensure that the move is valid
        // in this.gameState
        // whatever checks are done here will have to be documented in the
        // MoveMaker interface
        if (disc < Rules.DICE_DISC_1 || disc > Rules.BRIBE_DISC) {
            throw new IllegalArgumentException("Not a valid disc");
        }

        out.println("Warning: SanityChecker not completed for this method. "
                + "Test may generate invalid moves.");

        // if everyhing checks out, actually call the method
        return mover.chooseCardToActivate(disc);
    }

    @Override
    public void activateCardsDisc(int diceToUse, Card chosen) throws UnsupportedOperationException {
        out.println("Warning: SanityChecker not implemented for this method. "
                + "Test may generate invalid moves.");

        activateCardsDisc(diceToUse, chosen);
    }

    @Override
    public void activateMoneyDisc(int diceToUse) throws UnsupportedOperationException {
        out.println("Warning: SanityChecker not implemented for this method. "
                + "Test may generate invalid moves.");

        mover.activateMoneyDisc(diceToUse);
    }

    @Override
    public void endTurn() throws UnsupportedOperationException {
        out.println("Warning: SanityChecker not implemented for this method. "
                + "Test may generate invalid moves.");

        mover.endTurn();
    }

    @Override
    public void placeCard(Card toPlace, int discToPlaceOn) throws UnsupportedOperationException {
        out.println("Warning: SanityChecker not implemented for this method. "
                + "Test may generate invalid moves.");

        mover.placeCard(toPlace, discToPlaceOn);
    }

    @Override
    public CardActivator activateBribeDisc(int diceToUse) throws UnsupportedOperationException {
        out.println("Warning: SanityChecker not implemented for this method. "
                + "Test may generate invalid moves.");

        return mover.activateBribeDisc(diceToUse);
    }
}
