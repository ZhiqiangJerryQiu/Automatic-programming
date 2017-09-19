package tests.verifiedBuilding;

import java.util.ArrayList;

import framework.Rules;
import framework.Test;
import framework.cards.Card;
import framework.interfaces.GameState;
import framework.interfaces.MoveMaker;
import framework.interfaces.activators.OnagerActivator;

/**
 * 
 * Test the functionality of Onager
 * 
 * @author Karla Burnett
 *
 */

public class CardActivatorOnagerBasicTest extends Test {

    @Override
    public String getShortDescription() {
        return "Test the basic functionality of Onager";
    }

    @Override
    public void run(GameState gameState, MoveMaker move) throws AssertionError,
            UnsupportedOperationException, IllegalArgumentException {
        
        gameState.setWhoseTurn(0);
        
        // Empty the discard pile and deck
        gameState.setDeck(new ArrayList<Card>());
        gameState.setDiscard(new ArrayList<Card>());
        
        // Set up the game state with the board as follows:
        //                [1]                //
        // Onager         [2] Forum          //
        //                [3]                //
        // Onager         [4]                //
        //                [5]                //
        //                [6] Onager         //
        //                [7]                //
        Card [] board = new Card[Rules.NUM_DICE_DISCS];
        for (int i = 0; i < Rules.NUM_DICE_DISCS; i++) {
           board[i] = Card.NOT_A_CARD;
        }
        
        board[1] = Card.ONAGER;
        board[3] = Card.ONAGER;
        gameState.setPlayerCardsOnDiscs(0, board);
        
        board[1] = Card.FORUM;
        board[3] = Card.NOT_A_CARD;
        board[5] = Card.ONAGER;
        gameState.setPlayerCardsOnDiscs(1, board);
        
        // Initialise the dice
        gameState.setActionDice(new int[] {4, 4, 2});
        
        // ---- LOSING A BATTLE ----
        
        // Activate the Onager on disc 4, choosing to attack the Forum but lose
        OnagerActivator activator = (OnagerActivator) move.chooseCardToActivate(Rules.DICE_DISC_4);
        activator.chooseDiceDisc(2);
        activator.giveAttackDieRoll(4);
        activator.complete();
        
        // Check that neither the Forum nor the Onager has mysteriously disappered
        assert(gameState.getPlayerCardsOnDiscs(0)[3] == Card.ONAGER);
        assert(gameState.getPlayerCardsOnDiscs(1)[1] == Card.FORUM);
        
        // ---- WINNING A BATTLE ----
        
        // Activate the Onager on disc 4, choosing to attack the Forum but this time win
        activator = (OnagerActivator) move.chooseCardToActivate(Rules.DICE_DISC_4);
        activator.chooseDiceDisc(2);
        activator.giveAttackDieRoll(6);
        activator.complete();
        
        // Check that the Onager has not mysteriously disappered, but that the
        // forum is gone (and in the discard pile)
        assert(gameState.getPlayerCardsOnDiscs(0)[3] == Card.ONAGER);
        assert(gameState.getPlayerCardsOnDiscs(1)[1] == Card.NOT_A_CARD);
        assert(gameState.getDiscard().contains(Card.FORUM));
        
        // ---- BATTLE DIE EQUAL TO CARD DEFENCE (WIN) ----
        
        // Activate the Onager on disc 2, choosing to attack opponent's Onager
        activator = (OnagerActivator) move.chooseCardToActivate(Rules.DICE_DISC_2);
        activator.chooseDiceDisc(6);
        activator.giveAttackDieRoll(4);
        activator.complete();
        
        // Check that your Onager has not mysteriously disappered, but that the
        // opponent's Onager is gone (and in the discard pile)
        assert(gameState.getPlayerCardsOnDiscs(0)[1] == Card.ONAGER);
        assert(gameState.getPlayerCardsOnDiscs(1)[5] == Card.NOT_A_CARD);
        assert(gameState.getDiscard().contains(Card.ONAGER));
        
        // Check that board state is right
        board = gameState.getPlayerCardsOnDiscs(0);
        for (int i = 0; i < board.length; i++) {
           if (i == 1 || i == 3) {
              assert(board[i] == Card.ONAGER);
           } else {
              assert(board[i] == Card.NOT_A_CARD);
           }
        }
        
        board = gameState.getPlayerCardsOnDiscs(1);
        for (int i = 0; i < board.length; i++) {
           assert(board[i] == Card.NOT_A_CARD);
        }
    }
}
