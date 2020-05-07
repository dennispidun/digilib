package de.unihildesheim.digilib.genre;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.websocket.server.PathParam;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/genres")
public class GenreController {

    private GenreProvider provider;

    public GenreController(GenreProvider provider) {
        this.provider = provider;
    }

    @GetMapping
    public List<String> findGenres(@PathParam("search") String search) {
        return provider.find(search)
                .stream()
                .map(genre -> genre.getGenre())
                .collect(Collectors.toList());
    }

}
