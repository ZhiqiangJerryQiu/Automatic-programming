package tests.verifiedSpec2;

import framework.Test;
import framework.cards.Card;
import framework.interfaces.GameState;
import framework.interfaces.MoveMaker;
import framework.interfaces.activators.GladiatorActivator;
import framework.interfaces.activators.ScaenicusActivator;
import framework.interfaces.activators.TelephoneBoxActivator;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created with IntelliJ IDEA.
 * @author Daniel.Morton
 * @author Robert.Cen
 * 
 */
public class CardActivatorTelephoneBoxScaenicusParadoxTest extends Test {


    @Override
    public String getShortDescription() {
        return "Test for the Scaenicus time paradox when replaying from a Telephone Box";
    }

    @Override
    public void run(GameState gameState, MoveMaker move) throws AssertionError, UnsupportedOperationException, IllegalArgumentException {
    	gameState.setWhoseTurn(0);
        gameState.setPlayerVictoryPoints(0, 10);
        gameState.setPlayerVictoryPoints(1, 10);
        gameState.setPlayerSestertii(0, 10);
        gameState.setPlayerSestertii(1, 10);
        gameState.setActionDice(new int[]{2,3,1});
        
        Collection<Card> p1Hand = new ArrayList<Card>();
        p1Hand.add(Card.CENTURIO);
        p1Hand.add(Card.FORUM);
        gameState.setPlayerHand(0,p1Hand);
        
        Collection<Card> p2Hand = new ArrayList<Card>();
        p2Hand.add(Card.GRIMREAPER);
        p2Hand.add(Card.TELEPHONEBOX);
        gameState.setPlayerHand(1,p2Hand);
        assert(!gameState.getPlayerHand(1).contains(Card.LEGAT));
        
        Card[] p1cardsOnField = {Card.TELEPHONEBOX,
                Card.SCAENICUS,
                Card.GLADIATOR,
                Card.NOT_A_CARD,
                Card.NOT_A_CARD,
                Card.NOT_A_CARD,
                Card.NOT_A_CARD};
        Card[] p2cardsOnField = {Card.LEGAT,
                Card.NOT_A_CARD,
                Card.NOT_A_CARD,
                Card.NOT_A_CARD,
                Card.NOT_A_CARD,
                Card.NOT_A_CARD,
                Card.NOT_A_CARD};
        gameState.setPlayerCardsOnDiscs(0,p1cardsOnField);
        gameState.setPlayerCardsOnDiscs(1,p2cardsOnField);
        
        ArrayList<Card> discard = new ArrayList<Card>();
        discard.add(Card.KAT);
        gameState.setDiscard(discard);
        
        //Activate scaenicus to copy a gladiator
        ScaenicusActivator scaenicusActivator = (ScaenicusActivator)move.chooseCardToActivate(2);
        GladiatorActivator gladiatorActivator = (GladiatorActivator)scaenicusActivator.getScaenicusMimicTarget(3);
        gladiatorActivator.chooseDiceDisc(1);
        gladiatorActivator.complete();
        scaenicusActivator.complete();

        //Test that the gladiator was copied and opponents legat was returned to hand.
        assert(gameState.getPlayerCardsOnDiscs(1)[0]==Card.NOT_A_CARD);
        assert(gameState.getPlayerHand(1).contains(Card.LEGAT));
        assert(gameState.getPlayerCardsOnDiscs(0)[0]==Card.TELEPHONEBOX);
        assert(gameState.getPlayerCardsOnDiscs(0)[1]==Card.SCAENICUS);
        assert(gameState.getPlayerCardsOnDiscs(0)[2]==Card.GLADIATOR);

        move.endTurn();
        gameState.setActionDice(new int[] {1,2,3});
        //Player should lose 7 points as the legat should of been sent back to hand
        assert (gameState.getPlayerVictoryPoints(1) == 3);
        assert (gameState.getPlayerSestertii(1) == 10);
        move.placeCard(Card.LEGAT, 1);
        assert (gameState.getPlayerSestertii(1) == 5);
        
        move.endTurn();
        gameState.setActionDice(new int[] {1, 2, 4});
        assert (gameState.getPlayerVictoryPoints(0) == 6);
        //Place centurio on top of gladiator
        move.placeCard(Card.CENTURIO, 3);        
        assert (gameState.getPlayerSestertii(0) == 1);
        assert (gameState.getPlayerCardsOnDiscs(0)[2] == Card.CENTURIO);
        assert (gameState.getDiscard().contains(Card.GLADIATOR));
        
        
        //Activate the telephone box to send the centurio 2 turns back
        TelephoneBoxActivator tBox = (TelephoneBoxActivator)move.chooseCardToActivate(1);
        tBox.setSecondDiceUsed(2);
        tBox.shouldMoveForwardInTime(false);
        tBox.chooseDiceDisc(3);
        tBox.complete();
        
        //Should cause time paradox as during the time travel, scaenicus
        //attempts to activate a different card (centurio)
        assert (gameState.isGameCompleted());
    }

}
