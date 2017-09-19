package tests.verifiedBuilding;

import framework.Rules;
import framework.Test;
import framework.cards.Card;
import framework.interfaces.GameState;
import framework.interfaces.MoveMaker;
import framework.interfaces.activators.SicariusActivator;
import framework.interfaces.activators.TribunusPlebisActivator;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/*
**
** Authors: Aroma Therapy
** Edward Lee, Matthew Dang, Justine Chau
**
 */

public class BribeDiscTest extends Test {

    private GameState gameState;

    private List<Card> deck;
    private List<Card> discard;

    private static final int PLAYER_1 = 0;
    private static final int PLAYER_2 = 1;

    @Override
    public String getShortDescription() {
        return "Testing the bribe disk";
    }

    @Override
    public void run(GameState gameState, MoveMaker move) throws AssertionError, UnsupportedOperationException, IllegalArgumentException {

         this.gameState = gameState;

         initialiseDeck();
         intialiseDiscard();
         gameState.setDeck(this.deck);
         gameState.setDiscard(this.discard);

         initialiseVPs();
         initialiseSestertii();

         Card[] playerOneCards = new Card[] {
                 Card.FORUM,
                 Card.SCAENICUS,
                 Card.NOT_A_CARD,
                 Card.MERCATUS,
                 Card.LEGAT,
                 Card.PRAETORIANUS,
                 Card.TRIBUNUSPLEBIS
         };
         gameState.setPlayerCardsOnDiscs(PLAYER_1, playerOneCards);

         Card[] playerTwoCards = new Card[] {
                 Card.NOT_A_CARD,
                 Card.CENTURIO,
                 Card.TRIBUNUSPLEBIS,
                 Card.MACHINA,
                 Card.CONSUL,
                 Card.LEGIONARIUS,
                 Card.NOT_A_CARD
         };
        gameState.setPlayerCardsOnDiscs(PLAYER_2, playerTwoCards);

        List<Card> hand;
        hand = new LinkedList<Card>();
        hand.add(Card.SICARIUS);
        gameState.setPlayerHand(PLAYER_1, hand);

        gameState.setWhoseTurn(PLAYER_1);
        gameState.setActionDice(new int[]{2,3,4});

        TribunusPlebisActivator tba = (TribunusPlebisActivator) move.activateBribeDisc(2);
        tba.complete();

        assert(gameState.getPlayerVictoryPoints(PLAYER_1) == 11);
        assert(gameState.getPlayerSestertii(PLAYER_1) == 98);
        assert(gameState.getPlayerVictoryPoints(PLAYER_2) == 9);
        assert(gameState.getPlayerSestertii(PLAYER_2) == 100);

        Card[] field;
        field = gameState.getPlayerCardsOnDiscs(PLAYER_1);
        assert(field[6] == Card.TRIBUNUSPLEBIS);
        assert(gameState.getActionDice().length == 2);

        move.placeCard(Card.SICARIUS, Rules.BRIBE_DISC);

        assert(gameState.getPlayerSestertii(PLAYER_1) == 89);
        SicariusActivator sicarius = (SicariusActivator) move.activateBribeDisc(3);
        sicarius.chooseDiceDisc(Rules.DICE_DISC_2);
        sicarius.complete();

        assert(gameState.getPlayerVictoryPoints(PLAYER_1) == 11);
        assert(gameState.getPlayerSestertii(PLAYER_1) == 86);
        assert(gameState.getPlayerVictoryPoints(PLAYER_2) == 9);
        assert(gameState.getPlayerSestertii(PLAYER_2) == 100);

        field = gameState.getPlayerCardsOnDiscs(PLAYER_1);

        assert(field[6] == Card.NOT_A_CARD);
        assert(gameState.getActionDice().length == 1);

        field = gameState.getPlayerCardsOnDiscs(PLAYER_2);
        assert(field[1] == Card.NOT_A_CARD);

    }

    private void initialiseSestertii() {
        this.gameState.setPlayerSestertii(PLAYER_1, 100);
        this.gameState.setPlayerSestertii(PLAYER_2, 100);
    }

    private void initialiseVPs() {    //both now have 10 VPs
        this.gameState.setPlayerVictoryPoints(PLAYER_1, 10);
        this.gameState.setPlayerVictoryPoints(PLAYER_2, 10);
    }

    private void intialiseDiscard() {
        this.discard = new ArrayList<Card>();
    }

    private void initialiseDeck() {
            this.deck = new ArrayList<Card>();
            deck.add(Card.FORUM);
            deck.add(Card.MERCATUS);
            deck.add(Card.LEGAT);
            deck.add(Card.PRAETORIANUS);
            deck.add(Card.SCAENICUS);
            deck.add(Card.CENTURIO);
            deck.add(Card.TRIBUNUSPLEBIS);
            deck.add(Card.MACHINA);
            deck.add(Card.CONSUL);
            deck.add(Card.LEGIONARIUS);
            deck.add(Card.NERO);
            deck.add(Card.ARCHITECTUS);
            deck.add(Card.LEGIONARIUS);
            deck.add(Card.HARUSPEX);
            deck.add(Card.TURRIS);
            deck.add(Card.SCAENICUS);
            deck.add(Card.VELITES);
            deck.add(Card.SENATOR);
            deck.add(Card.ARCHITECTUS);
            deck.add(Card.FORUM);
            deck.add(Card.MACHINA);
            deck.add(Card.AESCULAPINUM);
            deck.add(Card.CONSUL);
            deck.add(Card.GLADIATOR);
            deck.add(Card.CONSILIARIUS);
            deck.add(Card.ONAGER);
            deck.add(Card.TURRIS);
            deck.add(Card.BASILICA);
            deck.add(Card.MERCATOR);
            deck.add(Card.AESCULAPINUM);
            deck.add(Card.BASILICA);
            deck.add(Card.LEGIONARIUS);
            deck.add(Card.FORUM);
            deck.add(Card.TRIBUNUSPLEBIS);
            deck.add(Card.FORUM);
            deck.add(Card.MERCATUS);
            deck.add(Card.CONSILIARIUS);
            deck.add(Card.FORUM);
            deck.add(Card.SICARIUS);
            deck.add(Card.CENTURIO);
            deck.add(Card.GLADIATOR);
            deck.add(Card.TEMPLUM);
            deck.add(Card.VELITES);
            deck.add(Card.LEGAT);
            deck.add(Card.ESSEDUM);
            deck.add(Card.ONAGER);
            deck.add(Card.ESSEDUM);
            deck.add(Card.HARUSPEX);
            deck.add(Card.FORUM);
            deck.add(Card.TEMPLUM);
            deck.add(Card.PRAETORIANUS);
            deck.add(Card.SENATOR);
    }


}
