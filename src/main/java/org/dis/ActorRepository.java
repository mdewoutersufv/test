package org.dis;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ActorRepository extends JpaRepository<Actor, Long> {

    List<Actor> findByNombreStartsWithIgnoreCase(String nombre);
}
