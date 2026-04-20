package com.auramusic.backend.controller;

import com.auramusic.backend.model.Song;
import com.auramusic.backend.repository.SongRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import com.auramusic.backend.model.Artist;
import com.auramusic.backend.model.Genre;
import com.auramusic.backend.repository.ArtistRepository;
import com.auramusic.backend.repository.GenreRepository;

import java.util.List;

@RestController // Le dice a Spring Boot que este es un "mesero" que responde datos (API REST)
@RequestMapping("/api/songs") // La ruta principal en el navegador para llegar aquí
@CrossOrigin(origins = "*") // Permite que cualquier app (web o móvil) se conecte sin bloqueos de seguridad
public class SongController {

    @Autowired // Esto conecta automáticamente a nuestro "mesero" con el "cocinero" (Repositorio)
    private SongRepository songRepository;

    @Autowired
    private ArtistRepository artistRepository;

    @Autowired
    private GenreRepository genreRepository;

    // 1. Método para OBTENER todas las canciones (Lo usará el cliente)
    @GetMapping
    public List<Song> getAllSongs() {
        return songRepository.findAll(); // El repositorio busca todas las canciones y las devuelve
    }

    // 2. Método para GUARDAR una nueva canción (Lo usará el Administrador)
    @PostMapping
    public ResponseEntity<Song> createSong(@RequestBody Song song) {
        // 1. Revisar y guardar el Artista
        if (song.getArtist() != null && song.getArtist().getNombre() != null) {
            Artist existingArtist = artistRepository.findByNombre(song.getArtist().getNombre());
            if (existingArtist != null) {
                song.setArtist(existingArtist); // Si ya existe, lo usa
            } else {
                Artist newArtist = new Artist();
                newArtist.setNombre(song.getArtist().getNombre());
                artistRepository.save(newArtist); // Si no existe, lo crea
                song.setArtist(newArtist);
            }
        }

        // 2. Revisar y guardar el Género
        if (song.getGenre() != null && song.getGenre().getNombre() != null) {
            Genre existingGenre = genreRepository.findByNombre(song.getGenre().getNombre());
            if (existingGenre != null) {
                song.setGenre(existingGenre); // Si ya existe, lo usa
            } else {
                Genre newGenre = new Genre();
                newGenre.setNombre(song.getGenre().getNombre());
                genreRepository.save(newGenre); // Si no existe, lo crea
                song.setGenre(newGenre);
            }
        }

        // 3. Guardar la canción final
        Song savedSong = songRepository.save(song);
        return ResponseEntity.ok(savedSong);
    }
}