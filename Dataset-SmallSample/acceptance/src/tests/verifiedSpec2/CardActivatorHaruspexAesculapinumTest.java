package tests.verifiedSpec2;

import framework.Rules;
import framework.Test;
import framework.cards.Card;
import framework.interfaces.GameState;
import framework.interfaces.MoveMaker;
import framework.interfaces.activators.AesculapinumActivator;
import framework.interfaces.activators.CenturioActivator;
import framework.interfaces.activators.HaruspexActivator;

import java.util.*;

/**
 * @author : Anne
 * @author : Wen Di
 * Test Haruspex and Aesculapinum
 * Test format and fancy methods sniped from ChrisFong(TM) ^_^
 */

public class CardActivatorHaruspexAesculapinumTest extends Test {

    private final int PLAYER_1 = 0;
    private final int PLAYER_2 = 1;

    private List<Card> deck;
    private LinkedList<Card> discard;

    private Card[][] playerFields;
    private List<Card>[] playerHands;

    private int[] playerVPs;
    private int[] playerSestertiis;

    private GameState gameState;

    public String getShortDescription() {
        return "Testing the basic functionality of Haruspex";
    }

    public void run(GameState gameState, MoveMaker move) throws AssertionError,
            UnsupportedOperationException, IllegalArgumentException {

        this.gameState = gameState;
        /*
         * Initialisation
         */
        initialisePlayerVPs();
        transferVPsToState();
        assertVPs();
        
        initialisePlayerSestertiis();
        playerSestertiis[PLAYER_1] = 15;
        playerSestertiis[PLAYER_2] = 15;       
        transferSestertiiToState();
        assertSestertiis();
        
        initialiseDeck();
        gameState.setDeck(deck);
        assertDeck();
        
        initialiseDiscard();
        gameState.setDiscard(discard);
        assertDiscard();

        initialisePlayerFields();
        transferFieldsToState();
        assertFields();

        initialisePlayerHands();
        playerHands[PLAYER_1].add(Card.HARUSPEX);
        playerHands[PLAYER_1].add(Card.HARUSPEX);
        playerHands[PLAYER_1].add(Card.ONAGER);
        playerHands[PLAYER_1].add(Card.PRAETORIANUS);
        playerHands[PLAYER_1].add(Card.ESSEDUM);
        
        playerHands[PLAYER_2].add(Card.SCAENICUS);
        playerHands[PLAYER_2].add(Card.ESSEDUM);
        playerHands[PLAYER_2].add(Card.CENTURIO);
        playerHands[PLAYER_2].add(Card.AESCULAPINUM);
        playerHands[PLAYER_2].add(Card.MACHINA);
        
        transferHandsToState();
        assertHands();

        gameState.setWhoseTurn(PLAYER_1);
        gameState.setActionDice(new int[] {1,2,1});
        
        //Place a Haruspex
        move.placeCard(Card.HARUSPEX, Rules.DICE_DISC_1);
        
        playerHands[PLAYER_1].remove(Card.HARUSPEX);
        assertHands();
        playerFields[PLAYER_1][Rules.DICE_DISC_1-1] = Card.HARUSPEX;
        assertFields();
        playerSestertiis[PLAYER_1] -= 4;
        assertSestertiis();
        assert (gameState.getActionDice().length == 3);
        
        //Activate the Haruspex
        HaruspexActivator haha = (HaruspexActivator) move.chooseCardToActivate(Rules.DICE_DISC_1);
        haha.chooseCardFromPile(getIndexFromPile(Card.TRIBUNUSPLEBIS, gameState.getDeck()));
        haha.complete();
        
        playerHands[PLAYER_1].add(Card.TRIBUNUSPLEBIS);
        assertHands();
        deck.remove(Card.TRIBUNUSPLEBIS);
        assertDeck();
        assertFields();
        assertSestertiis();
        assert (gameState.getActionDice().length == 2);
        
        //Place a Haruspex on bribe disc
        move.placeCard(Card.HARUSPEX, Rules.BRIBE_DISC);
        
        playerHands[PLAYER_1].remove(Card.HARUSPEX);
        assertHands();
        playerFields[PLAYER_1][Rules.BRIBE_DISC-1] = Card.HARUSPEX;
        assertFields();
        playerSestertiis[PLAYER_1] -= 4;
        assertSestertiis();
        assert (gameState.getActionDice().length == 2);
        
        //Activate the Haruspex
        haha = (HaruspexActivator) move.activateBribeDisc(2);
        haha.chooseCardFromPile(getIndexFromPile(Card.FORUM, gameState.getDeck()));
        haha.complete();
        
        playerHands[PLAYER_1].add(Card.FORUM);
        assertHands();
        deck.remove(Card.FORUM);
        assertDeck();
        assertFields();
        playerSestertiis[PLAYER_1] -= 2;
        assertSestertiis();
        assert (gameState.getActionDice().length == 1);     

        assertVPs();
        assert (!gameState.isGameCompleted());
        move.endTurn();
        //Next player's turn
        gameState.setActionDice(new int[] {4,5,1});
        
        playerVPs[PLAYER_2] -= 7;
        assertVPs();
        

        //Place a Centurio
        move.placeCard(Card.CENTURIO, Rules.DICE_DISC_1);
        
        playerHands[PLAYER_2].remove(Card.CENTURIO);
        assertHands();
        playerFields[PLAYER_2][Rules.DICE_DISC_1-1] = Card.CENTURIO;
        assertFields();
        playerSestertiis[PLAYER_2] -= 9;
        assertSestertiis();
        assert (gameState.getActionDice().length == 3);   
        
        //Place an Aesculapinum
        move.placeCard(Card.AESCULAPINUM, Rules.DICE_DISC_5);
        
        playerHands[PLAYER_2].remove(Card.AESCULAPINUM);
        assertHands();
        playerFields[PLAYER_2][Rules.DICE_DISC_5-1] = Card.AESCULAPINUM;
        assertFields();
        playerSestertiis[PLAYER_2] -= 5;
        assertSestertiis();
        assert (gameState.getActionDice().length == 3);    
        
        //Activate the Centurio
        CenturioActivator sparta = (CenturioActivator) move.chooseCardToActivate(Rules.DICE_DISC_1);
        sparta.giveAttackDieRoll(1);
        sparta.chooseCenturioAddActionDie(true);
        sparta.chooseActionDice(4);
        sparta.complete();
        
        assertHands();
        playerFields[PLAYER_1][Rules.DICE_DISC_1-1] = Card.NOT_A_CARD;
        assertFields();
        assertSestertiis();
        discard.add(Card.HARUSPEX);
        assertDiscard();
        assert (gameState.getActionDice().length == 1);         
        
        //Activate the Aesculapinum
        AesculapinumActivator argh = (AesculapinumActivator) move.chooseCardToActivate(Rules.DICE_DISC_5);
        argh.chooseCardFromPile(getIndexFromPile(Card.HARUSPEX, gameState.getDiscard()));
        argh.complete();

        playerHands[PLAYER_2].add(Card.HARUSPEX);
        assertHands();
        assertFields();
        assertSestertiis();
        discard.remove(Card.HARUSPEX);
        assertDiscard();
        assert (gameState.getActionDice().length == 0);      
        
        assert (!gameState.isGameCompleted());
        
        move.endTurn();
        //Next player's turn
        playerVPs[PLAYER_1] -= 6;
        assertVPs();
        assert (!gameState.isGameCompleted());
        
        move.endTurn();
        //Next player's turn
        playerVPs[PLAYER_2] -= 5;
        assert (gameState.getPlayerVictoryPoints(PLAYER_2) <= 0);
        assert (gameState.isGameCompleted());
    }
    
	
	/*
	 * printFields out everything on both player's fields 
	 * can use it for debugging
	 */
	private static String padRight(String s, int width) {
	     return String.format("%1$-" + width + "s", s);  
	}

