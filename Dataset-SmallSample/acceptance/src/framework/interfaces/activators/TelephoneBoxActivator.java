/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package framework.interfaces.activators;

/**
 *
 * @author Damon Stacey (damon.stacey)
 */
public interface TelephoneBoxActivator extends Targeted, CardActivator {

   /**
     * Select direction in time
     *
     * <p>
     * This method selects the direction in time the time 
     * machine moves the selected card, where true is forward movement. 
     * </p>
     *
     * @param isForward
     * @return
     */
    public void shouldMoveForwardInTime (boolean isForward);

   /**
     * Select direction in time
     *
     * <p>
     * This method selects the duration of the time jump through the use of a second dice
     * </p>
     *
     * @param diceValue
     * @return
     */
    public void setSecondDiceUsed (int diceValue);

}
