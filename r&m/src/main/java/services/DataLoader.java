package com.example.service;

import com.example.model.Personaje;
import com.example.model.Episodio;
import com.example.model.Localizacion;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

public class DataLoader {

    private static final String BASE_URL = "https://rickandmortyapi.com/api/";
    private final Gson gson = new Gson();

    private final EntityManagerFactory emf;
    private final EntityManager em;

    public DataLoader() {
        emf = Persistence.createEntityManagerFactory("default");
        em = emf.createEntityManager();
    }

    public void close() {
        em.close();
        emf.close();
    }

    private String getJsonFromApi(String endpoint) throws Exception {
        URL url = new URL(BASE_URL + endpoint);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        int status = con.getResponseCode();

        if (status != 200) {
            throw new RuntimeException("Error HTTP: " + status);
        }

        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuilder content = new StringBuilder();

        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }

        in.close();
        con.disconnect();

        return content.toString();
    }

    public void loadAllData() throws Exception {
        em.getTransaction().begin();

        Map<Long, Localizacion> locationsMap = loadAllLocations();
        Map<Long, Episodio> episodesMap = loadAllEpisodes();
        Map<Long, Personaje> charactersMap = loadAllCharacters(locationsMap, episodesMap);

        for (Personaje p : charactersMap.values()) {
            for (Episodio e : p.getEpisodes()) {
                e.getCharacters().add(p);
                em.merge(e);
            }
            em.merge(p);
        }

        em.getTransaction().commit();
    }

    private Map<Long, Localizacion> loadAllLocations() throws Exception {
        Map<Long, Localizacion> locationMap = new HashMap<>();

        int page = 1;
        int pages;

        do {
            String json = getJsonFromApi("location?page=" + page);
            JsonObject obj = gson.fromJson(json, JsonObject.class);

            pages = obj.getAsJsonObject("info").get("pages").getAsInt();

            Type listType = new TypeToken<List<JsonObject>>(){}.getType();
            List<JsonObject> results = gson.fromJson(obj.get("results"), listType);

            for (JsonObject locJson : results) {
                Localizacion loc = parseLocation(locJson);
                em.persist(loc);
                locationMap.put(loc.getId(), loc);
            }

            page++;
        } while (page <= pages);

        return locationMap;
    }

    private Localizacion parseLocation(JsonObject json) {
        Localizacion loc = new Localizacion();
        loc.setId(json.get("id").getAsLong());
        loc.setName(json.get("name").getAsString());
        loc.setType(json.get("type").getAsString());
        loc.setDimension(json.get("dimension").getAsString());
        return loc;
    }

    private Map<Long, Episodio> loadAllEpisodes() throws Exception {
        Map<Long, Episodio> episodeMap = new HashMap<>();

        int page = 1;
        int pages;

        do {
            String json = getJsonFromApi("episode?page=" + page);
            JsonObject obj = gson.fromJson(json, JsonObject.class);

            pages = obj.getAsJsonObject("info").get("pages").getAsInt();

            Type listType = new TypeToken<List<JsonObject>>(){}.getType();
            List<JsonObject> results = gson.fromJson(obj.get("results"), listType);

            for (JsonObject epJson : results) {
                Episodio ep = parseEpisode(epJson);
                em.persist(ep);
                episodeMap.put(ep.getId(), ep);
            }

            page++;
        } while (page <= pages);

        return episodeMap;
    }

    private Episodio parseEpisode(JsonObject json) {
        Episodio ep = new Episodio();
        ep.setId(json.get("id").getAsLong());
        ep.setName(json.get("name").getAsString());
        ep.setAirDate(json.get("air_date").getAsString());
        ep.setEpisode(json.get("episode").getAsString());
        return ep;
    }

    private Map<Long, Personaje> loadAllCharacters(Map<Long, Localizacion> locations, Map<Long, Episodio> episodes) throws Exception {
        Map<Long, Personaje> characterMap = new HashMap<>();

        int page = 1;
        int pages;

        do {
            String json = getJsonFromApi("character?page=" + page);
            JsonObject obj = gson.fromJson(json, JsonObject.class);

            pages = obj.getAsJsonObject("info").get("pages").getAsInt();

            Type listType = new TypeToken<List<JsonObject>>(){}.getType();
            List<JsonObject> results = gson.fromJson(obj.get("results"), listType);

            for (JsonObject chJson : results) {
                Personaje p = parseCharacter(chJson, locations, episodes);
                em.persist(p);
                characterMap.put(p.getId(), p);
            }

            page++;
        } while (page <= pages);

        return characterMap;
    }

    private Personaje parseCharacter(JsonObject json, Map<Long, Localizacion> locations, Map<Long, Episodio> episodes) {
        Personaje p = new Personaje();
        p.setId(json.get("id").getAsLong());
        p.setName(json.get("name").getAsString());
        p.setStatus(json.get("status").getAsString());
        p.setSpecies(json.get("species").getAsString());
        p.setType(json.get("type").getAsString());
        p.setGender(json.get("gender").getAsString());
        p.setImage(json.get("image").getAsString());

        // Origen
        JsonObject origin = json.getAsJsonObject("origin");
        if (origin != null && origin.has("url") && !origin.get("url").getAsString().isEmpty()) {
            Long originId = extractIdFromUrl(origin.get("url").getAsString());
            p.setOrigin(locations.get(originId));
        }

        // Location actual
        JsonObject location = json.getAsJsonObject("location");
        if (location != null && location.has("url") && !location.get("url").getAsString().isEmpty()) {
            Long locationId = extractIdFromUrl(location.get("url").getAsString());
            p.setLocation(locations.get(locationId));
        }

        List<String> episodeUrls = new ArrayList<>();
        json.getAsJsonArray("episode").forEach(e -> episodeUrls.add(e.getAsString()));

        Set<Episodio> eps = new HashSet<>();
        for (String epUrl : episodeUrls) {
            Long epId = extractIdFromUrl(epUrl);
            Episodio ep = episodes.get(epId);
            if (ep != null) {
                eps.add(ep);
            }
        }
        p.setEpisodes(eps);

        return p;
    }

    private Long extractIdFromUrl(String url) {
        String[] parts = url.split("/");
        return Long.parseLong(parts[parts.length - 1]);
    }
}
