package simpledotcom;




public class SimpleDotComTestDrive {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		SimplePalabra simple = new SimplePalabra();

		int[] ubicaciones = {2,3,4};
		
		simple.setCeldasUbicacion(ubicaciones);
		
		String intentoUsuario = "5";
		
		String resultado = simple.chequearValor(intentoUsuario);
		
		String resultadoTest = "fallo";
		
		if(resultado.equals("hit"))
			resultadoTest ="paso";
		
		System.out.println("La prueba " + resultadoTest);
		
		
	}

}
