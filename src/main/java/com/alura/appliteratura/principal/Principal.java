package com.alura.appliteratura.principal;

import com.alura.appliteratura.model.DatosLibros;
import com.alura.appliteratura.service.ConsumoApi;
import com.alura.appliteratura.service.ConvierteDatos;

import java.net.URL;
import java.util.Scanner;

public class Principal {

    private Scanner teclado = new Scanner(System.in);

    private ConsumoApi consumoApi = new ConsumoApi();
    private String URL_BASE = "gutendex.com";
    private ConvierteDatos convierteDatos = new ConvierteDatos();


    public void mostrarMenu(){
        System.out.println("Escriba el nombre del libro que desea buscar");

        //busca datos generales de los libros
        var nombreLibro = teclado.nextLine();

        var json = consumoApi.obtenerDatos(URL_BASE +"/books" );
        var datos = convierteDatos.obtenerDatos(json, DatosLibros.class);
        System.out.println(datos);
    }
}
