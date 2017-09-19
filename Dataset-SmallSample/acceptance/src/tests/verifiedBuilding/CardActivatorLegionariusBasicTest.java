package tests.verifiedBuilding;

import java.util.ArrayList;
import java.util.Collection;

import framework.Rules;
import framework.Test;
import framework.cards.Card;
import framework.interfaces.GameState;
import framework.interfaces.MoveMaker;
import framework.interfaces.activators.LegionariusActivator;

/**
 * 
 * Test the functionality of Legionarius
 * 
 * @author Junjie CHEN
 *
 */

public class CardActivatorLegionariusBasicTest extends Test {

    @Override
    public String getShortDescription() {
        return "Test the functionality of Legionarius";
    }

    @Override
    public void run(GameState gameState, MoveMaker move) throws AssertionError,
            UnsupportedOperationException, IllegalArgumentException {
        
        gameState.setWhoseTurn(0);
        gameState.setDiscard(new ArrayList<Card>());
        
        Collection<Card> hand = new ArrayList<Card>();
        hand.add(Card.LEGIONARIUS);
        hand.add(Card.LEGIONARIUS);
        hand.add(Card.LEGIONARIUS);
        
        gameState.setPlayerHand(0, hand);
        
        //the opponent's field would have 3 character cards
        Card[] field = new Card[Rules.NUM_DICE_DISCS];
        for(int i = 0; i < field.length; i++) {
            field[i] = Card.NOT_A_CARD;
        }
        field[0] = Card.BASILICA;
        field[2] = Card.GLADIATOR;
        field[5] = Card.NERO;
        
        gameState.setPlayerCardsOnDiscs(1, field);
        
        gameState.setPlayerSestertii(0, 12);
        gameState.setActionDice(new int[] {1,4,6});
        
        move.placeCard(Card.LEGIONARIUS, Rules.DICE_DISC_1);
        
        assert(gameState.getPlayerSestertii(0) == 8);
        
        //kill the basilica
        LegionariusActivator activator = (LegionariusActivator)move.chooseCardToActivate(Rules.DICE_DISC_1);
        activator.giveAttackDieRoll(6);
        activator.complete();
        
        field = gameState.getPlayerCardsOnDiscs(1);
        assert(field[0] == Card.NOT_A_CARD);
        assert(gameState.getDiscard().contains(Card.BASILICA));
        
        //cant kill the nero because nero has 9 defence
        
        move.placeCard(Card.LEGIONARIUS, Rules.DICE_DISC_6);
        assert(gameState.getPlayerSestertii(0) == 4);
        
        //try to kill the nero
        activator =  (LegionariusActivator)move.chooseCardToActivate(Rules.DICE_DISC_6);
        activator.giveAttackDieRoll(6);
        activator.complete();
        
        field = gameState.getPlayerCardsOnDiscs(1);
        assert(field[5] == Card.NERO);
        assert(!gameState.getDiscard().contains(Card.NERO));
    }

}
