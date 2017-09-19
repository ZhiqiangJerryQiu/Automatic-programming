package tests.verifiedSpec2;

import framework.Rules;
import framework.Test;
import framework.cards.Card;
import framework.interfaces.GameState;
import framework.interfaces.MoveMaker;
import framework.interfaces.activators.AesculapinumActivator;
import framework.interfaces.activators.TelephoneBoxActivator;
import framework.interfaces.activators.VelitesActivator;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created with IntelliJ IDEA.
 * @Author : Chris FONG
 * Date: 19/05/12
 * Time: 10:56 PM
 * To change this template use File | Settings | File Templates.
 */
public class TelephoneBoxBreakingAesculapinumTest extends Test {

    private final int PLAYER_1 = 0;
    private final int PLAYER_2 = 1;

    @Override
    public String getShortDescription() {
        return "Testing Aesculapinum interacting with Telephone Box";
    }

    @Override
    public void run(GameState gameState, MoveMaker move) throws AssertionError, UnsupportedOperationException, IllegalArgumentException {

        Collection<Card> hand = new ArrayList<Card>();
        hand.add(Card.TURRIS);
        gameState.setDiscard(new ArrayList<Card>());
        gameState.setPlayerHand(PLAYER_1,hand);
        gameState.setPlayerHand(PLAYER_2,new ArrayList<Card>());

        gameState.setPlayerSestertii(PLAYER_1,100);
        gameState.setPlayerSestertii(PLAYER_2,100);
        gameState.setPlayerVictoryPoints(PLAYER_1, 15);
        gameState.setPlayerVictoryPoints(PLAYER_2,15);

        Card[][] playerFields = new Card[Rules.NUM_PLAYERS][Rules.NUM_DICE_DISCS];

        playerFields[PLAYER_1] = new Card[] {
                Card.SCAENICUS,
                Card.LEGIONARIUS,
                Card.MERCATOR,
                Card.TELEPHONEBOX,
                Card.SCAENICUS,
                Card.SCAENICUS,
                Card.SCAENICUS
        };

        playerFields[PLAYER_2] = new Card[] {
                Card.SCAENICUS,
                Card.SCAENICUS,
                Card.VELITES,
                Card.AESCULAPINUM,
                Card.SCAENICUS,
                Card.SCAENICUS,
                Card.SCAENICUS
        };


        gameState.setPlayerCardsOnDiscs(PLAYER_1,playerFields[PLAYER_1]);
        gameState.setPlayerCardsOnDiscs(PLAYER_2,playerFields[PLAYER_2]);

        /*
        * Turn 1
        * Pass - Do Nothing
        */
        gameState.setWhoseTurn(PLAYER_1);
        gameState.setActionDice(new int[] {3,3,5});

        move.endTurn();

        /*
         * Turn 2
         * Velites kills Legionarius (Dice roll = Legionarius's defence + 1)
         * Velites just kills Mercator (Dice roll = Mercator's defence)
         * Aesculapinum retrieves Legionarius to Player 2's hand
         */

        gameState.setActionDice(new int[] {3,3,4});

        assert(gameState.getPlayerCardsOnDiscs(PLAYER_1)[1] == Card.LEGIONARIUS);
        VelitesActivator velites = (VelitesActivator) move.chooseCardToActivate(Rules.DICE_DISC_3);
        velites.chooseDiceDisc(Rules.DICE_DISC_2);
        velites.giveAttackDieRoll(6);
        velites.complete();
        assert(gameState.getPlayerCardsOnDiscs(PLAYER_1)[1] == Card.NOT_A_CARD);

        assert(gameState.getPlayerCardsOnDiscs(PLAYER_1)[2] == Card.MERCATOR);
        velites = (VelitesActivator) move.chooseCardToActivate(Rules.DICE_DISC_3);
        velites.chooseDiceDisc(Rules.DICE_DISC_3);
        velites.giveAttackDieRoll(2);
        velites.complete();
        assert(gameState.getPlayerCardsOnDiscs(PLAYER_1)[2] == Card.NOT_A_CARD);

        assert(gameState.getDiscard().contains(Card.LEGIONARIUS));
        assert(gameState.getDiscard().contains(Card.MERCATOR));
        AesculapinumActivator aesculapinum = (AesculapinumActivator) move.chooseCardToActivate(Rules.DICE_DISC_4);
        aesculapinum.chooseCardFromPile(getIndexFromPile(Card.LEGIONARIUS,gameState.getDiscard()));
        aesculapinum.complete();
        assert(!gameState.getDiscard().contains(Card.LEGIONARIUS));
        assert(gameState.getPlayerHand(PLAYER_2).contains(Card.LEGIONARIUS));

        move.endTurn();

        /*
        * Turn 3
        * Place a Turris down
        * Send a Turris back to the 1st turn
        * Velites no longer kills Mercator, shifting the index of Legionarius in the Discard Pile
        */

        gameState.setActionDice(new int[] {4,2,2});
        move.placeCard(Card.TURRIS,Rules.BRIBE_DISC);
        assert(gameState.getPlayerCardsOnDiscs(PLAYER_1)[6] == Card.TURRIS);
        TelephoneBoxActivator telephoneBox = (TelephoneBoxActivator) move.chooseCardToActivate(Rules.DICE_DISC_4);
        telephoneBox.chooseDiceDisc(Rules.BRIBE_DISC);
        telephoneBox.shouldMoveForwardInTime(false);
        telephoneBox.setSecondDiceUsed(2);
        telephoneBox.complete();
        assert(!gameState.getDiscard().contains(Card.MERCATOR));
        assert(gameState.getPlayerHand(PLAYER_2).contains(Card.LEGIONARIUS));

    }
}
