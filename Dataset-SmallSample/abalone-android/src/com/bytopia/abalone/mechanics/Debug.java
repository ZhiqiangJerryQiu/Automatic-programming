/**
* Copyright (c) 2010-2011 Yaroslav Geryatovich, Alexander Yakushev
* Distributed under the GNU GPL v3. For full terms see the file docs/COPYING.
*/
package com.bytopia.abalone.mechanics;

public class Debug {

	public static void main(String[] args) throws Exception {
		System.out.println("start");
		ConsoleWatcher cw = new ConsoleWatcher();
		Game g = new Game(new ClassicLayout(), (byte)3, new AiAnn(), new AiDeborah(), cw, (byte)0);
		cw.setGame(g);
		g.start();
	}

	public static byte convDir(String s) {
		if (s.equals("NW"))
			return Direction.NorthWest;//	public static Cell convCell(String s) {
//		return new Cell((int) s.charAt(0) - (int) 'A' + 1, Integer
//		.parseInt(Character.toString(s.charAt(1))));
//}
		else if (s.equals("N"))
			return Direction.North;
		else if (s.equals("E"))
			return Direction.East;
		else if (s.equals("SE"))
			return Direction.SouthEast;
		else if (s.equals("S"))
			return Direction.South;
		else
			return Direction.West;
	}

//	public static Cell convCell(String s) {
//		return new Cell((int) s.charAt(0) - (int) 'A' + 1, Integer
//				.parseInt(Character.toString(s.charAt(1))));
//	}

	public static byte convSide(String s) {
		if (s.equals("W"))
			return Side.WHITE;
		else
			return Side.BLACK;
	}
}
