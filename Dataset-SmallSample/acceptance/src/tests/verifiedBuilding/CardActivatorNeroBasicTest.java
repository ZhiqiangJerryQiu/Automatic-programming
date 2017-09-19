package tests.verifiedBuilding;

import java.util.ArrayList;
import java.util.Collection;

import framework.Rules;
import framework.Test;
import framework.cards.Card;
import framework.interfaces.GameState;
import framework.interfaces.MoveMaker;
import framework.interfaces.activators.NeroActivator;

/**
 * 
 * Test the functionality of Nero
 * 
 * @author Junjie CHEN
 *
 */

public class CardActivatorNeroBasicTest extends Test {

    @Override
    public String getShortDescription() {
        return "Test the functionality of Nero";
    }

    @Override
    public void run(GameState gameState, MoveMaker move) throws AssertionError,
            UnsupportedOperationException, IllegalArgumentException {
        
        gameState.setWhoseTurn(0);
        
        Collection<Card> hand = new ArrayList<Card>();
        hand.add(Card.NERO);
        hand.add(Card.NERO);
        hand.add(Card.NERO);
        
        gameState.setPlayerHand(0, hand);
        
        //the opponent's field would have 3 building cards
        Card[] field = new Card[Rules.NUM_DICE_DISCS];
        for(int i = 0; i < field.length; i++) {
            field[i] = Card.NOT_A_CARD;
        }
        field[0] = Card.FORUM;
        field[1] = Card.NERO;
        field[3] = Card.ONAGER;
        
        gameState.setPlayerCardsOnDiscs(1, field);
        
        gameState.setPlayerSestertii(0, 24);
        gameState.setActionDice(new int[] {1,3,4});
        
        //place the Nero here
        move.placeCard(Card.NERO, Rules.DICE_DISC_1);
        
        assert(gameState.getPlayerSestertii(0) == 16);
        assert(gameState.getPlayerHand(0).size() == 2);
        assert(gameState.getPlayerHand(0).contains(Card.NERO));
        
        field = gameState.getPlayerCardsOnDiscs(0);
        assert(field[0] == Card.NERO);
        
        //kill the Forum
        NeroActivator activator = (NeroActivator) move.chooseCardToActivate(Rules.DICE_DISC_1);
        activator.chooseDiceDisc(Rules.DICE_DISC_1);
        activator.complete();
        
        //Nero would die
        field = gameState.getPlayerCardsOnDiscs(0);
        assert(field[0] == Card.NOT_A_CARD);
        assert(gameState.getDiscard().contains(Card.NERO));
        
        //Forum died as well
        field = gameState.getPlayerCardsOnDiscs(1);
        assert(field[0] == Card.NOT_A_CARD);
        assert(gameState.getDiscard().contains(Card.FORUM));
        
        //place another Nero
        move.placeCard(Card.NERO, Rules.DICE_DISC_3);
        
        assert(gameState.getPlayerSestertii(0) == 8);
        assert(gameState.getPlayerHand(0).size() == 1);
        assert(gameState.getPlayerHand(0).contains(Card.NERO));
        
        field = gameState.getPlayerCardsOnDiscs(0);
        assert(field[2] == Card.NERO);
        
        //kill the OnagerBehaviour
        activator = (NeroActivator) move.chooseCardToActivate(Rules.DICE_DISC_3);
        activator.chooseDiceDisc(Rules.DICE_DISC_4);
        activator.complete();
        
        //Nero would die
        field = gameState.getPlayerCardsOnDiscs(0);
        assert(field[2] == Card.NOT_A_CARD);
        assert(gameState.getDiscard().contains(Card.NERO));
        
        //OnagerBehaviour died as well
        field = gameState.getPlayerCardsOnDiscs(1);
        assert(field[3] == Card.NOT_A_CARD);
        assert(gameState.getDiscard().contains(Card.ONAGER));
        
        //place another Nero
        move.placeCard(Card.NERO, Rules.DICE_DISC_4);
        
        assert(gameState.getPlayerSestertii(0) == 0);
        assert(gameState.getPlayerHand(0).size() == 0);
        assert(!gameState.getPlayerHand(0).contains(Card.NERO));
        
        field = gameState.getPlayerCardsOnDiscs(0);
        assert(field[3] == Card.NERO);
        
    }
}
