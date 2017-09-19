package tests.verifiedBuilding;
 
import java.util.ArrayList;
import java.util.Collection;
 
import framework.Test;
import framework.cards.Card;
import framework.interfaces.GameState;
import framework.interfaces.MoveMaker;
 
 
/**
 * Testing the basic mechanics of Mercatus.
 *
 * @author Nicholas Higgins (nicholas.higgins)
 * @author Calvin Tam (calvin.tam)
 */
public class CardActivatorMercatusBBasicTest extends Test {
 
	@Override
	public String getShortDescription() {
		return "Checks the basic mechanics of Mercatus";
	}
 
	@Override
	public void run(GameState gameState, MoveMaker move) throws AssertionError,
			UnsupportedOperationException, IllegalArgumentException {
 
        // Set up the player stats
        gameState.setPlayerVictoryPoints(0, 10);
        gameState.setPlayerVictoryPoints(1, 10);
        gameState.setPlayerSestertii(0, 6);
        gameState.setPlayerSestertii(1, 0);
 
        // Set up the game state for the test
        gameState.setWhoseTurn(0);
        gameState.setActionDice(new int [] {4, 5, 4});
 
        Card [] noForums = {Card.SICARIUS,
                Card.MERCATUS,
                Card.NOT_A_CARD,
                Card.CONSUL,
                Card.NOT_A_CARD,
                Card.ESSEDUM,
                Card.MACHINA};
 
    	 // Place 5 cards on the opponent's side with no forums
        gameState.setPlayerCardsOnDiscs(1, noForums);
 
        Collection<Card> hand = new ArrayList<Card>();
        hand.add(Card.MERCATUS);
        gameState.setPlayerHand(0, hand);
 
        // Place the Mercatus on disc 4 and activate it
        move.placeCard(Card.MERCATUS, 4);
        move.chooseCardToActivate(4).complete();
 
        // Check that player 0 hasn't gained victory points
        assert(gameState.getPlayerVictoryPoints(0) == 10);
        assert(gameState.getPlayerVictoryPoints(1) == 10);
        assert(gameState.getPlayerSestertii(0) == 0);
        assert(gameState.getPlayerSestertii(1) == 0);
 
        Card [] opponentSide = {Card.FORUM,
                                Card.AESCULAPINUM,
                                Card.NOT_A_CARD,
                                Card.FORUM,
                                Card.NOT_A_CARD,
                                Card.ESSEDUM,
                                Card.FORUM};
 
        // Place 5 new cards on the opponent's side which inclue 3 forums
        gameState.setPlayerCardsOnDiscs(1, opponentSide);
 
        // Activate the Mercatus again
        move.chooseCardToActivate(4).complete();
 
        // Check that player 0 has gained 3 victory points, and player 1
        //has lost 3
        assert(gameState.getPlayerVictoryPoints(0) == 13);
 
        //This is assuming the new rule has been applied and the points are taken
        //from your opponent
        assert(gameState.getPlayerVictoryPoints(1) == 7);
        assert(gameState.getPlayerSestertii(0) == 0);
        assert(gameState.getPlayerSestertii(1) == 0);
 
	}
}
