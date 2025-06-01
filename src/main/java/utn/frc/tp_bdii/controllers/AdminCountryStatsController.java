package utn.frc.tp_bdii.controllers;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import utn.frc.tp_bdii.models.Rating;
import utn.frc.tp_bdii.models.User;
import utn.frc.tp_bdii.repositories.UserRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class AdminCountryStatsController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/rating-distribution")
    public ResponseEntity<?> getRatingDistributionByCountry(HttpServletRequest request, @RequestParam String movieId) {
        // 🔍 Log del role y movieId recibido
        String role = (String) request.getAttribute("role");
        System.out.println("ROLE en request: " + role);
        System.out.println("MovieId buscado: " + movieId);

        if (!"ADMIN".equalsIgnoreCase(role)) {
            System.out.println("⛔ Acceso denegado: no es ADMIN");
            return ResponseEntity.status(403).body("No autorizado");
        }

        Map<String, Integer> distribution = new HashMap<>();

        List<User> users = userRepository.findAll();
        for (User user : users) {
            System.out.println("▶ Usuario: " + user.getUsername() + " (" + user.getCountry() + ")");

            if (user.getRatings() != null && user.getCountry() != null) {
                for (Rating r : user.getRatings()) {
                    System.out.println(" - Rating: " + r.getMovieId() + " => score: " + r.getScore());

                    if (r.getMovieId() != null &&
                            String.valueOf(r.getMovieId()).equals(String.valueOf(movieId))) {

                        distribution.put(user.getCountry(), distribution.getOrDefault(user.getCountry(), 0) + 1);
                        System.out.println("✅ Coincidencia encontrada con movieId: " + movieId);
                    } else {
                        System.out.println("❌ No coincide: " + r.getMovieId() + " ≠ " + movieId);
                    }
                }
            }
        }

        System.out.println("📊 Distribución final: " + distribution);

        // 🔧 Convertimos a Map<String, Object> para evitar serialización vacía
        Map<String, Object> result = new HashMap<>();
        distribution.forEach(result::put);
        return ResponseEntity.ok(result);
    }
}
