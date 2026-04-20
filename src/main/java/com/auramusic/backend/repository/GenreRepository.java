package com.auramusic.backend.repository;

import com.auramusic.backend.model.Genre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GenreRepository extends JpaRepository<Genre, Integer> {
    Genre findByNombre(String nombre);
}