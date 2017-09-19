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
public interface ForumActivator extends ActionDiceUser, CardActivator {

    /**
     * Choose whether to activate a Templum with your forum activation
     * @param activate true if the user wishes to activate a Templum.
     */
    void chooseActivateTemplum(boolean activate);


    /**
     * Choose the dice to use with the Templum.
     *
     * This should only be called if chooseActivateTemplum has previously
     * been called with true as a parameter.
     *
     * @param diceValue the dice to use
     */
    void chooseActivateTemplum(int diceValue);

}
