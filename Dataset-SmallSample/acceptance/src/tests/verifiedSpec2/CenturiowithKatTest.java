package tests.verifiedSpec2;

import framework.Rules;
import framework.Test;
import framework.cards.Card;
import framework.interfaces.GameState;
import framework.interfaces.MoveMaker;
import framework.interfaces.activators.CenturioActivator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Reynard
 * Date: 18/05/12
 * Time: 11:05 PM
 * To change this template use File | Settings | File Templates.
 */
public class CenturiowithKatTest extends Test{

    private final int PLAYER_1 = 0;
    private final int PLAYER_2 = 1;

    private List<Card> deck;
    private List<Card> discard;

    private Card[][] playerFields;
    private List<Card>[] playerHands;

    private int[] playerVPs;
    private int[] playerSestertiis;

    private GameState gameState;

    public String getShortDescription() {
        return "Centurio killing Kat with Grim Reaper at disc";
    }

    public void run(GameState gameState, MoveMaker move) throws AssertionError,
            UnsupportedOperationException, IllegalArgumentException {
      if (1==1) {
         throw new IllegalArgumentException();
      }        

        this.gameState = gameState;
        /*
           Starting Process:
               * Initialises Deck to a pre-determined deck
               * Initialises Field to empty fields
               * Initialises Hands to empty hands
               * Initialises Players' VPs to 10
               * Initialises Players' Sestertiis to 0
        */

        initialiseDeck();
        initialiseDiscard();
        gameState.setDeck(deck);
        gameState.setDiscard(discard);

        assertDeck();

        initialisePlayerFields();
        transferFieldsToState();

        assertFields();

        initialisePlayerHands();
        transferHandsToState();

        assertHands();

        initialisePlayerVPs();
        transferVPsToState();

        initialisePlayerSestertiis();
        transferSestertiiToState();

        assertVPs();
        assertSestertiis();

        /*
            Set Hands:
                * Gives each Player their first 5 cards
                * Sets those cards in predetermined positions
                * Initialises to Player 1's turn

        */

        for (int i = 0; i < Rules.NUM_PLAYERS ; i++) {
            for (int j = 0; j < 5; j++) {
                playerHands[i].add(deck.remove(0));
            }
        }

        /*
           Hands:
               Player 1        Player 2:
                   -				-
                   -				-
                   -				-
                   -				-
                   -				-


           Place onto Fields:
                             1				2			3			4			5				6			7
               Player 1:<NOT_A_CARD>,<NOT_A_CARD>,<NOT_A_CARD>,<NOT_A_CARD>,<NOT_A_CARD>,<NOT_A_CARD>,<NOT_A_CARD>
               Player 2:<NOT_A_CARD>,<NOT_A_CARD>,<NOT_A_CARD>,<NOT_A_CARD>,<NOT_A_CARD>,<NOT_A_CARD>,<NOT_A_CARD>
        */

        playerHands[PLAYER_1] = new ArrayList<Card>();
        playerHands[PLAYER_2] = new ArrayList<Card>();

        playerFields[PLAYER_1] =
                new Card[] {
                        Card.CENTURIO,
                        Card.NOT_A_CARD,
                        Card.FORUM,
                        Card.NOT_A_CARD,
                        Card.FORUM,
                        Card.TURRIS,
                        Card.TELEPHONEBOX
                };

        playerFields[PLAYER_2] =
                new Card[] {
                        Card.KAT,
                        Card.NOT_A_CARD,
                        Card.NOT_A_CARD,
                        Card.SENATOR,
                        Card.KAT,
                        Card.MACHINA,
                        Card.GRIMREAPER
                };

        gameState.setDeck(deck);

        transferHandsToState();
        transferFieldsToState();

        assertHands();
        assertFields();
        gameState.setWhoseTurn(PLAYER_1);
        gameState.setActionDice(new int [] {1, 1, 1});
        assert (gameState.getActionDice().length == 3);
        CenturioActivator ca = (CenturioActivator) move.chooseCardToActivate(1);
        ca.chooseCenturioAddActionDie(false);
        ca.giveAttackDieRoll(2);        //8
        ca.complete();
        assert (gameState.getPlayerCardsOnDiscs(PLAYER_2)[0].toString().equals(Card.KAT.toString()));
        assert (gameState.getActionDice().length == 2);
        ca = (CenturioActivator) move.chooseCardToActivate(1);
        ca.chooseCenturioAddActionDie(false);
        ca.giveAttackDieRoll(2);        //7
        ca.complete();
        assert (gameState.getPlayerCardsOnDiscs(PLAYER_2)[0].toString().equals(Card.KAT.toString()));
        assert (gameState.getActionDice().length == 1);
        ca = (CenturioActivator) move.chooseCardToActivate(1);
        ca.chooseCenturioAddActionDie(false);
        ca.giveAttackDieRoll(2);        //6
        ca.complete();
        assert (gameState.getPlayerCardsOnDiscs(PLAYER_2)[0].toString().equals(Card.KAT.toString()));
        assert (gameState.getActionDice().length == 0);
        gameState.setActionDice(new int [] {1, 1, 1});
        assert (gameState.getPlayerCardsOnDiscs(PLAYER_2)[0].toString().equals(Card.KAT.toString()));
        ca = (CenturioActivator) move.chooseCardToActivate(1);
        ca.chooseCenturioAddActionDie(false);
        ca.giveAttackDieRoll(2);        //5
        ca.complete();
        assert (gameState.getActionDice().length == 2);
        assert (gameState.getPlayerCardsOnDiscs(PLAYER_2)[0].toString().equals(Card.KAT.toString()));
        ca = (CenturioActivator) move.chooseCardToActivate(1);
        ca.chooseCenturioAddActionDie(false);
        ca.giveAttackDieRoll(2);        //4
        ca.complete();
        assert (gameState.getActionDice().length == 1);
        assert (gameState.getPlayerCardsOnDiscs(PLAYER_2)[0].toString().equals(Card.KAT.toString()));
        gameState.setActionDice(new int [] {1, 1, 1});
        assert (gameState.getPlayerCardsOnDiscs(PLAYER_2)[0].toString().equals(Card.KAT.toString()));
        ca = (CenturioActivator) move.chooseCardToActivate(1);
        ca.chooseCenturioAddActionDie(false);
        ca.giveAttackDieRoll(2);        //3
        ca.complete();
        assert (gameState.getActionDice().length == 2);
        assert (gameState.getPlayerCardsOnDiscs(PLAYER_2)[0].toString().equals(Card.KAT.toString()));
        ca = (CenturioActivator) move.chooseCardToActivate(1);
        ca.chooseCenturioAddActionDie(false);
        ca.giveAttackDieRoll(2);        //2
        ca.complete();
        assert (gameState.getActionDice().length == 1);
        assert (gameState.getPlayerCardsOnDiscs(PLAYER_2)[0].toString().equals(Card.KAT.toString()));
        ca = (CenturioActivator) move.chooseCardToActivate(1);
        ca.chooseCenturioAddActionDie(false);
        ca.giveAttackDieRoll(2);        //1
        ca.complete();
        assert (gameState.getActionDice().length == 0);

        gameState.setActionDice(new int [] {1, 1, 1});
        assert (gameState.getPlayerCardsOnDiscs(PLAYER_2)[0].toString().equals(Card.KAT.toString()));
        ca = (CenturioActivator) move.chooseCardToActivate(1);
        ca.chooseCenturioAddActionDie(false);
        ca.giveAttackDieRoll(2);        // 0
        ca.complete();
        assert (gameState.getActionDice().length == 2);
        assert (gameState.getPlayerCardsOnDiscs(PLAYER_2)[0].toString().equals(Card.NOT_A_CARD.toString()));    
        assert (gameState.getDiscard().size() == 0);
        gameState.setWhoseTurn(PLAYER_2);
        assert(gameState.getPlayerHand(PLAYER_2).contains(Card.KAT));


    }

