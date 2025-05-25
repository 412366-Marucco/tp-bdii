package utn.frc.tp_bdii.controllers;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import utn.frc.tp_bdii.models.Rating;
import utn.frc.tp_bdii.models.User;
import utn.frc.tp_bdii.repositories.UserRepository;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private UserRepository userRepository;

    private boolean isAdmin(HttpServletRequest request) {
        String role = (String) request.getAttribute("role");
        return "ADMIN".equalsIgnoreCase(role);
    }

    @GetMapping("/stats")
    public ResponseEntity<?> getStats(HttpServletRequest request) {
        if (!isAdmin(request)) return ResponseEntity.status(403).body("No autorizado");

        long totalUsers = userRepository.count();
        long totalRatings = userRepository.findAll().stream()
                .mapToLong(u -> u.getRatings().size()).sum();
        long totalFavorites = userRepository.findAll().stream()
                .mapToLong(u -> u.getFavorites().size()).sum();

        return ResponseEntity.ok(Map.of(
                "totalUsers", totalUsers,
                "totalRatings", totalRatings,
                "totalFavorites", totalFavorites
        ));
    }

    @GetMapping("/active-users")
    public ResponseEntity<?> getMostActiveUsers(HttpServletRequest request) {
        if (!isAdmin(request)) return ResponseEntity.status(403).body("No autorizado");

        List<Map<String, Object>> active = userRepository.findAll().stream()
                .map(u -> {
                    Map<String, Object> result = new HashMap<>();
                    result.put("username", u.getUsername());
                    result.put("ratings", u.getRatings().size());
                    result.put("favorites", u.getFavorites().size());
                    result.put("activity", u.getRatings().size() + u.getFavorites().size());
                    return result;
                })

                .sorted((a, b) -> Integer.compare((int) b.get("activity"), (int) a.get("activity")))
                .limit(10)
                .collect(Collectors.toList());

        return ResponseEntity.ok(active);
    }

    @GetMapping("/most-voted")
    public ResponseEntity<?> getMostVotedMovies(HttpServletRequest request) {
        if (!isAdmin(request)) return ResponseEntity.status(403).body("No autorizado");

        Map<String, Long> countPerMovie = new HashMap<>();

        for (User user : userRepository.findAll()) {
            for (Rating rating : user.getRatings()) {
                countPerMovie.put(
                        rating.getMovieId(),
                        countPerMovie.getOrDefault(rating.getMovieId(), 0L) + 1
                );
            }
        }

        List<Map<String, Object>> top = countPerMovie.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .limit(10)
                .map(e -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("movieId", e.getKey());
                    map.put("count", e.getValue());
                    return map;
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(top);
    }


    @GetMapping("/top-rated")
    public ResponseEntity<?> getTopRatedMovies(HttpServletRequest request) {
        if (!isAdmin(request)) return ResponseEntity.status(403).body("No autorizado");

        Map<String, List<Integer>> ratingsPerMovie = new HashMap<>();

        userRepository.findAll().forEach(user ->
                user.getRatings().forEach(r ->
                        ratingsPerMovie.computeIfAbsent(r.getMovieId(), k -> new ArrayList<>()).add(r.getScore())
                )
        );

        List<Map<String, Object>> result = ratingsPerMovie.entrySet().stream()
                .filter(e -> e.getValue().size() >= 1)
                .map(e -> {
                    Map<String, Object> r = new HashMap<>();
                    r.put("movieId", e.getKey());
                    r.put("average", e.getValue().stream().mapToInt(i -> i).average().orElse(0));
                    return r;
                })
                .sorted((a, b) -> Double.compare((double) b.get("average"), (double) a.get("average")))
                .limit(10)
                .collect(Collectors.toList());

        return ResponseEntity.ok(result);
    }


    @GetMapping("/most-divisive")
    public ResponseEntity<?> getMostDivisiveMovies(HttpServletRequest request) {
        if (!isAdmin(request)) return ResponseEntity.status(403).body("No autorizado");

        Map<String, List<Integer>> ratingsPerMovie = new HashMap<>();

        userRepository.findAll().forEach(user ->
                user.getRatings().forEach(r ->
                        ratingsPerMovie.computeIfAbsent(r.getMovieId(), k -> new ArrayList<>()).add(r.getScore())
                )
        );

        List<Map<String, Object>> result = ratingsPerMovie.entrySet().stream()
                .filter(e -> e.getValue().size() >= 3) // al menos 3 votos para tener sentido
                .map(e -> {
                    List<Integer> scores = e.getValue();
                    double average = scores.stream().mapToInt(i -> i).average().orElse(0);
                    double variance = scores.stream()
                            .mapToDouble(i -> Math.pow(i - average, 2))
                            .average().orElse(0);
                    double stdDev = Math.sqrt(variance);

                    Map<String, Object> map = new HashMap<>();
                    map.put("movieId", e.getKey());
                    map.put("stdDev", stdDev);
                    return map;
                })
                .sorted((a, b) -> Double.compare((double) b.get("stdDev"), (double) a.get("stdDev")))
                .limit(10)
                .toList();

        return ResponseEntity.ok(result);
    }


}
