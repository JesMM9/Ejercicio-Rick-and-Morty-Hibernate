package main.java;

import org.hibernate.Session;

import main.java.com.hibernate.ConnectionUtil;
import main.java.model.Personaje;
import main.java.model.Episodio;

public class Principal {
	
	public static void main(String[] args) {
		
		//Creamos y comenzamos transacción con base de datos
		Session session=ConnectionUtil.getSessionFactory().openSession();
		session.beginTransaction();
				
		//Generamos un nuevo personaje
		Personaje personaje=new Personaje(0, null, null, null, null, null, null, null, args);
		
		//Generamos un nuevo episodio
		Episodio episodio=new Episodio(0, null, null, null, args);
		
		//Persistimos esta entidad
		session.persist(personaje);
		session.persist(episodio);
				
		//Hacemos commit en base de datos
		session.getTransaction().commit();
				
		//Liberamos la conexión
		session.close();
		
	}
	
	public void obtenerPersonaje() {
		Session session=ConnectionUtil.getSessionFactory().openSession();
		session.beginTransaction();
		var personaje=session.find(Personaje.class, 1L);
		
		assert(personaje!=null && personaje.getId()==1L);
		
		session.close();
		
	}
	
	public void obtenerEpisodio() {
		Session session=ConnectionUtil.getSessionFactory().openSession();
		session.beginTransaction();
		var episodio=session.find(Episodio.class, 1L);
		
		assert(episodio!=null && episodio.getId()==1L);
		
		session.close();
		
	}

}