    private void initialiseDeck () {

        deck = new ArrayList<Card>();
        deck.add(Card.GLADIATOR);
        deck.add(Card.TELEPHONEBOX);
        deck.add(Card.FORUM);
        deck.add(Card.FORUM);
        deck.add(Card.TURRIS);
        deck.add(Card.MACHINA);
        deck.add(Card.KAT);
        deck.add(Card.TEMPLUM);
        deck.add(Card.SENATOR);
        deck.add(Card.AESCULAPINUM); //#1
        deck.add(Card.CONSILIARIUS);
        deck.add(Card.LEGIONARIUS);
        deck.add(Card.LEGAT);
        deck.add(Card.VELITES);
        deck.add(Card.BASILICA);
        deck.add(Card.ONAGER);
        deck.add(Card.PRAETORIANUS);
        deck.add(Card.TRIBUNUSPLEBIS);
        deck.add(Card.ESSEDUM);
        deck.add(Card.SENATOR);
        deck.add(Card.FORUM);
        deck.add(Card.PRAETORIANUS);
        deck.add(Card.CENTURIO);
        deck.add(Card.ARCHITECTUS);
        deck.add(Card.HARUSPEX);
        deck.add(Card.NERO);
        deck.add(Card.LEGAT);
        deck.add(Card.KAT);
        deck.add(Card.CONSILIARIUS);
        deck.add(Card.BASILICA);
        deck.add(Card.CONSUL);
        deck.add(Card.MERCATUS);
        deck.add(Card.MERCATUS);
        deck.add(Card.FORUM);
        deck.add(Card.TURRIS);
        deck.add(Card.CENTURIO);
        deck.add(Card.SCAENICUS);
        deck.add(Card.MERCATOR);
        deck.add(Card.SCAENICUS);
        deck.add(Card.GRIMREAPER);
        deck.add(Card.SICARIUS);
        deck.add(Card.ONAGER);
        deck.add(Card.LEGIONARIUS);
        deck.add(Card.GLADIATOR);
        deck.add(Card.VELITES);
        deck.add(Card.AESCULAPINUM);
        deck.add(Card.FORUM);
        deck.add(Card.ESSEDUM);
        deck.add(Card.CONSUL);
        deck.add(Card.TEMPLUM);
        deck.add(Card.TRIBUNUSPLEBIS);
        deck.add(Card.FORUM);
        deck.add(Card.MACHINA);
        deck.add(Card.ARCHITECTUS);
        deck.add(Card.LEGIONARIUS);
        deck.add(Card.HARUSPEX);

    }

