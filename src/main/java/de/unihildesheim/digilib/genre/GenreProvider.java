package de.unihildesheim.digilib.genre;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GenreProvider {


    private GenreRepository repository;

    public GenreProvider(GenreRepository repository) {
        this.repository = repository;
    }

    public Genre getOrSave(Genre genre) {
        if (genre == null) {
            return null;
        }

        Optional<Genre> foundGenre = repository.findByGenre(genre.getGenre());
        if (foundGenre.isEmpty()) {
            Genre newGenre = new Genre(genre.getGenre());
            return repository.save(newGenre);
        } else {
            return foundGenre.get();
        }
    }

    public List<Genre> find(String genre) {
        return repository.findByGenreContaining(genre);
    }
}
