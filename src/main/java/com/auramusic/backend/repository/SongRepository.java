package com.auramusic.backend.repository;

import com.auramusic.backend.model.Song;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SongRepository extends JpaRepository<Song, Integer> {
    // ¡Así de simple! Spring Boot ya sabe cómo guardar, borrar y buscar canciones solo con esta línea.
}