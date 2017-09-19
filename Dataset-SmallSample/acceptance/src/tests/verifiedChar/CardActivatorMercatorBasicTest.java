package tests.verifiedChar;

import java.util.ArrayList;
import java.util.Collection;

import framework.Rules;
import framework.Test;
import framework.cards.Card;
import framework.interfaces.GameState;
import framework.interfaces.MoveMaker;
import framework.interfaces.activators.MercatorActivator;

/**
 * 
 * Test the basic functionality of Mercator
 * 
 * @author Jacky CHEN
 *
 */

public class CardActivatorMercatorBasicTest extends Test {

    @Override
    public String getShortDescription() {
        return "Test the basic functionality of Mercator";
    }

    @Override
    public void run(GameState gameState, MoveMaker move) throws AssertionError,
            UnsupportedOperationException, IllegalArgumentException {
        

        gameState.setWhoseTurn(0);
        gameState.setPlayerVictoryPoints(0, 10);
        gameState.setPlayerVictoryPoints(1, 10);
        gameState.setPlayerSestertii(0, 30);
        gameState.setPlayerSestertii(1, 30);
        
        Collection<Card> hand = new ArrayList<Card>();
        hand.add(Card.MERCATOR);
        
        gameState.setPlayerHand(0, hand);
        
        //no cards on the field
        Card[] field = new Card[Rules.NUM_DICE_DISCS];
        for(int i = 0; i < field.length; i++) {
            field[i] = Card.NOT_A_CARD;
        }
        
        gameState.setActionDice(new int[] {1,1,1});
        
        //================ test starts ===================
        move.placeCard(Card.MERCATOR, Rules.DICE_DISC_1);
        
        assert(gameState.getPlayerSestertii(0) == 23);
        
        MercatorActivator activator = (MercatorActivator) move.chooseCardToActivate(Rules.DICE_DISC_1);
        activator.chooseMercatorBuyNum(4);
        activator.complete();
        
        assert(gameState.getPlayerSestertii(0) == 15);
        assert(gameState.getPlayerSestertii(1) == 38);
        assert(gameState.getPlayerVictoryPoints(0) == 14);
        assert(gameState.getPlayerVictoryPoints(1) == 6);
        
    }

    
    
}
