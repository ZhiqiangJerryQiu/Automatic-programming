package tests.verifiedChar;

import java.util.Collection;
import java.util.LinkedList;

import framework.Rules;
import framework.Test;
import framework.cards.Card;
import framework.interfaces.GameState;
import framework.interfaces.MoveMaker;
import framework.interfaces.activators.CenturioActivator;
import framework.interfaces.activators.LegionariusActivator;

/**
 * Created with IntelliJ IDEA.
 * User: chrisfong
 * Date: 9/05/12
 * Time: 11:54 AM
 * To change this template use File | Settings | File Templates.
 */
public class CardActivatorCenturioBasicTest extends Test {

    private final int PLAYER_1 = 0;
    private final int PLAYER_2 = 1;

    @Override
    public String getShortDescription() {
        return "Test the the card Centurio";
    }

    @Override
    public void run(GameState gameState, MoveMaker move) throws AssertionError,
            UnsupportedOperationException, IllegalArgumentException {
        if (1==1) {
            throw new IllegalArgumentException();
        }
        /*
            Defaults both Players' fields to:

                  1           2           3               4           5           6           7
            <NOT_A_CARD>,<NOT_A_CARD>,<NOT_A_CARD>,<NOT_A_CARD>,<NOT_A_CARD>,<NOT_A_CARD>,<NOT_A_CARD>,

        */

        emptyFields(gameState);

        /*
            Set both players to:
                *VP - 10
                *Sesterii - 10
         */

        for (int i = 0; i < Rules.NUM_PLAYERS; i++) {
            gameState.setPlayerSestertii(i, 10);
            gameState.setPlayerVictoryPoints(i, 10);
        }

        /*
             Set Player 1's field to:

                      1            2             3           4            5             6           7
                <LEGIONARIUS>,<NOT_A_CARD>,<NOT_A_CARD>,<NOT_A_CARD>,<NOT_A_CARD>,<NOT_A_CARD>,<NOT_A_CARD>

         */

        gameState.setPlayerCardsOnDiscs(PLAYER_1,
                    new Card[] {
                            Card.LEGIONARIUS,
                            Card.NOT_A_CARD,
                            Card.NOT_A_CARD,
                            Card.NOT_A_CARD,
                            Card.NOT_A_CARD,
                            Card.NOT_A_CARD,
                            Card.NOT_A_CARD
                    });


        /*
            Set Player 2's field to:

                     1            2             3           4            5             6           7
               <CENTURIO>,<NOT_A_CARD>,<NOT_A_CARD>,<NOT_A_CARD>,<NOT_A_CARD>,<NOT_A_CARD>,<NOT_A_CARD>

        */


        gameState.setPlayerCardsOnDiscs(PLAYER_2,
                new Card[] {
                        Card.CENTURIO,
                        Card.NOT_A_CARD,
                        Card.NOT_A_CARD,
                        Card.NOT_A_CARD,
                        Card.NOT_A_CARD,
                        Card.NOT_A_CARD,
                        Card.NOT_A_CARD
                });

        /*
            Attack the Centurio with Legionarius

            Tests:
                * Centurio has 5 defence
                    * Legionarius cannot kill Centurio with a 4 roll
                    * Legionarius can kill Centurio with a 5 roll
         */

        gameState.setWhoseTurn(PLAYER_1);
        gameState.setActionDice(new int[] {1,1,6});

        assert(gameState.getPlayerCardsOnDiscs(PLAYER_2)[0] == Card.CENTURIO);

        LegionariusActivator theEnemy = (LegionariusActivator) move.chooseCardToActivate(Rules.DICE_DISC_1);
        theEnemy.giveAttackDieRoll(4);
        theEnemy.complete();

        assert(gameState.getPlayerCardsOnDiscs(PLAYER_2)[0] == Card.CENTURIO);

        theEnemy = (LegionariusActivator) move.chooseCardToActivate(Rules.DICE_DISC_1);
        theEnemy.giveAttackDieRoll(5);
        theEnemy.complete();

        assert(gameState.getPlayerCardsOnDiscs(PLAYER_2)[0] == Card.NOT_A_CARD);

        move.endTurn();

        /*
           Sets the Action die available to {1 ,3, 4}
           Give Player 2 a Centurio card into his/her hand
        */

        Collection<Card> player2Hand = new LinkedList<Card>();
        player2Hand.add(Card.CENTURIO);

        gameState.setPlayerHand(PLAYER_2,player2Hand);

        /*

            Player 2 plays the Centurio into DICE_DISC_1:

                 1           2           3               4           5           6           7
            <CENTURIO>,<NOT_A_CARD>,<NOT_A_CARD>,<NOT_A_CARD>,<NOT_A_CARD>,<NOT_A_CARD>,<NOT_A_CARD>,

            Tests:
                * Centurio costs 9 sesterii to play

         */

        assert(gameState.getPlayerCardsOnDiscs(PLAYER_2)[0] == Card.NOT_A_CARD);
        assert(gameState.getPlayerSestertii(PLAYER_2) == 10);

        move.placeCard(Card.CENTURIO,Rules.DICE_DISC_1);

        assert(gameState.getPlayerCardsOnDiscs(PLAYER_2)[0] == Card.CENTURIO);
        assert(gameState.getPlayerSestertii(PLAYER_2) == 1);

        /*

            Player 2 attacks Legionarius with the Centurio
                * Centurio attacks Legionarius with 1 + 4 = 5 [ Should kill ]
                * Centurio attacks NOT_A_CARD [Crash testing]

         */

        gameState.setActionDice(new int[] {1,1,3});

        /*

            Tests:
               * Centurio attacks Legionarius with 4 + 0 = 4 [ Shouldn't kill ]

        */

        CenturioActivator theHero = (CenturioActivator) move.chooseCardToActivate(Rules.DICE_DISC_1);

        assert(gameState.getPlayerCardsOnDiscs(PLAYER_1)[0] == Card.LEGIONARIUS);

        theHero.giveAttackDieRoll(4);
        theHero.chooseCenturioAddActionDie(false);
        theHero.complete();

        assert(gameState.getPlayerCardsOnDiscs(PLAYER_1)[0] == Card.LEGIONARIUS);

        /*

            Tests:
               * Centurio attacks Legionarius with 5 + 0 = 5 [ Should kill ]

        */

        theHero = (CenturioActivator) move.chooseCardToActivate(Rules.DICE_DISC_1);
        
        theHero.giveAttackDieRoll(5);
        theHero.chooseCenturioAddActionDie(false);
        theHero.complete();

        assert(gameState.getPlayerCardsOnDiscs(PLAYER_1)[0] == Card.NOT_A_CARD);

        /*

            Restore Player 1's field to:

                    1            2             3           4            5             6           7
                <LEGIONARIUS>,<NOT_A_CARD>,<NOT_A_CARD>,<NOT_A_CARD>,<NOT_A_CARD>,<NOT_A_CARD>,<NOT_A_CARD>

         */


        gameState.setPlayerCardsOnDiscs(PLAYER_1,
                new Card[] {
                     Card.LEGIONARIUS,
                     Card.NOT_A_CARD,
                     Card.NOT_A_CARD,
                     Card.NOT_A_CARD,
                     Card.NOT_A_CARD,
                     Card.NOT_A_CARD,
                     Card.NOT_A_CARD
                 });


        /*

            Tests:
               * Centurio attacks Legionarius with 1 + 3 = 4 [ Shouldn't kill ]

        */
        
        gameState.setActionDice(new int[] {1,1,3});
        
        theHero = (CenturioActivator) move.chooseCardToActivate(Rules.DICE_DISC_1);

        theHero.giveAttackDieRoll(1);
        theHero.chooseActionDice(3);
        theHero.chooseCenturioAddActionDie(true);
        theHero.complete();

        assert(gameState.getPlayerCardsOnDiscs(PLAYER_1)[0] == Card.LEGIONARIUS);

        /*

            Tests:
               * Centurio attacks Legionarius with 1 + 4 = 5 [ Should kill ]

        */

        gameState.setActionDice(new int[] {1,1,4});

        theHero = (CenturioActivator) move.chooseCardToActivate(Rules.DICE_DISC_1);
        
        theHero.giveAttackDieRoll(1);
        theHero.chooseActionDice(4);
        theHero.chooseCenturioAddActionDie(true);
        theHero.complete();

//        /*
//
//            Tests:
//                * Centurio attacks NOT_A_CARD [ Testing crashes ]
//
//         */
//
//        assert(gameState.getPlayerCardsOnDiscs(PLAYER_1)[0] == Card.NOT_A_CARD);
//
//        theHero = (CenturioActivator) move.chooseCardToActivate(Rules.DICE_DISC_1);
//        
//        theHero.giveAttackDieRoll(1);
//        theHero.chooseCenturioAddActionDie(false);
//        theHero.complete();
//
//        assert(gameState.getPlayerCardsOnDiscs(PLAYER_1)[0] == Card.NOT_A_CARD);

    }

    private void emptyFields(GameState gameState) {
        Card[] emptyField = new Card[Rules.NUM_DICE_DISCS];

        for (int i = 0 ; i < Rules.NUM_DICE_DISCS; i++) {
            emptyField[i] = Card.NOT_A_CARD;
        }

        for (int i = 0 ; i < Rules.NUM_PLAYERS; i++) {
            gameState.setPlayerCardsOnDiscs(i,emptyField);
        }

    }


}
