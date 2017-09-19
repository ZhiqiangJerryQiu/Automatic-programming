package tests.verifiedBuilding;

import java.util.ArrayList;
import java.util.Collection;

import framework.Rules;
import framework.Test;
import framework.cards.Card;
import framework.interfaces.GameState;
import framework.interfaces.MoveMaker;
import framework.interfaces.activators.MercatusActivator;

/**
 * 
 * Test the basic functionality of Mercatus
 * 
 * @author Jacky CHEN
 *
 */

public class CardActivatorMercatusBasicTest extends Test {

    @Override
    public String getShortDescription() {
        return "Test the basic functionality of Mercatus";
    }

    @Override
    public void run(GameState gameState, MoveMaker move) throws AssertionError,
            UnsupportedOperationException, IllegalArgumentException {
      if (1==1) {
         throw new IllegalArgumentException();
      }        

        gameState.setWhoseTurn(0);
        gameState.setPlayerVictoryPoints(0, 10);
        gameState.setPlayerVictoryPoints(1, 10);
        gameState.setPlayerSestertii(0, 30);
        assert(gameState.getPoolVictoryPoints() == 16);
        
        Collection<Card> hand = new ArrayList<Card>();
        hand.add(Card.MERCATUS);
        
        gameState.setPlayerHand(0, hand);
        
        //no cards on the field
        Card[] field = new Card[Rules.NUM_DICE_DISCS];
        for(int i = 0; i < field.length; i++) {
            field[i] = Card.NOT_A_CARD;
        }
        
        gameState.setPlayerCardsOnDiscs(0, field);
        
        //set three forums in the opponent's field
        field = new Card[Rules.NUM_DICE_DISCS];
        for(int i = 0; i < field.length; i++) {
            field[i] = Card.NOT_A_CARD;
        }
        field[2] = Card.FORUM;
        field[4] = Card.FORUM;
        field[5] = Card.FORUM;
        
        gameState.setPlayerCardsOnDiscs(1, field);
        
        gameState.setActionDice(new int[] {4,4,4});
        
        //================ test starts ===================
        move.placeCard(Card.MERCATUS, Rules.DICE_DISC_4);
        
        assert(gameState.getPlayerSestertii(0) == 24);
        
        MercatusActivator activator = (MercatusActivator) move.chooseCardToActivate(Rules.DICE_DISC_4);
        activator.complete();

        assert(gameState.getPlayerVictoryPoints(0) == 13);
        assert(gameState.getPlayerVictoryPoints(1) == 7);
        
    }

    
    
}
