package tests.verifiedChar;

import java.util.ArrayList;

import framework.Rules;
import framework.Test;
import framework.cards.Card;
import framework.interfaces.GameState;
import framework.interfaces.MoveMaker;
import framework.interfaces.activators.VelitesActivator;

/**
 * 
 * Test the functionality of Velites
 * 
 * @author Karla Burnett
 *
 */

public class CardActivatorVelitesABasicTest extends Test {

    @Override
    public String getShortDescription() {
        return "Test the basic functionality of Velites";
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
        // Velites        [2] Legat          //
        //                [3]                //
        // Velites        [4]                //
        //                [5]                //
        //                [6] Velites        //
        //                [7]                //
        Card [] board = new Card[Rules.NUM_DICE_DISCS];
        for (int i = 0; i < Rules.NUM_DICE_DISCS; i++) {
           board[i] = Card.NOT_A_CARD;
        }
        
        board[1] = Card.VELITES;
        board[3] = Card.VELITES;
        gameState.setPlayerCardsOnDiscs(0, board);
        
        board[1] = Card.LEGAT;
        board[3] = Card.NOT_A_CARD;
        board[5] = Card.VELITES;
        gameState.setPlayerCardsOnDiscs(1, board);
        
        // Initialise the dice
        gameState.setActionDice(new int[] {4, 4, 2});
        
        // ---- LOSING A BATTLE ----
        
        // Activate the Velites on disc 4, choosing to attack the Legat but lose
        VelitesActivator activator = (VelitesActivator) move.chooseCardToActivate(Rules.DICE_DISC_4);
        activator.chooseDiceDisc(2);
        activator.giveAttackDieRoll(1);
        activator.complete();
        
        // Check that neither the Forum nor the Onager has mysteriously disappered
        assert(gameState.getPlayerCardsOnDiscs(0)[3] == Card.VELITES);
        assert(gameState.getPlayerCardsOnDiscs(1)[1] == Card.LEGAT);
        
        // ---- WINNING A BATTLE ----
        
        // Activate the Velites on disc 4, choosing to attack the Legat but this time win
        activator = (VelitesActivator) move.chooseCardToActivate(Rules.DICE_DISC_4);
        activator.chooseDiceDisc(2);
        activator.giveAttackDieRoll(4);
        activator.complete();
        
        // Check that the Velites has not mysteriously disappered, but that the
        // legat is gone (and in the discard pile)
        assert(gameState.getPlayerCardsOnDiscs(0)[3] == Card.VELITES);
        assert(gameState.getPlayerCardsOnDiscs(1)[1] == Card.NOT_A_CARD);
        assert(gameState.getDiscard().contains(Card.LEGAT));
        
        // ---- BATTLE DIE EQUAL TO CARD DEFENCE (WIN) ----
        
        // Activate the Velites on disc 2, choosing to attack opponent's Velites
        activator = (VelitesActivator) move.chooseCardToActivate(Rules.DICE_DISC_2);
        activator.chooseDiceDisc(6);
        activator.giveAttackDieRoll(3);
        activator.complete();
        
        // Check that your Velites has not mysteriously disappered, but that the
        // opponent's Velites is gone (and in the discard pile)
        assert(gameState.getPlayerCardsOnDiscs(0)[1] == Card.VELITES);
        assert(gameState.getPlayerCardsOnDiscs(1)[5] == Card.NOT_A_CARD);
        assert(gameState.getDiscard().contains(Card.VELITES));
        
        // Check that board state is right
        board = gameState.getPlayerCardsOnDiscs(0);
        for (int i = 0; i < board.length; i++) {
           if (i == 1 || i == 3) {
              assert(board[i] == Card.VELITES);
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
