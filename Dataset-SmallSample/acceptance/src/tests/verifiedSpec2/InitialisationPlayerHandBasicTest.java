package tests.verifiedSpec2;

import java.util.LinkedList;
import java.util.List;

import framework.Rules;
import framework.Test;
import framework.cards.Card;
import framework.interfaces.GameState;
import framework.interfaces.MoveMaker;
/**
 * Testing the basic mechanics of victory point addition and removal.
 * @author Damon (Stacey damon.stacey)
 */
public class InitialisationPlayerHandBasicTest extends Test {

   @Override
   public String getShortDescription() {
      return "Checking all players hands can be populated..";
   }

   @Override
   public void run(GameState gameState, MoveMaker move)
                                          throws AssertionError,
                                          UnsupportedOperationException,
                                          IllegalArgumentException {

      List<Card> hand = new LinkedList<Card>();
      List<Card> cards = getPopulatedCards();
      //testing that any hand can hold up to the entire deck. (entirely of each card)
      for (int k = 0; k < Rules.NUM_PLAYERS; k++) {
         for (Card c : cards) {
            hand = new LinkedList<Card>();
            gameState.setPlayerHand(k, hand);
            if (!c.toString().equals("Not A Card")) {           
               for (int i = 0; i < 53; i++) {
                  hand.add(c); 
                  gameState.setPlayerHand(k, hand);
                  hand = (List<Card>) gameState.getPlayerHand(k);
                  assert (hand.size() == i + 1);
                  for (int j = 1; j < i+1; j++) {        
                     assert (contains(hand, c));
                  }
               }
            }
         }
      }
      //testing thata players hand can hold one of every card at once
      for (int k = 0; k < Rules.NUM_PLAYERS; k++) {
         hand = cards;
         gameState.setPlayerHand(k, hand);
         for (Card c : cards) {
            assert(contains(hand, c));
         }
      }      
   }
   private List<Card> getPopulatedCards() {
      List<Card> cards = new LinkedList<Card>();
      cards.add(Card.AESCULAPINUM);
      cards.add(Card.ARCHITECTUS);
      cards.add(Card.BASILICA);
      cards.add(Card.CENTURIO);
      cards.add(Card.CONSILIARIUS);
      cards.add(Card.CONSUL);
      cards.add(Card.ESSEDUM);
      cards.add(Card.FORUM);
      cards.add(Card.GLADIATOR);
      cards.add(Card.HARUSPEX);
      cards.add(Card.LEGAT);
      cards.add(Card.LEGIONARIUS);
      cards.add(Card.MACHINA);
      cards.add(Card.MERCATOR);
      cards.add(Card.MERCATUS);
      cards.add(Card.NERO);
      cards.add(Card.ONAGER);
      cards.add(Card.PRAETORIANUS);
      cards.add(Card.SCAENICUS);
      cards.add(Card.SENATOR);
      cards.add(Card.SICARIUS);
      cards.add(Card.TEMPLUM);
      cards.add(Card.TRIBUNUSPLEBIS);
      cards.add(Card.TURRIS);
      cards.add(Card.VELITES);
      return cards;
   }
   
   private boolean contains (List<Card> cards, Card card) {
      boolean contains = false;
      for (int i = 0; i < cards.size() && (!contains); i++) {
         if (cards.get(i).toString().equals(card.toString())) {
            contains = true;
         } 
      } 
      return contains;   
   }
}
