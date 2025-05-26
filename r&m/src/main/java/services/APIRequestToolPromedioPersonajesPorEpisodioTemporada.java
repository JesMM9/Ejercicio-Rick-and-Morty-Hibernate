package main.java.services;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class APIRequestToolPromedioPersonajesPorEpisodioTemporada {

    private static final String BASE_URL = "https://rickandmortyapi.com/api/episode";

    public void calcularPromedioPersonajesPorTemporada() {
        HttpClient client = HttpClient.newHttpClient();
        Gson gson = new Gson();
        String url = BASE_URL;

        Map<String, Integer> totalPersonajes = new HashMap<>();
        Map<String, Integer> totalEpisodios = new HashMap<>();

        try {
            while (url != null) {
                HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).build();
                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                JsonObject data = gson.fromJson(response.body(), JsonObject.class);

                JsonArray episodes = data.getAsJsonArray("results");
                for (int i = 0; i < episodes.size(); i++) {
                    JsonObject ep = episodes.get(i).getAsJsonObject();
                    String season = ep.get("episode").getAsString().substring(0, 3);
                    int personajes = ep.getAsJsonArray("characters").size();

                    totalPersonajes.put(season, totalPersonajes.getOrDefault(season, 0) + personajes);
                    totalEpisodios.put(season, totalEpisodios.getOrDefault(season, 0) + 1);
                }

                url = data.getAsJsonObject("info").get("next").isJsonNull() ? null
                      : data.getAsJsonObject("info").get("next").getAsString();
            }

            System.out.println("Promedio de personajes por episodio por temporada:");
            for (String temporada : totalPersonajes.keySet()) {
                double promedio = (double) totalPersonajes.get(temporada) / totalEpisodios.get(temporada);
                System.out.printf("%s -> %.2f personajes/episodio%n", temporada, promedio);
            }

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
