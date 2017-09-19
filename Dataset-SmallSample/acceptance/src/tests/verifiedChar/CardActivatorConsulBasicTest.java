package tests.verifiedChar;

import java.util.ArrayList;
import java.util.Collection;

import framework.Rules;
import framework.Test;
import framework.cards.Card;
import framework.interfaces.GameState;
import framework.interfaces.MoveMaker;
import framework.interfaces.activators.ConsulActivator;

/**
 * 
 * Test the the card Consul
 * 
 * @author Junjie CHEN
 *
 */

public class CardActivatorConsulBasicTest extends Test {

    @Override
    public String getShortDescription() {
        return "Test the the card Consul";
    }

    @Override
    public void run(GameState gameState, MoveMaker move) throws AssertionError,
            UnsupportedOperationException, IllegalArgumentException {
        if (1==1) {
            throw new IllegalArgumentException();
        }

        gameState.setWhoseTurn(0);
        
        Collection<Card> hand = new ArrayList<Card>();
        hand.add(Card.CONSUL);
        
        gameState.setPlayerHand(0, hand);
        gameState.setPlayerSestertii(0, 10);
        
        move.placeCard(Card.CONSUL, Rules.DICE_DISC_2);
        
        gameState.setActionDice(new int[] {2,2,2});
        
        ConsulActivator activator = (ConsulActivator)move.chooseCardToActivate(Rules.DICE_DISC_2);
        activator.chooseConsulChangeAmount(1);
        activator.chooseWhichDiceChanges(2);
        activator.complete();
        
        int[] dice = gameState.getActionDice();
        
        boolean found = false;
        for(int die : dice) {            
            if(die == 3) {
                found = true;
            }
        }
        
        assert(found);
    }

}
