package tests.verifiedSpec2;

import framework.Rules;
import framework.Test;
import framework.cards.Card;
import framework.interfaces.GameState;
import framework.interfaces.MoveMaker;
import framework.interfaces.activators.*;
import java.util.ArrayList;
import java.util.Collection;

/*
 * DROPDATABASEPLAYTROUGH - A game scenario by...
 *
 * @author Geoffrey Chen (geoffrey.chen) @author Michael Zhou (michael.zhou)
 */
public class DropdatabasePlaythroughTest extends Test {
    //consts aka. non-magicnumbers

    private final int PLAYER_ONE = 0;
    private final int PLAYER_TWO = 1;

    @Override
    public String getShortDescription() {
        return "A playthrough brought to you by Dropdatabase";
    }

    @Override
    public void run(GameState gameState, MoveMaker move) throws AssertionError,
            UnsupportedOperationException, IllegalArgumentException {
        // begin test...

        // set up player one
        gameState.setPlayerVictoryPoints(PLAYER_ONE, 12);
        gameState.setPlayerSestertii(PLAYER_ONE, 29);

        Collection<Card> handP1 = new ArrayList<Card>();
        handP1.add(Card.KAT);
        handP1.add(Card.FORUM);
        handP1.add(Card.AESCULAPINUM);
        handP1.add(Card.TRIBUNUSPLEBIS);
        handP1.add(Card.CENTURIO);
        gameState.setPlayerHand(0, handP1);

        // set up player two
        gameState.setPlayerVictoryPoints(PLAYER_TWO, 17);
        gameState.setPlayerSestertii(PLAYER_TWO, 24);

        Collection<Card> handP2 = new ArrayList<Card>();
        handP2.add(Card.FORUM);             // player two got the whole set!
        handP2.add(Card.TEMPLUM);           // lucky man
        handP2.add(Card.BASILICA);
        handP2.add(Card.ONAGER);
        handP2.add(Card.GLADIATOR);
        gameState.setPlayerHand(1, handP2);

        //check if VP placed correctly
        assert gameState.getPlayerVictoryPoints(0) == 12;
        assert gameState.getPlayerVictoryPoints(1) == 17;

        // set intial game state
        gameState.setWhoseTurn(0);
        deckUp(gameState);

        // check deck size
        assert gameState.getDeck().size() == 6;

        // check if hands are correct
        Collection<Card> check = gameState.getPlayerHand(PLAYER_ONE);

        assert check.contains(Card.KAT);
        assert check.contains(Card.FORUM);
        assert check.contains(Card.AESCULAPINUM);
        assert check.contains(Card.TRIBUNUSPLEBIS);
        assert check.contains(Card.CENTURIO);

        check.clear();
        check = gameState.getPlayerHand(PLAYER_TWO);

        assert check.contains(Card.FORUM);
        assert check.contains(Card.TEMPLUM);
        assert check.contains(Card.BASILICA);
        assert check.contains(Card.ONAGER);
        assert check.contains(Card.GLADIATOR);

        // Make boards empty.
        gameState.setPlayerCardsOnDiscs(PLAYER_ONE, new Card[]{
                    Card.NOT_A_CARD,
                    Card.NOT_A_CARD,
                    Card.NOT_A_CARD,
                    Card.NOT_A_CARD,
                    Card.NOT_A_CARD,
                    Card.NOT_A_CARD,
                    Card.NOT_A_CARD,});

        gameState.setPlayerCardsOnDiscs(PLAYER_TWO, new Card[]{
                    Card.NOT_A_CARD,
                    Card.NOT_A_CARD,
                    Card.NOT_A_CARD,
                    Card.NOT_A_CARD,
                    Card.NOT_A_CARD,
                    Card.NOT_A_CARD,
                    Card.NOT_A_CARD,});

        // place cards - player one
        move.placeCard(Card.KAT, Rules.DICE_DISC_1);
        move.placeCard(Card.FORUM, Rules.DICE_DISC_2);
        move.placeCard(Card.AESCULAPINUM, Rules.DICE_DISC_3);
        move.placeCard(Card.TRIBUNUSPLEBIS, Rules.DICE_DISC_4);
        move.placeCard(Card.CENTURIO, Rules.DICE_DISC_5);

        // place cards - player two
        // note that basilica does not lie next to forum
        move.endTurn();
        int i = gameState.getWhoseTurn();
        move.placeCard(Card.FORUM, Rules.DICE_DISC_1);
        move.placeCard(Card.TEMPLUM, Rules.DICE_DISC_2);
        move.placeCard(Card.BASILICA, Rules.DICE_DISC_3);
        move.placeCard(Card.ONAGER, Rules.DICE_DISC_4);
        move.placeCard(Card.GLADIATOR, Rules.DICE_DISC_5);

        // check card positions
        assert gameState.getPlayerCardsOnDiscs(PLAYER_ONE)[0] == Card.KAT;
        assert gameState.getPlayerCardsOnDiscs(PLAYER_ONE)[1] == Card.FORUM;
        assert gameState.getPlayerCardsOnDiscs(PLAYER_ONE)[2] == Card.AESCULAPINUM;
        assert gameState.getPlayerCardsOnDiscs(PLAYER_ONE)[3] == Card.TRIBUNUSPLEBIS;
        assert gameState.getPlayerCardsOnDiscs(PLAYER_ONE)[4] == Card.CENTURIO;
        assert gameState.getPlayerCardsOnDiscs(PLAYER_ONE)[5] == Card.NOT_A_CARD;
        assert gameState.getPlayerCardsOnDiscs(PLAYER_ONE)[6] == Card.NOT_A_CARD;

        //hands should also be empty
        assert gameState.getPlayerHand(PLAYER_ONE).isEmpty();
        assert gameState.getPlayerHand(PLAYER_TWO).isEmpty();

        assert gameState.getPlayerCardsOnDiscs(PLAYER_TWO)[0] == Card.FORUM;
        assert gameState.getPlayerCardsOnDiscs(PLAYER_TWO)[1] == Card.TEMPLUM;
        assert gameState.getPlayerCardsOnDiscs(PLAYER_TWO)[2] == Card.BASILICA;
        assert gameState.getPlayerCardsOnDiscs(PLAYER_TWO)[3] == Card.ONAGER;
        assert gameState.getPlayerCardsOnDiscs(PLAYER_TWO)[4] == Card.GLADIATOR;
        assert gameState.getPlayerCardsOnDiscs(PLAYER_TWO)[5] == Card.NOT_A_CARD;
        assert gameState.getPlayerCardsOnDiscs(PLAYER_TWO)[6] == Card.NOT_A_CARD;

        // check if all money is deducted (doesn't happen in the real game, but
        // done as a part of the playthrough
        assert gameState.getPlayerSestertii(PLAYER_ONE) == 0;
        assert gameState.getPlayerSestertii(PLAYER_TWO) == 0;

        // back to player one - set die
        move.endTurn();
        gameState.setActionDice(new int[]{1, 5, 4});

        //player one draws a card from the deck
        move.activateCardsDisc(1, Card.KAT);

        //check if player has received another KAT
        check.clear();
        check = gameState.getPlayerHand(PLAYER_ONE);
        assert check.contains(Card.KAT);

        //check if deck is smaller
        assert gameState.getDeck().size() == 5;

        // "I needed money 'cos I got none"
        // Place second dice on money disc
        move.activateMoneyDisc(5);
        assert gameState.getPlayerSestertii(PLAYER_ONE) == 5;

        // "Robbin' people with a six gun"
        // Take a VP
        move.chooseCardToActivate(4).complete();
        assert gameState.getPlayerVictoryPoints(PLAYER_ONE) == 11;
        assert gameState.getPlayerVictoryPoints(PLAYER_TWO) == 9;

        // player one ends turn
        move.endTurn();

        // check if next player's turn
        assert gameState.getWhoseTurn() == PLAYER_TWO;

        // roll the dice once again
        gameState.setActionDice(new int[]{1, 4, 5});

        // check dice rolling function
        int diceCheck[] = gameState.getActionDice();
        assert gameState.getActionDice()[0] == 1;
        assert gameState.getActionDice()[1] == 4;
        assert gameState.getActionDice()[2] == 5;
        // activate forum
        ForumActivator forumA = (ForumActivator) move.chooseCardToActivate(Rules.DICE_DISC_1);
        forumA.chooseActionDice(5);
        forumA.complete();

        // check if VP added correctly
        assert gameState.getPlayerVictoryPoints(PLAYER_TWO) == 12;

        // activate onager
        OnagerActivator onagerA = (OnagerActivator) move.chooseCardToActivate(4);
        onagerA.chooseDiceDisc(2);
        onagerA.giveAttackDieRoll(1);
        onagerA.complete();

        //Aww attack failed - VGTA
        //Player one's forum should be alive
        assert gameState.getPlayerCardsOnDiscs(PLAYER_ONE)[0] == Card.KAT;
        assert gameState.getPlayerCardsOnDiscs(PLAYER_ONE)[1] == Card.FORUM;
        assert gameState.getPlayerCardsOnDiscs(PLAYER_ONE)[2] == Card.AESCULAPINUM;
        assert gameState.getPlayerCardsOnDiscs(PLAYER_ONE)[3] == Card.TRIBUNUSPLEBIS;
        assert gameState.getPlayerCardsOnDiscs(PLAYER_ONE)[4] == Card.CENTURIO;
        assert gameState.getPlayerCardsOnDiscs(PLAYER_ONE)[5] == Card.NOT_A_CARD;
        assert gameState.getPlayerCardsOnDiscs(PLAYER_ONE)[6] == Card.NOT_A_CARD;

        // all dice discs used, end turn
        move.endTurn();

        // VP spot check, please blow into the breathalyzer...
        assert gameState.getPlayerVictoryPoints(PLAYER_ONE) == 9;
        assert gameState.getPlayerVictoryPoints(PLAYER_TWO) == 12;

        // it should also be player one's turn
        assert gameState.getWhoseTurn() == 0;

        // roll the dice
        gameState.setActionDice(new int[]{5, 4, 3});

        // activate CENTURIO
        // attacking forum without adding a battle die
        CenturioActivator centurioA = (CenturioActivator) move.chooseCardToActivate(Rules.DICE_DISC_5);
        centurioA.giveAttackDieRoll(6);
        centurioA.chooseCenturioAddActionDie(false);
        centurioA.complete();

        // FORUM is now gone
        assert gameState.getPlayerCardsOnDiscs(PLAYER_TWO)[0] == Card.FORUM;
        assert gameState.getPlayerCardsOnDiscs(PLAYER_TWO)[1] == Card.TEMPLUM;
        assert gameState.getPlayerCardsOnDiscs(PLAYER_TWO)[2] == Card.BASILICA;
        assert gameState.getPlayerCardsOnDiscs(PLAYER_TWO)[3] == Card.ONAGER;
        assert gameState.getPlayerCardsOnDiscs(PLAYER_TWO)[4] == Card.NOT_A_CARD;
        assert gameState.getPlayerCardsOnDiscs(PLAYER_TWO)[5] == Card.NOT_A_CARD;
        assert gameState.getPlayerCardsOnDiscs(PLAYER_TWO)[6] == Card.NOT_A_CARD;

        // lay down KAT - kat covers kat and is destroyed
        move.placeCard(Card.KAT, Rules.DICE_DISC_1);
        assert gameState.getPlayerCardsOnDiscs(PLAYER_ONE)[0] == Card.KAT;
        assert gameState.getDiscard().contains(Card.KAT);

        // rez zombified KATelyn stark using HARUSPEX
        AesculapinumActivator aesculA = (AesculapinumActivator) move.chooseCardToActivate(3);
        aesculA.chooseCardFromPile(gameState.getDiscard().lastIndexOf(Card.KAT));
        aesculA.complete();

        // should be in hand + not in the graveyard
        assert gameState.getPlayerHand(PLAYER_ONE).contains(Card.KAT);
        assert !gameState.getDiscard().contains(Card.KAT);

        // money should be deducted
        assert gameState.getPlayerSestertii(PLAYER_ONE) == 0;

        // end turn
        move.endTurn();

        // check second player VP - should be 12 - 3 (as gladiator was destroyed)
        assert gameState.getPlayerVictoryPoints(PLAYER_TWO) == 9;
        assert gameState.getWhoseTurn() == PLAYER_TWO;

        // rtd
        gameState.setActionDice(new int[]{2, 4, 6});

        // draw two cards and grab some money - lods of emone
        move.activateCardsDisc(2, Card.BASILICA);
        move.activateMoneyDisc(6);

        // check deck is correct size 6 - 1 - 2;
        assert gameState.getDeck().size() == 3;

        // check graveyard for AESCULAPINUM
        // Re: Reynard Edrick Wiradinata - AESCULAPINUM is discarded when you
        // draw two cards as you picked BASILICA
        assert gameState.getDiscard().contains(Card.AESCULAPINUM);

        // should be in hand
        assert gameState.getPlayerHand(PLAYER_TWO).contains(Card.BASILICA);

        // you have enough money, play it over forum
        move.placeCard(Card.BASILICA, 2);

        // check money should be 6 - 6
        assert gameState.getPlayerSestertii(PLAYER_TWO) == 0;

        // last dice is used to activate onager to destroy AESCULAPINUM
        // I can't spell AESCULAPINUM
        onagerA = (OnagerActivator) move.chooseCardToActivate(4);
        onagerA.chooseDiceDisc(3);
        onagerA.giveAttackDieRoll(6);
        onagerA.complete();

        // check board for player two and discard pile
        assert gameState.getPlayerCardsOnDiscs(PLAYER_TWO)[0] == Card.FORUM;
        assert gameState.getPlayerCardsOnDiscs(PLAYER_TWO)[1] == Card.BASILICA;
        assert gameState.getPlayerCardsOnDiscs(PLAYER_TWO)[2] == Card.BASILICA;
        assert gameState.getPlayerCardsOnDiscs(PLAYER_TWO)[3] == Card.ONAGER;
        assert gameState.getPlayerCardsOnDiscs(PLAYER_TWO)[4] == Card.NOT_A_CARD;
        assert gameState.getPlayerCardsOnDiscs(PLAYER_TWO)[5] == Card.NOT_A_CARD;
        assert gameState.getPlayerCardsOnDiscs(PLAYER_TWO)[6] == Card.NOT_A_CARD;
        assert gameState.getDiscard().contains(Card.TEMPLUM);

        // check player one's board for deaths
        assert gameState.getPlayerCardsOnDiscs(PLAYER_ONE)[0] == Card.KAT;
        assert gameState.getPlayerCardsOnDiscs(PLAYER_ONE)[1] == Card.FORUM;
        assert gameState.getPlayerCardsOnDiscs(PLAYER_ONE)[2] == Card.NOT_A_CARD;
        assert gameState.getPlayerCardsOnDiscs(PLAYER_ONE)[3] == Card.TRIBUNUSPLEBIS;
        assert gameState.getPlayerCardsOnDiscs(PLAYER_ONE)[4] == Card.CENTURIO;
        assert gameState.getPlayerCardsOnDiscs(PLAYER_ONE)[5] == Card.NOT_A_CARD;
        assert gameState.getPlayerCardsOnDiscs(PLAYER_ONE)[6] == Card.NOT_A_CARD;

        // all dice discs used, end turn
        move.endTurn();

        // player one's vp should be 9 - 3
        assert gameState.getPlayerVictoryPoints(PLAYER_ONE) == 6;
        assert gameState.getPlayerVictoryPoints(PLAYER_TWO) == 9;

        // it should also be player one's turn
        assert gameState.getWhoseTurn() == PLAYER_ONE;

        // roll the dice
        gameState.setActionDice(new int[]{1, 3, 1});

        // Bad roll - VGS!
        // grab money
        move.activateMoneyDisc(1);
        move.activateMoneyDisc(3);
        move.activateMoneyDisc(1);
        assert gameState.getPlayerSestertii(PLAYER_ONE) == 5;

        // end turn
        move.endTurn();

        // player two's VP should be 9 - 3
        assert gameState.getPlayerVictoryPoints(PLAYER_ONE) == 6;
        assert gameState.getPlayerVictoryPoints(PLAYER_TWO) == 6;
        assert gameState.getWhoseTurn() == PLAYER_TWO;

        // roll the dice
        gameState.setActionDice(new int[]{3, 1, 4});

        // draw 3 cards and gain 4 money
        move.activateCardsDisc(3, Card.LEGIONARIUS);
        move.activateMoneyDisc(4);

        // place LEGIONARIUS pointing at KAT
        move.placeCard(Card.LEGIONARIUS, 1);

        // unleash LEGIONARIUS on KAT, KAT should live (but with 8 lives)
        LegionariusActivator legionA = (LegionariusActivator) move.chooseCardToActivate(1);
        legionA.giveAttackDieRoll(2);
        legionA.complete();

        // Check field
        assert gameState.getPlayerCardsOnDiscs(PLAYER_ONE)[0] == Card.KAT;
        assert gameState.getPlayerCardsOnDiscs(PLAYER_ONE)[1] == Card.FORUM;
        assert gameState.getPlayerCardsOnDiscs(PLAYER_ONE)[2] == Card.NOT_A_CARD;
        assert gameState.getPlayerCardsOnDiscs(PLAYER_ONE)[3] == Card.TRIBUNUSPLEBIS;
        assert gameState.getPlayerCardsOnDiscs(PLAYER_ONE)[4] == Card.CENTURIO;
        assert gameState.getPlayerCardsOnDiscs(PLAYER_ONE)[5] == Card.NOT_A_CARD;
        assert gameState.getPlayerCardsOnDiscs(PLAYER_ONE)[6] == Card.NOT_A_CARD;

        assert gameState.getPlayerCardsOnDiscs(PLAYER_TWO)[0] == Card.LEGIONARIUS;
        assert gameState.getPlayerCardsOnDiscs(PLAYER_TWO)[1] == Card.BASILICA;
        assert gameState.getPlayerCardsOnDiscs(PLAYER_TWO)[2] == Card.BASILICA;
        assert gameState.getPlayerCardsOnDiscs(PLAYER_TWO)[3] == Card.ONAGER;
        assert gameState.getPlayerCardsOnDiscs(PLAYER_TWO)[4] == Card.NOT_A_CARD;
        assert gameState.getPlayerCardsOnDiscs(PLAYER_TWO)[5] == Card.NOT_A_CARD;
        assert gameState.getPlayerCardsOnDiscs(PLAYER_TWO)[6] == Card.NOT_A_CARD;
        
        // Re Oswyn: Removed because there are absolutely no instructions on
        // how an empty deck is handled (but we're not going to touch the deck
        // or the discard for the rest of the game).
        //assert gameState.getDiscard().contains(Card.TEMPLUM);

        // Money should be deducted to 0 (rolled 4 used 4)
        assert gameState.getPlayerSestertii(PLAYER_TWO) == 0;

        // end turn blah blah
        move.endTurn();

        // player one's VP should be 6 - 3
        assert gameState.getPlayerVictoryPoints(PLAYER_ONE) == 3;
        assert gameState.getPlayerVictoryPoints(PLAYER_TWO) == 6;
        assert gameState.getWhoseTurn() == PLAYER_ONE;

        // roll the dice
        gameState.setActionDice(new int[]{4, 2, 1});

        // activate TRIBUNIS PLEBIS
        move.chooseCardToActivate(4).complete();

        // activate FORUM
        forumA = (ForumActivator) move.chooseCardToActivate(2);
        forumA.chooseActionDice(1);
        forumA.chooseActivateTemplum(false);
        forumA.complete();

        // gained 2 extra VP
        assert gameState.getPlayerVictoryPoints(PLAYER_ONE) == 5;

        // end turn
        move.endTurn();

        // player two's VP should be 6 - 3 - 1 (from TRIBUNIS PLEBIS)
        assert gameState.getPlayerVictoryPoints(PLAYER_TWO) == 2;
        assert gameState.getWhoseTurn() == PLAYER_TWO;

        // roll the dice
        gameState.setActionDice(new int[]{1, 1, 1});

        //attack KAT three times
        legionA = (LegionariusActivator) move.chooseCardToActivate(1);
        legionA.giveAttackDieRoll(5);
        legionA.complete();

        legionA = (LegionariusActivator) move.chooseCardToActivate(1);
        legionA.giveAttackDieRoll(5);
        legionA.complete();

        legionA = (LegionariusActivator) move.chooseCardToActivate(1);
        legionA.giveAttackDieRoll(5);
        legionA.complete();

        // KAT still should be there
        assert gameState.getPlayerCardsOnDiscs(PLAYER_ONE)[0] == Card.KAT;

        // end turn
        move.endTurn();
        assert gameState.getPlayerVictoryPoints(PLAYER_ONE) == 2;
        assert gameState.getPlayerVictoryPoints(PLAYER_TWO) == 2;
        assert gameState.getWhoseTurn() == PLAYER_ONE;

        // player one should lose next round, do nothing
        move.endTurn();

        // player two won - you're winner!
        assert gameState.isGameCompleted();
    }

    // set up deck
    private void deckUp(GameState gameState) {
        ArrayList<Card> deck = new ArrayList();

        // there are 52 +  cards in the deck
        // but we are going to work with a subset
        deck.add(Card.KAT);
        deck.add(Card.AESCULAPINUM);
        deck.add(Card.BASILICA);
        deck.add(Card.MACHINA);
        deck.add(Card.CENTURIO);
        deck.add(Card.LEGIONARIUS);

        gameState.setDeck(deck);
    }
}
