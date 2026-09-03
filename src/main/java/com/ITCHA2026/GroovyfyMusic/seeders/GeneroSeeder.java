package com.ITCHA2026.GroovyfyMusic.seeders;

import com.ITCHA2026.GroovyfyMusic.entities.Genero;
import com.ITCHA2026.GroovyfyMusic.repository.GeneroRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class GeneroSeeder implements CommandLineRunner {

    private final GeneroRepository generoRepository;

    @Override
    public void run(String... args) throws Exception {
        if (generoRepository.count() == 0) {
            log.info("cargando los generos musicales...");

            List<String> nombresGeneros = List.of(
                    "Pop",
                    "Rock",
                    "Reggaetón",
                    "Hip-Hop",
                    "Trap",
                    "Música Clásica",
                    "Electrónica",
                    "Salsa",
                    "Bachata",
                    "Indie",
                    "Jazz",
                    "R&B",
                    "Metal",
                    "Blues",
                    "Country",
                    "Reggae",
                    "Merengue",
                    "Cumbia",
                    "K-Pop",
                    "House",
                    "Techno",
                    "Disco",
                    "Soul",
                    "Funk",
                    "Lo-Fi"
            );

            List<Genero> generos = nombresGeneros.stream()
                    .map(nombre -> {
                        Genero g = new Genero();
                        g.setNombre(nombre);
                        return g;
                    })
                    .toList();

            generoRepository.saveAll(generos);
            log.info("Seeder completado: Se guardaron {} géneros en la base de datos.", generos.size());
        } else {
            log.info(" La tabla 'genero' ya contiene registros. Se omite el seeding.");
        }
    }
}