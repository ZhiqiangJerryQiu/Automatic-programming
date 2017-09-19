package tests.verifiedSpec2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import framework.Rules;
import framework.Test;
import framework.cards.Card;
import framework.interfaces.GameState;
import framework.interfaces.MoveMaker;
import framework.interfaces.activators.CenturioActivator;
import framework.interfaces.activators.ConsiliariusActivator;
import framework.interfaces.activators.ConsulActivator;
import framework.interfaces.activators.ForumActivator;
import framework.interfaces.activators.LegatActivator;
import framework.interfaces.activators.PraetorianusActivator;
import framework.interfaces.activators.TribunusPlebisActivator;
import framework.interfaces.activators.VelitesActivator;

/**
 * Author: Chris FONG
 */
public class PlaythroughChrisFongTest extends Test {

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

            Deck Indicator:
                #1

        */

        for (int i = 0; i < Rules.NUM_PLAYERS ; i++) {
            for (int j = 0; j < 5; j++) {
                playerHands[i].add(deck.remove(0));
            }
        }

        gameState.setDeck(deck);

        /*
            Hands:
                Player 1        Player 2:
                FORUM           CENTURIO
                MERCATUS        TRIBUNUSPLEBIS
                LEGAT           MACHINA
                PRAETORIANUS    CONSUL
                SCAENICUS       LEGIONARIUS

            Place onto Fields:
			         			 1          2              3            4            5         6             7
            	Player 1:	<FORUM>,    <SCAENICUS>  ,<NOT_A_CARD>, <MERCATUS>,   <LEGAT>,<PRAETORIANUS>,<NOT_A_CARD>
				Player 2:	<NOT_A_CARD>,<CENTURIO>,<TRIBUNUSPLEBIS>,<MACHINA>,  <CONSUL>,<LEGIONARIUS>, <NOT_A_CARD>


        */

        playerHands[PLAYER_1] = new ArrayList<Card>();
        playerHands[PLAYER_2] = new ArrayList<Card>();

        playerFields[PLAYER_1] =
                new Card[] {
                        Card.FORUM,
                        Card.SCAENICUS,
                        Card.NOT_A_CARD,
                        Card.MERCATUS,
                        Card.LEGAT,
                        Card.PRAETORIANUS,
                        Card.NOT_A_CARD
                };

        playerFields[PLAYER_2] =
                new Card[] {
                        Card.NOT_A_CARD,
                        Card.CENTURIO,
                        Card.TRIBUNUSPLEBIS,
                        Card.MACHINA,
                        Card.CONSUL,
                        Card.LEGIONARIUS,
                        Card.NOT_A_CARD
                };

        transferHandsToState();
        transferFieldsToState();

        assertHands();
        assertFields();

        /*
            Set to Player 1's Turn
            Set Action Die:
                1 , 1 , 2
         */

        gameState.setWhoseTurn(PLAYER_1);
        gameState.setActionDice(new int[]{2,5,6});

        /*
            Use Action Die 2 to Draw 2 Cards

            Cards Drawn:
                * Nero
                * Architectus

            Select Architectus.

            New Hand:
            	* Architectus

         */

        playerHands[PLAYER_1].add(Card.ARCHITECTUS);
        discard.add(Card.NERO);
        deck.remove(Card.NERO);
        deck.remove(Card.ARCHITECTUS);

        move.activateCardsDisc(2, Card.ARCHITECTUS);
        assertHands();
        assertDeck();
        assertDiscard();

        /*
        	Activate Legat:
        		*Player 1's new VP should be 12
         */

        playerVPs[PLAYER_1] += 2;

        LegatActivator legat = (LegatActivator)move.chooseCardToActivate(Rules.DICE_DISC_5);
        legat.complete();

        assertVPs();
        assert(gameState.getActionDice().length == 1);

        /*
        	Activate Praetorianus
        		*Block disc 3 (Tribunus Plebis)
         */

