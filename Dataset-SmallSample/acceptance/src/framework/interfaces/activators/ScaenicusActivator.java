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
public interface ScaenicusActivator extends CardActivator {

    /**
     * Select a card to mimic with the Scaenicus.
     *
     * <p>
     * This method selects a card for an activated Scaenicus to mimic.
     * A new CardActivator corresponding to the chosen card is returned,
     * so the test may use it to activate the selected card.
     * </p>
     *
     * @param diceDisc
     * @return
     */
    CardActivator getScaenicusMimicTarget (int diceDisc);
}
