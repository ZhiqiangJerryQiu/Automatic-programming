/*
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package framework.interfaces;

/**
 *This interface will be the entry point of the tester to your game.
 *
 * TODO: We need to ask the reps for an entry class name
 *
 * <p>
 * This interface is required to be implemented by everyone. You will
 * want to look at MoveMaker and GameState and be sure that you understand
 * how they work and how you can interface with them.
 * </p>
 *
 * @author Lasath Fernando (lasath.fernando)
 * @author Matthew Moss (matthew.moss)
 * @author Benjamin James Wright (ben.wright)
 * @author Damon Stacey (damon.stacey)
 */
public interface AcceptanceInterface {

   /**
    * Return a {@link MoveMaker} that will modify the given GameState.
    *
    * <p>
    * This MoveMaker will be used by the tests to modify the GameState
    * that was given by getInitialState. The affected GameState is
    * included as a parameter so you can ensure that the MoveMaker will
    * operate on the correct GameState.
    * </p>
    *
    * @param state the GameState that the mover will apply changes to
    * @return a MoveMaker that will modify the given GameState
    */
   public MoveMaker getMover(GameState state);

   /**
    * Instantiate a {@link GameState} object.
    *
    * <p>
    * The created GameState should be a mutable new instance as this is called
    * before each test is run.
    * </p>
    *
    * <p>
    * The state should be set in the initial condition as defined per:
    * TODO: add the crap that makes an initial state here.
    * </p>
    *
    * @return a GameState at the initial state
    */
   public GameState getInitialState();
}
