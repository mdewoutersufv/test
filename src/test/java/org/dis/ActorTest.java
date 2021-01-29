package org.dis;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/*Unit test for Actor. */
public class ActorTest {
    Actor actor;

    @BeforeEach
    public void init(){
        actor = new Actor("Jhon Cena","https://es.wikipedia.org/wiki/John_Cena");
    }

    @Test
    @DisplayName("Test método getNombre () " )
    public void testGetNombre(){
        assertEquals("Jhon Cena",actor.getNombre());
    }

    @Test
    @DisplayName("Test método getEnlaceWikipedia ()")
    public void testGetEnlaceWikipedia(){
        assertEquals("https://es.wikipedia.org/wiki/John_Cena",actor.getEnlaceWikipedia());
    }

}

