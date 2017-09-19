package simpledotcom;

import java.util.ArrayList;

public class Palabra {

	private ArrayList<String> celdasUbicacion;
	
	//ya no necesitamos numOfHits
	
	public void setCeldasUbicacion(ArrayList<String> celdasUbicacion) {
		this.celdasUbicacion = celdasUbicacion;
	}
	
	public String chequearValor(String intentoUsuario) {
		
		String resultado = "miss";
		
		int index = celdasUbicacion.indexOf(intentoUsuario);
		
		if(index>=0){
			celdasUbicacion.remove(index);
			if(celdasUbicacion.isEmpty()){
				resultado = "kill";
			} else{
				resultado = "hit";
			}
		}
		
		return resultado;
	}	
}