        PraetorianusActivator praetorianus = (PraetorianusActivator)move.chooseCardToActivate(Rules.DICE_DISC_6);
        praetorianus.chooseDiceDisc(Rules.DICE_DISC_3);
        praetorianus.complete();

        move.endTurn();
        assert(gameState.getWhoseTurn() == PLAYER_2);

        playerVPs[PLAYER_2] -= 2;
        assertVPs();

        /*
          	Start of Round Summary

          	Victory Points:
          		Player 1: 12
          		Player 2: 8
          		Pool:	  16

          	Sestertii:
          		Player 1: 0
          		Player 2: 0

        	Hands:
	            Player 1:        Player 2:
				Architectus


        	Fields:
		         			 1          2              3            4            5         6             7
        	Player 1:	<FORUM>,    <SCAENICUS>  ,<NOT_A_CARD>,	 <MERCATUS>,   <LEGAT>,<PRAETORIANUS>,<NOT_A_CARD>
			Player 2:	<NOT_A_CARD>,<CENTURIO>,<TRIBUNUSPLEBIS>*,<MACHINA>,  <CONSUL>,<LEGIONARIUS>, <NOT_A_CARD>

			* indicates blocked

         */


        /*
        	Checking that Disc 3 is blocked
         */

        gameState.setActionDice(new int[] {2,3,6});
        TribunusPlebisActivator tribunusPlebis = (TribunusPlebisActivator) move.chooseCardToActivate(Rules.DICE_DISC_3);
        tribunusPlebis.complete();

        assertVPs();
        assert(gameState.getActionDice().length == 3);

        /*
         	Use Dice 6 to get 6 Sestertiis for Player two
         	Use Dice 3 to draw 3 cards, and choose Turris.
         */

        move.activateMoneyDisc(6);
        playerSestertiis[PLAYER_2] += 6;

        assertSestertiis();

        deck.remove(Card.LEGIONARIUS);
        deck.remove(Card.HARUSPEX);
        deck.remove(Card.TURRIS);
        discard.add(Card.LEGIONARIUS);
        discard.add(Card.HARUSPEX);
        playerHands[PLAYER_2].add(Card.TURRIS);

        move.activateCardsDisc(3, Card.TURRIS);
        assertDeck();
        assertDiscard();
        assertHands();

        /*
        	Place the Turris on Dice Disc 1
         */

        move.placeCard(Card.TURRIS, Rules.DICE_DISC_1);
        playerHands[PLAYER_2].remove(Card.TURRIS);
        playerFields[PLAYER_2][0] = Card.TURRIS;
        playerSestertiis[PLAYER_2] -= 6;

        assertSestertiis();
        assertHands();
        assertFields();

        move.endTurn();

        playerVPs[PLAYER_1] -= 2;
        assert(gameState.getWhoseTurn() == PLAYER_1);
        assertVPs();

        /*
			Start of Round Summary

			Victory Points:
				Player 1: 10
				Player 2: 8
				Pool:	  18

			Sestertii:
				Player 1: 0
				Player 2: 0

			Hands:
			    Player 1:        Player 2:
				Architectus


			Fields:
				     			 1       2              3            4         5         6             7
				Player 1:	<FORUM>, <SCAENICUS>  ,<NOT_A_CARD>, <MERCATUS>,<LEGAT>,<PRAETORIANUS>,<NOT_A_CARD>
				Player 2:	<TURRIS>,<CENTURIO>,<TRIBUNUSPLEBIS>,<MACHINA>, <CONSUL>,<LEGIONARIUS>, <NOT_A_CARD>

         */

        gameState.setActionDice(new int[] {3,3,6});

        /*
         	Use Dice 3 to draw 3 cards and choose Velites
         	Use Dice 6 to gain 6 sestertiis
         */

        deck.remove(Card.SCAENICUS);
        deck.remove(Card.VELITES);
        deck.remove(Card.SENATOR);
        playerHands[PLAYER_1].add(Card.VELITES);
        discard.add(Card.SCAENICUS);
        discard.add(Card.SENATOR);

