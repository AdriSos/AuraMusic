package com.auramusic.backend.controller;

import com.auramusic.backend.model.*;
import com.auramusic.backend.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/likes")
@CrossOrigin(origins = "*")
public class LikeController {

    @Autowired private LikeRepository likeRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private SongRepository songRepository;

    @PostMapping("/toggle")
    @Transactional // Necesario para poder borrar de la base de datos
    public ResponseEntity<String> toggleLike(@RequestBody Map<String, Object> payload) {
        String email = (String) payload.get("email");
        Integer songId = (Integer) payload.get("songId");

        User user = userRepository.findByEmail(email);
        Song song = songRepository.findById(songId).orElse(null);

        if (user == null || song == null) {
            return ResponseEntity.badRequest().body("Error al buscar usuario o canción");
        }

        // Si ya le había dado "Me gusta", lo quitamos. Si no, lo agregamos.
        if (likeRepository.existsByUserIdAndSongId(user.getId(), song.getId())) {
            likeRepository.deleteByUserIdAndSongId(user.getId(), song.getId());
            return ResponseEntity.ok("Removido de favoritos ");
        } else {
            Like like = new Like();
            like.setUser(user);
            like.setSong(song);
            likeRepository.save(like);
            return ResponseEntity.ok("Añadido a favoritos ❤");
        }
    }

    @GetMapping("/user/{email}")
    public ResponseEntity<java.util.List<Song>> getFavorites(@PathVariable String email) {
        User user = userRepository.findByEmail(email);
        if(user == null) return ResponseEntity.badRequest().build();

        java.util.List<Like> likes = likeRepository.findByUserId(user.getId());
        java.util.List<Song> favSongs = new java.util.ArrayList<>();
        for(Like l : likes) { favSongs.add(l.getSong()); }

        return ResponseEntity.ok(favSongs);
    }
}