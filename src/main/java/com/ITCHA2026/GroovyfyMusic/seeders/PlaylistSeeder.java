package com.ITCHA2026.GroovyfyMusic.seeders;

import com.ITCHA2026.GroovyfyMusic.entities.Playlist;
import com.ITCHA2026.GroovyfyMusic.enums.TipoPlaylist;
import com.ITCHA2026.GroovyfyMusic.repository.PlaylistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class PlaylistSeeder implements CommandLineRunner {

    private final PlaylistRepository playlistRepository;

    private static final List<String> NOMBRES_DESTACADAS = List.of(
            "Éxitos del momento",
            "Vibras relajadas",
            "Chill Nocturno",
            "Mix diario 1",
            "Mix diario 2"
    );

    @Override
    public void run(String... args) {


        Set<String> existentesNombres = playlistRepository.findByTipo(TipoPlaylist.DESTACADA)
                .stream()
                .map(p -> p.getNombre().toLowerCase())
                .collect(Collectors.toSet());

        List<Playlist> nuevasPlaylists = new ArrayList<>();

        for (String nombre : NOMBRES_DESTACADAS) {
            if (!existentesNombres.contains(nombre.toLowerCase())) {
                Playlist playlist = new Playlist();
                playlist.setNombre(nombre);
                playlist.setTipo(TipoPlaylist.DESTACADA);
                playlist.setUsuario(null);
                nuevasPlaylists.add(playlist);
            }
        }

        if (!nuevasPlaylists.isEmpty()) {
            playlistRepository.saveAll(nuevasPlaylists);
        }
    }
}