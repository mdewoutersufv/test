package org.dis;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/*Unit test for Pelicula. */
public class PeliculaTest {
    Actor actor1;
    Actor actor2;
    List<Actor> reparto = new ArrayList<Actor>();
    Pelicula pelicula;

    @BeforeEach
    public void init(){
        actor1 = new Actor("Jhon Cena","https://es.wikipedia.org/wiki/John_Cena");
        actor2 = new Actor("Jhon Cena2","https://es.wikipedia.org/wiki/John_Cena");
        reparto.add(actor1);
        reparto.add(actor2);
        pelicula = new Pelicula("Terminator","es una sinopsis","Acción","es un enlace",1984,108,reparto);
    }

    @Test
    @DisplayName("Test método getTitulo () " )
    public void testGetTitulo(){
        assertEquals("Terminator",pelicula.getTitulo());
    }

    @Test
    @DisplayName("Test método getSinposis () " )
    public void testGetSinposis(){
        assertEquals("es una sinopsis",pelicula.getSinopsis());
    }

    @Test
    @DisplayName("Test método getGenero () " )
    public void testGetGenero(){
        assertEquals("Acción",pelicula.getGenero());
    }

    @Test
    @DisplayName("Test método getEnlace () " )
    public void testGetEnlace(){
        assertEquals("es un enlace",pelicula.getEnlace());
    }

    @Test
    @DisplayName("Test método getAgno () " )
    public void testGetAgno(){
        assertEquals(1984,pelicula.getAgno());
    }

    @Test
    @DisplayName("Test método getDuracion () " )
    public void testGetDuracion(){
        assertEquals(108,pelicula.getDuracion());
    }

    @Test
    @DisplayName("Test método getReparto () " )
    public void testGetReparto(){
        assertEquals(reparto,pelicula.getReparto());
    }


}