        move.activateCardsDisc(3, Card.VELITES);
        assert(gameState.getActionDice().length == 2);

        assertHands();
        assertDeck();
        assertDiscard();

        move.activateMoneyDisc(6);

        playerSestertiis[PLAYER_1] += 6;
        assertSestertiis();
        assert(gameState.getActionDice().length == 1);

        /*
        	Use 5 Sestertii to play Velites on Dice 3
        	Use Dice 3 to attack Tribunus Plebis with Tribunus Plebis' default defence
        		*Nothing should change due to Turris
         */

        playerHands[PLAYER_1].remove(Card.VELITES);
        playerFields[PLAYER_1][2] = Card.VELITES;

        move.placeCard(Card.VELITES, Rules.DICE_DISC_3);
        assertHands();
        assertFields();

        playerSestertiis[PLAYER_1] -= 5;

        VelitesActivator velites = (VelitesActivator)move.chooseCardToActivate(Rules.DICE_DISC_3);
        velites.chooseDiceDisc(3);
        velites.giveAttackDieRoll(5);
        velites.complete();

        assertDiscard();
        assertFields();

        move.endTurn();

        playerVPs[PLAYER_2] -= 1;
        assert(gameState.getWhoseTurn() == PLAYER_2);
        assertVPs();

        /*
			Start of Round Summary

			Victory Points:
				Player 1: 10
				Player 2: 7
				Pool:	  19

			Sestertii:
				Player 1: 1
				Player 2: 0

			Hands:
			    Player 1:        Player 2:
				Architectus


			Fields:
				     			 1       2              3            4         5         6             7
				Player 1:	<FORUM>, <SCAENICUS>  ,<VELITES> , <MERCATUS>,<LEGAT>,<PRAETORIANUS>,<NOT_A_CARD>
				Player 2:	<TURRIS>,<CENTURIO>,<TRIBUNUSPLEBIS>,<MACHINA>, <CONSUL>,<LEGIONARIUS>, <NOT_A_CARD>

         */

        gameState.setActionDice(new int[] {1,2,5});

        /*
        	Consul changes the Value of Dice 1
        		* Becomes 1 + 1 = 2
        	Centurio attacks Scaenicus with 1 + (1 + 1) = 3
        		* Kills Scaenicus
         */

        ConsulActivator consul = (ConsulActivator) move.chooseCardToActivate(Rules.DICE_DISC_5);
        consul.chooseWhichDiceChanges(1);
        consul.chooseConsulChangeAmount(1);
        consul.complete();
        assert(gameState.getActionDice().length == 2);

        CenturioActivator centurio = (CenturioActivator) move.chooseCardToActivate(Rules.DICE_DISC_2);
        centurio.giveAttackDieRoll(1);
        centurio.chooseActionDice(2);
        centurio.chooseCenturioAddActionDie(true);
        centurio.complete();

        playerFields[PLAYER_1][1] = Card.NOT_A_CARD;
        discard.add(Card.SCAENICUS);

        assertFields();
        assertDiscard();

        move.endTurn();

        playerVPs[PLAYER_1] -= 2;
        assertVPs();

        /*
			Start of Round Summary

			Victory Points:
				Player 1: 8
				Player 2: 7
				Pool:	  21

			Sestertii:
				Player 1: 1
				Player 2: 0

			Hands:
			    Player 1:        Player 2:
				Architectus


			Fields:
				     			 1       2              3            4         5         6             7
				Player 1:	<FORUM>, <NOT_A_CARD>  ,<VELITES> , <MERCATUS>,<LEGAT>,<PRAETORIANUS>,<NOT_A_CARD>
				Player 2:	<TURRIS>,<CENTURIO>,<TRIBUNUSPLEBIS>,<MACHINA>, <CONSUL>,<LEGIONARIUS>, <NOT_A_CARD>

         */

        gameState.setActionDice(new int[] {1,6,6});

