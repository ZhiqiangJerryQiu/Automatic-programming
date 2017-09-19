package tests.verifiedChar;

import java.util.ArrayList;
import java.util.Collection;

import framework.Rules;
import framework.Test;
import framework.cards.Card;
import framework.interfaces.GameState;
import framework.interfaces.MoveMaker;
import framework.interfaces.activators.SicariusActivator;

/**
 * 
 * Test the functionality of Sicarius
 * 
 * @author Junjie CHEN
 *
 */

public class CardActivatorSicariusBasicTest extends Test {

    @Override
    public String getShortDescription() {
        return "Test the functionality of Sicarius";
    }

    @Override
    public void run(GameState gameState, MoveMaker move) throws AssertionError,
            UnsupportedOperationException, IllegalArgumentException {
        
        gameState.setWhoseTurn(0);
        
        Collection<Card> hand = new ArrayList<Card>();
        hand.add(Card.SICARIUS);
        hand.add(Card.SICARIUS);
        hand.add(Card.SICARIUS);
        
        gameState.setPlayerHand(0, hand);
        
        //the opponent's field would have 3 character cards
        Card[] field = new Card[Rules.NUM_DICE_DISCS];
        for(int i = 0; i < field.length; i++) {
            field[i] = Card.NOT_A_CARD;
        }
        field[0] = Card.SICARIUS;
        field[2] = Card.GLADIATOR;
        field[5] = Card.NERO;
        
        gameState.setPlayerCardsOnDiscs(1, field);
        
        gameState.setPlayerSestertii(0, 27);
        gameState.setActionDice(new int[] {1,3,1});
        
        //place the Sicarius here
        move.placeCard(Card.SICARIUS, Rules.DICE_DISC_1);
        
        assert(gameState.getPlayerSestertii(0) == 18);
        assert(gameState.getPlayerHand(0).size() == 2);
        assert(gameState.getPlayerHand(0).contains(Card.SICARIUS));
        
        field = gameState.getPlayerCardsOnDiscs(0);
        assert(field[0] == Card.SICARIUS);
        
        //kill the gladiator
        SicariusActivator activator = (SicariusActivator) move.chooseCardToActivate(Rules.DICE_DISC_1);
        activator.chooseDiceDisc(Rules.DICE_DISC_3);
        activator.complete();
        
        //sicarius would die
        field = gameState.getPlayerCardsOnDiscs(0);
        assert(field[0] == Card.NOT_A_CARD);
        assert(gameState.getDiscard().contains(Card.SICARIUS));
        
        //gladiator died as well
        field = gameState.getPlayerCardsOnDiscs(1);
        assert(field[2] == Card.NOT_A_CARD);
        assert(gameState.getDiscard().contains(Card.GLADIATOR));
        
        //place another sicarius
        move.placeCard(Card.SICARIUS, Rules.DICE_DISC_3);
        
        assert(gameState.getPlayerSestertii(0) == 9);
        assert(gameState.getPlayerHand(0).size() == 1);
        assert(gameState.getPlayerHand(0).contains(Card.SICARIUS));
        
        field = gameState.getPlayerCardsOnDiscs(0);
        assert(field[2] == Card.SICARIUS);
        
        //kill the nero
        activator = (SicariusActivator) move.chooseCardToActivate(Rules.DICE_DISC_3);
        activator.chooseDiceDisc(Rules.DICE_DISC_6);
        activator.complete();
        
        //sicarius would die
        field = gameState.getPlayerCardsOnDiscs(0);
        assert(field[2] == Card.NOT_A_CARD);
        assert(gameState.getDiscard().contains(Card.SICARIUS));
        
        //nero died as well
        field = gameState.getPlayerCardsOnDiscs(1);
        assert(field[5] == Card.NOT_A_CARD);
        assert(gameState.getDiscard().contains(Card.NERO));
        
        //place another Sicarius
        move.placeCard(Card.SICARIUS, Rules.DICE_DISC_4);
        
        assert(gameState.getPlayerSestertii(0) == 0);
        assert(gameState.getPlayerHand(0).size() == 0);
        assert(!gameState.getPlayerHand(0).contains(Card.SICARIUS));
        
        field = gameState.getPlayerCardsOnDiscs(0);
        assert(field[3] == Card.SICARIUS);
        
        //advance to next player's turn
        move.endTurn();
        
        gameState.setActionDice(new int[] {1,1,1});
        
        //kill the opponent Sicarius
        activator = (SicariusActivator)move.chooseCardToActivate(Rules.DICE_DISC_1);
        activator.chooseDiceDisc(Rules.DICE_DISC_4);
        activator.complete();
        
        //Sicarius would die
        field = gameState.getPlayerCardsOnDiscs(1);
        assert(field[0] == Card.NOT_A_CARD);
        assert(gameState.getDiscard().contains(Card.SICARIUS));
        
        //the other Sicarius died as well
        field = gameState.getPlayerCardsOnDiscs(0);
        assert(field[3] == Card.NOT_A_CARD);
    }

}
