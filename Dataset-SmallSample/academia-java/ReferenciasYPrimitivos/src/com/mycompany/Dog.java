package com.mycompany;

public class Dog {
	
	String name;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		
		Dog dog1 = new Dog();
		dog1.bark();
		dog1.name = "Bart";

		
		Dog[] myDogs = new Dog[3];
		myDogs[0] = new Dog();
		myDogs[1] = new Dog();
		myDogs[2] = dog1;

		myDogs[0].name = "Fred";
		myDogs[1].name = "Marge";

		System.out.print("last don't name is ");
		System.out.println(myDogs[2].name);

		int x = 0;
		while (x < myDogs.length) {
			myDogs[x].bark();
			x = x + 1;
		}
		


	}

	private void bark() {
		System.out.println("ruff ruff ruff");
		
	}

}
