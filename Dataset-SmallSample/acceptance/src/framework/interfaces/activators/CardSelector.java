package framework.interfaces.activators;


/**
 * A component of the CardActivator family.
 *
 * @author Matthew Moss (matthew.moss)
 * @author Lasath Fernando (lasath.fernando)
 * @author Damon Stacey (damon.stacey)
 */
public interface CardSelector {

   /**
    * The user chooses a card from a pile.
    *
    * <p>
    * The tester will get your discard pile, and scan through it for the card required.
    * So, the card they have choosen will be at getDiscard().get(indexOfCard);
    * </p>
    *
    * @param indexOfCard the index of the card to use
    */
   void chooseCardFromPile (int indexOfCard);
}
