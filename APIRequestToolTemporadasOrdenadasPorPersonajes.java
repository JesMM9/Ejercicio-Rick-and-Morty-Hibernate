package main.java.services;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class APIRequestToolTemporadasOrdenadasPorPersonajes {

    private static final String BASE_URL = "https://rickandmortyapi.com/api/episode";

    public void temporadasOrdenadasPorPersonajes() {
        HttpClient client = HttpClient.newHttpClient();
        Gson gson = new Gson();
        String url = BASE_URL;

        Map<String, Set<String>> personajesPorTemporada = new HashMap<>();

        try {
            while (url != null) {
                HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).build();
                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                JsonObject data = gson.fromJson(response.body(), JsonObject.class);
                JsonArray episodes = data.getAsJsonArray("results");

                for (int i = 0; i < episodes.size(); i++) {
                    JsonObject ep = episodes.get(i).getAsJsonObject();
                    String temporada = ep.get("episode").getAsString().substring(0, 3); // "S01", "S02", etc.
                    JsonArray characters = ep.getAsJsonArray("characters");

                    personajesPorTemporada.putIfAbsent(temporada, new HashSet<>());
                    for (int j = 0; j < characters.size(); j++) {
                        personajesPorTemporada.get(temporada).add(characters.get(j).getAsString());
                    }
                }

                url = data.getAsJsonObject("info").get("next").isJsonNull() ? null
                      : data.getAsJsonObject("info").get("next").getAsString();
            }

            List<Map.Entry<String, Set<String>>> ordenado = new ArrayList<>(personajesPorTemporada.entrySet());
            ordenado.sort((a, b) -> Integer.compare(b.getValue().size(), a.getValue().size()));

            System.out.println("Temporadas ordenadas por cantidad de personajes distintos:");
            for (Map.Entry<String, Set<String>> entry : ordenado) {
                System.out.printf("%s -> %d personajes distintos%n", entry.getKey(), entry.getValue().size());
            }

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
