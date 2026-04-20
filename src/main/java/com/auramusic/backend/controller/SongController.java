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

@RestController
@RequestMapping("/api/songs")
@CrossOrigin(origins = "*")
public class SongController {

    @Autowired private SongRepository songRepository;
    @Autowired private ArtistRepository artistRepository;
    @Autowired private GenreRepository genreRepository;

    @GetMapping
    public List<Song> getAllSongs() {
        return songRepository.findAll();
    }

    @PostMapping
    public ResponseEntity<?> createSong(@RequestBody Song song) {
        try {
            // --- NUEVA REGLA: EVITAR CANCIONES REPETIDAS ---
            List<Song> todas = songRepository.findAll();
            for (Song s : todas) {
                if (s.getNombre().equalsIgnoreCase(song.getNombre()) &&
                        s.getArtist() != null && song.getArtist() != null &&
                        s.getArtist().getNombre().equalsIgnoreCase(song.getArtist().getNombre())) {
                    return ResponseEntity.badRequest().body("Error: ¡La canción '" + song.getNombre() + "' de " + song.getArtist().getNombre() + " ya está registrada!");
                }
            }

            // 1. Guardar Artista
            if (song.getArtist() != null && song.getArtist().getNombre() != null) {
                Artist existingArtist = artistRepository.findByNombre(song.getArtist().getNombre());
                if (existingArtist != null) { song.setArtist(existingArtist); }
                else {
                    Artist newArtist = new Artist(); newArtist.setNombre(song.getArtist().getNombre());
                    artistRepository.save(newArtist); song.setArtist(newArtist);
                }
            }

            // 2. Guardar Género
            if (song.getGenre() != null && song.getGenre().getNombre() != null) {
                Genre existingGenre = genreRepository.findByNombre(song.getGenre().getNombre());
                if (existingGenre != null) { song.setGenre(existingGenre); }
                else {
                    Genre newGenre = new Genre(); newGenre.setNombre(song.getGenre().getNombre());
                    genreRepository.save(newGenre); song.setGenre(newGenre);
                }
            }

            Song savedSong = songRepository.save(song);
            return ResponseEntity.ok(savedSong);

        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error interno al guardar: " + e.getMessage());
        }
    }

    // --- AHORA ESTE MÉTODO ACTUALIZA TODOS LOS CAMPOS ---
    @PutMapping("/{id}")
    public ResponseEntity<?> updateSong(@PathVariable Integer id, @RequestBody Song songDetails) {
        try {
            Song existingSong = songRepository.findById(id).orElse(null);
            if (existingSong == null) { return ResponseEntity.notFound().build(); }

            existingSong.setNombre(songDetails.getNombre());
            existingSong.setDuracion(songDetails.getDuracion());
            existingSong.setAlbum(songDetails.getAlbum());
            existingSong.setImagenUrl(songDetails.getImagenUrl());
            existingSong.setAudioUrl(songDetails.getAudioUrl());
            existingSong.setFechaLanzamiento(songDetails.getFechaLanzamiento());

            // Actualizar Artista en Edición
            if (songDetails.getArtist() != null && songDetails.getArtist().getNombre() != null) {
                Artist existingArtist = artistRepository.findByNombre(songDetails.getArtist().getNombre());
                if (existingArtist != null) { existingSong.setArtist(existingArtist); }
                else {
                    Artist newArtist = new Artist(); newArtist.setNombre(songDetails.getArtist().getNombre());
                    artistRepository.save(newArtist); existingSong.setArtist(newArtist);
                }
            }

            // Actualizar Género en Edición
            if (songDetails.getGenre() != null && songDetails.getGenre().getNombre() != null) {
                Genre existingGenre = genreRepository.findByNombre(songDetails.getGenre().getNombre());
                if (existingGenre != null) { existingSong.setGenre(existingGenre); }
                else {
                    Genre newGenre = new Genre(); newGenre.setNombre(songDetails.getGenre().getNombre());
                    genreRepository.save(newGenre); existingSong.setGenre(newGenre);
                }
            }

            Song updatedSong = songRepository.save(existingSong);
            return ResponseEntity.ok(updatedSong);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error al actualizar la canción: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteSong(@PathVariable Integer id) {
        try {
            songRepository.deleteById(id);
            return ResponseEntity.ok("Canción eliminada correctamente");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error al eliminar la canción: " + e.getMessage());
        }
    }
}