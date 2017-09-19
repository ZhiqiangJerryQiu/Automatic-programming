/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package framework.interfaces.activators;

/**
 * A component of the CardActivator family.
 *
 * @author Matthew Moss (matthew.moss)
 * @author Lasath Fernando (lasath.fernando)
 * @author Damon Stacey (damon.stacey)
 */
public interface ActionDiceUser {

    /**
     * The user chooses an action dice.
     *
     * <p>
     * An action dice of the given value is used. A dice of that value
     * must exist in the user's current unused action dice. The same
     * dice cannot be used multiple times on the same turn. The same
     * value can be used if the user has multiple dice of that value.
     * </p>
     *
     * <p>
     * Action dice are always referred to by their value, rather than
     * their index, because action dice do not have either an implicit
     * or explicit ordering.
     * </p>
     *
     * @param actionDiceValue the value of the dice to use
     */
    void chooseActionDice (int actionDiceValue);
}
