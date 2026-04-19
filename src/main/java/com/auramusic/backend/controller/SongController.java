package com.auramusic.backend.controller;

import com.auramusic.backend.model.Song;
import com.auramusic.backend.repository.SongRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController // Le dice a Spring Boot que este es un "mesero" que responde datos (API REST)
@RequestMapping("/api/songs") // La ruta principal en el navegador para llegar aquí
@CrossOrigin(origins = "*") // Permite que cualquier app (web o móvil) se conecte sin bloqueos de seguridad
public class SongController {

    @Autowired // Esto conecta automáticamente a nuestro "mesero" con el "cocinero" (Repositorio)
    private SongRepository songRepository;

    // 1. Método para OBTENER todas las canciones (Lo usará el cliente)
    @GetMapping
    public List<Song> getAllSongs() {
        return songRepository.findAll(); // El repositorio busca todas las canciones y las devuelve
    }

    // 2. Método para GUARDAR una nueva canción (Lo usará el Administrador)
    @PostMapping
    public Song createSong(@RequestBody Song song) {
        return songRepository.save(song); // El repositorio guarda los datos que le enviamos
    }
}