package tests.verifiedBuilding;

import framework.Rules;
import framework.Test;
import framework.cards.Card;
import framework.interfaces.GameState;
import framework.interfaces.MoveMaker;
import framework.interfaces.activators.LegionariusActivator;
import framework.interfaces.activators.PraetorianusActivator;
import framework.interfaces.activators.ScaenicusActivator;
import framework.interfaces.activators.TribunusPlebisActivator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @Author : Junjie CHEN
 * 
 * All dice values randomly generated from http://www.random.org/dice/
 * 
 */
public class PlaythroughCharacterTest extends Test {

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
        return "Playthrough of the Game";
    }

    public void run(GameState gameState, MoveMaker move) throws AssertionError,
            UnsupportedOperationException, IllegalArgumentException {

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
			    LEGIONARIUS		MERCATOR
				SENATOR			TRIBUNUSPLEBIS
			    PRAETORIANUS	CONSILIARIUS
				ARCHITECTUS		SCAENICUS
			    TRIBUNUSPLEBIS	HARUSPEX
				
	            
	        Place onto Fields:
							  1		    	2			3			    4	          5            6			7
				Player 1:<PRAETORIANUS>,<SENATOR>,<TRIBUNUSPLEBIS>,<LEGIONARIUS>,<NOT_A_CARD>,<ARCHITECTUS>,<NOT_A_CARD>
				Player 2:<MERCATOR>,<NOT_A_CARD>,<TRIBUNUSPLEBIS>,<CONSILIARIUS>,  <HARUSPEX>,<NOT_A_CARD>,<SCAENICUS>
         */
        
        playerHands[PLAYER_1] = new ArrayList<Card>();
        playerHands[PLAYER_2] = new ArrayList<Card>();
        		
        playerFields[PLAYER_1] =
                new Card[] {
		                Card.PRAETORIANUS,
		                Card.SENATOR,
		                Card.TRIBUNUSPLEBIS,
		                Card.LEGIONARIUS,
		                Card.NOT_A_CARD,
		                Card.ARCHITECTUS,
		                Card.NOT_A_CARD
		        };

        playerFields[PLAYER_2] =
                new Card[] {
		                Card.MERCATOR,
		                Card.NOT_A_CARD,
		                Card.TRIBUNUSPLEBIS,
		                Card.CONSILIARIUS,
		                Card.HARUSPEX,
		                Card.NOT_A_CARD,
		                Card.SCAENICUS
		        };

        gameState.setDeck(deck);

        transferHandsToState();
        transferFieldsToState();

        assertHands();
        assertFields();

        gameState.setWhoseTurn(PLAYER_1);
        gameState.setActionDice(new int[] {1,3,4});

        PraetorianusActivator cockBlock = (PraetorianusActivator) move.chooseCardToActivate(Rules.DICE_DISC_1);
        cockBlock.chooseDiceDisc(Rules.DICE_DISC_3);
        cockBlock.complete();

        discard.add(Card.CONSILIARIUS);
        playerFields[PLAYER_2][3] = Card.NOT_A_CARD;
        LegionariusActivator stabbyStabby = (LegionariusActivator) move.chooseCardToActivate(Rules.DICE_DISC_4);
        stabbyStabby.giveAttackDieRoll(6);
        stabbyStabby.complete();
        assertDiscard();
        assertFields();

        playerVPs[PLAYER_1] += 1;
        playerVPs[PLAYER_2] -= 1;
        TribunusPlebisActivator plebs = (TribunusPlebisActivator) move.chooseCardToActivate(Rules.DICE_DISC_3);
        plebs.complete();
        assertVPs();

        move.endTurn();

        /*
             Start of Round Summary

             Victory Points:
                 Player 1: 11
                 Player 2: 6
                 Pool:	   16

             Sestertii:
                 Player 1: 0
                 Player 2: 0

             Hands:
                Player 1:        Player 2:


             Fields:
			                1		    	2			3			    4	          5            6			7
				Player 1:<PRAETORIANUS>,<SENATOR>,<TRIBUNUSPLEBIS>,<LEGIONARIUS>,<NOT_A_CARD>,<ARCHITECTUS>,<NOT_A_CARD>
				Player 2:<MERCATOR>,<NOT_A_CARD>,<TRIBUNUSPLEBIS>,<NOT_A_CARD>,  <HARUSPEX>,  <NOT_A_CARD>, <SCAENICUS>
        */

        playerVPs[PLAYER_2] -= 3;
        assertVPs();

        gameState.setActionDice(new int[] {3,3,4});

        deck.remove(Card.GRIMREAPER);
        deck.remove(Card.LEGAT);
        deck.remove(Card.KAT);
        discard.add(Card.LEGAT);
        discard.add(Card.KAT);
        playerHands[PLAYER_2].add(Card.GRIMREAPER);
        move.activateCardsDisc(3,Card.GRIMREAPER);
        assertDeck();
        assertDiscard();
        assertHands();

        playerSestertiis[PLAYER_2] += 4;
        move.activateMoneyDisc(4);
        assertSestertiis();

        //THE DISC IS BLOCKED!
        plebs = (TribunusPlebisActivator) move.chooseCardToActivate(Rules.DICE_DISC_3);
        plebs.complete();
        assertVPs();

        //TRY MIMICING THE BLOCKED DISC - Thanks Matt Moss :)
        playerSestertiis[PLAYER_2] -= 3;
        playerVPs[PLAYER_2] += 1;
        playerVPs[PLAYER_1] -= 1;

        ScaenicusActivator ditto = (ScaenicusActivator) move.activateBribeDisc(3);
        plebs = (TribunusPlebisActivator) ditto.getScaenicusMimicTarget(Rules.DICE_DISC_3);
        plebs.complete();
        ditto.complete();

        assertSestertiis();
        assertVPs();

        move.endTurn();


        /*
             Start of Round Summary

             Victory Points:
                 Player 1: 8
                 Player 2: 7
                 Pool:	   16

             Sestertii:
                 Player 1: 0
                 Player 2: 1

             Hands:
                Player 1:        Player 2:


             Fields:
                            1		    	2			3			    4	          5            6			7
                Player 1:<PRAETORIANUS>,<SENATOR>,<TRIBUNUSPLEBIS>,<LEGIONARIUS>,<NOT_A_CARD>,<ARCHITECTUS>,<NOT_A_CARD>
                Player 2:<MERCATOR>,<NOT_A_CARD>,<TRIBUNUSPLEBIS>,<NOT_A_CARD>,  <HARUSPEX>,  <NOT_A_CARD>, <SCAENICUS>
        */

        playerVPs[PLAYER_1] -= 2;
        assertVPs();

    }

    private void initialiseDeck () {

        deck = new ArrayList<Card>();
        deck.add(Card.LEGIONARIUS);
        deck.add(Card.SENATOR);
        deck.add(Card.PRAETORIANUS);
        deck.add(Card.ARCHITECTUS);
        deck.add(Card.TRIBUNUSPLEBIS);
        deck.add(Card.MERCATOR);
        deck.add(Card.TRIBUNUSPLEBIS);
        deck.add(Card.CONSILIARIUS);
        deck.add(Card.SCAENICUS);
        deck.add(Card.HARUSPEX);         //#1
        deck.add(Card.GRIMREAPER);
        deck.add(Card.LEGAT);
        deck.add(Card.KAT);
        deck.add(Card.SENATOR);
        deck.add(Card.VELITES);
        deck.add(Card.CONSUL);
        deck.add(Card.NERO);
        deck.add(Card.LEGAT);
        deck.add(Card.ESSEDUM);
        deck.add(Card.LEGIONARIUS);
        deck.add(Card.CENTURIO);
        deck.add(Card.SCAENICUS);
        deck.add(Card.GLADIATOR);
        deck.add(Card.CENTURIO);
        deck.add(Card.ARCHITECTUS);
        deck.add(Card.VELITES);
        deck.add(Card.KAT);
        deck.add(Card.CONSUL);
        deck.add(Card.HARUSPEX);
        deck.add(Card.ESSEDUM);
        deck.add(Card.CONSILIARIUS);
        deck.add(Card.GLADIATOR);
        deck.add(Card.SICARIUS);
        deck.add(Card.PRAETORIANUS);
        deck.add(Card.LEGIONARIUS);


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
            assert(Arrays.equals(gameState.getPlayerCardsOnDiscs(i),playerFields[i]));
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
