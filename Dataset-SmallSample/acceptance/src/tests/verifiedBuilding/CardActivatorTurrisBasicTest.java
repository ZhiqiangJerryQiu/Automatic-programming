package tests.verifiedBuilding;

import java.util.ArrayList;
import java.util.Collection;

import framework.Test;
import framework.cards.Card;
import framework.interfaces.GameState;
import framework.interfaces.MoveMaker;
import framework.interfaces.activators.LegionariusActivator;

/**
 * 
 * Test the functionality of Turris
 * 
 * @author Chris Fong
 *
 */

public class CardActivatorTurrisBasicTest extends Test {

    private static final int PLAYER_1 = 0;
    private static final int PLAYER_2 = 1;
    
    @Override
    public String getShortDescription() {
        return "Test the functionality of Turris";
    }

    @Override
    public void run(GameState gameState, MoveMaker move) throws AssertionError,
            UnsupportedOperationException, IllegalArgumentException {
      if (1==1) {
         throw new IllegalArgumentException();
      }        
        
        gameState.setPlayerSestertii(PLAYER_1, 16);
        gameState.setPlayerSestertii(PLAYER_2, 9);
        
        Collection<Card> hand = new ArrayList<Card>();
        hand.add(Card.TURRIS);
        hand.add(Card.TURRIS);
        
        gameState.setPlayerHand(PLAYER_1, hand);
        
        gameState.setPlayerCardsOnDiscs(PLAYER_1, 
                new Card[] {Card.NOT_A_CARD,
                            Card.PRAETORIANUS,
                            Card.NOT_A_CARD,
                            Card.NOT_A_CARD,
                            Card.NOT_A_CARD,
                            Card.NOT_A_CARD,
                            Card.NOT_A_CARD});
            
        gameState.setPlayerCardsOnDiscs(PLAYER_2, 
                new Card[] {Card.NOT_A_CARD,
                            Card.LEGIONARIUS,
                            Card.LEGIONARIUS,
                            Card.NOT_A_CARD,
                            Card.NOT_A_CARD,
                            Card.NOT_A_CARD,
                            Card.NOT_A_CARD});
        
        gameState.setWhoseTurn(PLAYER_2);
        gameState.setActionDice(new int[] {2,4,6});
        
        LegionariusActivator Nero = (LegionariusActivator) move.chooseCardToActivate(2);
        Nero.giveAttackDieRoll(4);
        Nero.complete();
        
        assert(gameState.getPlayerCardsOnDiscs(PLAYER_1)[1] == Card.NOT_A_CARD);
        
        gameState.setPlayerCardsOnDiscs(PLAYER_1, 
                new Card[] {Card.NOT_A_CARD,
                            Card.PRAETORIANUS,
                            Card.NOT_A_CARD,
                            Card.NOT_A_CARD,
                            Card.NOT_A_CARD,
                            Card.NOT_A_CARD,
                            Card.NOT_A_CARD});
        
        move.endTurn();
        gameState.setActionDice(new int[] {2,3,2});
        move.placeCard(Card.TURRIS, 3);
        
        assert(gameState.getPlayerCardsOnDiscs(PLAYER_1)[2] == Card.TURRIS);
        assert(gameState.getPlayerSestertii(PLAYER_1) == 10);
        
        move.endTurn();
        gameState.setActionDice(new int[] {2,3,2});

        Nero = (LegionariusActivator) move.chooseCardToActivate(2);
        Nero.giveAttackDieRoll(4);
        Nero.complete();
        
        assert(gameState.getPlayerCardsOnDiscs(PLAYER_1)[1] == Card.PRAETORIANUS);
        assert(gameState.getDiscard().contains(Card.PRAETORIANUS));
        
        Nero = (LegionariusActivator) move.chooseCardToActivate(3);
        Nero.giveAttackDieRoll(6);
        Nero.complete();
        
        assert(gameState.getPlayerCardsOnDiscs(PLAYER_1)[2] == Card.NOT_A_CARD);
        assert(gameState.getDiscard().contains(Card.TURRIS));
        
        Nero = (LegionariusActivator) move.chooseCardToActivate(2);
        Nero.giveAttackDieRoll(4);
        Nero.complete();
        
        assert(gameState.getPlayerCardsOnDiscs(PLAYER_1)[1] == Card.NOT_A_CARD);
    }

}
