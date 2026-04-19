package com.auramusic.backend.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.List;

@Entity
@Table(name = "playlists")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Playlist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String nombre;

    // Relación: Muchas playlists pertenecen a un Usuario
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    // Relación Muchos a Muchos: Una playlist tiene muchas canciones
    @ManyToMany
    @JoinTable(
            name = "playlist_songs", // El nombre de la tabla intermedia en SQL
            joinColumns = @JoinColumn(name = "playlist_id"),
            inverseJoinColumns = @JoinColumn(name = "song_id")
    )
    private List<Song> songs;
}