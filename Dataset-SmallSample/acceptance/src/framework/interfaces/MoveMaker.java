package framework.interfaces;

import framework.cards.Card;
import framework.interfaces.activators.CardActivator;

/**
 * This interface defines all possible actions a theoretical player
 * can make in a game at any given point.
 *
 * <p>
 * This interface can be implemented in your gameState object, or any
 * other class of your choosing. The tests will call these methods to
 * make changes in your game, simulating actual play.
 * </p>
 *
 * <p>
 * When implementing, you can be confidant that any method called will
 * be valid for the present gameState, and so will not require data
 * validation by the game being tested. The author of a test is
 * required to ensure it is correct before including it in the testing
 * package. The final implementation of the testing framework will
 * perform a number of automatic sanity checks on each method.
 * </p>
 *
 * @author Matthew Moss (matthew.moss)
 * @author Lasath Fernando (lasath.fernando)
 */
public interface MoveMaker {

    /**
     * Activate the card that is currently on the given dice disc.
     *
     * TODO: fix dis shit
     *
     * <p>
     * This will never be called if:
     * <ul>
     * <li>the player does not have the appropriate action dice to
     * activate the chosen card</li>
     * <li>the card cannot be activated at the current time</li>
     * <li>the ActionData parameter does not match the activated
     * card</li>
     * <li>the dice disc is not present in the current game</li>
     * </ul>
     *  </p>
     *
     * @param disc the disc where the card to be activated is
     * @param parameters the ActivateData needed by that specific card
     *
     * @throws UnsupportedOperationException if the move is not yet
     * implemented
     */
    public CardActivator chooseCardToActivate(int disc)
                                   throws UnsupportedOperationException;

    /**
     * Activate the cards disc with the given action die, and choose
     * to keep the given card.
     *
     * <p>
     * After this method is called:
     * <ul>
     * <li>the appropriate number of cards will be removed from the
     * deck</li>
     * <li>an instance of the card given will be in the player's
     * hand</li>
     * <li>the other cards removed from the deck will be present at the
     * top of the discard pile in unspecified order</li>
     * <li>the appropriate action die will have been used</li>
     * </ul>
     * </p>
     *
     * <p>
     * This will never be called if:
     * <ul>
     * <li>if the user does not have an unused action die of the given
     * value</li>
     * <li>the cards drawn from the deck do not include the given
     * card</li>
     * </ul>
     *
     * @param diceToUse which value dice to use to activate the disc
     * @param chosen which card to keep from the group drawn from the
     * deck
     *
     * @throws UnsupportedOperationException if the move is not yet
     * implemented
     */
    public void activateCardsDisc(int diceToUse, Card chosen)
                                   throws UnsupportedOperationException;

    /**
     * Activate the Money Disc with the given action die.
     *
     * <p>
     * After this method is called:
     * <ul>
     * <li>the appropriate action die will have been used</li>
     * <li>the correct amount of sestertii will have been added to the
     * player's Sestertii</li>
     * </ul>
     * </p>
     *
     * <p>
     * This will never be called if:
     * <ul>
     * <li>if the user does not have an unused action die of the given
     * value</li>
     * </ul>
     *
     * @param diceToUse which value dice to activate the disc with
     * @throws UnsupportedOperationException if the move is not yet
     * implemented
     */
    public void activateMoneyDisc(int diceToUse)
                                   throws UnsupportedOperationException;

   /**
     * Activate the Bribe Disc with the given action die.
     *
     * <p>
     * After this method is called:
     * <ul>
     * <li>the appropriate action die will have been used</li>
     * <li>the correct amount of sestertii will have been removed from the
     * player's Sestertii</li>
     * <li>the card on the disc will be activated and 
     * </ul>
     * </p>
     *
     * <p>
     * This will never be called if:
     * <ul>
     * <li>if the user does not have an unused action die of the given
     * value</li>
     * <li>the card cannot be activated at the current time</li>
     * <li> there is no card on this disc </li>
     * <li>the ActionData parameter does not match the activated
     * card</li>
     * </ul>
     *
     * @param diceToUse which value dice to activate the disc with
     * @throws UnsupportedOperationException if the move is not yet
     * implemented
     */
    public CardActivator activateBribeDisc(int diceToUse)
                                   throws UnsupportedOperationException;

    /**
     * End the turn of the current player.
     *
     * <p>
     * After this method is called:
     * <ul>
     * <li>The old next player is now the current player</li>
     * <li>the appropriate number of dice will have been rolled</li>
     * <li>the appropriate number of victory points will have been
     * removed for vacant dice discs</li>
     * </ul>
     * </p>
     *
     * <p>
     * There are no restrictions on the calling of this method.
     * </p>
     *
     * @throws UnsupportedOperationException if the move is not yet
     * implemented
     */
    public void endTurn()
                                   throws UnsupportedOperationException;

    /**
     * Place a card from the current player's hand on to the selected
     * dice disc.
     *
     * <p>
     * After this method is called:
     * <ul>
     * <li>the selected card is removed from the current player's hand
     * </li>
     * <li>the selected card will be present on the dice disc</li>
     * <li>the required amount of Sestertii will have been deducted from
     * the player's hand</li>
     * <li>any extra changes specific to the placed card will be in
     * effect</li>
     * </ul>
     * </p>
     *
     * <p>
     * This will never be called if:
     * <ul>
     * <li>the player's hand doesn't contain a card of the given
     * type</li>
     * <li>the player has insufficient Sestertii to place the given
     * card</li>
     * <li>the dice disc is not valid for the current game</li>
     * </ul>
     * </p>
     *
     * @param toPlace the type of card to be placed
     * @param discToPlaceOn the disc on which the card will be placed
     *
     * @throws UnsupportedOperationException if the move is not yet
     * implemented
     */
    public void placeCard(Card toPlace, int discToPlaceOn)
                                   throws UnsupportedOperationException;
}
