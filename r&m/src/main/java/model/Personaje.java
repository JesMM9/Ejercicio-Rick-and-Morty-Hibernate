package main.java.model;

/*
 * Ejemplo contenido de un personaje:
 * 
 * {
      "id": 7,
      "name": "Abradolf Lincler",
      "status": "unknown",
      "species": "Human",
      "type": "Genetic experiment",
      "gender": "Male",
      "origin": {
        "name": "Earth (Replacement Dimension)",
        "url": "https://rickandmortyapi.com/api/location/20"
      },
      "location": {
        "name": "Testicle Monster Dimension",
        "url": "https://rickandmortyapi.com/api/location/21"
      },
      "image": "https://rickandmortyapi.com/api/character/avatar/7.jpeg",
      "episode": [
        "https://rickandmortyapi.com/api/episode/10",
        "https://rickandmortyapi.com/api/episode/11"
      ],
      "url": "https://rickandmortyapi.com/api/character/7",
      "created": "2017-11-04T19:59:20.523Z"
    }
 * 
 * 
 * */

public class Personaje {

	public static final String PERSONAJE_URL_API = "https://rickandmortyapi.com/api/character/";

	private long id;
	private String name;
	private String status;
	private String species;
	private String type;
	private String gender;
	private String url;
	private String created;
	private String[] episode;

	public Personaje(long id, String name, String status, String species, String type, String gender, String url,
			String created, String[] episode) {
		super();
		this.id = id;
		this.name = name;
		this.status = status;
		this.species = species;
		this.type = type;
		this.gender = gender;
		this.url = url;
		this.created = created;
		this.episode=episode;
	}

	public Personaje(long id, String name, String species) {
		super();
		this.id = id;
		this.name = name;
		this.species = species;
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

	public String getSpecies() {
		return species;
	}

	public void setSpecies(String species) {
		this.species = species;
	}
	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getCreated() {
		return created;
	}

	public void setCreated(String created) {
		this.created = created;
	}
	
	public String[] getEpisode() {
		return episode;
	}

	public void setEpisode(String[] episode) {
		this.episode = episode;
	}

	public Personaje() {
		super();
	}

	@Override
	public String toString() {
		return "Personaje [id="+id+", name="+name+", status="+status+", "
				+ "species="+species+", type="+type+", gender="+gender+", url="+url+", created="+created+"]";
	}

	
}
