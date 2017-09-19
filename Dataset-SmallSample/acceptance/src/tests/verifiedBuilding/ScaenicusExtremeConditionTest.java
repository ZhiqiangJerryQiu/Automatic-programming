package tests.verifiedBuilding;

import framework.Rules;
import framework.Test;
import framework.cards.Card;
import framework.interfaces.GameState;
import framework.interfaces.MoveMaker;
import framework.interfaces.activators.NeroActivator;
import framework.interfaces.activators.ScaenicusActivator;


/**
 * 
 * Test one extreme condition of Scaenicus
 * 
 * @author Frederick Zhang
 * 
 */
public class ScaenicusExtremeConditionTest extends Test {

	private static final int NERO = 6;
	private final int PLAYER_1 = 0;
	private final int PLAYER_2 = 1;
	private final int SCAENICUS_A = 1;
	private final int SCAENICUS_B = 2;
	@Override
	public String getShortDescription() {
		return "Test one extreme condition of Scaenicus";
	}

	@Override
	public void run(GameState gameState, MoveMaker move) throws AssertionError,
			UnsupportedOperationException, IllegalArgumentException {

		gameState.setWhoseTurn(0);
        gameState.setActionDice(new int[] {1, 2, 6});
	        // Set up the game state with the board as follows:
	        // Scaenicus      [1]                //
	        // Scaenicus      [2] Turris         //
	        //                [3]                //
	        //  		      [4] Forum          //
	        //                [5]                //
	        // Nero		      [6] 		         //
	        // 			      [7]                //
	        gameState.setPlayerCardsOnDiscs(PLAYER_1,
					new Card[] {
							Card.SCAENICUS,
							Card.SCAENICUS,
							Card.NOT_A_CARD,
							Card.NOT_A_CARD,
							Card.NOT_A_CARD,
							Card.NERO,
							Card.NOT_A_CARD
	        });
	        gameState.setPlayerCardsOnDiscs(PLAYER_2,
					new Card[] {
							Card.NOT_A_CARD,
							Card.TURRIS,
							Card.NOT_A_CARD,
							Card.FORUM,
							Card.NOT_A_CARD,
							Card.NOT_A_CARD,
							Card.NOT_A_CARD
	        });
		ScaenicusActivator scaenicusActivator = (ScaenicusActivator) move
				.chooseCardToActivate(SCAENICUS_A);
		ScaenicusActivator tempActivator = (ScaenicusActivator) scaenicusActivator
				.getScaenicusMimicTarget(SCAENICUS_B); // A should be able to
														// mimic B not one,
		ScaenicusActivator secondTempActivator = (ScaenicusActivator) tempActivator
				.getScaenicusMimicTarget(SCAENICUS_B); // not two,
		ScaenicusActivator thirdTempActivator = (ScaenicusActivator) secondTempActivator
				.getScaenicusMimicTarget(SCAENICUS_B); // but 3 times
		NeroActivator neroActivator = (NeroActivator) thirdTempActivator
				.getScaenicusMimicTarget(NERO);
		neroActivator.chooseDiceDisc(Rules.DICE_DISC_2);
		neroActivator.complete();
		thirdTempActivator.complete();
		secondTempActivator.complete();
		tempActivator.complete();
		scaenicusActivator.complete();
	        
	        // Discarded cards: SCAENICUS_A, Forum
	        // SCAENICUS_B, NERO shall be intact
	        assert(gameState.getPlayerCardsOnDiscs(PLAYER_1)[Rules.DICE_DISC_1-1] == Card.NOT_A_CARD);
	        assert(gameState.getPlayerCardsOnDiscs(PLAYER_1)[Rules.DICE_DISC_2-1] == Card.SCAENICUS);
	        assert(gameState.getPlayerCardsOnDiscs(PLAYER_1)[Rules.DICE_DISC_6-1] == Card.NERO);
	        assert(gameState.getPlayerCardsOnDiscs(PLAYER_2)[Rules.DICE_DISC_2-1] == Card.NOT_A_CARD);
	}

}