	private static String padLeft(String s, int width) {
	    return String.format("%1$#" + width + "s", s);  
	}

	private void printFields() {
		System.out.println(padLeft("Yours | Expected",30));
		for (int i = 0; i < Rules.NUM_PLAYERS; i++) {
			System.out.println(padLeft("---Player " + (i+1) + "---",27));
			for (int j = 0; j < Rules.NUM_DICE_DISCS; j++) {
				Card yours = gameState.getPlayerCardsOnDiscs(i)[j];
				Card test = playerFields[i][j];
				System.out.print (padRight (yours.toString(), 20));
				if (yours != test) {
					System.out.print("*");
				} else {
					System.out.print ("|");					
				}
				System.out.print (padLeft (test.toString(), 20));
				System.out.println();
			}
		}

	}

    /*
     * Below are ChrisFong(TM) methods
     */
    private void initialiseDeck () {
        deck = new ArrayList<Card>();
        deck.add(Card.SENATOR);
        deck.add(Card.AESCULAPINUM);
        deck.add(Card.TURRIS);
        deck.add(Card.FORUM);
        deck.add(Card.MERCATOR);
        deck.add(Card.LEGIONARIUS);
        deck.add(Card.GLADIATOR);
        deck.add(Card.GRIMREAPER);
        deck.add(Card.MERCATUS);
        deck.add(Card.SCAENICUS);
        deck.add(Card.MACHINA);
        deck.add(Card.NERO);
        deck.add(Card.KAT);
        deck.add(Card.CENTURIO);
        deck.add(Card.ESSEDUM);
        deck.add(Card.ARCHITECTUS);
        deck.add(Card.ONAGER);
        deck.add(Card.SICARIUS);
        deck.add(Card.BASILICA);
        deck.add(Card.CONSUL);
        deck.add(Card.LEGIONARIUS);
        deck.add(Card.ARCHITECTUS);
        deck.add(Card.HARUSPEX);
        deck.add(Card.VELITES);
        deck.add(Card.PRAETORIANUS);
        deck.add(Card.LEGIONARIUS);
        deck.add(Card.FORUM);
        deck.add(Card.LEGAT);
        deck.add(Card.CONSUL);
        deck.add(Card.ESSEDUM);
        deck.add(Card.CENTURIO);
        deck.add(Card.FORUM);
        deck.add(Card.CONSILIARIUS);
        deck.add(Card.CONSILIARIUS);
        deck.add(Card.TEMPLUM);
        deck.add(Card.PRAETORIANUS);
        deck.add(Card.TEMPLUM);
        deck.add(Card.GLADIATOR);
        deck.add(Card.KAT);
        deck.add(Card.TELEPHONEBOX);
        deck.add(Card.FORUM);
        deck.add(Card.MERCATUS);
        deck.add(Card.FORUM);
        deck.add(Card.TRIBUNUSPLEBIS);
        deck.add(Card.VELITES);
        deck.add(Card.SCAENICUS);
        deck.add(Card.BASILICA);
        deck.add(Card.ONAGER);
        deck.add(Card.SENATOR);
        deck.add(Card.AESCULAPINUM);
        deck.add(Card.TURRIS);
        deck.add(Card.MACHINA);
        deck.add(Card.LEGAT);
        deck.add(Card.HARUSPEX);
        deck.add(Card.TRIBUNUSPLEBIS);
        deck.add(Card.FORUM);
    }

    private void initialiseDiscard () {
        this.discard = new LinkedList<Card>();
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
            assert(playerHands[i].containsAll(gameState.getPlayerHand(i)));
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
            System.out.println(playerSestertiis[i] + " compared to " + gameState.getPlayerSestertii(i));
            assert(gameState.getPlayerSestertii(i) == playerSestertiis[i]);
        }
    }
}
