package tests.verifiedSpec2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import framework.Rules;
import framework.Test;
import framework.cards.Card;
import framework.interfaces.GameState;
import framework.interfaces.MoveMaker;
import framework.interfaces.activators.AesculapinumActivator;
import framework.interfaces.activators.ForumActivator;
import framework.interfaces.activators.GladiatorActivator;
import framework.interfaces.activators.LegatActivator;
import framework.interfaces.activators.SenatorActivator;

/**
 * Author: Chris FONG
 * 
 * All dice values randomly generated from http://www.random.org/dice/
 * 
 */
public class PlaythroughChrisFongTwoTest extends Test {

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
        gameState.setWhoseTurn(PLAYER_1);
        gameState.setActionDice(new int[] {1,2,5});

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

            Deck Indicator:
                #1

        */
        
        for (int i = 0; i < Rules.NUM_PLAYERS ; i++) {
            for (int j = 0; j < 5; j++) {
                playerHands[i].add(deck.remove(0));
            }
        }
        
        /*
        Hands:
            Player 1        Player 2:
            GLADIATOR       TEMPLUM
            FORUM			SENATOR
            FORUM           MACHINA
            TURRIS    		KAT
            TELEPHONEBOX	AESCULAPINUM       
            
        Place onto Fields:
		         			 1          2           3            4           5         6             7
        	Player 1:	<GLADIATOR>,<NOT_A_CARD>, <FORUM>,  <NOT_A_CARD>, <FORUM>, <TURRIS>,  <TELEPHONEBOX>
			Player 2:	<TEMPLUM>,	<NOT_A_CARD>,<NOT_A_CARD>,<SENATOR>,   <KAT>,   <MACHINA>, <AESCULAPINUM>
        */
        
        playerHands[PLAYER_1] = new ArrayList<Card>();
        playerHands[PLAYER_2] = new ArrayList<Card>();
        		
        playerFields[PLAYER_1] =
                new Card[] {
		                Card.GLADIATOR,
		                Card.NOT_A_CARD,
		                Card.FORUM,
		                Card.NOT_A_CARD,
		                Card.FORUM,
		                Card.TURRIS,
		                Card.TELEPHONEBOX
		        };
        
        playerFields[PLAYER_2] =
                new Card[] {
		                Card.TEMPLUM,
		                Card.NOT_A_CARD,
		                Card.NOT_A_CARD,
		                Card.SENATOR,
		                Card.KAT,
		                Card.MACHINA,
		                Card.AESCULAPINUM
		        };
        
        gameState.setDeck(deck);

        transferHandsToState();
        transferFieldsToState();

        assertHands();
        assertFields();

        /*
            Gameplay starts here
            ActionDice = {1,2,5}
         */

        /*
            Gladiator returns Senator to PLAYER_2's hand
         */
        assertFields();

        playerFields[PLAYER_2][3] = Card.NOT_A_CARD;
        playerHands[PLAYER_2].add(Card.SENATOR);

        GladiatorActivator russellCrowe = (GladiatorActivator)move.chooseCardToActivate(Rules.DICE_DISC_1);
        russellCrowe.chooseDiceDisc(Rules.DICE_DISC_4);
        russellCrowe.complete();
        assertFields();
        assertHands();

        /*
            Draw 2 Cards and pick Legionarius to keep
         */

        deck.remove(Card.LEGIONARIUS);
        deck.remove(Card.CONSILIARIUS);
        discard.add(Card.CONSILIARIUS);
        playerHands[PLAYER_1].add(Card.LEGIONARIUS);

        move.activateCardsDisc(2,Card.LEGIONARIUS);

        assertDeck();
        assertDiscard();
        assertHands();

        playerSestertiis[PLAYER_1] += 5;

        move.activateMoneyDisc(5);
        assertSestertiis();

        move.endTurn();

        /*
             Start of Round Summary

             Victory Points:
                 Player 1: 10
                 Player 2: 7
                 Pool:	  19

             Sestertii:
                 Player 1: 5
                 Player 2: 0

             Hands:
                Player 1:        Player 2:
                                 Senator

             Fields:
                                1           2           3             4          5         6             7
                Player 1:  <GLADIATOR>,<NOT_A_CARD>, <FORUM>,   <NOT_A_CARD>, <FORUM>, <TURRIS>,  <TELEPHONEBOX>
                Player 2:	<TEMPLUM>, <NOT_A_CARD>,<NOT_A_CARD>,<NOT_A_CARD>, <KAT>,  <MACHINA>, <AESCULAPINUM>

        */

