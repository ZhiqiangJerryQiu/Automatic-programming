package simpledotcom;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;



public class GameHelper {

	
	public String getUserlnput(String prompt){
		String inputLine = null;
		System.out.print(prompt +" ");
		
		double sueldo = 1500.56; //default para punto flotante
		
		float sueldoChiquito = 600.00f;
		
		char c = 'd';
		
		BufferedReader is = new BufferedReader(new InputStreamReader(System.in));
		try {
			inputLine = is.readLine();
			if (inputLine. length () == 0) return null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return inputLine;
	}
}
