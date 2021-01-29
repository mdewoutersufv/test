package org.dis;

import javax.persistence.*;
import java.util.List;

@Entity
public class Pelicula {

    @Id
    @GeneratedValue
    private Long peliculaId;
    private String titulo;
    @Column(columnDefinition = "TEXT")
    private String sinopsis;
    private String genero;
    private String enlace;
    private int agno;
    private int duracion;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinTable(name = "pelicula_actor",
            joinColumns = @JoinColumn(name = "pelicula_id"),
            inverseJoinColumns = @JoinColumn(name = "actor_id"))
    private List<Actor> reparto;


    public Pelicula(String titulo, String sinopsis, String genero, String enlace, int agno, int duracion, List<Actor> reparto) {
        this.titulo = titulo;
        this.sinopsis = sinopsis;
        this.genero = genero;
        this.enlace = enlace;
        this.agno = agno;
        this.duracion = duracion;
        this.reparto = reparto;
    }

    public Pelicula(String titulo, String sinopsis, String enlace, int agno, int duracion, List<Actor> reparto) {
        this.titulo = titulo;
        this.sinopsis = sinopsis;
        this.genero = null;
        this.enlace = enlace;
        this.agno = agno;
        this.duracion = duracion;
        this.reparto = reparto;
    }

    protected Pelicula() {

    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getSinopsis() {
        return sinopsis;
    }

    public void setSinopsis(String sinopsis) {
        this.sinopsis = sinopsis;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public String getEnlace() {
        return enlace;
    }

    public void setEnlace(String enlace) {
        this.enlace = enlace;
    }

    public int getAgno() {
        return agno;
    }

    public void setAgno(int agno) {
        this.agno = agno;
    }

    public int getDuracion() {
        return duracion;
    }

    public void setDuracion(int duracion) {
        this.duracion = duracion;
    }

    public List<Actor> getReparto() {
        return reparto;
    }

    public void setReparto(List<Actor> reparto) {
        this.reparto = reparto;
    }

    @Override
    public String toString() {
        return "Titulo=" + titulo + ", Sinopsis=" + sinopsis + ", Genero=" + genero + ", Enlace=" + enlace +
                ", Año=" + agno + ", Duracion=" + duracion + ", reparto=";
    }

    public Long getPeliculaId() {
        return peliculaId;
    }

    public void setPeliculaId(Long peliculaId) {
        this.peliculaId = peliculaId;
    }

    public void aniadirActor(Actor actor){
        reparto.add(actor);
    }
    public void eliminarActor(Actor actor){
        reparto.remove(actor);
    }

}
