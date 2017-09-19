package tests.verifiedChar;

import framework.Rules;
import framework.Test;
import framework.cards.Card;
import framework.interfaces.GameState;
import framework.interfaces.MoveMaker;
import framework.interfaces.activators.ConsulActivator;
import framework.interfaces.activators.ScaenicusActivator;

/**
 * Created with IntelliJ IDEA.
 * @Author : Junjie CHEN
 * Date: 19/05/12
 * Time: 10:56 PM
 * To change this template use File | Settings | File Templates.
 */
public class ScaenicusInfiniteLoopTest extends Test {

    private final int PLAYER_1 = 0;
    private final int PLAYER_2 = 1;

    @Override
    public String getShortDescription() {
        return "Testing Scaenicus infinite loops";
    }

    @Override
    public void run(GameState gameState, MoveMaker move) throws AssertionError, UnsupportedOperationException, IllegalArgumentException {

        Card[][] playerFields = new Card[Rules.NUM_PLAYERS][Rules.NUM_DICE_DISCS];
        playerFields[PLAYER_1] = new Card[] {
                Card.SCAENICUS,
                Card.SCAENICUS,
                Card.SCAENICUS,
                Card.SCAENICUS,
                Card.CONSUL,
                Card.SCAENICUS,
                Card.SCAENICUS
        };

        playerFields[PLAYER_2] = new Card[] {
                Card.NOT_A_CARD,
                Card.NOT_A_CARD,
                Card.NOT_A_CARD,
                Card.NOT_A_CARD,
                Card.NOT_A_CARD,
                Card.NOT_A_CARD,
                Card.NOT_A_CARD
        };

        gameState.setWhoseTurn(PLAYER_1);
        gameState.setActionDice(new int[] {1,1,5});

        gameState.setPlayerCardsOnDiscs(PLAYER_1,playerFields[PLAYER_1]);
        gameState.setPlayerCardsOnDiscs(PLAYER_2,playerFields[PLAYER_2]);
        ScaenicusActivator ditto = (ScaenicusActivator) move.chooseCardToActivate(Rules.DICE_DISC_1);
        ScaenicusActivator ditto1 = (ScaenicusActivator) ditto.getScaenicusMimicTarget(Rules.DICE_DISC_2);
        ScaenicusActivator ditto2 = (ScaenicusActivator) ditto1.getScaenicusMimicTarget(Rules.DICE_DISC_1);
        ConsulActivator consul = (ConsulActivator) ditto2.getScaenicusMimicTarget(Rules.DICE_DISC_5);
        consul.chooseWhichDiceChanges(5);
        consul.chooseConsulChangeAmount(1);
        consul.complete();
        ditto2.complete();
        ditto1.complete();
        ditto.complete();

        boolean found = false;
        for (int i = 0; !found && i < gameState.getActionDice().length ; i++) {
            if (gameState.getActionDice()[i] == 6) {
                found = true;
            }
        }

        assert(found);

    }
}