        playerVPs[PLAYER_2] -= 3;
        assertVPs();

        gameState.setActionDice(new int[] {3,5,6});

        playerHands[PLAYER_2].add(Card.PRAETORIANUS);
        deck.remove(Card.LEGAT);
        deck.remove(Card.VELITES);
        deck.remove(Card.BASILICA);
        deck.remove(Card.ONAGER);
        deck.remove(Card.PRAETORIANUS);
        discard.add(Card.LEGAT);
        discard.add(Card.VELITES);
        discard.add(Card.BASILICA);
        discard.add(Card.ONAGER);

        move.activateCardsDisc(5,Card.PRAETORIANUS);
        assertHands();
        assertDeck();
        assertDiscard();

        playerSestertiis[PLAYER_2] += 6;
        move.activateMoneyDisc(6);
        assertSestertiis();

        playerSestertiis[PLAYER_2] -= 4;
        playerFields[PLAYER_2][2] = Card.PRAETORIANUS;
        playerHands[PLAYER_2].remove(Card.PRAETORIANUS);
        move.placeCard(Card.PRAETORIANUS, Rules.DICE_DISC_3);
        assertSestertiis();
        assertFields();
        assertHands();

        playerSestertiis[PLAYER_2] += 3;
        move.activateMoneyDisc(3);
        assertSestertiis();

        move.endTurn();

        /*
             Start of Round Summary

             Victory Points:
                 Player 1: 8
                 Player 2: 7
                 Pool:	  21

             Sestertii:
                 Player 1: 5
                 Player 2: 5

             Hands:
                Player 1:        Player 2:
                                 Senator

             Fields:
                                1           2           3             4          5         6             7
                Player 1:  <GLADIATOR>,<NOT_A_CARD>, <FORUM>,   <NOT_A_CARD>, <FORUM>, <TURRIS>,  <TELEPHONEBOX>
                Player 2:	<TEMPLUM>, <NOT_A_CARD>,<PRAETORIANUs>,<NOT_A_CARD>, <KAT>,  <MACHINA>, <AESCULAPINUM>

        */

        playerVPs[PLAYER_1] -= 2;
        assertVPs();

        gameState.setActionDice(new int[]{1,4,5});

        playerVPs[PLAYER_1] += 4;
        ForumActivator openLearning = (ForumActivator) move.chooseCardToActivate(Rules.DICE_DISC_5);
        openLearning.chooseActionDice(4);
        openLearning.complete();
        assertVPs();

        playerFields[PLAYER_2][4] = Card.NOT_A_CARD;
        playerHands[PLAYER_2].add(Card.KAT);
        russellCrowe = (GladiatorActivator) move.chooseCardToActivate(Rules.DICE_DISC_1);
        russellCrowe.chooseDiceDisc(Rules.DICE_DISC_5);
        russellCrowe.complete();
        assertFields();
        assertHands();

        move.endTurn();

        /*
             Start of Round Summary

             Victory Points:
                 Player 1: 12
                 Player 2: 4
                 Pool:	   20

             Sestertii:
                 Player 1: 5
                 Player 2: 5

             Hands:
                Player 1:        Player 2:
                                 Senator
                                 Kat
             Fields:
                                1           2           3             4              5         6             7
                Player 1:  <GLADIATOR>,<NOT_A_CARD>, <FORUM>,   <NOT_A_CARD>,     <FORUM>,  <TURRIS>,  <TELEPHONEBOX>
                Player 2:	<TEMPLUM>, <NOT_A_CARD>,<PRAETORIANUS>,<NOT_A_CARD>,<NOT_A_CARD>,<MACHINA>,<AESCULAPINUM>

        */

        playerVPs[PLAYER_2] -= 3;
        assertVPs();

        gameState.setActionDice(new int[]{1,2,6});

        /*
            Current Discard:
               0.Onager
               1.Basilica
               2.Velites
               3.Legat
               4.Consiliarius
         */

