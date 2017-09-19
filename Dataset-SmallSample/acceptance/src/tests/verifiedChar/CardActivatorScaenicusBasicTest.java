package tests.verifiedChar;

import framework.Rules;
import framework.Test;
import framework.cards.Card;
import framework.interfaces.GameState;
import framework.interfaces.MoveMaker;
import framework.interfaces.activators.LegionariusActivator;
import framework.interfaces.activators.ScaenicusActivator;
import framework.interfaces.activators.SicariusActivator;
import framework.interfaces.activators.TribunusPlebisActivator;

/**
 * 
 * Test the functionality of Scaenicus
 * 
 * @author Karla Burnett
 *
 */

public class CardActivatorScaenicusBasicTest extends Test {

    @Override
    public String getShortDescription() {
        return "Test the basic functionality of Scaenicus";
    }

    @Override
    public void run(GameState gameState, MoveMaker move) throws AssertionError,
            UnsupportedOperationException, IllegalArgumentException {
        
        gameState.setWhoseTurn(0);
        
        // Set up the game state with the board as follows:
        // Tribunus       [1]                //
        // Scaenicus      [2] Legat          //
        //                [3]                //
        // Scaenicus      [4] Legat          //
        //                [5]                //
        // Sicarius       [6] Legat          //
        // Legionarius    [7]                //
        Card [] board = new Card[Rules.NUM_DICE_DISCS];
        for (int i = 0; i < Rules.NUM_DICE_DISCS; i++) {
           board[i] = Card.NOT_A_CARD;
        }
        
        board[1] = Card.LEGAT;
        board[3] = Card.LEGAT;
        board[5] = Card.LEGAT;
        gameState.setPlayerCardsOnDiscs(1, board);
        
        board[0] = Card.TRIBUNUSPLEBIS;
        board[1] = Card.SCAENICUS;
        board[3] = Card.SCAENICUS;
        board[5] = Card.SICARIUS;
        board[6] = Card.LEGIONARIUS;
        gameState.setPlayerCardsOnDiscs(0, board);


        // Initialise the dice
        gameState.setActionDice(new int[] {2, 2, 4});
        
        // ---- MIMICKING A CARD WITH NO POSITION SIGNIFICANCE ----
        // Namely Tribunus Plebis
        gameState.setPlayerVictoryPoints(0, 10);
        gameState.setPlayerVictoryPoints(1, 10);
        gameState.setPlayerSestertii(0, 0);
        gameState.setPlayerSestertii(1, 0);
        
        ScaenicusActivator scaenicusActivator = (ScaenicusActivator) move.chooseCardToActivate(2);
        TribunusPlebisActivator tribActivator = (TribunusPlebisActivator) scaenicusActivator.getScaenicusMimicTarget(1);
        tribActivator.complete();
        scaenicusActivator.complete();
        
        // Check that the correct number of victory points were gained
        assert(gameState.getPlayerVictoryPoints(0) == 11);
        assert(gameState.getPlayerVictoryPoints(1) == 9);
        assert(gameState.getPlayerSestertii(0) == 0);
        assert(gameState.getPlayerSestertii(1) == 0);
        
        // ---- MIMICKING A CARD WITH POSITION SIGNIFICANCE ----
        // Namely Legionarius
        
        scaenicusActivator = (ScaenicusActivator) move.chooseCardToActivate(2);
        LegionariusActivator legionActivator = (LegionariusActivator) scaenicusActivator.getScaenicusMimicTarget(7);
        legionActivator.giveAttackDieRoll(3);
        legionActivator.complete();
        scaenicusActivator.complete();
        
        // Check that the legat was destroyed, but that the Scaenicus and 
        // Legionarius are still there
        assert(gameState.getPlayerCardsOnDiscs(0)[1] == Card.SCAENICUS);
        assert(gameState.getPlayerCardsOnDiscs(0)[6] == Card.LEGIONARIUS);
        assert(gameState.getPlayerCardsOnDiscs(1)[1] == Card.NOT_A_CARD);
        
        // ---- MIMICKING ANOTHER SCAENICUS ----     
        scaenicusActivator = (ScaenicusActivator) move.chooseCardToActivate(4);
        ScaenicusActivator tempActivator = (ScaenicusActivator) scaenicusActivator.getScaenicusMimicTarget(2);
        legionActivator = (LegionariusActivator) tempActivator.getScaenicusMimicTarget(7);
        legionActivator.giveAttackDieRoll(3);
        legionActivator.complete();
        tempActivator.complete();
        scaenicusActivator.complete();
        
        // Check that the legat was destroyed, but that the Scaenicus and 
        // Legionarius are still there
        assert(gameState.getPlayerCardsOnDiscs(0)[1] == Card.SCAENICUS);
        assert(gameState.getPlayerCardsOnDiscs(0)[3] == Card.SCAENICUS);
        assert(gameState.getPlayerCardsOnDiscs(0)[6] == Card.LEGIONARIUS);
        assert(gameState.getPlayerCardsOnDiscs(1)[3] == Card.NOT_A_CARD);
        
        // ---- MOCKING A CARD THAT DESTROYS ITSELF ----
        // Namely Sicarius
        // Get some more action dice
        move.endTurn();
        move.endTurn();
        gameState.setActionDice(new int[] {2, 2, 4});
                
        scaenicusActivator = (ScaenicusActivator) move.chooseCardToActivate(2);
        SicariusActivator sicariusActivator = (SicariusActivator) scaenicusActivator.getScaenicusMimicTarget(6);
        sicariusActivator.chooseDiceDisc(Rules.DICE_DISC_6);
        sicariusActivator.complete();
        scaenicusActivator.complete();
        
        // Check that the scaenicus and legat were destroyed, but not the sicarius
        assert(gameState.getPlayerCardsOnDiscs(0)[1] == Card.NOT_A_CARD);
        assert(gameState.getPlayerCardsOnDiscs(0)[5] == Card.SICARIUS);
        assert(gameState.getPlayerCardsOnDiscs(1)[5] == Card.NOT_A_CARD);
        
   }
}
