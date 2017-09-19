package framework.interfaces;

import framework.cards.Card;
import java.util.Collection;
import java.util.List;

/**
 * Exposes the state of the game currently being tested to the tester.
 *
 * <p>
 * This class should be implemented by either your gameState, or a
 * wrapper class. The tests will call these methods to change the state
 * of your game, and make sure that the correct changes are made by
 * the move interface.
 * </p>
 *
 * <p>
 * This interface references players by integer. The integers used are
 * to be from 0 to ({@link framework.Rules#NUM_PLAYERS
 * NUM_PLAYERS} - 1), with the order of the numbers
 * matching the order of play.
 * </p>
 *
 * <p>
 * It is the tester's responsibility to ensure that any state changes
 * represent a legal gameState at the point where any move methods are
 * called. Making moves on an invalid gameState will result in undefined
 * behavior.
 * </p>
 *
 * @author Benjamin James Wright (ben.wright)
 * @author Damon Stacey (damon.stacey)
 * @author Matthew Moss (matthew.moss)
 * @author Lasath Fernando (lasath.fernando)
 * @author Tim Schmid (tim.schmid)
 */
public interface GameState {

   /**
    * Get the current turn's player number
    *
    * <p>
    * This method will return an integer between 0 and
    * ({@link framework.Rules#NUM_PLAYERS NUM_PLAYERS} - 1), as
    * specified in the GameState interface.
    * </p>
    *
    * @return the number of the current player
    */
   public int getWhoseTurn ();

   /**
    * Set the current player.
    *
    * <p>
    * This method sets which player is currently having a turn. Valid
    * inputs are between 0 and ({@link framework.Rules#NUM_PLAYERS
    * NUM_PLAYERS} - 1) inclusive.
    * </p>
    *
    * @param player the player whose turn it will be
    */
   public void setWhoseTurn(int player);

   /**
    * Gets the GameState's current deck.
    *
    * <p>
    * The current deck of the GameState is to be returned as a List of
    * Cards. The first item in the list is the next card that would be
    * drawn from the deck, and so on.
    * </p>
    *
    * @return the current GameState deck
    */
   public List<Card> getDeck ();

   /**
    * Sets the GameState's current deck.
    *
    * <p>
    * The new deck of the GameState is to be given as a List of Cards.
    * The first item in the list is the next card that would be
    * drawn from the deck, and so on.
    * </p>
    *
    * @param deck the new deck of the GameState
    */
   public void setDeck (List<Card> deck);

   /**
    * Gets the GameState's current discard pile.
    *
    * <p>
    * The current discard pile of the GameState is to be returned as a
    * List of Cards. The first item in the list is the most recently
    * discarded card, and so on.
    * </p>
    *
    * @return the current GameState discard pile
    */
   public List<Card> getDiscard ();

   /**
    * Sets the GameState's current discard pile.
    *
    * <p>
    * The current discard pile of the GameState is to be given as a
    * List of Cards. The first item in the list is the most recently
    * discarded card, and so on.
    * </p>
    *
    * @param discard the new discard pile of the GameState
    */
   public void setDiscard (List<Card> discard);

   /**
    * Gets a player's current Sestertii.
    *
    * <p>
    * The current Sestertii (money) of the specified player is returned
    * as an integer. Correct player indexing is discussed in the
    * GameState interface header.
    * </p>
    *
    * @param playerNum which player's Sestertii to return
    * @return the player's Sestertii
    */
   public int getPlayerSestertii (int playerNum);

   /**
    * Sets a player's current Sestertii.
    *
    * <p>
    * The new Sestertii (money) of the specified player is given
    * as an integer. Correct player indexing is discussed in the
    * GameState interface header.
    * </p>
    *
    * @param playerNum which player's Sestertii to set
    * @param amount the quantity of Sestertii for the player to have
    */
   public void setPlayerSestertii (int playerNum, int amount);

