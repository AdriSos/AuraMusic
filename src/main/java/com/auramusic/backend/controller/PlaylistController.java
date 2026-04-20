package com.auramusic.backend.controller;

import com.auramusic.backend.model.*;
import com.auramusic.backend.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;

@RestController
@RequestMapping("/api/playlists")
@CrossOrigin(origins = "*")
public class PlaylistController {

    @Autowired private PlaylistRepository playlistRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private SongRepository songRepository;

    @GetMapping("/user/{email}")
    public ResponseEntity<List<Playlist>> getUserPlaylists(@PathVariable String email) {
        User user = userRepository.findByEmail(email);
        if(user == null) return ResponseEntity.badRequest().build();
        return ResponseEntity.ok(playlistRepository.findByUserId(user.getId()));
    }

    @PostMapping("/create")
    public ResponseEntity<String> createPlaylist(@RequestBody Map<String, String> payload) {
        User user = userRepository.findByEmail(payload.get("email"));
        Playlist p = new Playlist();
        p.setNombre(payload.get("nombre"));
        p.setUser(user);
        p.setSongs(new ArrayList<>());
        playlistRepository.save(p);
        return ResponseEntity.ok("Playlist creada");
    }

    @PostMapping("/add-song")
    public ResponseEntity<String> addSong(@RequestBody Map<String, Integer> payload) {
        Playlist p = playlistRepository.findById(payload.get("playlistId")).orElse(null);
        Song s = songRepository.findById(payload.get("songId")).orElse(null);
        if(p != null && s != null) {
            if(!p.getSongs().contains(s)) {
                p.getSongs().add(s);
                playlistRepository.save(p);
                return ResponseEntity.ok("Añadida a la playlist ");
            }
            return ResponseEntity.ok("La canción ya está en esta playlist.");
        }
        return ResponseEntity.badRequest().body("Error al añadir");
    }

    @GetMapping("/{id}/songs")
    public ResponseEntity<List<Song>> getPlaylistSongs(@PathVariable Integer id) {
        Playlist p = playlistRepository.findById(id).orElse(null);
        if(p == null) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(p.getSongs());
    }


    @PostMapping("/remove-song")
    public ResponseEntity<String> removeSong(@RequestBody Map<String, Integer> payload) {
        Playlist p = playlistRepository.findById(payload.get("playlistId")).orElse(null);
        Song s = songRepository.findById(payload.get("songId")).orElse(null);
        if(p != null && s != null) {
            p.getSongs().remove(s);
            playlistRepository.save(p);
            return ResponseEntity.ok("Canción removida de la playlist 🗑");
        }
        return ResponseEntity.badRequest().body("Error al remover");
    }

    // NUEVO: Método para eliminar la playlist completa
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePlaylist(@PathVariable Integer id) {
        playlistRepository.deleteById(id);
        return ResponseEntity.ok("Playlist eliminada por completo ");
    }
}