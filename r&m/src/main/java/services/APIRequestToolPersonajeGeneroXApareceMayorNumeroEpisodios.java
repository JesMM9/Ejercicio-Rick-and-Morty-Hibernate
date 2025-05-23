package main.java.services;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import main.java.model.Personaje;

public class APIRequestToolPersonajeGeneroXApareceMayorNumeroEpisodios {

    private static final String BASE_URL = "https://rickandmortyapi.com/api/character/?gender=";

    public Personaje loadCharacter(String gender) {
        HttpClient client = HttpClient.newHttpClient();
        Gson gson = new Gson();

        String url = BASE_URL + gender;
        List<Personaje> personajes = new ArrayList<>();

        try {
            // Se realiza la paginación
            while (url != null) {
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(url))
                        .build();

                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

                JsonObject responseObject = gson.fromJson(response.body(), JsonObject.class);
                JsonArray resultsArray = responseObject.getAsJsonArray("results");

                List<Personaje> pagina = gson.fromJson(resultsArray, new TypeToken<List<Personaje>>() {}.getType());
                personajes.addAll(pagina);

                // Obtener la siguiente página
                JsonObject info = responseObject.getAsJsonObject("info");
                if (info.has("next") && !info.get("next").isJsonNull()) {
                    url = info.get("next").getAsString();
                } else {
                    url = null;
                }
            }

            // Obtener personaje con más episodios
            return personajes.stream()
                    .max(Comparator.comparingInt(p -> p.getEpisode().length))
                    .orElse(null);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}