        ForumActivator forum = (ForumActivator)move.chooseCardToActivate(Rules.DICE_DISC_1);
        forum.chooseActionDice(6);
        forum.complete();

        playerVPs[PLAYER_1] += 6;
        assertVPs();

        move.activateMoneyDisc(6);
        playerSestertiis[PLAYER_1] += 6;
        assertSestertiis();

        move.endTurn();

        playerVPs[PLAYER_2] -= 1;
        assertVPs();

        /*
			Start of Round Summary

			Victory Points:
				Player 1: 14
				Player 2: 6
				Pool:	  15

			Sestertii:
				Player 1: 7
				Player 2: 0

			Hands:
			    Player 1:        Player 2:
				Architectus


			Fields:
				     			 1       2              3            4         5         6             7
				Player 1:	<FORUM>, <NOT_A_CARD>  ,<VELITES> , <MERCATUS>,<LEGAT>,<PRAETORIANUS>,<NOT_A_CARD>
				Player 2:	<TURRIS>,<CENTURIO>,<TRIBUNUSPLEBIS>,<MACHINA>, <CONSUL>,<LEGIONARIUS>, <NOT_A_CARD>

         */

        gameState.setActionDice(new int[]{3,3,6});

        move.activateMoneyDisc(3);
        move.activateMoneyDisc(3);

        playerSestertiis[PLAYER_2] += 6;

        assertSestertiis();

        deck.remove(Card.ARCHITECTUS);
        deck.remove(Card.FORUM);
        deck.remove(Card.MACHINA);
        deck.remove(Card.AESCULAPINUM);
        deck.remove(Card.CONSUL);
        deck.remove(Card.GLADIATOR);
        discard.add(Card.ARCHITECTUS);
        discard.add(Card.FORUM);
        discard.add(Card.MACHINA);
        discard.add(Card.AESCULAPINUM);
        discard.add(Card.CONSUL);
        playerHands[PLAYER_2].add(Card.GLADIATOR);

        move.activateCardsDisc(6, Card.GLADIATOR);
        assertHands();
        assertDeck();
        assertDiscard();

        playerSestertiis[PLAYER_2] -= 6;
        playerFields[PLAYER_2][6] = Card.GLADIATOR;
        playerHands[PLAYER_2].remove(Card.GLADIATOR);
        move.placeCard(Card.GLADIATOR, Rules.BRIBE_DISC);

        assertSestertiis();
        assertFields();

        move.endTurn();

        playerVPs[PLAYER_1] -= 2;
        assertVPs();

        /*
			Start of Round Summary

			Victory Points:
				Player 1: 12
				Player 2: 6
				Pool:	  18

			Sestertii:
				Player 1: 7
				Player 2: 0

			Hands:
			    Player 1:        Player 2:
				Architectus


			Fields:
				     			 1       2              3            4         5         6             7
				Player 1:	<FORUM>, <NOT_A_CARD>  ,<VELITES> , <MERCATUS>,<LEGAT>,<PRAETORIANUS>,<NOT_A_CARD>
				Player 2:	<TURRIS>,<CENTURIO>,<TRIBUNUSPLEBIS>,<MACHINA>, <CONSUL>,<LEGIONARIUS>,<GLADIATOR>

         */

        assert(gameState.getWhoseTurn() == PLAYER_1);

        gameState.setActionDice(new int[] {1,6,6});

        deck.remove(Card.CONSILIARIUS);
        deck.remove(Card.ONAGER);
        deck.remove(Card.TURRIS);
        deck.remove(Card.BASILICA);
        deck.remove(Card.MERCATOR);
        deck.remove(Card.AESCULAPINUM);
        discard.add(Card.CONSILIARIUS);
        discard.add(Card.ONAGER);
        discard.add(Card.TURRIS);
        discard.add(Card.MERCATOR);
        discard.add(Card.AESCULAPINUM);
        playerHands[PLAYER_1].add(Card.BASILICA);

