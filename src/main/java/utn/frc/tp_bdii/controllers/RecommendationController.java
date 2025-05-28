package utn.frc.tp_bdii.controllers;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import utn.frc.tp_bdii.models.User;
import utn.frc.tp_bdii.repositories.UserRepository;
import utn.frc.tp_bdii.services.MovieService;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/user")
public class RecommendationController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MovieService movieService;

    @GetMapping("/recommendations")
    public ResponseEntity<?> getRecommendations(HttpServletRequest request) {
        String username = (String) request.getAttribute("username");
        User user = userRepository.findByUsername(username);

        Set<String> ratedAndFav = new HashSet<>(user.getFavorites());
        user.getRatings().forEach(r -> ratedAndFav.add(r.getMovieId()));

        Map<String, Integer> genreCount = new HashMap<>();

        // Contar géneros en favoritos y puntuaciones
        for (String movieId : ratedAndFav) {
            Map<String, Object> movie = movieService.getMovieDetailsAsMap(movieId);
            List<Map<String, Object>> genres = (List<Map<String, Object>>) movie.get("genres");
            for (Map<String, Object> g : genres) {
                String name = (String) g.get("name");
                genreCount.put(name, genreCount.getOrDefault(name, 0) + 1);
            }
        }

        if (genreCount.isEmpty()) return ResponseEntity.ok(Collections.emptyList());

        // Elegir el género dominante
        String topGenre = genreCount.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .get()
                .getKey();

        // Buscar películas buenas de ese género
        List<Map<String, Object>> discover = movieService.discoverTopMoviesByGenre(topGenre);

        List<Map<String, Object>> filtered = discover.stream()
                .filter(m -> !ratedAndFav.contains(String.valueOf(m.get("id"))))
                .limit(10)
                .collect(Collectors.toList());

        return ResponseEntity.ok(filtered);
    }
}
