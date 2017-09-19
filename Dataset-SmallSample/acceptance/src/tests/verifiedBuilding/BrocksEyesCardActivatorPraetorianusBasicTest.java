package tests.verifiedBuilding;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import framework.Rules;
import framework.Test;
import framework.cards.Card;
import framework.interfaces.GameState;
import framework.interfaces.MoveMaker;
import framework.interfaces.activators.*;

/**
 * <p>Further tests for Praetorianus functionality</p>
 * <ul><li> Tests that Praetorianus allows card laying but not activation </li>
 * <li> Tests that a Praetorianus may block another Praetorianus's activation </li>
 * <li> Extra printFields() method left in here for your debugging leisure </li></ul> 
 * <p>Methods originally created by Chris Fong TM in PlaythroughChrisFongTest.java</p>
 * @author Wen Di
 * @author Anne
 */

public class BrocksEyesCardActivatorPraetorianusBasicTest extends Test {
	
	private final int DEAD_SENATOR_DICE = 3;
	private final int DEAD_PRAETORIANUS_DICE = 4;
	
    private final int PLAYER_1 = 0;
    private final int PLAYER_2 = 1;

    private List<Card> discard;

    private Card[][] playerFields;
    private List<Card>[] playerHands;

    private int[] playerVPs;
    private int[] playerSestertiis;

    private GameState gameState;

	public String getShortDescription() {
		return "Testing the basic functionality of Praetorianus";
	}