    private void initialiseDiscard () {

        this.discard = new ArrayList<Card>();

    }

    private void initialisePlayerFields () {

        playerFields = new Card[Rules.NUM_PLAYERS][Rules.NUM_DICE_DISCS];

        for (int i = 0; i < Rules.NUM_PLAYERS; i++) {
            for (int j = 0; j < Rules.NUM_DICE_DISCS; j++) {
                playerFields[i][j] = Card.NOT_A_CARD;
            }
        }

    }

    private void initialisePlayerHands() {

        playerHands = (ArrayList<Card>[]) new ArrayList[Rules.NUM_PLAYERS];

        for (int i = 0; i < Rules.NUM_PLAYERS; i++) {
            playerHands[i] = new ArrayList<Card>();
        }

    }

    private void initialisePlayerVPs() {

        playerVPs = new int[Rules.NUM_PLAYERS];

        for (int i = 0; i < Rules.NUM_PLAYERS; i++) {
            playerVPs[i] = 10;
        }

    }

    private void initialisePlayerSestertiis() {

        this.playerSestertiis = new int[Rules.NUM_PLAYERS];

        for (int i = 0; i < Rules.NUM_PLAYERS; i++) {
            this.playerSestertiis[i] = 0;
        }

    }

    private void transferSestertiiToState() {
        for (int i = 0; i < Rules.NUM_PLAYERS; i++) {
            this.gameState.setPlayerSestertii(i, playerSestertiis[i]);
        }
    }

    private void transferVPsToState() {
        for (int i = 0; i < Rules.NUM_PLAYERS; i++) {
            this.gameState.setPlayerVictoryPoints(i, playerVPs[i]);
        }
    }

    private void transferHandsToState() {
        for (int i = 0; i < Rules.NUM_PLAYERS; i++) {
            this.gameState.setPlayerHand(i, playerHands[i]);
        }
    }

    private void transferFieldsToState() {

        for (int i = 0; i < Rules.NUM_PLAYERS; i++) {
            this.gameState.setPlayerCardsOnDiscs(i,playerFields[i]);
        }

    }

    private void assertDeck() {
        gameState.getDeck().equals(deck);
    }

    private void assertDiscard() {
        gameState.getDiscard().equals(discard);
    }

    private void assertFields() {

        for (int i = 0; i < Rules.NUM_PLAYERS; i++) {
            assert(Arrays.equals(gameState.getPlayerCardsOnDiscs(i), playerFields[i]));
        }

    }

    private void assertHands() {

        for (int i = 0; i < Rules.NUM_PLAYERS; i++) {
            assert(gameState.getPlayerHand(i).containsAll(playerHands[i]));
        }

    }

    private void assertVPs() {

        int poolVPs = 36;

        for (int i = 0; i < Rules.NUM_PLAYERS; i++) {
            poolVPs -= playerVPs[i];
            assert(gameState.getPlayerVictoryPoints(i) == playerVPs[i]);
        }

        assert(gameState.getPoolVictoryPoints() == poolVPs);

    }

    private void assertSestertiis() {

        for (int i = 0; i < Rules.NUM_PLAYERS; i++) {
            assert(gameState.getPlayerSestertii(i) == playerSestertiis[i]);
        }

    }
}
