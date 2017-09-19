package tests.verifiedBuilding;

import java.util.ArrayList;
import java.util.Collection;

import framework.Rules;
import framework.Test;
import framework.cards.Card;
import framework.interfaces.GameState;
import framework.interfaces.MoveMaker;
import framework.interfaces.activators.LegionariusActivator;
import framework.interfaces.activators.MachinaActivator;

public class CardActivatorMachinaBasicTest extends Test {

	/**
	 *
	 * Test the basic functionality of Machina
	 *
	 * @author Chris FONG
	 *
	 */
	
	private final int PLAYER_1 = 0;
	private final int PLAYER_2 = 1;

	private final int MACHINA_COST = 4;
	private final int MACHINA_DEFENCE = 4;
	
	@Override
	public String getShortDescription() {
		return "Testing the card Machina";
	}

	@Override
	public void run(GameState gameState, MoveMaker move) throws AssertionError,
			UnsupportedOperationException, IllegalArgumentException {
        if (1==1) {
            throw new IllegalArgumentException();
        }

		emptyFields(gameState);
		
		gameState.setPlayerCardsOnDiscs(PLAYER_1, 
						new Card[] {
								Card.FORUM,
								Card.FORUM,
								Card.MACHINA,
								Card.ONAGER,
								Card.ARCHITECTUS,
								Card.TRIBUNUSPLEBIS,
								Card.BASILICA			
						});
		
		gameState.setPlayerCardsOnDiscs(PLAYER_2, 
				new Card[] {
						Card.NOT_A_CARD,
						Card.NOT_A_CARD,
						Card.LEGIONARIUS,
						Card.NOT_A_CARD,
						Card.NOT_A_CARD,
						Card.NOT_A_CARD,
						Card.NOT_A_CARD			
				});
		
		gameState.setWhoseTurn(PLAYER_2);
		gameState.setActionDice(new int[] {3,3,3});
		
		assert(gameState.getPlayerCardsOnDiscs(PLAYER_1)[2] == Card.MACHINA);
		LegionariusActivator theEnemy = (LegionariusActivator)move.chooseCardToActivate(Rules.DICE_DISC_3);
		theEnemy.giveAttackDieRoll(MACHINA_DEFENCE-1);
		theEnemy.complete();
		assert(gameState.getPlayerCardsOnDiscs(PLAYER_1)[2] == Card.MACHINA);		
		theEnemy = (LegionariusActivator)move.chooseCardToActivate(Rules.DICE_DISC_3);
		theEnemy.giveAttackDieRoll(MACHINA_DEFENCE);
		theEnemy.complete();
		assert(gameState.getPlayerCardsOnDiscs(PLAYER_1)[2] == Card.NOT_A_CARD);
		
		move.endTurn();
		
		assert(gameState.getWhoseTurn() == PLAYER_1);
		
		gameState.setPlayerSestertii(PLAYER_1, MACHINA_COST);
		
		Collection<Card> hand = new ArrayList<Card>();
		hand.add(Card.MACHINA);
		gameState.setPlayerHand(PLAYER_1, hand);
	
		assert(gameState.getPlayerSestertii(PLAYER_1) == MACHINA_COST);
		move.placeCard(Card.MACHINA, Rules.DICE_DISC_3);
		assert(gameState.getPlayerSestertii(PLAYER_1) == 0);
		assert(gameState.getPlayerCardsOnDiscs(PLAYER_1)[2] == Card.MACHINA);
		
		gameState.setActionDice (new int[] {3,3,3});
		
		MachinaActivator theHero = (MachinaActivator) move.chooseCardToActivate(Rules.DICE_DISC_3);
		
		theHero.placeCard(Card.FORUM, Rules.DICE_DISC_1);
		theHero.placeCard(Card.MACHINA, Rules.DICE_DISC_2);
		theHero.placeCard(Card.BASILICA, Rules.DICE_DISC_3);
		theHero.placeCard(Card.FORUM, Rules.DICE_DISC_4);
		theHero.placeCard(Card.ONAGER, Rules.DICE_DISC_5);
		
		theHero.complete();
		
		assert(gameState.getPlayerCardsOnDiscs(PLAYER_1)[0] == Card.FORUM);
		assert(gameState.getPlayerCardsOnDiscs(PLAYER_1)[1] == Card.MACHINA);
		assert(gameState.getPlayerCardsOnDiscs(PLAYER_1)[2] == Card.BASILICA);
		assert(gameState.getPlayerCardsOnDiscs(PLAYER_1)[3] == Card.FORUM);
		assert(gameState.getPlayerCardsOnDiscs(PLAYER_1)[4] == Card.ONAGER);
		assert(gameState.getPlayerCardsOnDiscs(PLAYER_1)[5] == Card.TRIBUNUSPLEBIS);
		assert(gameState.getPlayerCardsOnDiscs(PLAYER_1)[6] == Card.NOT_A_CARD);
				
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