	public void run(GameState gameState, MoveMaker move) throws AssertionError,
	UnsupportedOperationException, IllegalArgumentException {
		this.gameState = gameState;
		
		/** 
		 * Initialise discard to an empty state
		 * Initialise playerHands to an empty state
		 * Initialise player VPs to 17 (for testing purposes)
		 * Initialise player Gold to 0
		 */
				
		initialiseDiscard();
		gameState.setDiscard(discard);

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

		assertSestertiis();
		
		/** 
		 * Set player 1 Hand to have one Velites and one Praetorianus
		 * Set player 2 Hand to have one Senator and two Praetorianus
		 * Set playerFields where player 1 has an Senator  
		 * Set playerFields where player 2 has a Turris and 1 Velites
		 * Set playerFields where opponent has 
		 */
		
        playerHands[PLAYER_1] = new ArrayList<Card>();
        playerHands[PLAYER_2] = new ArrayList<Card>();
        
		playerHands[PLAYER_1].add(Card.VELITES);
		playerHands[PLAYER_1].add(Card.PRAETORIANUS);
		
		playerHands[PLAYER_2].add(Card.SENATOR);
		playerHands[PLAYER_2].add(Card.PRAETORIANUS);
		playerHands[PLAYER_2].add(Card.PRAETORIANUS);
		
		playerFields[PLAYER_1] =
				new Card[] {
				Card.NOT_A_CARD,
				Card.VELITES,
				Card.TURRIS,
				Card.NOT_A_CARD,
				Card.NOT_A_CARD,
				Card.NOT_A_CARD,
				Card.NOT_A_CARD
		};

		playerFields[PLAYER_2] =
				new Card[] {
				Card.NOT_A_CARD,
				Card.SENATOR,
				Card.NOT_A_CARD,
				Card.NOT_A_CARD,
				Card.NOT_A_CARD,
				Card.NOT_A_CARD,
				Card.NOT_A_CARD
		};
		
		transferInitialFieldsToState();
		transferInitialHandsToState();
		
		
		assertHands();
		assertFields();
		assertSestertiis();
		assertVPs();
		
		/** 
		 * Set player 1 turn
		 * Set action die [2, 2, 6]
		 */
		
		gameState.setWhoseTurn(PLAYER_1);
		gameState.setActionDice(new int[] {2, 6, 6});
		
		/**
		 * Use action die 2 to activate Velites and attack opponent's Senator
		 * with attack roll of 3. 
		 * Use actiondie 6 to get gold twice
		 * And end turn.  
		 * Should work.
		 */
			
		VelitesActivator velites = (VelitesActivator)move.chooseCardToActivate(2);
		velites.giveAttackDieRoll(DEAD_SENATOR_DICE);
		velites.chooseDiceDisc(2);
		velites.complete();
		
		playerFields[PLAYER_2] =
				new Card[] {
				Card.NOT_A_CARD,
				Card.NOT_A_CARD,
				Card.NOT_A_CARD,
				Card.NOT_A_CARD,
				Card.NOT_A_CARD,
				Card.NOT_A_CARD,
				Card.NOT_A_CARD
		};
		
		discard.add(Card.SENATOR);
		
        assertDiscard();
        assertFields();
        assert(gameState.getActionDice().length == 2);
        
        move.activateMoneyDisc(6);
        playerSestertiis[PLAYER_1] += 6;
        
        move.activateMoneyDisc(6);
        playerSestertiis[PLAYER_1] += 6;
        
        assertSestertiis();
        assert(gameState.getActionDice().length == 0);
        
        move.endTurn();
        
        /**
         * Set action die to [2,6,6]
         * Player 2 uses two dice to get 12 sestertii
         * Player 2 now lays down a new Senator and a Praetorianus
         * Player activates Praetorianus with final action die to
         * block dice disc 4 (opponent's empty dice disc)
         * And ends turn
         * End state:
         * PLayer 1:
         * <NOT_A_CARD, VELITES, TURRIS, NOT_A_CARD, NOT_A_CARD, NOT_A_CARD, NOT_A_CARD>
         * Player 2:
         * <NOT_A_CARD, PRAETORIANUS, SENATOR, NOT_A_CARD, NOT_A_CARD, NOT_A_CARD, NOT_A_CARD>
         */
        
        gameState.setActionDice(new int[] {2, 6, 6});
        
        move.activateMoneyDisc(6);
        playerSestertiis[PLAYER_2] += 6;
        
        move.activateMoneyDisc(6);
        playerSestertiis[PLAYER_2] += 6;
        
        assertSestertiis();
        
        move.placeCard(Card.PRAETORIANUS, 2);
        playerSestertiis[PLAYER_2] -= 4;
        
        move.placeCard(Card.SENATOR, 3);
        playerSestertiis[PLAYER_2] -= 3;
        
        playerFields[PLAYER_2] =
				new Card[] {
				Card.NOT_A_CARD,
				Card.PRAETORIANUS,
				Card.SENATOR,
				Card.NOT_A_CARD,
				Card.NOT_A_CARD,
				Card.NOT_A_CARD,
				Card.NOT_A_CARD
		};
        
        playerHands[PLAYER_2].remove(Card.SENATOR);
        playerHands[PLAYER_2].remove(Card.PRAETORIANUS);
        
        assertFields();
        assertHands();
        assertSestertiis();
        
        assert(gameState.getActionDice().length == 1);
        
        PraetorianusActivator prae = (PraetorianusActivator) move.chooseCardToActivate(2);
        prae.chooseDiceDisc(4);
        prae.complete();
        
        assert(gameState.getActionDice().length == 0);
        
        move.endTurn();
        
        /**
         * Action die is set to [3, 6, 6]
         * Player 1 lays down a Velites on dice disc 4 (blocked but allowed)
         * Player 1 tries to attack Senator with Velites  from dice disc 4 and fails
         * no change in state but action die is used up (activation was accepted but failed)
         */
        gameState.setActionDice(new int[] {4, 6, 6});
        
        move.placeCard(Card.VELITES, 4);
        playerSestertiis[PLAYER_1] -= 5;
        
        playerFields[PLAYER_1] =
				new Card[] {
				Card.NOT_A_CARD,
				Card.VELITES,
				Card.TURRIS,
				Card.VELITES,
				Card.NOT_A_CARD,
				Card.NOT_A_CARD,
				Card.NOT_A_CARD
		};
        
        playerHands[PLAYER_1].remove(Card.VELITES);
        
        assertFields();
        assertHands();
        assertSestertiis();
        
        // this activation will fail (action die not used)
        velites = (VelitesActivator)move.chooseCardToActivate(4);
		velites.giveAttackDieRoll(DEAD_SENATOR_DICE);
		velites.chooseDiceDisc(3);
		velites.complete();
        
		assertDiscard();
        assertFields();
        assert(gameState.getActionDice().length == 3);
        	
		/**
		 * Player 1 continues to use the second action die to get gold, 
		 * lays down a Praetorianus on dice disc 6 and uses the remaining die
		 * to activate it, blocking Player 2's Praetorianus on dice disc 2
		 * action die used up, end turn
		 */
        
        move.activateMoneyDisc(6);
        playerSestertiis[PLAYER_1] += 6;
        
        assert(gameState.getActionDice().length == 2);
        
        move.placeCard(Card.PRAETORIANUS, 6);
        playerSestertiis[PLAYER_1] -= 4;
        
        playerFields[PLAYER_1] =
				new Card[] {
				Card.NOT_A_CARD,
				Card.VELITES,
				Card.TURRIS,
				Card.VELITES,
				Card.NOT_A_CARD,
				Card.PRAETORIANUS,
				Card.NOT_A_CARD
		};
        
        playerHands[PLAYER_1].remove(Card.PRAETORIANUS);
        
        assertHands();
        assertFields();
        assertSestertiis();
        
        prae = (PraetorianusActivator) move.chooseCardToActivate(6);
        prae.chooseDiceDisc(2);
        prae.complete();
        
        assert(gameState.getActionDice().length == 1);

        // have more dice? Well, get some gold. ._.
        move.activateMoneyDisc(4);
        playerSestertiis[PLAYER_1] += 4;
        
        assertSestertiis();
        assert(gameState.getActionDice().length == 0);
        move.endTurn();
        
        /**
         * Set action die [2, 6, 6]
         * Player 2 gets more gold using action die 6
         * Player 2 lays another Praetorianus on dice disc 4
         * Player 2 activates one Praetorianus to block Opponent's Velites on dice disc 2
         * but this will sadly fail because it has already been blocked.
         * Player 2 successfully activates another Praetorianus to block Turris (allowed, but
         * has no effect and cannot be tested properly)
         * Final action die (from failed activation) used to get more gold.
         * All action die used
		 */
               
        gameState.setActionDice(new int[] {2, 6, 6});
        
        move.activateMoneyDisc(6);
        playerSestertiis[PLAYER_2] += 6;
        
        move.placeCard(Card.PRAETORIANUS, 6);
        playerSestertiis[PLAYER_2] -= 4;
        
        playerHands[PLAYER_2].remove(Card.PRAETORIANUS);
        
        playerFields[PLAYER_2] =
				new Card[] {
				Card.NOT_A_CARD,
				Card.PRAETORIANUS,
				Card.SENATOR,
				Card.NOT_A_CARD,
				Card.NOT_A_CARD,
				Card.PRAETORIANUS,
				Card.NOT_A_CARD
		};
        
        assertFields();
        assertHands();
        assertSestertiis();
        assert(gameState.getActionDice().length == 2);
        
        // this activation will fail (action die not used)
        prae = (PraetorianusActivator)move.chooseCardToActivate(2);
        prae.chooseDiceDisc(2);
        prae.complete();
        
        // this activation will pass, but cannot be tested
        prae = (PraetorianusActivator)move.chooseCardToActivate(6);
        prae.chooseDiceDisc(3);
        prae.complete();
        
        assert(gameState.getActionDice().length == 1);
        
        // have more dice? Well, get some gold. ._.
        move.activateMoneyDisc(2);
        playerSestertiis[PLAYER_2] += 2;
        
        assertSestertiis();
        assert(gameState.getActionDice().length == 0);
        
        move.endTurn();
        
        /**
         * Finally, the epic conclusion to Player 2's Praetorianus!
         * Set action die to [2, 4, 6]
         * Player 1 activates Velites from dice disc 2 and kills a Praetorianus!
         * Player 1 activates another Velites from disc 4 and kills the other Praetorianus!
         * Player 1 activates his own Praetorianus, crippling Senator! :D
         */
        
        gameState.setActionDice(new int[] {2, 4, 6});
        
        velites = (VelitesActivator) move.chooseCardToActivate(2);
        velites.chooseDiceDisc(2);
        velites.giveAttackDieRoll(DEAD_PRAETORIANUS_DICE);
        velites.complete();
        
        playerFields[PLAYER_2] =
				new Card[] {
				Card.NOT_A_CARD,
				Card.NOT_A_CARD,
				Card.SENATOR,
				Card.NOT_A_CARD,
				Card.NOT_A_CARD,
				Card.PRAETORIANUS,
				Card.NOT_A_CARD
		};
        
        discard.add(Card.PRAETORIANUS);
        
        assertDiscard();
        assertFields();
        assert(gameState.getActionDice().length == 2);
        
        velites = (VelitesActivator) move.chooseCardToActivate(4);
        velites.chooseDiceDisc(6);
        velites.giveAttackDieRoll(DEAD_PRAETORIANUS_DICE);
        velites.complete();
        
        playerFields[PLAYER_2] =
				new Card[] {
				Card.NOT_A_CARD,
				Card.NOT_A_CARD,
				Card.SENATOR,
				Card.NOT_A_CARD,
				Card.NOT_A_CARD,
				Card.NOT_A_CARD,
				Card.NOT_A_CARD
		};
        
        discard.add(Card.PRAETORIANUS);
        
        assertDiscard();
        assertFields();
        assert(gameState.getActionDice().length == 1);
        
        prae = (PraetorianusActivator)move.chooseCardToActivate(6);
        prae.chooseDiceDisc(3);
        prae.complete();
        
        assert(gameState.getActionDice().length == 0);
        assertSestertiis();

        playerVPs[PLAYER_1] = 9;
        playerVPs[PLAYER_2] = 5;
        assertVPs();
	}

	

	private void transferInitialFieldsToState() {
		for (int i = 0; i < Rules.NUM_PLAYERS; i++) {
			this.gameState.setPlayerCardsOnDiscs(i,playerFields[i]);
		}
	}

	private void transferInitialHandsToState() {
		for (int i = 0; i < Rules.NUM_PLAYERS; i++) {
			this.gameState.setPlayerHand(i, playerHands[i]);
		}
	}

	private static String padRight(String s, int width) {
	     return String.format("%1$-" + width + "s", s);  
	}

	private static String padLeft(String s, int width) {
	    return String.format("%1$#" + width + "s", s);  
	}
	
	/*
	 * Prints out all the fields for debugging
	 * Left in here for other's debugging purposes
	 */
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
            playerVPs[i] = 17;
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
