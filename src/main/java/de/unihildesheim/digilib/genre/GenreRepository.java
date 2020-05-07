package de.unihildesheim.digilib.genre;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface GenreRepository extends JpaRepository<Genre, Long> {

    Optional<Genre> findByGenre(String genre);

    List<Genre> findByGenreContaining(String genre);

}
