package main.java;

import main.crud.PersonajeCRUD;
import main.model.Personaje;

public class Principal {
    public static void main(String[] args) {
        PersonajeCRUD crud = new PersonajeCRUD();

        // Crear personajes
        crud.guardar(new Personaje("Alucard", "Castlevania", 95000));
        crud.guardar(new Personaje("Goku", "Dragon Ball", 100000));
        crud.guardar(new Personaje("Zorro", "One Piece", 80000));

        // Listar
        System.out.println("📜 Todos los personajes:");
        crud.listarTodos().forEach(System.out::println);

        // Buscar uno
        System.out.println("\n🔍 Personaje con ID 1:");
        System.out.println(crud.buscar(1));

        // Actualizar
        crud.actualizarPoder(1, 120000);
        System.out.println("\n⚡ Poder actualizado:");
        System.out.println(crud.buscar(1));

        // Eliminar
        crud.eliminar(2);
        System.out.println("\n💀 Lista después de eliminar ID 2:");
        crud.listarTodos().forEach(System.out::println);
    }
}
