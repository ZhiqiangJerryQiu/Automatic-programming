package tests.verifiedChar;

import framework.Rules;
import framework.Test;
import framework.cards.Card;
import framework.interfaces.GameState;
import framework.interfaces.MoveMaker;
import framework.interfaces.activators.ConsiliariusActivator;

/**
 * 
 * Test the card Consiliarius
 * 
 * @author Chris FONG
 *
 */

public class CardActivatorConsiliariusBasicTest extends Test {

    private final int PLAYER_1 = 0;

    @Override
    public String getShortDescription() {
        return "Test the card Consiliarius";
    }

    @Override
    public void run(GameState gameState, MoveMaker move) throws AssertionError,
            UnsupportedOperationException, IllegalArgumentException {
        emptyFields(gameState);

        /*

            Initialise to be Player 1's turn with no Sessteriis to play cards

         */

        gameState.setWhoseTurn(PLAYER_1);
        gameState.setPlayerSestertii(PLAYER_1, 0);

        /*
            Initialise field to

                  1            2             3           4            5             6           7
            <CENTURIO>,<LEGIONARIUS>,<CONSILIARIUS>,<TURRIS>,<HARUSPEX>,<MACHINA>,<NOT_A_CARD>

         */

        gameState.setPlayerCardsOnDiscs(PLAYER_1,
                new Card[] {
                        Card.CENTURIO,
                        Card.LEGIONARIUS,
                        Card.CONSILIARIUS,
                        Card.TURRIS,
                        Card.HARUSPEX,
                        Card.MACHINA,
                        Card.NOT_A_CARD
                });

        /*

            Activate Consiliarius

         */
        gameState.setActionDice(new int[]{3,3,3});
        ConsiliariusActivator theHero = (ConsiliariusActivator) move.chooseCardToActivate(Rules.DICE_DISC_3);
        
        /*
        Check Player 1's field is

              1            2             3           4        5          6          7
        <NOT_A_CARD>,<NOT_A_CARD>,<NOT_A_CARD>,<TURRIS>,<NOT_A_CARD>,<MACHINA>,<NOT_A_CARD>

         */

        theHero.placeCard(Card.CONSILIARIUS, Rules.DICE_DISC_2);
        theHero.placeCard(Card.CENTURIO, Rules.DICE_DISC_3);
        theHero.placeCard(Card.HARUSPEX, Rules.DICE_DISC_4);
        theHero.placeCard(Card.LEGIONARIUS, Rules.DICE_DISC_5);
        Card[] result = new Card[] {
                Card.NOT_A_CARD,
                Card.CONSILIARIUS,
                Card.CENTURIO,
                Card.HARUSPEX,
                Card.LEGIONARIUS,
                Card.MACHINA,
                Card.NOT_A_CARD
        };
        theHero.complete();

        for (int i = 0; i < Rules.NUM_DICE_DISCS ; i++) {
            assert(gameState.getPlayerCardsOnDiscs(PLAYER_1)[i] == result[i]);
        }
        
        /*
           Check Player 1's field is

                 1            2             3           4        5          6          7
           <NOT_A_CARD>,<CONSILIARIUS>,<CENTURIO>,<HARUSPEX>,<LEGIONARIUS>,<MACHINA>,<NOT_A_CARD>

        */

        result = new Card[] {
                Card.NOT_A_CARD,
                Card.CONSILIARIUS,
                Card.CENTURIO,
                Card.HARUSPEX,
                Card.LEGIONARIUS,
                Card.MACHINA,
                Card.NOT_A_CARD
        };

        for (int i = 0; i < Rules.NUM_DICE_DISCS ; i++) {
            assert(gameState.getPlayerCardsOnDiscs(PLAYER_1)[i] == result[i]);
        }

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
