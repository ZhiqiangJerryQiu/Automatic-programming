/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package framework.interfaces.activators;

/**
 *
 * @author Lasath Fernando (lasath.fernando)
 * @author Matthew Moss (matthew.moss)
 * @author Damon Stacey (damon.stacey)
 */
public interface ConsulActivator extends CardActivator{

    /**
     * Choose the amount a dice disc value changes by.
     *
     * <p>
     * Valid changes are -1 or 1 in the current game.
     * </p>
     *
     * @param amount the amount to change by.
     */
    void chooseConsulChangeAmount (int amount);

    void chooseWhichDiceChanges(int originalRoll);
}