   /**
    * Gets a player's current Victory Points.
    *
    * <p>
    * The current Victory Points of the specified player are returned as
    * an integer. Correct player indexing is discussed in the
    * GameState interface header.
    * </p>
    *
    * @param playerNum which player's Victory Points to get
    * @return the player's Victory Points
    */
   public int getPlayerVictoryPoints (int playerNum);

    /**
    * Gives a player VPs from the stockpile or give the stockpile VPs from a player.
    *
    * <p>
    * The new Victory Points of the specified player are given as an
    * integer. Correct player indexing is discussed in the GameState
    * interface header.
    * </p>
    * <p>
    * If the given amount is more than what the player already has,
    * then points need to be removed from the stockpile and given
    * to the player and vice versa.
    * </p>
    *
    * @param playerNum which player's Victory Points to set
    * @param points the player's Victory Points
    */
   public void setPlayerVictoryPoints (int playerNum, int points);

   /**
    * Gets the contents of a player's current Hand.
    *
    * <p>
    * The contents of the hand of the specified player is returned as an
    * unordered collection of Cards. Correct player indexing is
    * discussed in the GameState interface header.
    * </p>
    *
    * @param playerNum which player's hand cards to get
    * @return the contents of the player's hand
    */
   public Collection<Card> getPlayerHand (int playerNum);

   /**
    * Sets the contents of a player's current Hand.
    *
    * <p>
    * The contents of the hand of the specified player is given as an
    * unordered collection of Cards. Correct player indexing is
    * discussed in the GameState interface header.
    * </p>
    *
    * @param playerNum which player's hand cards to set
    * @param hand the contents of the the player's hand
    */
   public void setPlayerHand (int playerNum, Collection<Card> hand);

   /**
    * Gets the cards currently laid on a player's dice discs.
    *
    * <p>
    * The cards on the specified player's dice discs are returned in an
    * array of length {@link framework.Rules#NUM_DICE_DISCS
    * NUM_DICE_DISCS}. The 0th index in the array represents the dice
    * disc of value 1. Dice discs with no card are returned with
    * Card.NOT_A_CARD as their value. Correct player indexing is
    * discussed in the GameState interface header.
    * </p>
    *
    * @param playerNum which player's dice disc contents to get
    * @return the cards currently on the player's dice discs
    */
   public Card[] getPlayerCardsOnDiscs (int playerNum);

   /**
    * Sets the contents of a player's dice discs.
    *
    * <p>
    * The cards on the specified player's dice discs are given in an
    * array of length {@link framework.Rules#NUM_DICE_DISCS
    * NUM_DICE_DISCS}. The 0th index in the array represents the dice
    * disc of value 1. Dice discs with no card are returned with
    * Card.NOT_A_CARD as their value. Correct player indexing is
    * discussed in the GameState interface header.
    * </p>
    *
    * @param playerNum which player's cards to set
    * @param discCards the cards to be placed on the dice discs
    */
   public void setPlayerCardsOnDiscs (int playerNum, Card[] discCards);


   /**
    * Gets the current player's action dice values.
    *
    * <p>
    * The values of the current player's action dice are returned in an
    * array in unspecified order. Dice are to be referred to by their
    * value, <i>not</i> by their position in this array.
    * </p>
    *
    * @return the current player's dice
    */
   public int[] getActionDice ();

   /**
    * Sets the current player's action dice values.
    *
    * <p>
    * The values of the current player's action dice are given in an
    * array in unspecified order. Dice are to be referred to by their
    * value, <i>not</i> by their position in this array.
    * </p>
    *
    * @param dice the new values of the current player's dice
    */
   public void setActionDice (int[] dice);

   /**
    * Returns the number of Victory Points not currently held by a
    * player.
    *
    * <p>
    * The number of victory points not held by any player are returned.
    * This method is included so that the total number of Victory
    * Points in a game can be tested.
    * </p>
    *
    * @return the number of Victory Points not held by any player
    */
   public int getPoolVictoryPoints ();
   
   /**
    * Returns true iff a game has been started AND the game has come to completion
    * otherwise return false.
    *
    * @return whether a game has come to completion
    */
   public boolean isGameCompleted();
}