        move.activateCardsDisc(6, Card.BASILICA);

        assertHands();
        assertDeck();
        assertDiscard();

        playerHands[PLAYER_1].remove(Card.BASILICA);
        move.placeCard(Card.BASILICA, Rules.DICE_DISC_2);
        playerFields[PLAYER_1][1] = Card.BASILICA;
        playerSestertiis[PLAYER_1] -= 6;

        forum = (ForumActivator)move.chooseCardToActivate(Rules.DICE_DISC_1);
        forum.chooseActionDice(6);
        forum.complete();

        playerVPs[PLAYER_1] += (6 + 2);

        assertVPs();

        move.endTurn();

        playerVPs[PLAYER_2] -= 0;
        assertVPs();

        /*
			Start of Round Summary

			Victory Points:
				Player 1: 20
				Player 2: 6
				Pool:	  10

			Sestertii:
				Player 1: 1
				Player 2: 0

			Hands:
			    Player 1:        Player 2:
				Architectus


			Fields:
				     			 1       2              3            4         5         6             7
				Player 1:	<FORUM>, <BASILICA>  ,<VELITES> , <MERCATUS>,	<LEGAT>,<PRAETORIANUS>,<NOT_A_CARD>
				Player 2:	<TURRIS>,<CENTURIO>,<TRIBUNUSPLEBIS>,<MACHINA>, <CONSUL>,<LEGIONARIUS>,<GLADIATOR>

         */


        gameState.setActionDice(new int[] {1,2,5});
        move.activateMoneyDisc(1);
        playerSestertiis[PLAYER_2] += 1;
        assertSestertiis();

        consul = (ConsulActivator) move.chooseCardToActivate(Rules.DICE_DISC_5);
        consul.chooseWhichDiceChanges(2);
        consul.chooseConsulChangeAmount(1);
        consul.complete();

        playerVPs[PLAYER_1] -= 1;
        playerVPs[PLAYER_2] += 1;
        tribunusPlebis = (TribunusPlebisActivator) move.chooseCardToActivate(3);
        tribunusPlebis.complete();
        assertVPs();

        move.endTurn();

        playerVPs[PLAYER_1] -= 1;
        assertVPs();

        /*
			Start of Round Summary

			Victory Points:
				Player 1: 18
				Player 2: 7
				Pool:	  9

			Sestertii:
				Player 1: 1
				Player 2: 1

			Hands:
			    Player 1:        Player 2:
				Architectus


			Fields:
				     			 1       2              3            4         5         6             7
				Player 1:	<FORUM>, <BASILICA>  ,<VELITES> , <MERCATUS>,	<LEGAT>,<PRAETORIANUS>,<NOT_A_CARD>
				Player 2:	<TURRIS>,<CENTURIO>,<TRIBUNUSPLEBIS>,<MACHINA>, <CONSUL>,<LEGIONARIUS>,<GLADIATOR>

         */

        gameState.setActionDice(new int[] {3, 5, 5});

        velites = (VelitesActivator) move.chooseCardToActivate(Rules.DICE_DISC_3);
        velites.chooseDiceDisc(5);
        velites.giveAttackDieRoll(6);
        velites.complete();
        discard.add(Card.CONSUL);
        playerFields[PLAYER_2][4] = Card.NOT_A_CARD;
        assertDiscard();
        assertHands();

        deck.remove(Card.BASILICA);
        deck.remove(Card.LEGIONARIUS);
        deck.remove(Card.FORUM);
        deck.remove(Card.TRIBUNUSPLEBIS);
        deck.remove(Card.FORUM);
        discard.add(Card.BASILICA);
        discard.add(Card.LEGIONARIUS);
        discard.add(Card.FORUM);
        discard.add(Card.FORUM);
        playerHands[PLAYER_1].add(Card.TRIBUNUSPLEBIS);

        move.activateCardsDisc(5, Card.TRIBUNUSPLEBIS);
        assertDeck();
        assertDiscard();
        assertHands();

