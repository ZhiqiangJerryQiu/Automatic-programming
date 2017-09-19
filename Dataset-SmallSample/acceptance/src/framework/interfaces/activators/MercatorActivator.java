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
public interface MercatorActivator extends CardActivator {

    /**
     * Choose the number of victory points to buy with the Mercator.
     * @param VPToBuy the number of points to buy
     */
    void chooseMercatorBuyNum (int VPToBuy);
}