        playerSestertiis[PLAYER_2] -= 1;
        discard.remove(Card.LEGAT);
        playerHands[PLAYER_2].add(Card.LEGAT);
        AesculapinumActivator monsterReborn = (AesculapinumActivator) move.activateBribeDisc(Rules.DICE_DISC_1);
        monsterReborn.chooseCardFromPile(getIndexFromPile(Card.LEGAT, gameState.getDiscard()));
        monsterReborn.complete();
        assertSestertiis();
        assertDiscard();
        assertHands();

        playerSestertiis[PLAYER_2] += 6;
        move.activateMoneyDisc(6);
        assertSestertiis();

        playerSestertiis[PLAYER_2] -= 3;
        playerHands[PLAYER_2].remove(Card.SENATOR);
        playerHands[PLAYER_2].remove(Card.LEGAT);
        playerHands[PLAYER_2].remove(Card.KAT);
        playerFields[PLAYER_2][1] = Card.SENATOR;
        playerFields[PLAYER_2][3] = Card.LEGAT;
        playerFields[PLAYER_2][4] = Card.KAT;

        move.placeCard(Card.SENATOR, Rules.DICE_DISC_2);
        SenatorActivator caesar = (SenatorActivator) move.chooseCardToActivate(Rules.DICE_DISC_2);
        caesar.layCard(Card.LEGAT, Rules.DICE_DISC_4);
        caesar.layCard(Card.KAT, Rules.DICE_DISC_5);
        caesar.complete();

        assertHands();
        assertFields();
        assertSestertiis();

        move.endTurn();

        /*
             Start of Round Summary

             Victory Points:
                 Player 1: 10
                 Player 2: 4
                 Pool:	   22

             Sestertii:
                 Player 1: 5
                 Player 2: 7

             Hands:
                Player 1:        Player 2:


             Fields:
                                1           2           3           4           5         6         7
                Player 1:  <GLADIATOR>,<NOT_A_CARD>, <FORUM>,  <NOT_A_CARD>, <FORUM>, <TURRIS>,<TELEPHONEBOX>
                Player 2:	<TEMPLUM>, <SENATOR>,<PRAETORIANUS>,<LEGAT>,      <KAT>, <MACHINA>,<AESCULAPINUM>

        */

        gameState.setActionDice(new int[] {4,2,3});
        playerVPs[PLAYER_1] -= 2;
        assertVPs();

        playerVPs[PLAYER_1] += 4;
        openLearning = (ForumActivator) move.chooseCardToActivate(Rules.DICE_DISC_3);
        openLearning.chooseActionDice(4);
        openLearning.complete();

        deck.remove(Card.ESSEDUM);
        deck.remove(Card.TRIBUNUSPLEBIS);
        discard.add(Card.TRIBUNUSPLEBIS);
        playerHands[PLAYER_1].add(Card.ESSEDUM);
        move.activateCardsDisc(2, Card.ESSEDUM);
        assertDeck();
        assertDiscard();
        assertHands();

        move.endTurn();

        /*
         Start of Round Summary

         Victory Points:
             Player 1: 14
             Player 2: 4
             Pool:	   18

         Sestertii:
             Player 1: 5
             Player 2: 7

         Hands:
             Player 1:        Player 2:
             Essedum

         Fields:
                            1           2           3           4           5        6         7
             Player 1:  <GLADIATOR>,<NOT_A_CARD>, <FORUM>, <NOT_A_CARD>,<FORUM>,<TURRIS>,<TELEPHONEBOX>
             Player 2:	<TEMPLUM>, <SENATOR>,<PRAETORIANUS>,<LEGAT>,      <KAT>,<MACHINA>,<AESCULAPINUM>

        */

        gameState.setActionDice(new int[]{5,6,4});

        playerVPs[PLAYER_2] += 2;
        LegatActivator legat = (LegatActivator) move.chooseCardToActivate(4);
        legat.complete();
        assertVPs();

        deck.remove(Card.SENATOR);
        deck.remove(Card.FORUM);
        deck.remove(Card.PRAETORIANUS);
        deck.remove(Card.CENTURIO);
        deck.remove(Card.ARCHITECTUS);
        discard.add(Card.SENATOR);
        discard.add(Card.PRAETORIANUS);
        discard.add(Card.CENTURIO);
        discard.add(Card.ARCHITECTUS);
        playerHands[PLAYER_2].add(Card.FORUM);
        move.activateCardsDisc(5, Card.FORUM);
        assertDeck();
        assertDiscard();
        assertHands();

