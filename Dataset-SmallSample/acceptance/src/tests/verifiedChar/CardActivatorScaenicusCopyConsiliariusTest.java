package tests.verifiedChar;

import framework.Test;
import framework.cards.Card;
import framework.interfaces.GameState;
import framework.interfaces.MoveMaker;
import framework.interfaces.activators.ConsiliariusActivator;
import framework.interfaces.activators.ScaenicusActivator;

/**
 *
 * Test basic activation of Scaenicus on a Consiliarius
 *
 * @author Daniel Morton
 *
 */

public class CardActivatorScaenicusCopyConsiliariusTest extends Test {

    @Override
    public String getShortDescription() {
        return "Test Scaenicus can copy Consiliarius";
    }

    @Override
    public void run(GameState gameState, MoveMaker move) throws AssertionError, UnsupportedOperationException, IllegalArgumentException {
        gameState.setPlayerVictoryPoints(0, 10);
        gameState.setPlayerVictoryPoints(1, 10);
        gameState.setPlayerSestertii(0, 10);
        gameState.setPlayerSestertii(1, 10);
        assert (gameState.getPoolVictoryPoints() == 16);
        Card[] cardsOnP1Discs = {Card.LEGAT,
                Card.CONSILIARIUS,
                Card.SCAENICUS,
                Card.MERCATOR,
                Card.LEGIONARIUS,
                Card.NOT_A_CARD,
                Card.NOT_A_CARD};
        gameState.setPlayerCardsOnDiscs(0,cardsOnP1Discs);
        gameState.setActionDice(new int[]{2,3,4});
        gameState.setWhoseTurn(0);
        ScaenicusActivator scaenicusActivator = (ScaenicusActivator)move.chooseCardToActivate(3);
        ConsiliariusActivator consiliariusActivator = (ConsiliariusActivator)scaenicusActivator.getScaenicusMimicTarget(2);
        consiliariusActivator.placeCard(Card.MERCATOR,3);
        consiliariusActivator.placeCard(Card.LEGIONARIUS,4);
        consiliariusActivator.placeCard(Card.SCAENICUS,5);
        consiliariusActivator.placeCard(Card.CONSILIARIUS,6);
        consiliariusActivator.placeCard(Card.LEGAT,7);

        consiliariusActivator.complete();
        scaenicusActivator.complete();

        assert(gameState.getPlayerCardsOnDiscs(0)[0]==Card.NOT_A_CARD);
        assert(gameState.getPlayerCardsOnDiscs(0)[1]==Card.NOT_A_CARD);
        assert(gameState.getPlayerCardsOnDiscs(0)[2]==Card.MERCATOR);
        assert(gameState.getPlayerCardsOnDiscs(0)[3]==Card.LEGIONARIUS);
        assert(gameState.getPlayerCardsOnDiscs(0)[4]==Card.SCAENICUS);
        assert(gameState.getPlayerCardsOnDiscs(0)[5]==Card.CONSILIARIUS);
        assert(gameState.getPlayerCardsOnDiscs(0)[6]==Card.LEGAT);

        assert (gameState.getActionDice().length == 2);
        assert (!gameState.isGameCompleted());





    }
}
