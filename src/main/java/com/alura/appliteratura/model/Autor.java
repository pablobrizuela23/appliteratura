package com.alura.appliteratura.model;

import jakarta.persistence.*;

@Entity
public class Autor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;
    private String fechaNac;
    private String anioFallecimiento;


    public Autor(){}
    public Autor(DatosAutor datos) {
        this.nombre = datos.nombre();
        this.fechaNac = datos.fechaNac();
        this.anioFallecimiento = datos.anioFallecimiento();
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getFechaNac() {
        return fechaNac;
    }

    public void setFechaNac(String fechaNac) {
        this.fechaNac = fechaNac;
    }

    public String getAnioFallecimiento() {
        return anioFallecimiento;
    }

    public void setAnioFallecimiento(String anioFallecimiento) {
        this.anioFallecimiento = anioFallecimiento;
    }
}
