package main.java.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

/*
 * Ejemplo contenido de un episodio:
 * 
 * {
      "id": 1,
      "name": "Pilot",
      "air_date": "December 2, 2013",
      "episode": "S01E01",
      "characters": [
        "https://rickandmortyapi.com/api/character/1",
        "https://rickandmortyapi.com/api/character/2",
        "https://rickandmortyapi.com/api/character/35",
        "https://rickandmortyapi.com/api/character/38",
        "https://rickandmortyapi.com/api/character/62",
        "https://rickandmortyapi.com/api/character/92",
        "https://rickandmortyapi.com/api/character/127",
        "https://rickandmortyapi.com/api/character/144",
        "https://rickandmortyapi.com/api/character/158",
        "https://rickandmortyapi.com/api/character/175",
        "https://rickandmortyapi.com/api/character/179",
        "https://rickandmortyapi.com/api/character/181",
        "https://rickandmortyapi.com/api/character/239",
        "https://rickandmortyapi.com/api/character/249",
        "https://rickandmortyapi.com/api/character/271",
        "https://rickandmortyapi.com/api/character/338",
        "https://rickandmortyapi.com/api/character/394",
        "https://rickandmortyapi.com/api/character/395",
        "https://rickandmortyapi.com/api/character/435"
      ],
      "url": "https://rickandmortyapi.com/api/episode/1",
      "created": "2017-11-10T12:56:33.798Z"
    }
 * 
 * 
 * */
@Entity
public class Episodio {

	public static final String EPISODIO_URL_API = "https://rickandmortyapi.com/api/episode/";

	@Id
	private long id;
	@Column
	private String name;
	@Column
	private String air_date;
	@Column
	private String episode;
	@Column
	private String[] characters;
	
	public Episodio(long id, String name, String air_date, String episode, String[] characters) {
		super();
		this.id = id;
		this.name = name;
		this.air_date = air_date;
		this.episode = episode;
		this.characters = characters;
	}

	public long getId() {
		return id;
	}



	public void setId(long id) {
		this.id = id;
	}



	public String getName() {
		return name;
	}



	public void setName(String name) {
		this.name = name;
	}



	public String getAir_date() {
		return air_date;
	}



	public void setAir_date(String air_date) {
		this.air_date = air_date;
	}



	public String getEpisode() {
		return episode;
	}



	public void setEpisode(String episode) {
		this.episode = episode;
	}



	public String[] getCharacters() {
		return characters;
	}



	public void setCharacters(String[] characters) {
		this.characters = characters;
	}

	@Override
	public String toString() {
		return "Episodio [id="+id+", name="+name+", air_date="+air_date+", episode="+episode+"]";
	}

	
}