        playerSestertiis[PLAYER_2] -= 5;
        playerHands[PLAYER_2].remove(Card.FORUM);
        discard.add(Card.SENATOR);
        playerFields[PLAYER_2][1] = Card.FORUM;
        move.placeCard(Card.FORUM, Rules.DICE_DISC_2);
        assertHands();
        assertDiscard();
        assertFields();

        playerSestertiis[PLAYER_2] += 6;
        move.activateMoneyDisc(6);
        assertSestertiis();

        move.endTurn();

        /*
         Start of Round Summary

         Victory Points:
             Player 1: 12
             Player 2: 6
             Pool:	   18

         Sestertii:
             Player 1: 5
             Player 2: 8

         Hands:
             Player 1:        Player 2:
             Essedum

         Fields:
                            1           2           3           4           5        6         7
             Player 1:  <GLADIATOR>,<NOT_A_CARD>, <FORUM>, <NOT_A_CARD>,<FORUM>,<TURRIS>,<TELEPHONEBOX>
             Player 2:	<TEMPLUM>,  <FORUM>,   <PRAETORIANUS>,<LEGAT>,   <KAT>,<MACHINA>,<AESCULAPINUM>

        */

        playerVPs[PLAYER_1] -= 2;
        assertVPs();

        gameState.setActionDice(new int[] {6,3,4});

        playerVPs[PLAYER_1] += 6;
        openLearning = (ForumActivator)move.chooseCardToActivate(Rules.DICE_DISC_3);
        openLearning.chooseActionDice(6);
        openLearning.complete();
        assertVPs();

        playerSestertiis[PLAYER_1] += 4;
        move.activateMoneyDisc(4);
        assertSestertiis();

        playerSestertiis[PLAYER_1] -= 6;
        playerHands[PLAYER_1].remove(Card.ESSEDUM);
        playerFields[PLAYER_1][3] = Card.ESSEDUM;
        move.placeCard(Card.ESSEDUM,Rules.DICE_DISC_4);
        assertSestertiis();
        assertHands();
        assertFields();

        move.endTurn();

        /*
         Start of Round Summary

         Victory Points:
             Player 1: 18
             Player 2: 6
             Pool:	   12

         Sestertii:
             Player 1: 5
             Player 2: 8

         Hands:
             Player 1:        Player 2:

         Fields:
                            1           2           3           4           5        6         7
             Player 1:  <GLADIATOR>,<ESSEDUM>, <FORUM>, <NOT_A_CARD>,<FORUM>,<TURRIS>,<TELEPHONEBOX>
             Player 2:	<TEMPLUM>,  <FORUM>,   <PRAETORIANUS>,<LEGAT>,   <KAT>,<MACHINA>,<AESCULAPINUM>

        */

        gameState.setActionDice(new int[]{6,2,6});

        playerVPs[PLAYER_2] += 12;
        openLearning = (ForumActivator) move.chooseCardToActivate(2);
        openLearning.chooseActivateTemplum(true);
        openLearning.chooseActivateTemplum(6);
        openLearning.chooseActionDice(6);
        openLearning.complete();
        assertVPs();

        assert(gameState.isGameCompleted());

        /*
         End of Game Summary

         Victory Points:
             Player 1: 18
             Player 2: 18
             Pool:	   0

         Sestertii:
             Player 1: 5
             Player 2: 8

         Hands:
             Player 1:        Player 2:


         Fields:
                            1           2           3           4           5        6         7
             Player 1:  <GLADIATOR>,<ESSEDUM>, <FORUM>, <NOT_A_CARD>,<FORUM>,<TURRIS>,<TELEPHONEBOX>
             Player 2:	<TEMPLUM>,  <FORUM>,   <PRAETORIANUS>,<LEGAT>,   <KAT>,<MACHINA>,<AESCULAPINUM>

        */

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
        deck.add(Card.LEGIONARIUS);  //#2
        deck.add(Card.LEGAT);
        deck.add(Card.VELITES);
        deck.add(Card.BASILICA);
        deck.add(Card.ONAGER);
        deck.add(Card.PRAETORIANUS); //#3
        deck.add(Card.TRIBUNUSPLEBIS);
        deck.add(Card.ESSEDUM);      //#4
        deck.add(Card.SENATOR);
        deck.add(Card.FORUM);
        deck.add(Card.PRAETORIANUS);
        deck.add(Card.CENTURIO);
        deck.add(Card.ARCHITECTUS);  //#5
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