        move.activateMoneyDisc(5);

        playerSestertiis[PLAYER_1] += 5;
        assertSestertiis();

        move.placeCard(Card.TRIBUNUSPLEBIS,Rules.BRIBE_DISC);

        playerHands[PLAYER_1].remove(Card.TRIBUNUSPLEBIS);
        playerFields[PLAYER_1][6] = Card.TRIBUNUSPLEBIS;
        playerSestertiis[PLAYER_1] -= 5;
        assertSestertiis();

        move.endTurn();

        assert(gameState.getWhoseTurn() == PLAYER_2);

        playerVPs[PLAYER_2] -= 1;
        assertVPs();

        /*
			Start of Round Summary

			Victory Points:
				Player 1: 18
				Player 2: 6
				Pool:	  9

			Sestertii:
				Player 1: 1
				Player 2: 1

			Hands:
			    Player 1:        Player 2:
				Architectus


			Fields:
				     			 1       2              3            4         5         6             7
				Player 1:	<FORUM>, <BASILICA>  ,<VELITES> , <MERCATUS>,	<LEGAT>,<PRAETORIANUS>,<TRIBUNUSPLEBIS>
				Player 2:	<TURRIS>,<CENTURIO>,<TRIBUNUSPLEBIS>,<MACHINA>, <NOT_A_CARD>,<LEGIONARIUS>,<GLADIATOR>

         */

        gameState.setActionDice(new int[] {2,5,6});

        deck.remove(Card.MERCATUS);
        deck.remove(Card.CONSILIARIUS);
        deck.remove(Card.FORUM);
        deck.remove(Card.SICARIUS);
        deck.remove(Card.CENTURIO);
        deck.remove(Card.GLADIATOR);
        discard.add(Card.MERCATUS);
        discard.add(Card.FORUM);
        discard.add(Card.SICARIUS);
        discard.add(Card.CENTURIO);
        discard.add(Card.GLADIATOR);
        playerHands[PLAYER_2].add(Card.CONSILIARIUS);
        move.activateCardsDisc(5, Card.CONSILIARIUS);

        playerSestertiis[PLAYER_2] += 6;
        move.activateMoneyDisc(6);
        assertSestertiis();

        playerFields[PLAYER_2][6] = Card.CONSILIARIUS;
        playerHands[PLAYER_2].remove(Card.CONSILIARIUS);
        discard.add(Card.GLADIATOR);

        move.placeCard(Card.CONSILIARIUS, Rules.BRIBE_DISC);
        playerSestertiis[PLAYER_2] -= 4;

        assertFields();
        assertHands();
        assertDiscard();
        ConsiliariusActivator consiliarius = (ConsiliariusActivator) move.activateBribeDisc(2);

        playerFields[PLAYER_2][0] = Card.CENTURIO;
        consiliarius.placeCard(Card.CENTURIO, Rules.DICE_DISC_1);

        playerFields[PLAYER_2][5] = Card.NOT_A_CARD;
        playerFields[PLAYER_2][1] = Card.LEGIONARIUS;
        consiliarius.placeCard(Card.LEGIONARIUS, Rules.DICE_DISC_2);

        playerFields[PLAYER_2][2] = Card.CONSILIARIUS;
        consiliarius.placeCard(Card.CONSILIARIUS, Rules.DICE_DISC_3);

        playerFields[PLAYER_2][6] = Card.TRIBUNUSPLEBIS;
        consiliarius.placeCard(Card.TRIBUNUSPLEBIS, Rules.BRIBE_DISC);

        consiliarius.complete();
        playerSestertiis[PLAYER_2] -= 2;
        assertSestertiis();
        discard.add(Card.TURRIS);

        assertFields();
        assertDiscard();
        assertVPs();

        move.endTurn();

