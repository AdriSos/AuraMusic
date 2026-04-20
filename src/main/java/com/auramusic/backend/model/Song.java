package com.auramusic.backend.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDate;

@Entity
@Table(name = "songs")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Song {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private Integer duracion;

    private String album;

    @Column(name = "imagen_url")
    private String imagenUrl;

    @Column(name = "audio_url")
    private String audioUrl;

    @Column(columnDefinition = "TEXT")
    private String letra;

    @Column(name = "fecha_lanzamiento")
    private LocalDate fechaLanzamiento;


    @ManyToOne
    @JoinColumn(name = "genre_id")
    private Genre genre;


    @ManyToOne
    @JoinColumn(name = "artist_id")
    private Artist artist;
}