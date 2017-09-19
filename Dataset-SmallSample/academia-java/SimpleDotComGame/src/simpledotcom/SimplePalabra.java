package simpledotcom;



public class SimplePalabra {



	private int[] celdasUbicacion;
	private int numOfHits = 0;

	public void setCeldasUbicacion(int[] ubicaciones) {
		this.celdasUbicacion = ubicaciones;
		
	}

	public String chequearValor(String intentoUsuario) {
		
		int intento = Integer.parseInt(intentoUsuario);
		
		String resultado = "miss";
		
		for(int valor : celdasUbicacion){
			
			if(valor == intento){
				resultado = "hit";
				numOfHits ++;
				break;
			}
		}

		if( numOfHits == celdasUbicacion.length){
			resultado = "kill";
		}
		
		System.out.println(resultado);
		
		return resultado;
	}



}
