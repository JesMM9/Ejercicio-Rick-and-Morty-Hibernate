package main.java.services;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class APIRequestToolUltimoEpisodioPersonajeX {

    private static final String BASE_URL = "https://rickandmortyapi.com/api/character/?name=";

    public String obtenerUltimoEpisodio(String nombrePersonaje) {
        HttpClient client = HttpClient.newHttpClient();
        Gson gson = new Gson();

        try {
            String url = BASE_URL + nombrePersonaje.replace(" ", "%20");
            HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            JsonObject json = gson.fromJson(response.body(), JsonObject.class);
            JsonObject personaje = json.getAsJsonArray("results").get(0).getAsJsonObject();
            List<String> episodios = gson.fromJson(personaje.get("episode"), List.class);

            if (episodios.isEmpty()) return "Este personaje no aparece en ningún episodio.";
            return "Último episodio: " + episodios.get(episodios.size() - 1);
        } catch (Exception e) {
            return "Error al obtener el personaje: " + e.getMessage();
        }
    }
}
