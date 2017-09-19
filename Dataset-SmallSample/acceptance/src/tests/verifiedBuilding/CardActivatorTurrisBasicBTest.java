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
 * @author: Wen Di
 * @author: Anne
 * Further tests for Turris functionality
 * Methods originally created by Chris Fong TM in PlaythroughChrisFongTest.java
 */

public class CardActivatorTurrisBasicBTest extends Test {

	private final int DEAD_SENATOR_DICE = 3;
	private final int DEAD_TURRIS_DICE = 6;
	
    private final int PLAYER_1 = 0;
    private final int PLAYER_2 = 1;

    private List<Card> discard;

    private Card[][] playerFields;
    private List<Card>[] playerHands;

    private int[] playerVPs;
    private int[] playerSestertiis;

    private GameState gameState;

	public String getShortDescription() {
		return "Testing the basic functionality of Turris";
	}

	public void run(GameState gameState, MoveMaker move) throws AssertionError,
	UnsupportedOperationException, IllegalArgumentException {
		this.gameState = gameState;
		
		/** 
		 * Initialise discard to an empty state
		 * Initialise playerHands to an empty state
		 * Initialise player VPs to 10
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

		assertVPs();
		assertSestertiis();
		
		/** 
		 * Set player 1 Hand to have three Turris and one Senator
		 * Set playerFields where player 1 has a Senator and player 2 has two Legionarius
		 */
		
        playerHands[PLAYER_1] = new ArrayList<Card>();
        playerHands[PLAYER_2] = new ArrayList<Card>();
        
		playerHands[PLAYER_1].add(Card.TURRIS);
		playerHands[PLAYER_1].add(Card.TURRIS);
		playerHands[PLAYER_1].add(Card.TURRIS);
		playerHands[PLAYER_1].add(Card.SENATOR);
		
		playerFields[PLAYER_1] =
				new Card[] {
				Card.NOT_A_CARD,
				Card.SENATOR,
				Card.NOT_A_CARD,
				Card.NOT_A_CARD,
				Card.NOT_A_CARD,
				Card.NOT_A_CARD,
				Card.NOT_A_CARD
		};

		playerFields[PLAYER_2] =
				new Card[] {
				Card.NOT_A_CARD,
				Card.LEGIONARIUS,
				Card.NERO,
				Card.NOT_A_CARD,
				Card.NOT_A_CARD,
				Card.ONAGER,
				Card.NOT_A_CARD
		};
		
		transferInitialFieldsToState();
		transferInitialHandsToState();
		
		
		assertHands();
		assertFields();
		
		/** 
		 * Set player 2 turn
		 * Set action die [2, 2, 6]
		 */
		
		gameState.setWhoseTurn(PLAYER_2);
		gameState.setActionDice(new int[] {2, 6, 6});
		
		/**
		 * Use action die 2 to activate Legionarius and attack Senator with attack roll of 3. 
		 * Use actiondie 6 to get gold twice
		 * And end turn.  
		 * Should work.
		 */
			
		LegionariusActivator legion = (LegionariusActivator)move.chooseCardToActivate(2);
		legion.giveAttackDieRoll(DEAD_SENATOR_DICE);
		legion.complete();
		
		playerFields[PLAYER_1] =
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
        playerSestertiis[PLAYER_2] += 6;
        
        move.activateMoneyDisc(6);
        playerSestertiis[PLAYER_2] += 6;
        
        assertSestertiis();
        assert(gameState.getActionDice().length == 0);
        
        move.endTurn();
        
        /**
         * Set action die to [2,6,6]
         * Player 1 uses two dice to get 12 sestertii
         * Player 1 now lays down a new Senator and a Turris
         * And ends turn
         */
        
        gameState.setActionDice(new int[] {2, 6, 6});
        
        move.activateMoneyDisc(6);
        playerSestertiis[PLAYER_1] += 6;
        
        move.activateMoneyDisc(6);
        playerSestertiis[PLAYER_1] += 6;
        
        assertSestertiis();
        
        move.placeCard(Card.TURRIS, 6);
        playerSestertiis[PLAYER_1] -= 6;
        
        move.placeCard(Card.SENATOR, 2);
        playerSestertiis[PLAYER_1] -= 3;
        
        playerFields[PLAYER_1] =
				new Card[] {
				Card.NOT_A_CARD,
				Card.SENATOR,
				Card.NOT_A_CARD,
				Card.NOT_A_CARD,
				Card.NOT_A_CARD,
				Card.TURRIS,
				Card.NOT_A_CARD
		};
        
        playerHands[PLAYER_1].remove(Card.SENATOR);
        playerHands[PLAYER_1].remove(Card.TURRIS);
        
        assertFields();
        assertHands();
        assertSestertiis();
        
        move.endTurn();
        
        /**
         * Action die is set to [2, 2, 6]
         * Player 2 tries to attack Senator with Legionarius again and fails
         * no change in state but action die is used up
         */
        
        gameState.setActionDice(new int[] {2, 2, 6});
        
        legion = (LegionariusActivator)move.chooseCardToActivate(2);
		legion.giveAttackDieRoll(DEAD_SENATOR_DICE);
		legion.complete();
        
		assertDiscard();
        assertFields();
        assert(gameState.getActionDice().length == 2);
		
		/**
		 * Player 2 activates Onager to attack Turris, wins.
		 * Turris in discard pile
		 * No card in player 1's dice disc 6
		 * action die used up
		 */
        
        OnagerActivator onager = (OnagerActivator)move.chooseCardToActivate(6);
        onager.giveAttackDieRoll(DEAD_TURRIS_DICE);
        onager.chooseDiceDisc(6);
        onager.complete();
        
        playerFields[PLAYER_1] =
				new Card[] {
				Card.NOT_A_CARD,
				Card.SENATOR,
				Card.NOT_A_CARD,
				Card.NOT_A_CARD,
				Card.NOT_A_CARD,
				Card.NOT_A_CARD,
				Card.NOT_A_CARD
		};
        
        discard.add(Card.TURRIS);
        
        assertDiscard();
        assertFields();
        assert(gameState.getActionDice().length == 1);
        
        /**
         * Player 2 attacks Senator with Legionarius again and wins.
         * Senator in discard Pile
         * All action die used
         * No cards in player's pile
		 */
               
        legion = (LegionariusActivator)move.chooseCardToActivate(2);
		legion.giveAttackDieRoll(DEAD_SENATOR_DICE);
		legion.complete();
        
		playerFields[PLAYER_1] =
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
        assert(gameState.getActionDice().length == 0);
        
        move.endTurn();
        
        /**
         * Set action die to [2, 6, 6]
         * Player 1's turn now
         * Use action die 6 to get gold x 2
         * Lay down two turrises
		 */
       
        gameState.setActionDice(new int[] {2, 6, 6});
        
        move.activateMoneyDisc(6);
        playerSestertiis[PLAYER_1] += 6;
        
        move.activateMoneyDisc(6);
        playerSestertiis[PLAYER_1] += 6;
        
        move.placeCard(Card.TURRIS, 6);
        playerSestertiis[PLAYER_1] -= 6;
        
        move.placeCard(Card.TURRIS, 3);
        playerSestertiis[PLAYER_1] -= 6;
        
        playerFields[PLAYER_1] =
				new Card[] {
				Card.NOT_A_CARD,
				Card.NOT_A_CARD,
				Card.TURRIS,
				Card.NOT_A_CARD,
				Card.NOT_A_CARD,
				Card.TURRIS,
				Card.NOT_A_CARD
		};
        
        playerHands[PLAYER_1].remove(Card.TURRIS);
        playerHands[PLAYER_1].remove(Card.TURRIS);
        
        assertFields();
        assertHands();
        assertSestertiis();
        
        move.endTurn();
        
        /**
         * Set action die to [3, 6, 6]
         * Player 2's turn
         * Uses one action die to activate Onager and kill Turris
         * It wasn't effective (Turris invibility with 7 defence!!!)
		 */
        
        gameState.setActionDice(new int[] {3, 6, 6});
        
        onager = (OnagerActivator)move.chooseCardToActivate(6);
        onager.chooseDiceDisc(6);
        onager.giveAttackDieRoll(DEAD_TURRIS_DICE);
        onager.complete();
        
        assertDiscard();
        assertFields();
        assert(gameState.getActionDice().length == 2);
        
        /**
         * Player 2 uses one action die to activate Nero and DESTROY Turris
         * It was supper effective! 
         * Unfortunately it killed itself too
		 */
        
        NeroActivator nero = (NeroActivator) move.chooseCardToActivate(3);
        nero.chooseDiceDisc(3);
        nero.complete();
        
        playerFields[PLAYER_1] =
				new Card[] {
				Card.NOT_A_CARD,
				Card.NOT_A_CARD,
				Card.NOT_A_CARD,
				Card.NOT_A_CARD,
				Card.NOT_A_CARD,
				Card.TURRIS,
				Card.NOT_A_CARD
		};
        
        playerFields[PLAYER_2] =
				new Card[] {
				Card.NOT_A_CARD,
				Card.LEGIONARIUS,
				Card.NOT_A_CARD,
				Card.NOT_A_CARD,
				Card.NOT_A_CARD,
				Card.ONAGER,
				Card.NOT_A_CARD
		};
        
        discard.add(Card.TURRIS);
        discard.add(Card.NERO);
        
        assertDiscard();
        assertFields();
        assert(gameState.getActionDice().length == 1);
        
        /**
         * Player 2 uses final action die to activate Onager again!
         * It activates, it kills, it wins!
         * Hooray for Player 2!
		 */
        
        onager = (OnagerActivator)move.chooseCardToActivate(6);
        onager.giveAttackDieRoll(DEAD_TURRIS_DICE);
        onager.chooseDiceDisc(6);
        onager.complete();
        
        playerFields[PLAYER_1] =
				new Card[] {
				Card.NOT_A_CARD,
				Card.NOT_A_CARD,
				Card.NOT_A_CARD,
				Card.NOT_A_CARD,
				Card.NOT_A_CARD,
				Card.NOT_A_CARD,
				Card.NOT_A_CARD
		};
        
        discard.add(Card.TURRIS);
        
        assertDiscard();
        assertFields();
        assert(gameState.getActionDice().length == 0);
        
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
