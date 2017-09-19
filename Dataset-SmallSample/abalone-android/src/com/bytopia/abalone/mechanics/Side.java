/**
* Copyright (c) 2010-2011 Yaroslav Geryatovich, Alexander Yakushev
* Distributed under the GNU GPL v3. For full terms see the file docs/COPYING.
*/
package com.bytopia.abalone.mechanics;

/**
 * Class that contains player sides. Not a enumeration because of performance
 * issues.
 * 
 * @author Bytopia
 */
public final class Side {

	/**
	 * White side.
	 */
	public final static byte WHITE = 2;
	
	/**
	 * Black side.
	 */
	public final static byte BLACK = 3;

	/**
	 * Returns the side that is opposite to the given.
	 * 
	 * @param s
	 *            given side
	 * @return opposite side
	 */
	public static byte opposite(byte s) {
		return s == BLACK ? WHITE : BLACK;
	}
	
	/**
	 * Private empty constructor to make the class the service one.
	 */
	private Side() {
	}

}
