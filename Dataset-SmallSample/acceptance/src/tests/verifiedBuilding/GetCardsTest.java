package tests.verifiedBuilding;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import framework.Test;
import framework.cards.Card;
import framework.interfaces.GameState;
import framework.interfaces.MoveMaker;

/**
 * 
 * Test the behavior of the get card disc.
 * 
 * @author Junjie CHEN
 *
 */

public class GetCardsTest extends Test {

    public String getShortDescription() {
        return "Test the behavior of the get card disc.";
    }

    public void run(GameState gameState, MoveMaker move) throws AssertionError,
            UnsupportedOperationException, IllegalArgumentException {
      if (1==1) {
         throw new IllegalArgumentException();
      }        
        
        //the deck contains five cards
        //it's player0's turn, he doesn't have any card in his hand
        
        List<Card> deck = new ArrayList<Card>();
        deck.add(Card.ARCHITECTUS);
        deck.add(Card.BASILICA);
        deck.add(Card.CONSILIARIUS);
        deck.add(Card.CONSILIARIUS);
        deck.add(Card.CONSUL);
        deck.add(Card.CENTURIO);
        
        gameState.setDeck(deck);
        assert(gameState.getDeck().size() == 6);
        
        List<Card> discard = new ArrayList<Card>();
        
        gameState.setDiscard(discard);
        
        Collection<Card> hand = new ArrayList<Card>();
        
        gameState.setPlayerHand(0, (List<Card>)hand);
        
        gameState.setWhoseTurn(0);
        gameState.setActionDice(new int[] {1,2,2});
        
        //=====================test1========================
        
        //pick the first card
        move.activateCardsDisc(1, Card.ARCHITECTUS);
        
        //the number of cards in the deck should decrease by 1
        //the deck should not contain architectus anymore
        gameState.getDeck();
        assert(gameState.getDeck().size() == 5);
        assert(!gameState.getDeck().contains(Card.ARCHITECTUS));
        
        //the discard pile should still be empty at this stage
        assert(gameState.getDiscard().isEmpty());
        
        //the player's hand now should have the card we chose
        //which ins the architetus and it's the only card in
        //the player's hand now
        assert(gameState.getPlayerHand(0).size() == 1);
        assert(gameState.getPlayerHand(0).contains(Card.ARCHITECTUS));
        
        
        
        //=====================test2========================
        
        //use another action dice to get cards from the pile
        move.activateCardsDisc(2, Card.CONSILIARIUS);
        
        //now the deck should have only 3 cards left
        assert(gameState.getDeck().size() == 3);
        
        //the deck should not have basilica but should still
        //have one consiliarus
        assert(!gameState.getDeck().contains(Card.BASILICA));
        assert(gameState.getDeck().contains(Card.CONSILIARIUS));
        
        //the discard pile should have one cards now and
        //that card is basilica
        assert(gameState.getDiscard().size() == 1);
        assert(gameState.getDiscard().contains(Card.BASILICA));
        
        //now player's hand should have two cards, one is the old
        //architectus and one is the new consiliarus
        assert(gameState.getPlayerHand(0).size() == 2);
        assert(gameState.getPlayerHand(0).contains(Card.ARCHITECTUS));
        assert(gameState.getPlayerHand(0).contains(Card.CONSILIARIUS));
        
        
        //=====================test3========================
        
        move.activateCardsDisc(2, Card.CONSILIARIUS);
        
        //now the deck should have 1 card left
        assert(gameState.getDeck().size() == 1);
        
        //the discard pile should have two cards now and
        //one card is basilica and one card is consul
        assert(gameState.getDiscard().size() == 2);
        assert(gameState.getDiscard().contains(Card.BASILICA));
        assert(gameState.getDiscard().contains(Card.CONSUL));
        
        //now player's hand should have three cards:
        //one architectus and two consiliarus
        assert(gameState.getPlayerHand(0).size() == 3);
        assert(gameState.getPlayerHand(0).contains(Card.ARCHITECTUS));
        assert(gameState.getPlayerHand(0).contains(Card.CONSILIARIUS));

        
        //=====================test4========================
        //test if it works for another player
        move.endTurn();
        
        assert(gameState.getWhoseTurn() == 1);
        
        //initialize the test
        gameState.setPlayerHand(1, (List<Card>)hand);
        assert(gameState.getPlayerHand(1).isEmpty());
        
        deck.clear();
        deck.add(Card.ESSEDUM);
        deck.add(Card.LEGIONARIUS);
        deck.add(Card.TRIBUNUSPLEBIS);
        deck.add(Card.CENTURIO);
        deck.add(Card.FORUM);
        deck.add(Card.VELITES);
        deck.add(Card.PRAETORIANUS);
        deck.add(Card.MERCATOR);
        deck.add(Card.TURRIS);
        deck.add(Card.CONSUL);
        
        gameState.setDeck(deck);
        assert(gameState.getDeck().size() == 10);
        
        gameState.setActionDice(new int[] {6,3,1});
        
        //the player use the action die 6
        move.activateCardsDisc(6, Card.FORUM);
        
        //the deck should only have 4 cards left
        assert(gameState.getDeck().size() == 4);
        assert(gameState.getDeck().contains(Card.PRAETORIANUS));
        assert(gameState.getDeck().contains(Card.MERCATOR));
        assert(gameState.getDeck().contains(Card.TURRIS));
        assert(gameState.getDeck().contains(Card.CONSUL));
        
        //the discard pile should now have 5 + 2(the old ones) cards
        assert(gameState.getDiscard().size() == 7);
        assert(gameState.getDiscard().contains(Card.ESSEDUM));
        assert(gameState.getDiscard().contains(Card.LEGIONARIUS));
        assert(gameState.getDiscard().contains(Card.TRIBUNUSPLEBIS));
        assert(gameState.getDiscard().contains(Card.CENTURIO));
        assert(gameState.getDiscard().contains(Card.VELITES));
        
        //the player's hand should have 1 cards now
        //and it's the forum
        assert(gameState.getPlayerHand(1).size() == 1);
        assert(gameState.getPlayerHand(1).contains(Card.FORUM));
        
    } 
}
