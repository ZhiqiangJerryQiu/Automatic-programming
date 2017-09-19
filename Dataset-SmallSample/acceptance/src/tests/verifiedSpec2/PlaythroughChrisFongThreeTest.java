package tests.verifiedSpec2;

import framework.Rules;
import framework.Test;
import framework.cards.Card;
import framework.interfaces.GameState;
import framework.interfaces.MoveMaker;
import framework.interfaces.activators.*;

import java.util.*;

/**
 * @Author : Chris FONG
 *
 * All dice values randomly generated from http://www.random.org/dice/
 *
 */
public class PlaythroughChrisFongThreeTest extends Test {

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
        return "Playthrough of the Game";
    }

    public void run(GameState gameState, MoveMaker move) throws AssertionError,
            UnsupportedOperationException, IllegalArgumentException {

        this.gameState = gameState;
        gameState.setWhoseTurn(PLAYER_1);
        gameState.setActionDice(new int[] {6,1,3});
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
               Player 1         Player 2:
               TribunusPlebis   Haruspex
               Velites                  Forum
               Scaenicus                Mercatus
               Velites                  Praetorianus
               Machina                  Kat


           Place onto Fields:
                                1                     2               3                 4                       5                        6                      7
               Player 1:<TRIBUNUSPLEBIS>, <VELITES>, <VELITES>,    <NOT_A_CARD>,<MACHINA>,  <NOT_A_CARD>,<SCAENICUS>
               Player 2:<HARUSPEX>,      <MERCATUS>,<PRAETORIANUS>,    <KAT>,  <NOT_A_CARD>,<NOT_A_CARD>,<FORUM>
        */

        playerHands[PLAYER_1] = new ArrayList<Card>();
        playerHands[PLAYER_2] = new ArrayList<Card>();

        playerFields[PLAYER_1] =
                new Card[] {
                        Card.TRIBUNUSPLEBIS,
                        Card.VELITES,
                        Card.VELITES,
                        Card.NOT_A_CARD,
                        Card.MACHINA,
                        Card.NOT_A_CARD,
                        Card.SCAENICUS
                };

        playerFields[PLAYER_2] =
                new Card[] {
                        Card.HARUSPEX,
                        Card.MERCATUS,
                        Card.PRAETORIANUS,
                        Card.KAT,
                        Card.NOT_A_CARD,
                        Card.NOT_A_CARD,
                        Card.FORUM
                };

        gameState.setDeck(deck);

        transferHandsToState();
        transferFieldsToState();

        assertHands();
        assertFields();

        /*
            Starting Game with Action Dice {6,1,3}
         */

        playerVPs[PLAYER_1] += 1;
        playerVPs[PLAYER_2] -= 1;
        TribunusPlebisActivator tribunusPlebis = (TribunusPlebisActivator) move.chooseCardToActivate(Rules.DICE_DISC_1);
        tribunusPlebis.complete();
        assertVPs();

        VelitesActivator velites = (VelitesActivator)move.chooseCardToActivate(Rules.DICE_DISC_3);
        velites.chooseDiceDisc(Rules.DICE_DISC_4);
        velites.giveAttackDieRoll(1);
        velites.complete();
        assertFields();

        playerSestertiis[PLAYER_1] += 6;
        move.activateMoneyDisc(6);
        assertSestertiis();

        move.endTurn();

        /*
             Start of Round Summary

             Victory Points:
                 Player 1: 11
                 Player 2: 7
                 Pool:    18

             Sestertii:
                 Player 1: 6
                 Player 2: 0

             Hands:
                Player 1:        Player 2:


             Fields:
                                1                     2               3                 4                       5                        6                      7
               Player 1:<TRIBUNUSPLEBIS>, <VELITES>, <VELITES>,    <NOT_A_CARD>,<MACHINA>,  <NOT_A_CARD>,<SCAENICUS>
               Player 2:<HARUSPEX>,      <MERCATUS>,<PRAETORIANUS>,    <KAT>,  <NOT_A_CARD>,<NOT_A_CARD>,<FORUM>
        */

        playerVPs[PLAYER_2] -= 2;
        assertVPs();

        gameState.setActionDice(new int[] {4,5,4});

        playerSestertiis[PLAYER_2] += 4;
        move.activateMoneyDisc(4);
        assertSestertiis();

        playerSestertiis[PLAYER_2] -= 4;
        playerVPs[PLAYER_2] += 5;
        ForumActivator forum = (ForumActivator) move.activateBribeDisc(4);
        forum.chooseActionDice(5);
        forum.chooseActivateTemplum(false);
        forum.complete();
        assertSestertiis();
        assertVPs();

        move.endTurn();

        /*
             Start of Round Summary

             Victory Points:
                 Player 1: 9
                 Player 2: 12
                 Pool:     15

             Sestertii:
                 Player 1: 6
                 Player 2: 0

             Hands:
                Player 1:        Player 2:


             Fields:
                                1                     2               3                 4                       5                        6                      7
               Player 1:<TRIBUNUSPLEBIS>, <VELITES>, <VELITES>,    <NOT_A_CARD>,<MACHINA>,  <TEMPLUM>,<SCAENICUS>
               Player 2:<HARUSPEX>,      <MERCATUS>,<PRAETORIANUS>,    <KAT>,  <NOT_A_CARD>,<NOT_A_CARD>,<FORUM>
        */

        playerVPs[PLAYER_1] -= 2;
        assertVPs();

        gameState.setActionDice(new int[]{6,2,1});

        deck.remove(Card.TEMPLUM);
        deck.remove(Card.MACHINA);
        discard.addLast(Card.MACHINA);
        playerHands[PLAYER_1].add(Card.TEMPLUM);
        move.activateCardsDisc(2, Card.TEMPLUM);
        assertDeck();
        assertDiscard();
        assertHands();

        playerHands[PLAYER_1].remove(Card.TEMPLUM);
        playerFields[PLAYER_1][5] = Card.TEMPLUM;
        playerSestertiis[PLAYER_1] -= 2;
        move.placeCard(Card.TEMPLUM, Rules.DICE_DISC_6);
        assertHands();
        assertFields();
        assertSestertiis();

        playerVPs[PLAYER_1] += 1;
        playerVPs[PLAYER_2] -= 1;
        tribunusPlebis = (TribunusPlebisActivator) move.chooseCardToActivate(Rules.DICE_DISC_1);
        tribunusPlebis.complete();
        assertVPs();

        playerSestertiis[PLAYER_1] += 6;
        move.activateMoneyDisc(6);
        assertSestertiis();

        move.endTurn();

        /*
             Start of Round Summary

             Victory Points:
                 Player 1: 10
                 Player 2: 9
                 Pool:     17

             Sestertii:
                 Player 1: 10
                 Player 2: 0

             Hands:
                Player 1:        Player 2:


             Fields:
                                1                     2               3                 4                       5                        6                      7
               Player 1:<TRIBUNUSPLEBIS>, <VELITES>, <VELITES>,    <NOT_A_CARD>,<MACHINA>,  <TEMPLUM>, <SCAENICUS>
               Player 2:<HARUSPEX>,      <MERCATUS>,<PRAETORIANUS>,    <KAT>,  <NOT_A_CARD>,<NOT_A_CARD>,<FORUM>
        */

        playerVPs[PLAYER_2] -= 2;
        assertVPs();

        gameState.setActionDice(new int[] {5,1,4});

        deck.remove(Card.TRIBUNUSPLEBIS);
        playerHands[PLAYER_2].add(Card.TRIBUNUSPLEBIS);
        move.activateCardsDisc(1,Card.TRIBUNUSPLEBIS);
        assertDeck();
        assertHands();

        playerSestertiis[PLAYER_2] += 4;
        move.activateMoneyDisc(4);
        assertSestertiis();

        deck.remove(Card.SENATOR);
        deck.remove(Card.HARUSPEX);
        deck.remove(Card.GLADIATOR);
        deck.remove(Card.LEGIONARIUS);
        deck.remove(Card.LEGIONARIUS);
        discard.addLast(Card.HARUSPEX);
        discard.addLast(Card.GLADIATOR);
        discard.addLast(Card.LEGIONARIUS);
        discard.addLast(Card.LEGIONARIUS);
        playerHands[PLAYER_2].add(Card.SENATOR);
        move.activateCardsDisc(5,Card.SENATOR);

        playerSestertiis[PLAYER_2] -= 3;
        playerHands[PLAYER_2].remove(Card.SENATOR);
        playerFields[PLAYER_2][4] = Card.SENATOR;
        move.placeCard(Card.SENATOR,Rules.DICE_DISC_5);
        assertSestertiis();
        assertHands();
        assertFields();

        move.endTurn();

        /*
             Start of Round Summary

             Victory Points:
                 Player 1: 9
                 Player 2: 9
                 Pool:     18

             Sestertii:
                 Player 1: 10
                 Player 2: 1

             Hands:
                Player 1:        Player 2:
                                 TribunusPlebis

             Fields:
                                1                     2               3                 4                       5             6                 7
               Player 1:<TRIBUNUSPLEBIS>, <VELITES>, <VELITES>,    <NOT_A_CARD>,<MACHINA>,<TEMPLUM>, <SCAENICUS>
               Player 2:<HARUSPEX>,      <MERCATUS>,<PRAETORIANUS>,    <KAT>,  <SENATOR>,<NOT_A_CARD>,<FORUM>
        */

        playerVPs[PLAYER_1] -= 1;
        assertVPs();

        gameState.setActionDice(new int[]{3,5,3});

        deck.remove(Card.CENTURIO);
        deck.remove(Card.FORUM);
        deck.remove(Card.GRIMREAPER);
        discard.addLast(Card.CENTURIO);
        discard.addLast(Card.FORUM);
        playerHands[PLAYER_1].add(Card.GRIMREAPER);
        move.activateCardsDisc(3,Card.GRIMREAPER);
        assertDeck();
        assertDiscard();
        assertHands();

        deck.remove(Card.PRAETORIANUS);
        deck.remove(Card.ARCHITECTUS);
        deck.remove(Card.AESCULAPINUM);
        deck.remove(Card.FORUM);
        deck.remove(Card.LEGAT);
        discard.addLast(Card.PRAETORIANUS);
        discard.addLast(Card.ARCHITECTUS);
        discard.addLast(Card.AESCULAPINUM);
        discard.addLast(Card.LEGAT);
        playerHands[PLAYER_1].add(Card.FORUM);
        move.activateCardsDisc(5,Card.FORUM);
        assertDeck();
        assertDiscard();
        assertHands();

        playerSestertiis[PLAYER_1] -= 6;
        playerFields[PLAYER_1][3] = Card.GRIMREAPER;
        playerHands[PLAYER_1].remove(Card.GRIMREAPER);
        move.placeCard(Card.GRIMREAPER, 4);
        assertSestertiis();
        assertFields();

        playerVPs[PLAYER_1] += 1;
        playerVPs[PLAYER_2] -= 1;
        playerSestertiis[PLAYER_1] -= 3;
        ScaenicusActivator scaenicus = (ScaenicusActivator) move.activateBribeDisc(3);
        tribunusPlebis = (TribunusPlebisActivator) scaenicus.getScaenicusMimicTarget(1);
        tribunusPlebis.complete();
        scaenicus.complete();
        assertSestertiis();
        assertVPs();

        move.endTurn();

        /*
             Start of Round Summary

             Victory Points:
                 Player 1: 10
                 Player 2: 7
                 Pool:     19

             Sestertii:
                 Player 1: 1
                 Player 2: 1

             Hands:
                Player 1:        Player 2:
                Forum            TribunusPlebis

             Fields:
                                1                     2               3                 4                       5             6                 7
               Player 1:<TRIBUNUSPLEBIS>, <VELITES>, <VELITES>,     <GRIMREAPER>,<MACHINA>,<TEMPLUM>, <SCAENICUS>
               Player 2:<HARUSPEX>,      <MERCATUS>,<PRAETORIANUS>,    <KAT>,  <SENATOR>,<NOT_A_CARD>,<FORUM>
        */

        playerVPs[PLAYER_2] -= 1;
        assertVPs();

        gameState.setActionDice(new int[]{4,3,4});

        deck.add(Card.KAT);
        deck.add(Card.FORUM);
        deck.add(Card.LEGIONARIUS);
        discard.remove(Card.KAT);
        discard.remove(Card.FORUM);
        playerHands[PLAYER_2].add(Card.LEGIONARIUS);
        move.activateCardsDisc(3, Card.LEGIONARIUS);
        assertHands();
        assertFields();
        assertDiscard();

        playerSestertiis[PLAYER_2] += 4;
        move.activateMoneyDisc(4);
        assertSestertiis();

        playerHands[PLAYER_2].remove(Card.LEGIONARIUS);
        playerFields[PLAYER_2][3] = Card.LEGIONARIUS;
        discard.addLast(Card.KAT);
        playerSestertiis[PLAYER_2] -= 4;
        move.placeCard(Card.LEGIONARIUS, Rules.DICE_DISC_4);
        assertHands();
        assertFields();
        assertDiscard();
        assertSestertiis();

        playerFields[PLAYER_1][3] = Card.NOT_A_CARD;
        discard.addLast(Card.GRIMREAPER);
        LegionariusActivator legionarius = (LegionariusActivator)move.chooseCardToActivate(Rules.DICE_DISC_4);
        legionarius.giveAttackDieRoll(6);
        legionarius.complete();
        assertFields();
        assertDiscard();

        move.endTurn();

        /*
             Start of Round Summary

             Victory Points:
                 Player 1: 9
                 Player 2: 7
                 Pool:     20

             Sestertii:
                 Player 1: 1
                 Player 2: 1

             Hands:
                Player 1:        Player 2:
                                 TribunusPlebis

             Fields:
                                1                     2               3                  4                      5             6                 7
               Player 1:<TRIBUNUSPLEBIS>, <VELITES>, <VELITES>,     <NOT_A_CARD>,<MACHINA>,<TEMPLUM>,<SCAENICUS>
               Player 2:<HARUSPEX>,      <MERCATUS>,<PRAETORIANUS>,<LEGIONARIUS>,<SENATOR>,<NOT_A_CARD>,<FORUM>
        */

        playerVPs[PLAYER_1] -= 1;
        assertVPs();

        gameState.setActionDice(new int[] {6,2,4});

        playerSestertiis[PLAYER_1] += 6;
        move.activateMoneyDisc(6);
        assertSestertiis();

        playerHands[PLAYER_1].remove(Card.FORUM);
        discard.addLast(Card.SCAENICUS);
        playerFields[PLAYER_1][6] = Card.FORUM;
        playerSestertiis[PLAYER_1] -= 5;
        move.placeCard(Card.FORUM,Rules.BRIBE_DISC);
        assertFields();
        assertHands();
        assertDeck();
        assertSestertiis();

        playerVPs[PLAYER_1] += 4;
        playerSestertiis[PLAYER_1] -= 2;
        forum = (ForumActivator) move.activateBribeDisc(2);
        forum.chooseActionDice(4);
        forum.chooseActivateTemplum(false);
        forum.complete();
        assertVPs();
        assertSestertiis();

        move.endTurn();

        /*
             Start of Round Summary

             Victory Points:
                 Player 1: 13
                 Player 2: 6
                 Pool:     17

             Sestertii:
                 Player 1: 0
                 Player 2: 1

             Hands:
                Player 1:        Player 2:
                                 TribunusPlebis

             Fields:
                                1                     2               3                 4                       5             6                 7
               Player 1:<TRIBUNUSPLEBIS>, <VELITES>, <VELITES>,    <NOT_A_CARD>,<MACHINA>,<TEMPLUM>,<FORUM>
               Player 2:<HARUSPEX>,      <MERCATUS>,<PRAETORIANUS>,<LEGIONARIUS>,<SENATOR>,<NOT_A_CARD>,<FORUM>
        */

        playerVPs[PLAYER_2] -= 1;
        assertVPs();

        gameState.setActionDice(new int[] {2,1,6});

        playerVPs[PLAYER_2] += 1;
        playerVPs[PLAYER_1] -= 1;
        MercatusActivator mercatus = (MercatusActivator) move.chooseCardToActivate(2);
        mercatus.complete();
        assertVPs();

        deck.remove(Card.FORUM);
        deck.remove(Card.SICARIUS);
        deck.remove(Card.BASILICA);
        deck.remove(Card.LEGAT);
        deck.remove(Card.TURRIS);
        deck.remove(Card.TURRIS);
        discard.addLast(Card.FORUM);
        discard.addLast(Card.BASILICA);
        discard.addLast(Card.LEGAT);
        discard.addLast(Card.TURRIS);
        discard.addLast(Card.TURRIS);
        playerHands[PLAYER_2].add(Card.SICARIUS);
        move.activateCardsDisc(6,Card.SICARIUS);
        assertDeck();
        assertDiscard();
        assertHands();

        playerSestertiis[PLAYER_2] += 1;
        move.activateMoneyDisc(1);
        assertSestertiis();

        move.endTurn();

        /*
             Start of Round Summary

             Victory Points:
                 Player 1: 11
                 Player 2: 7
                 Pool:     17

             Sestertii:
                 Player 1: 0
                 Player 2: 2

             Hands:
                Player 1:        Player 2:
                                 TribunusPlebis
                                 Sicarius

             Fields:
                                1                     2               3                 4                       5             6                 7
               Player 1:<TRIBUNUSPLEBIS>, <VELITES>, <VELITES>,    <NOT_A_CARD>,<MACHINA>,<TEMPLUM>,<FORUM>
               Player 2:<HARUSPEX>,      <MERCATUS>,<PRAETORIANUS>,<LEGIONARIUS>,<SENATOR>,<NOT_A_CARD>,<FORUM>
        */

        playerVPs[PLAYER_1] -= 1;
        assertVPs();

        gameState.setActionDice(new int[] {5,3,3});

        playerFields[PLAYER_1] =
                new Card[] {
                        Card.TEMPLUM,
                        Card.FORUM,
                        Card.VELITES,
                        Card.NOT_A_CARD,
                        Card.MACHINA,
                        Card.NOT_A_CARD,
                        Card.NOT_A_CARD
                };

        discard.addLast(Card.TRIBUNUSPLEBIS);
        discard.addLast(Card.VELITES);

        MachinaActivator machina = (MachinaActivator) move.chooseCardToActivate(5);
        machina.placeCard(Card.FORUM, 2);
        machina.placeCard(Card.TEMPLUM, 1);
        machina.placeCard(Card.MACHINA, 5);
        machina.complete();
        assertFields();
        assertDiscard();

        deck.remove(Card.FORUM);
        deck.remove(Card.MERCATUS);
        deck.remove(Card.ESSEDUM);
        discard.addLast(Card.FORUM);
        discard.addLast(Card.MERCATUS);
        playerHands[PLAYER_1].add(Card.ESSEDUM);
        move.activateCardsDisc(3, Card.ESSEDUM);
        assertDeck();
        assertDiscard();
        assertHands();

        playerSestertiis[PLAYER_1] += 3;
        move.activateMoneyDisc(3);
        assertSestertiis();

        move.endTurn();

        /*
             Start of Round Summary

             Victory Points:
                 Player 1: 11
                 Player 2: 6
                 Pool:     19

             Sestertii:
                 Player 1: 3
                 Player 2: 2

             Hands:
                Player 1:        Player 2:
                Essedum          TribunusPlebis
                                 Sicarius

             Fields:
                            1             2       3             4           5           6              7
               Player 1:<TEMPLUM>, <FORUM>,    <VELITES>,   <NOT_A_CARD>,<MACHINA>,<NOT_A_CARD>,<NOT_A_CARD>
               Player 2:<HARUSPEX>,<MERCATUS>,<PRAETORIANUS>,<LEGIONARIUS>,<SENATOR>,<NOT_A_CARD>,<FORUM>
        */

        playerVPs[PLAYER_2] -= 1;
        assertVPs();

        gameState.setActionDice(new int[] {1,2,5});

        playerFields[PLAYER_2][0] = Card.SICARIUS;
        playerFields[PLAYER_2][5] = Card.TRIBUNUSPLEBIS;
        playerHands[PLAYER_2].remove(Card.SICARIUS);
        playerHands[PLAYER_2].remove(Card.TRIBUNUSPLEBIS);
        discard.addLast(Card.HARUSPEX);

        SenatorActivator senator = (SenatorActivator) move.chooseCardToActivate(Rules.DICE_DISC_5);
        senator.layCard(Card.TRIBUNUSPLEBIS, Rules.DICE_DISC_6);
        senator.layCard(Card.SICARIUS, Rules.DICE_DISC_1);
        senator.complete();
        assertFields();
        assertSestertiis();
        assertDiscard();

        discard.addLast(Card.SICARIUS);
        discard.addLast(Card.VELITES);
        playerFields[PLAYER_1][2] = Card.NOT_A_CARD;
        playerFields[PLAYER_2][0] = Card.NOT_A_CARD;
        SicariusActivator sicarius = (SicariusActivator)move.chooseCardToActivate(1);
        sicarius.chooseDiceDisc(Rules.DICE_DISC_3);
        sicarius.complete();
        assertDiscard();
        assertFields();

        playerVPs[PLAYER_2] += 1;
        playerVPs[PLAYER_1] -= 1;
        mercatus = (MercatusActivator) move.chooseCardToActivate(Rules.DICE_DISC_2);
        mercatus.complete();
        assertVPs();

        move.endTurn();

        /*
             Start of Round Summary

             Victory Points:
                 Player 1: 6
                 Player 2: 7
                 Pool:     25

             Sestertii:
                 Player 1: 3
                 Player 2: 2

             Hands:
                Player 1:        Player 2:
                Essedum

             Fields:
                            1           2            3                4           5           6              7
               Player 1:<TEMPLUM>,   <FORUM>,   <NOT_A_CARD>,   <NOT_A_CARD>,<MACHINA>,<NOT_A_CARD>,<NOT_A_CARD>
               Player 2:<NOT_A_CARD>,<MERCATUS>,<PRAETORIANUS>,<LEGIONARIUS>,<SENATOR>,<TRIBUNUSPLEBIS>,<FORUM>
        */

        playerVPs[PLAYER_1] -= 4;
        assertVPs();

        gameState.setActionDice(new int[] {4,6,1});

        deck.remove(Card.CONSILIARIUS);
        deck.remove(Card.TELEPHONEBOX);
        deck.remove(Card.GLADIATOR);
        deck.remove(Card.ONAGER);
        discard.addLast(Card.CONSILIARIUS);
        discard.addLast(Card.TELEPHONEBOX);
        discard.addLast(Card.GLADIATOR);
        playerHands[PLAYER_1].add(Card.ONAGER);

        move.activateCardsDisc(4, Card.ONAGER);
        assertDeck();
        assertDiscard();
        assertHands();

        playerSestertiis[PLAYER_1] += 6;
        move.activateMoneyDisc(6);
        assertSestertiis();

        playerSestertiis[PLAYER_1] -= 5;
        playerFields[PLAYER_1][6] = Card.ONAGER;
        playerHands[PLAYER_1].remove(Card.ONAGER);
        move.placeCard(Card.ONAGER,Rules.BRIBE_DISC);
        assertSestertiis();
        assertFields();

        playerSestertiis[PLAYER_1] -= 1;
        playerFields[PLAYER_2][6] = Card.NOT_A_CARD;
        discard.addLast(Card.FORUM);
        OnagerActivator onager = (OnagerActivator) move.activateBribeDisc(1);
        onager.giveAttackDieRoll(5);
        onager.chooseDiceDisc(Rules.BRIBE_DISC);
        onager.complete();
        assertSestertiis();
        assertFields();
        assertDiscard();

        move.endTurn();

        /*
             Start of Round Summary

             Victory Points:
                 Player 1: 6
                 Player 2: 5
                 Pool:     25

             Sestertii:
                 Player 1: 3
                 Player 2: 2

             Hands:
                Player 1:        Player 2:
                Essedum

             Fields:
                            1           2            3                4           5           6              7
               Player 1:<TEMPLUM>,   <FORUM>,   <NOT_A_CARD>,   <NOT_A_CARD>,<MACHINA>,<NOT_A_CARD>,   <ONAGER>
               Player 2:<NOT_A_CARD>,<MERCATUS>,<PRAETORIANUS>,<LEGIONARIUS>,<SENATOR>,<TRIBUNUSPLEBIS>,<NOT_A_CARD>
        */

        playerVPs[PLAYER_2] -= 2;
        assertVPs();

        gameState.setActionDice(new int[]{6,2,6});

        deck.remove(Card.AESCULAPINUM);
        deck.remove(Card.ESSEDUM);
        deck.remove(Card.NERO);
        deck.remove(Card.SCAENICUS);
        deck.remove(Card.ONAGER);
        deck.remove(Card.CENTURIO);
        discard.add(Card.AESCULAPINUM);
        discard.add(Card.ESSEDUM);
        discard.add(Card.SCAENICUS);
        discard.add(Card.ONAGER);
        discard.add(Card.CENTURIO);
        playerHands[PLAYER_2].add(Card.NERO);

        move.activateCardsDisc(6,Card.NERO);
        assertHands();
        assertDiscard();
        assertDeck();

        deck.remove(Card.MERCATOR);
        deck.remove(Card.BASILICA);
        deck.remove(Card.CONSUL);
        deck.remove(Card.SENATOR);
        deck.remove(Card.CONSILIARIUS);
        deck.remove(Card.CONSUL);

        discard.add(Card.MERCATOR);
        discard.add(Card.BASILICA);
        discard.add(Card.CONSUL);
        discard.add(Card.SENATOR);
        discard.add(Card.CONSILIARIUS);

        playerHands[PLAYER_2].add(Card.CONSUL);

        move.activateCardsDisc(6,Card.CONSUL);

        assertHands();
        assertDiscard();
        assertDeck();

        deck.remove(Card.TEMPLUM);
        deck.remove(Card.ARCHITECTUS);
        discard.add(Card.ARCHITECTUS);
        playerHands[PLAYER_2].add(Card.TEMPLUM);

        //Out of Cards, shuffle discard into Deck

        deck = discard;
        Collections.shuffle(deck);
        initialiseDiscard();

        move.activateCardsDisc(2,Card.TEMPLUM);

        assertHands();
        assertDiscard();
        assertDeck();

        move.endTurn();

        /*
             Start of Round Summary

             Victory Points:
                 Player 1: 3
                 Player 2: 5
                 Pool:     28

             Sestertii:
                 Player 1: 3
                 Player 2: 2

             Hands:
                Player 1:        Player 2:
                Essedum          Nero
                                 Consul
                                 Templum

             Fields:
                            1           2            3                4           5           6              7
               Player 1:<TEMPLUM>,   <FORUM>,   <NOT_A_CARD>,   <NOT_A_CARD>,<MACHINA>,<NOT_A_CARD>,   <ONAGER>
               Player 2:<NOT_A_CARD>,<MERCATUS>,<PRAETORIANUS>,<LEGIONARIUS>,<SENATOR>,<TRIBUNUSPLEBIS>,<NOT_A_CARD>
        */

        playerVPs[PLAYER_1] -= 3;
        assertVPs();

        gameState.setActionDice(new int[] {2,1,1});

        playerVPs[PLAYER_1] += 2;
        forum = (ForumActivator) move.chooseCardToActivate(Rules.DICE_DISC_2);
        forum.chooseActionDice(1);
        forum.chooseActivateTemplum(true);
        forum.chooseActivateTemplum(1);
        forum.complete();
        assertVPs();

        move.endTurn();

        /*
             Start of Round Summary

             Victory Points:
                 Player 1: 5
                 Player 2: 3
                 Pool:     28

             Sestertii:
                 Player 1: 3
                 Player 2: 2

             Hands:
                Player 1:        Player 2:
                Essedum          Nero
                                 Consul
                                 Templum

             Fields:
                            1           2            3                4           5           6              7
               Player 1:<TEMPLUM>,   <FORUM>,   <NOT_A_CARD>,   <NOT_A_CARD>,<MACHINA>,<NOT_A_CARD>,   <ONAGER>
               Player 2:<NOT_A_CARD>,<MERCATUS>,<PRAETORIANUS>,<LEGIONARIUS>,<SENATOR>,<TRIBUNUSPLEBIS>,<NOT_A_CARD>
        */

        playerVPs[PLAYER_2] -= 2;
        assertVPs();

        gameState.setActionDice(new int[]{2,6,3});

        playerVPs[PLAYER_1] -= 1;
        playerVPs[PLAYER_2] += 1;
        mercatus = (MercatusActivator) move.chooseCardToActivate(Rules.DICE_DISC_2);
        mercatus.complete();
        assertVPs();

        playerVPs[PLAYER_1] -= 1;
        playerVPs[PLAYER_2] += 1;
        tribunusPlebis = (TribunusPlebisActivator) move.chooseCardToActivate(Rules.DICE_DISC_6);
        tribunusPlebis.complete();
        assertVPs();

        PraetorianusActivator praetorianus = (PraetorianusActivator) move.chooseCardToActivate(Rules.DICE_DISC_3);
        praetorianus.chooseDiceDisc(2);
        praetorianus.complete();

        move.endTurn();

        playerVPs[PLAYER_1] -= 3;
        assertVPs();

        assert(gameState.isGameCompleted());

        /*
            End of Game Summary

             Victory Points:
                 Player 1: 0
                 Player 2: 5
                 Pool:     31

             Sestertii:
                 Player 1: 3
                 Player 2: 2

             Hands:
                Player 1:        Player 2:
                Essedum          Nero
                                 Consul
                                 Templum

             Fields:
                            1           2            3                4           5           6              7
               Player 1:<TEMPLUM>,   <FORUM>,   <NOT_A_CARD>,   <NOT_A_CARD>,<MACHINA>,<NOT_A_CARD>,   <ONAGER>
               Player 2:<NOT_A_CARD>,<MERCATUS>,<PRAETORIANUS>,<LEGIONARIUS>,<SENATOR>,<TRIBUNUSPLEBIS>,<NOT_A_CARD>
        */

    }

    private void initialiseDeck () {

        deck = new ArrayList<Card>();
        deck.add(Card.TRIBUNUSPLEBIS);
        deck.add(Card.VELITES);
        deck.add(Card.SCAENICUS);
        deck.add(Card.VELITES);
        deck.add(Card.MACHINA);
        deck.add(Card.HARUSPEX);
        deck.add(Card.FORUM);
        deck.add(Card.MERCATUS);
        deck.add(Card.PRAETORIANUS);
        deck.add(Card.KAT);            //#1
        deck.add(Card.MACHINA);
        deck.add(Card.TEMPLUM);        //#2
        deck.add(Card.TRIBUNUSPLEBIS); //#3
        deck.add(Card.SENATOR);
        deck.add(Card.HARUSPEX);
        deck.add(Card.GLADIATOR);
        deck.add(Card.LEGIONARIUS);
        deck.add(Card.LEGIONARIUS);    //#4
        deck.add(Card.CENTURIO);
        deck.add(Card.FORUM);
        deck.add(Card.GRIMREAPER);     //#5
        deck.add(Card.PRAETORIANUS);
        deck.add(Card.ARCHITECTUS);
        deck.add(Card.AESCULAPINUM);
        deck.add(Card.FORUM);
        deck.add(Card.LEGAT);          //#6
        deck.add(Card.KAT);
        deck.add(Card.FORUM);
        deck.add(Card.LEGIONARIUS);    //#7
        deck.add(Card.FORUM);
        deck.add(Card.SICARIUS);
        deck.add(Card.BASILICA);
        deck.add(Card.LEGAT);
        deck.add(Card.TURRIS);
        deck.add(Card.TURRIS);         //#8
        deck.add(Card.FORUM);
        deck.add(Card.MERCATUS);
        deck.add(Card.ESSEDUM);        //#9
        deck.add(Card.CONSILIARIUS);
        deck.add(Card.TELEPHONEBOX);
        deck.add(Card.GLADIATOR);
        deck.add(Card.ONAGER);         //#10
        deck.add(Card.AESCULAPINUM);
        deck.add(Card.ESSEDUM);
        deck.add(Card.NERO);
        deck.add(Card.SCAENICUS);
        deck.add(Card.ONAGER);
        deck.add(Card.CENTURIO);       //#11
        deck.add(Card.MERCATOR);
        deck.add(Card.BASILICA);
        deck.add(Card.CONSUL);
        deck.add(Card.SENATOR);
        deck.add(Card.CONSILIARIUS);
        deck.add(Card.CONSUL);         //#12
        deck.add(Card.TEMPLUM);
        deck.add(Card.ARCHITECTUS);    //#13

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
