package main.java.services;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;

import com.google.gson.GsonBuilder;

import main.java.model.Personaje;

public class APIRequestTool {

	/**
	 * ToDo Debe replicarse este método para el resto de entidades.
	 * 
	 * Este método consulta la API para obtener la información del personaje cuyo id
	 * es facilitado. No controla si ese personaje no existe o la API es
	 * inaccesible.
	 * 
	 * @param id
	 * @return Objeto de tipo Personaje con la información de éste
	 */
	public Personaje loadCharacter(String id) {
		HttpClient client = HttpClient.newHttpClient();
		HttpRequest request = HttpRequest.newBuilder().uri(URI.create(Personaje.PERSONAJE_URL_API.concat(id))).build();
		String personajeJSON = client.sendAsync(request, BodyHandlers.ofString()).thenApply(HttpResponse::body).join();
		// Transforma la respuesta obtenida del servidor de un JSON a la clase Java y lo devuelve
		return new GsonBuilder().setPrettyPrinting().create().fromJson(personajeJSON, Personaje.class);
	}
}