        /*
			Start of Round Summary

			Victory Points:
				Player 1: 18
				Player 2: 6
				Pool:	  12

			Sestertii:
				Player 1: 1
				Player 2: 1

			Hands:
			    Player 1:        Player 2:
				Architectus


			Fields:
				     			 1     	    2              3            4         5        		 6           	  7
				Player 1:	<FORUM>, 	<BASILICA>  ,	<VELITES> , <MERCATUS>,	<LEGAT>,	<PRAETORIANUS>,	<TRIBUNUSPLEBIS>
				Player 2:	<CENTURIO>,<LEGIONARIUS>,<CONSILIARIUS>,<MACHINA>, <NOT_A_CARD>,<NOT_A_CARD>,	<TRIBUNUSPLEBIS>

         */
        assert(gameState.getWhoseTurn() == PLAYER_1);
        gameState.setActionDice(new int[] {2,5,6});

        legat = (LegatActivator) move.chooseCardToActivate(Rules.DICE_DISC_5);
        legat.complete();

        playerVPs[PLAYER_1] += 2;
        assertVPs();

        move.activateMoneyDisc(6);
        playerSestertiis[PLAYER_1] += 6;

        tribunusPlebis = (TribunusPlebisActivator) move.activateBribeDisc(2);
        tribunusPlebis.complete();

        playerSestertiis[PLAYER_1] -= 2;
        playerVPs[PLAYER_1] += 1;
        playerVPs[PLAYER_2] -= 1;
        assertSestertiis();
        assertVPs();

        move.endTurn();

        playerVPs[PLAYER_2] -= 2;
        assertVPs();

        /*
			Start of Round Summary

			Victory Points:
				Player 1: 21
				Player 2: 5
				Pool:	  10

			Sestertii:
				Player 1: 5
				Player 2: 1

			Hands:
			    Player 1:        Player 2:
				Architectus


			Fields:
				     			 1     	    2              3            4         5        		 6           	  7
				Player 1:	<FORUM>, 	<BASILICA>  ,	<VELITES> , <MERCATUS>,	<LEGAT>,	<PRAETORIANUS>,	<TRIBUNUSPLEBIS>
				Player 2:	<CENTURIO>,<LEGIONARIUS>,<CONSILIARIUS>,<MACHINA>, <NOT_A_CARD>,<NOT_A_CARD>,	<TRIBUNUSPLEBIS>

         */

        assert(gameState.getWhoseTurn() == PLAYER_2);

        gameState.setActionDice(new int[] {1,3,6});

        centurio = (CenturioActivator)move.chooseCardToActivate(Rules.DICE_DISC_1);
        centurio.giveAttackDieRoll(1);
        centurio.chooseCenturioAddActionDie(true);
        centurio.chooseActionDice(6);
        centurio.complete();

        playerFields[PLAYER_1][0] = Card.NOT_A_CARD;
        assertFields();

        move.activateMoneyDisc(3);
        playerSestertiis[PLAYER_2] += 3;
        assertSestertiis();

        move.endTurn();

        playerVPs[PLAYER_1] -= 1;
        assertVPs();
        assert(gameState.getWhoseTurn() == PLAYER_1);

        /*
			Start of Round Summary

			Victory Points:
				Player 1: 20
				Player 2: 3
				Pool:	  13

			Sestertii:
				Player 1: 5
				Player 2: 4

			Hands:
			    Player 1:        Player 2:
				Architectus


			Fields:
				     			 1     	  	  2              3          4         5        		 6           	  7
				Player 1:	<NOT_A_CARD>, <BASILICA>  ,	<VELITES> , <MERCATUS>,	<LEGAT>,	<PRAETORIANUS>,	<TRIBUNUSPLEBIS>
				Player 2:	<CENTURIO>,	<LEGIONARIUS>,<CONSILIARIUS>,<MACHINA>, <NOT_A_CARD>,<NOT_A_CARD>,	<TRIBUNUSPLEBIS>

         */

        gameState.setActionDice(new int[]{2,3,4});

