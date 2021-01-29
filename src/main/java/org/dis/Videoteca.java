package org.dis;

import java.util.Date;
import java.util.List;

public class Videoteca {

    private String nombre;
    private String ubicacion;
    private List<Pelicula> peliculas;
    private String fechaActualizacion;


    public Videoteca(String nombre, String ubicacion, List<Pelicula> peliculas, String fechaActualizacion) {
        this.nombre = nombre;
        this.ubicacion = ubicacion;
        this.peliculas = peliculas;
        this.fechaActualizacion = fechaActualizacion;
    }

    public List<Pelicula> getPeliculas() {
        return peliculas;
    }

    @Override
    public String toString() {
        return "Nombre=" + nombre + ", Ubicacion=" + ubicacion + ", Peliculas="  + ", fechaActualizacion=" + fechaActualizacion;
    }

    public String getNombre() {
        return nombre;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public String getFechaActualizacion() {
        return fechaActualizacion;
    }
}