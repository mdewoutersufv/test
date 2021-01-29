package org.dis;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import java.util.List;

@Entity
public class Actor
{
    @Id
    @GeneratedValue
    private Long actorId;

    private String nombre;
    private String enlaceWikipedia;
    @ManyToMany(mappedBy = "reparto")
    private List<Pelicula> peliculas;

    public Actor(String nombre, String enlaceWikipedia)
    {
        this.nombre = nombre;
        this.enlaceWikipedia = enlaceWikipedia;
    }

    protected Actor() {

    }

    @Override
    public String toString()
    {
        return "Nombre: " + nombre + ", Enlace de la Wikipedia:" + enlaceWikipedia;
    }

    public Long getActorId() {
        return actorId;
    }

    public void setActorId(Long actorId) {
        this.actorId = actorId;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEnlaceWikipedia() {
        return enlaceWikipedia;
    }

    public void setEnlaceWikipedia(String enlaceWikipedia) {
        this.enlaceWikipedia = enlaceWikipedia;
    }

    public List<Pelicula> getPeliculas() {
        return peliculas;
    }

    public void setPeliculas(List<Pelicula> peliculas) {
        this.peliculas = peliculas;
    }

}