        deck.remove(Card.GLADIATOR);
        deck.remove(Card.TEMPLUM);
        deck.remove(Card.VELITES);
        deck.remove(Card.LEGAT);
        discard.add(Card.GLADIATOR);
        discard.add(Card.TEMPLUM);
        discard.add(Card.VELITES);
        playerHands[PLAYER_1].add(Card.LEGAT);
        move.activateCardsDisc(4, Card.LEGAT);
        playerHands[PLAYER_1].remove(Card.LEGAT);
        playerFields[PLAYER_1][1] = Card.LEGAT;
        playerSestertiis[PLAYER_1] -= 5;
        discard.add(Card.BASILICA);
        move.placeCard(Card.LEGAT, Rules.DICE_DISC_2);

        assertHands();
        assertFields();
        assertDiscard();
        assertSestertiis();

        playerVPs[PLAYER_1] += 2;
        legat = (LegatActivator) move.chooseCardToActivate(Rules.DICE_DISC_2);
        legat.complete();

        playerFields[PLAYER_2][2] = Card.NOT_A_CARD;
        discard.add(Card.CONSILIARIUS);

        velites = (VelitesActivator) move.chooseCardToActivate(Rules.DICE_DISC_3);
        velites.chooseDiceDisc(Rules.DICE_DISC_3);
        velites.giveAttackDieRoll(5);
        velites.complete();

        assertFields();
        assertDiscard();

        move.endTurn();

        assert(gameState.getPlayerVictoryPoints(PLAYER_2) == 0);
        assert(gameState.isGameCompleted());

    }

    private void initialiseDeck () {

        deck = new ArrayList<Card>();
        deck.add(Card.FORUM);
        deck.add(Card.MERCATUS);
        deck.add(Card.LEGAT);
        deck.add(Card.PRAETORIANUS);
        deck.add(Card.SCAENICUS);
        deck.add(Card.CENTURIO);
        deck.add(Card.TRIBUNUSPLEBIS);
        deck.add(Card.MACHINA);
        deck.add(Card.CONSUL);
        deck.add(Card.LEGIONARIUS); // #1
        deck.add(Card.NERO);
        deck.add(Card.ARCHITECTUS); // #2
        deck.add(Card.LEGIONARIUS);
        deck.add(Card.HARUSPEX);
        deck.add(Card.TURRIS);		// #3
        deck.add(Card.SCAENICUS);
        deck.add(Card.VELITES);
        deck.add(Card.SENATOR);		// #4
        deck.add(Card.ARCHITECTUS);
        deck.add(Card.FORUM);
        deck.add(Card.MACHINA);
        deck.add(Card.AESCULAPINUM);
        deck.add(Card.CONSUL);
        deck.add(Card.GLADIATOR);	// #5
        deck.add(Card.CONSILIARIUS);
        deck.add(Card.ONAGER);
        deck.add(Card.TURRIS);
        deck.add(Card.BASILICA);
        deck.add(Card.MERCATOR);
        deck.add(Card.AESCULAPINUM);// #6
        deck.add(Card.BASILICA);
        deck.add(Card.LEGIONARIUS);
        deck.add(Card.FORUM);
        deck.add(Card.TRIBUNUSPLEBIS);
        deck.add(Card.FORUM);		//#7
        deck.add(Card.MERCATUS);
        deck.add(Card.CONSILIARIUS);
        deck.add(Card.FORUM);
        deck.add(Card.SICARIUS);
        deck.add(Card.CENTURIO);	//#8
        deck.add(Card.GLADIATOR);
        deck.add(Card.TEMPLUM);
        deck.add(Card.VELITES);
        deck.add(Card.LEGAT);		//#9
        deck.add(Card.ESSEDUM);
        deck.add(Card.ONAGER);
        deck.add(Card.ESSEDUM);
        deck.add(Card.HARUSPEX);
        deck.add(Card.FORUM);
        deck.add(Card.TEMPLUM);
        deck.add(Card.PRAETORIANUS);
        deck.add(Card.SENATOR);

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
