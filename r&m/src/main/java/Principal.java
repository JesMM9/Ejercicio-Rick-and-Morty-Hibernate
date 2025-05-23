package main.java;

import main.java.services.APIRequestTool;
import main.java.services.APIRequestToolPersonajeGeneroXApareceMayorNumeroEpisodios;

public class Principal {

	public static void main(String[] args) {
		/*
		 * Muestra por consola la información del personaje indicado.
		 */
		System.out.println(new APIRequestTool().loadCharacter("3"));
		System.out.println(new APIRequestToolPersonajeGeneroXApareceMayorNumeroEpisodios().loadCharacter("Male"));
	}

}
