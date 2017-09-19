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
public class InitialisationCardDiscBasicTest extends Test {

   @Override
   public String getShortDescription() {
      return "Checking Card discs can have any cards layed on them.";
   }

   @Override
   public void run(GameState gameState, MoveMaker move)
                                          throws AssertionError,
                                          UnsupportedOperationException,
                                          IllegalArgumentException {

      Card[] discCards = new Card[Rules.NUM_DICE_DISCS];
      for (int i= 0; i < Rules.NUM_PLAYERS; i++) {
         for (int j = 0; j < Rules.NUM_DICE_DISCS; j++) {
            discCards[j] = Card.NOT_A_CARD;         
         }
         gameState.setPlayerCardsOnDiscs (i, discCards);
         discCards = gameState.getPlayerCardsOnDiscs(i);
         for (int j = 0; j < Rules.NUM_DICE_DISCS; j++) {
            assert(discCards[j].toString().equals(Card.NOT_A_CARD.toString()));         
         }

         //checking players can lay thier cards on discs individually
         for (int j = 0; j < Rules.NUM_DICE_DISCS; j++) {
            for (Card c : getPopulatedCards()) {
               discCards[j] = c;         
               gameState.setPlayerCardsOnDiscs (i, discCards);
               discCards = gameState.getPlayerCardsOnDiscs(i);
               assert(discCards[j].toString().equals(c.toString()));         
            }
         }
         for (int j = 0; j < Rules.NUM_DICE_DISCS; j++) {
            discCards[j] = Card.NOT_A_CARD;         
         }

         gameState.setPlayerCardsOnDiscs (i, discCards);
         discCards = gameState.getPlayerCardsOnDiscs(i);
      }   

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
      cards.add(Card.NOT_A_CARD);
      return cards;
   }

}
