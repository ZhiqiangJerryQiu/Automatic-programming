package tests.verifiedSpec2;

import framework.Rules;
import framework.Test;
import framework.cards.Card;
import framework.interfaces.GameState;
import framework.interfaces.MoveMaker;
import framework.interfaces.activators.MercatorActivator;
import framework.interfaces.activators.TelephoneBoxActivator;
import framework.interfaces.activators.TribunusPlebisActivator;

/**
 *
 * @author Matt
 */
public class TelephoneBoxTimeSurfinUSATest extends Test {

    public static final int PLAYER_1 = 0;
    public static final int PLAYER_2 = 1;

    @Override
    public String getShortDescription() {
        return "Tests multiple cards moving forwards in time";
    }

    @Override
    public void run(GameState gameState, MoveMaker move) throws AssertionError, UnsupportedOperationException, IllegalArgumentException {

        // Player two has some average cards
        Card[] discs = new Card[]{
            Card.TRIBUNUSPLEBIS,
            Card.AESCULAPINUM,
            Card.CONSILIARIUS,
            Card.MERCATOR,
            Card.TURRIS,
            Card.TEMPLUM,
            Card.CONSUL};
        gameState.setPlayerCardsOnDiscs(PLAYER_2, discs);

        // Player one is gearing up for something pretty cool
        discs = new Card[]{
            Card.TRIBUNUSPLEBIS,
            Card.FORUM,
            Card.KAT,
            Card.PRAETORIANUS,
            Card.SCAENICUS,
            Card.GRIMREAPER,
            Card.TELEPHONEBOX};
        gameState.setPlayerCardsOnDiscs(PLAYER_1, discs);

        // Player one is definitely winning at this point
        gameState.setPlayerVictoryPoints(PLAYER_1, 20);
        gameState.setPlayerVictoryPoints(PLAYER_2, 1);

        // Hand out some cash
        gameState.setPlayerSestertii(PLAYER_1, 1000);
        gameState.setPlayerSestertii(PLAYER_2, 1000);

        // And with this, we're set to go
        gameState.setWhoseTurn(PLAYER_1);

        gameState.setActionDice(new int[] {5,5,1});

        TelephoneBoxActivator activator = (TelephoneBoxActivator) move.activateBribeDisc(1);
        activator.chooseDiceDisc(Rules.DICE_DISC_1); // Goodby Tribunis Plebis
        activator.setSecondDiceUsed(5);
        activator.shouldMoveForwardInTime(true);
        activator.complete();

        assert (gameState.getPlayerCardsOnDiscs(PLAYER_1)[0] == Card.NOT_A_CARD);
        assert (gameState.getPlayerCardsOnDiscs(PLAYER_1)[1] == Card.FORUM);
        assert (gameState.getPlayerCardsOnDiscs(PLAYER_1)[2] == Card.KAT);
        assert (gameState.getPlayerCardsOnDiscs(PLAYER_1)[3] == Card.PRAETORIANUS);
        assert (gameState.getPlayerCardsOnDiscs(PLAYER_1)[4] == Card.SCAENICUS);
        assert (gameState.getPlayerCardsOnDiscs(PLAYER_1)[5] == Card.GRIMREAPER);
        assert (gameState.getPlayerCardsOnDiscs(PLAYER_1)[6] == Card.TELEPHONEBOX);

        // And we're done for now

        assert (gameState.getPlayerSestertii(PLAYER_1) == 999); // 1 Sest spent on TM activation
        move.endTurn();
        move.endTurn();
        assert (gameState.getPlayerVictoryPoints(PLAYER_1) == 19); // Lost one, for one empty space

        gameState.setActionDice(new int[] {5,3,1});

        activator = (TelephoneBoxActivator) move.activateBribeDisc(1);
        activator.chooseDiceDisc(Rules.DICE_DISC_3); // Goodby Tribunis Plebis
        activator.setSecondDiceUsed(3);
        activator.shouldMoveForwardInTime(true);
        activator.complete();

        assert (gameState.getPlayerCardsOnDiscs(PLAYER_1)[0] == Card.NOT_A_CARD);
        assert (gameState.getPlayerCardsOnDiscs(PLAYER_1)[1] == Card.FORUM);
        assert (gameState.getPlayerCardsOnDiscs(PLAYER_1)[2] == Card.NOT_A_CARD);
        assert (gameState.getPlayerCardsOnDiscs(PLAYER_1)[3] == Card.PRAETORIANUS);
        assert (gameState.getPlayerCardsOnDiscs(PLAYER_1)[4] == Card.SCAENICUS);
        assert (gameState.getPlayerCardsOnDiscs(PLAYER_1)[5] == Card.GRIMREAPER);
        assert (gameState.getPlayerCardsOnDiscs(PLAYER_1)[6] == Card.TELEPHONEBOX);

        // And we're done for now

        assert (gameState.getPlayerSestertii(PLAYER_1) == 998); // 1 Sest spent on TM activation

        move.endTurn();
        move.endTurn();
        assert (gameState.getPlayerVictoryPoints(PLAYER_1) == 17); // Lost two, for one empty space

        gameState.setActionDice(new int[] {5,1,1});

        activator = (TelephoneBoxActivator) move.activateBribeDisc(5);
        activator.chooseDiceDisc(Rules.DICE_DISC_5); // Goodby Tribunis Plebis
        activator.setSecondDiceUsed(1);
        activator.shouldMoveForwardInTime(true);
        activator.complete();

        assert (gameState.getPlayerCardsOnDiscs(PLAYER_1)[0] == Card.NOT_A_CARD);
        assert (gameState.getPlayerCardsOnDiscs(PLAYER_1)[1] == Card.FORUM);
        assert (gameState.getPlayerCardsOnDiscs(PLAYER_1)[2] == Card.NOT_A_CARD);
        assert (gameState.getPlayerCardsOnDiscs(PLAYER_1)[3] == Card.PRAETORIANUS);
        assert (gameState.getPlayerCardsOnDiscs(PLAYER_1)[4] == Card.NOT_A_CARD);
        assert (gameState.getPlayerCardsOnDiscs(PLAYER_1)[5] == Card.GRIMREAPER);
        assert (gameState.getPlayerCardsOnDiscs(PLAYER_1)[6] == Card.TELEPHONEBOX);

        // And we're done for now

        assert (gameState.getPlayerSestertii(PLAYER_1) == 993); // 5 Sest spent on TM activation


        // Here's where things get interesting. The five cards in limbo should all appear during the opponent's next turn...

        move.endTurn();

        assert (gameState.getPlayerCardsOnDiscs(PLAYER_1)[0] == Card.TRIBUNUSPLEBIS);
        assert (gameState.getPlayerCardsOnDiscs(PLAYER_1)[1] == Card.FORUM);
        assert (gameState.getPlayerCardsOnDiscs(PLAYER_1)[2] == Card.KAT);
        assert (gameState.getPlayerCardsOnDiscs(PLAYER_1)[3] == Card.PRAETORIANUS);
        assert (gameState.getPlayerCardsOnDiscs(PLAYER_1)[4] == Card.SCAENICUS);
        assert (gameState.getPlayerCardsOnDiscs(PLAYER_1)[5] == Card.GRIMREAPER);
        assert (gameState.getPlayerCardsOnDiscs(PLAYER_1)[6] == Card.TELEPHONEBOX);

        // Now wasn't that something

        // Nobody should loose any points for a bit

        move.endTurn();
        move.endTurn();
        assert (gameState.getPlayerVictoryPoints(PLAYER_1) == 17);
        assert (gameState.getPlayerVictoryPoints(PLAYER_2) == 1);
        move.endTurn();
        move.endTurn();
        assert (gameState.getPlayerVictoryPoints(PLAYER_1) == 17);
        assert (gameState.getPlayerVictoryPoints(PLAYER_2) == 1);
        move.endTurn();
        move.endTurn();
        assert (gameState.getPlayerVictoryPoints(PLAYER_1) == 17);
        assert (gameState.getPlayerVictoryPoints(PLAYER_2) == 1);
        move.endTurn();
        move.endTurn();
        assert (gameState.getPlayerVictoryPoints(PLAYER_1) == 17);
        assert (gameState.getPlayerVictoryPoints(PLAYER_2) == 1);
        move.endTurn();
        move.endTurn();
        assert (gameState.getPlayerVictoryPoints(PLAYER_1) == 17);
        assert (gameState.getPlayerVictoryPoints(PLAYER_2) == 1);
        move.endTurn();
        move.endTurn();
        assert (gameState.getPlayerVictoryPoints(PLAYER_1) == 17);
        assert (gameState.getPlayerVictoryPoints(PLAYER_2) == 1);


        //Oh whats this, player two is making a comeback
        gameState.setActionDice(new int[] {4,4,1});

        assert (gameState.getWhoseTurn() == PLAYER_2);

        MercatorActivator mercator = (MercatorActivator) move.chooseCardToActivate(Rules.DICE_DISC_4);
        mercator.chooseMercatorBuyNum(10);
        mercator.complete();

        // Wow wow, well that was a turn around
        assert (gameState.getPlayerVictoryPoints(PLAYER_1) == 7);
        assert (gameState.getPlayerVictoryPoints(PLAYER_2) == 11);

        // But wait, he's coming back for more!
        mercator = (MercatorActivator) move.chooseCardToActivate(Rules.DICE_DISC_4);
        mercator.chooseMercatorBuyNum(6);
        mercator.complete();

        // Wow wow, well that was a turn around
        assert (gameState.getPlayerVictoryPoints(PLAYER_1) == 1);
        assert (gameState.getPlayerVictoryPoints(PLAYER_2) == 17);

        TribunusPlebisActivator tripleb = (TribunusPlebisActivator) move.chooseCardToActivate(Rules.DICE_DISC_1);
        tripleb.complete();

        // Tribunis Plebis'd to death. What a troll
        assert (gameState.getPlayerVictoryPoints(PLAYER_1) == 0);
        assert (gameState.getPlayerVictoryPoints(PLAYER_2) == 18);

        // And that's all folks
        assert (gameState.isGameCompleted());
    }
}
