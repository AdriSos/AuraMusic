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
import org.springframework.http.ResponseEntity;
import java.util.List;

import java.util.List;

@RestController
@RequestMapping("/api/songs")
@CrossOrigin(origins = "*")
public class SongController {

    @Autowired
    private SongRepository songRepository;

    @Autowired
    private ArtistRepository artistRepository;

    @Autowired
    private GenreRepository genreRepository;


    @GetMapping
    public List<Song> getAllSongs() {
        return songRepository.findAll(); // El repositorio busca todas las canciones y las devuelve
    }

    @PostMapping
    public ResponseEntity<?> createSong(@RequestBody Song song) {
        try {
            System.out.println("--- INTENTANDO GUARDAR CANCION ---");
            System.out.println("Cancion: " + song.getNombre());


            if (song.getArtist() != null && song.getArtist().getNombre() != null) {
                System.out.println("Buscando artista: " + song.getArtist().getNombre());
                Artist existingArtist = artistRepository.findByNombre(song.getArtist().getNombre());
                if (existingArtist != null) {
                    System.out.println("Artista encontrado, asociando...");
                    song.setArtist(existingArtist);
                } else {
                    System.out.println("Artista no encontrado, creando nuevo...");
                    Artist newArtist = new Artist();
                    newArtist.setNombre(song.getArtist().getNombre());
                    artistRepository.save(newArtist);
                    song.setArtist(newArtist);
                }
            }


            if (song.getGenre() != null && song.getGenre().getNombre() != null) {
                System.out.println("Buscando genero: " + song.getGenre().getNombre());
                Genre existingGenre = genreRepository.findByNombre(song.getGenre().getNombre());
                if (existingGenre != null) {
                    System.out.println("Genero encontrado, asociando...");
                    song.setGenre(existingGenre);
                } else {
                    System.out.println("Genero no encontrado, creando nuevo...");
                    Genre newGenre = new Genre();
                    newGenre.setNombre(song.getGenre().getNombre());
                    genreRepository.save(newGenre);
                    song.setGenre(newGenre);
                }
            }

            // 3. Guardar la canción final
            System.out.println("Guardando cancion final...");
            Song savedSong = songRepository.save(song);
            System.out.println("¡CANCION GUARDADA CON EXITO!");
            return ResponseEntity.ok(savedSong);

        } catch (Exception e) {
            System.err.println("!!! ERROR FATAL AL GUARDAR LA CANCION !!!");
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error interno al guardar: " + e.getMessage());
        }
    }


    @PutMapping("/{id}")
    public ResponseEntity<?> updateSong(@PathVariable Integer id, @RequestBody Song songDetails) {
        try {
            Song existingSong = songRepository.findById(id).orElse(null);
            if (existingSong == null) {
                return ResponseEntity.notFound().build(); // Si no existe, lanza error 404
            }

            // Actualizamos los datos básicos
            existingSong.setNombre(songDetails.getNombre());
            existingSong.setDuracion(songDetails.getDuracion());
            existingSong.setAlbum(songDetails.getAlbum());
            existingSong.setImagenUrl(songDetails.getImagenUrl());
            existingSong.setAudioUrl(songDetails.getAudioUrl());
            existingSong.setFechaLanzamiento(songDetails.getFechaLanzamiento());

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