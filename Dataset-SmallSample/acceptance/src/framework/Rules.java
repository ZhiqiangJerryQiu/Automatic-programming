package framework;

/**
 * Contains constants relating to the rules of Roma.
 *
 * @author Matthew Moss (matthew.moss)
 * @author Lasath Fernando (lasath.fernando)
 * @author Tim Schmid (tim.schmid)
 */
public class Rules {

    /**
     * The number of players in the game.
     */
    public static final int NUM_PLAYERS = 2;

    /**
     * The number of cards in a player's hand at the start of the game.
     */
    public static final int STARTING_HAND_CARDS = 5;

    /**
     * The total number of cards present in the game.
     *
     * <p>
     * This should be the sum of the deck pile, discard pile and all
     * players' hands.
     * </p>
     */
    public static final int NUM_CARDS = 56;

    /**
     * The total number of Victory Points present in the game.
     *
     * <p>
     * This should be the sum of each of the player's Victory Points,
     * plus the Victory Points in the pool.
     * </p>
     */
    public static final int GAME_VICTORY_POINTS = 36;

    /**
     * The number of Dice Discs in the game.
     */
    public static final int NUM_DICE_DISCS = 7;

   /**
    * The value used to refer to the 1st Dice Disc
    */
    public static final int DICE_DISC_1 = 1;

   /**
    * The value used to refer to the 2nd Dice Disc
    */
    public static final int DICE_DISC_2 = 2;

   /**
    * The value used to refer to the 3rd Dice Disc
    */
     public static final int DICE_DISC_3 = 3;
   /**
    * The value used to refer to the 4th Dice Disc
    */
    public static final int DICE_DISC_4 = 4;
   /**
    * The value used to refer to the 5th Dice Disc
    */public static final int DICE_DISC_5 = 5;
   /**
    * The value used to refer to the 6th Dice Disc
    */
    public static final int DICE_DISC_6 = 6;
   /**
    * The value used to refer to the Bribe or Pay to Play Dice Disc
    */
    public static final int BRIBE_DISC  = 7;



}
