package com.mycompany;

public class Player {

	public int number;

	public void guess() {
		number = (int) (Math.random() * 10);
		System.out.println("I'm guessing " + number);

		
	}

}
