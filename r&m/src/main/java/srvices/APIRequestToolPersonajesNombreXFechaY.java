package main.java.services;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class APIRequestToolPersonajesNombreXFechaY {

    private static final String BASE_URL = "https://rickandmortyapi.com/api/character";

    public List<String> filtrarPersonajesPorNombreYFecha(char inicial, String fechaMinimaISO) {
        HttpClient client = HttpClient.newHttpClient();
        Gson gson = new Gson();
        String url = BASE_URL;
        List<String> personajesFiltrados = new ArrayList<>();
        LocalDate fechaReferencia = LocalDate.parse(fechaMinimaISO); // formato: "2020-01-01"

        try {
            while (url != null) {
                HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).build();
                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                JsonObject data = gson.fromJson(response.body(), JsonObject.class);
                JsonArray results = data.getAsJsonArray("results");

                for (int i = 0; i < results.size(); i++) {
                    JsonObject personaje = results.get(i).getAsJsonObject();
                    String nombre = personaje.get("name").getAsString();
                    String fechaCreacion = personaje.get("created").getAsString(); // ej: "2017-11-04T18:50:21.651Z"

                    if (nombre.toLowerCase().startsWith(String.valueOf(inicial).toLowerCase())) {
                        LocalDate fechaCreado = LocalDate.parse(fechaCreacion.substring(0, 10), DateTimeFormatter.ISO_DATE);
                        if (fechaCreado.isAfter(fechaReferencia)) {
                            personajesFiltrados.add(nombre + " (creado en " + fechaCreado + ")");
                        }
                    }
                }

                url = data.getAsJsonObject("info").get("next").isJsonNull() ? null
                      : data.getAsJsonObject("info").get("next").getAsString();
            }

        } catch (Exception e) {
            System.out.println("Error al filtrar personajes: " + e.getMessage());
        }

        return personajesFiltrados;
    }
}
