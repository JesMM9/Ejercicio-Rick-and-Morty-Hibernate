package main.java.services;

import com.example.model.Personaje;
import com.example.model.Episodio;
import com.example.model.Localizacion;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class Consultas {

    private final EntityManagerFactory emf;
    private final EntityManager em;

    public Consultas() {
        emf = Persistence.createEntityManagerFactory("default");
        em = emf.createEntityManager();
    }

    public void close() {
        em.close();
        emf.close();
    }

    // Personaje del género X que aparece en mayor número de episodios
    public Personaje personajeGeneroConMasEpisodios(String genero) {
        String jpql = "SELECT p FROM Personaje p WHERE p.gender = :gen ORDER BY SIZE(p.episodes) DESC";
        TypedQuery<Personaje> query = em.createQuery(jpql, Personaje.class);
        query.setParameter("gen", genero);
        query.setMaxResults(1);
        return query.getResultStream().findFirst().orElse(null);
    }

    // Último episodio en el que aparece el personaje X
    public Episodio ultimoEpisodioDePersonaje(String nombrePersonaje) {
        String jpql = "SELECT e FROM Episodio e JOIN e.characters p WHERE p.name = :name ORDER BY e.id DESC";
        TypedQuery<Episodio> query = em.createQuery(jpql, Episodio.class);
        query.setParameter("name", nombrePersonaje);
        query.setMaxResults(1);
        return query.getResultStream().findFirst().orElse(null);
    }

    // Número medio de personajes por episodio y temporada
    // La temporada se extrae del campo episodio (ejemplo: "S01E01")
    public Double numeroMedioPersonajesPorEpisodioYTemporada() {
        String jpql = "SELECT AVG(size(e.characters)) FROM Episodio e";
        return em.createQuery(jpql, Double.class).getSingleResult();
    }

    // Temporadas ordenadas por número de personajes distintos que aparecen en ellas
    public List<Object[]> temporadasOrdenadasPorPersonajes() {
        String jpql = "SELECT SUBSTRING(e.episode, 2, 2) as temporada, COUNT(DISTINCT p.id) " +
                "FROM Episodio e JOIN e.characters p " +
                "GROUP BY temporada " +
                "ORDER BY COUNT(DISTINCT p.id) DESC";
        return em.createQuery(jpql, Object[].class).getResultList();
    }

    // Personaje/s cuyo nombre comienza por X con fecha de primera aparición posterior a FechaY
    // FechaY en formato "dd MMM yyyy" (por ejemplo "26 Nov 2013")
    public List<Personaje> personajesPorNombreYFecha(String nombreInicio, String fechaY) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy");
        LocalDate fechaFiltro = LocalDate.parse(fechaY, formatter);

        String jpql = "SELECT DISTINCT p FROM Personaje p JOIN p.episodes e " +
                "WHERE p.name LIKE :nombre " +
                "AND FUNCTION('STR_TO_DATE', e.airDate, '%d %b %Y') > :fecha";

        TypedQuery<Personaje> query = em.createQuery(jpql, Personaje.class);
        query.setParameter("nombre", nombreInicio + "%");
        query.setParameter("fecha", fechaFiltro);
        return query.getResultList();
    }

    // Ubicaciones ordenadas por número de residentes decreciente
    public List<Localizacion> ubicacionesOrdenadasPorResidentes() {
        String jpql = "SELECT l FROM Localizacion l LEFT JOIN l.residents r " +
                "GROUP BY l.id ORDER BY COUNT(r) DESC";
        return em.createQuery(jpql, Localizacion.class).getResultList();
    }

    // Ubicaciones filtradas por tipo de ubicación y especie de residentes
    public List<Localizacion> ubicacionesPorTipoYEspecie(String tipo, String especie) {
        String jpql = "SELECT DISTINCT l FROM Localizacion l JOIN l.residents r " +
                "WHERE l.type = :tipo AND r.species = :especie";
        TypedQuery<Localizacion> query = em.createQuery(jpql, Localizacion.class);
        query.setParameter("tipo", tipo);
        query.setParameter("especie", especie);
        return query.getResultList();
    }
}
