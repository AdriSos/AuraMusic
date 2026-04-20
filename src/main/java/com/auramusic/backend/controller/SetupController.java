package com.auramusic.backend.controller;

import com.auramusic.backend.model.Artist;
import com.auramusic.backend.model.Genre;
import com.auramusic.backend.model.User;
import com.auramusic.backend.repository.ArtistRepository;
import com.auramusic.backend.repository.GenreRepository;
import com.auramusic.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SetupController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ArtistRepository artistRepository;
    @Autowired
    private GenreRepository genreRepository;

    @GetMapping("/api/setup/init")
    public String initData() {
        // 1. Crear el usuario Administrador
        try {
            User admin = new User();
            admin.setNombre("Super Admin");
            admin.setEmail("admin@auramusic.com");
            admin.setPassword("fb95b5e0");
            admin.setRol("admin");
            userRepository.save(admin);
        } catch (Exception e) {

        }

        // 2. Crear un par de Artistas
        try {
            Artist a1 = new Artist(); a1.setNombre("Taylor Swift"); artistRepository.save(a1);
            Artist a2 = new Artist(); a2.setNombre("Bad Bunny"); artistRepository.save(a2);
        } catch (Exception e) {}

        // 3. Crear un par de Géneros
        try {
            Genre g1 = new Genre(); g1.setNombre("Pop"); genreRepository.save(g1);
            Genre g2 = new Genre(); g2.setNombre("Reggaeton"); genreRepository.save(g2);
        } catch (Exception e) {}

        return "¡Magia completada! Base de datos inicializada. Ya puedes iniciar sesion.";
    }
}