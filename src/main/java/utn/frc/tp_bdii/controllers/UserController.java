package utn.frc.tp_bdii.controllers;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import utn.frc.tp_bdii.models.Rating;
import utn.frc.tp_bdii.models.User;
import utn.frc.tp_bdii.repositories.UserRepository;
import utn.frc.tp_bdii.services.MovieService;
import utn.frc.tp_bdii.services.UserService;

import java.util.*;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MovieService movieService;
    @Autowired
    private UserService userService;

    @GetMapping("/me")
    public ResponseEntity<User> me(HttpServletRequest request) {
        String username = (String) request.getAttribute("username");
        System.out.println(">> username en /me: " + username);

        User user = userRepository.findByUsername(username);
        System.out.println(">> user encontrado: " + user);

        return ResponseEntity.ok(user);
    }
    @GetMapping("profile")
    public ResponseEntity<User> currentProfile(HttpServletRequest request) {
        String username = (String) request.getAttribute("username");


        User user = userRepository.findByUsername(username);


        return ResponseEntity.ok(user);
    }


    @PostMapping("/favorite")
    public ResponseEntity<?> addFavorite(HttpServletRequest request, @RequestBody Map<String, String> body) {
        String username = (String) request.getAttribute("username");
        String movieId = body.get("movieId");
        User user = userRepository.findByUsername(username);

        if (!user.getFavorites().contains(movieId)) {
            user.getFavorites().add(movieId);
            userRepository.save(user);
        }

        return ResponseEntity.ok("Película agregada a favoritos");
    }

    @PostMapping("/rate")
    public ResponseEntity<?> rate(HttpServletRequest request, @RequestBody Rating rating) {
        String username = (String) request.getAttribute("username");
        User user = userRepository.findByUsername(username);

        user.getRatings().removeIf(r -> r.getMovieId().equals(rating.getMovieId()));
        user.getRatings().add(rating);

        userRepository.save(user);
        return ResponseEntity.ok("Película puntuada");
    }

    @GetMapping("/favorites")
    public ResponseEntity<List<String>> getFavorites(HttpServletRequest request) {
        String username = (String) request.getAttribute("username");
        User user = userRepository.findByUsername(username);

        List<String> movieDetails = new ArrayList<>();
        for (String id : user.getFavorites()) {
            movieDetails.add(movieService.getMovieById(id));
        }

        return ResponseEntity.ok(movieDetails);
    }

    @GetMapping("/ratings")
    public ResponseEntity<List<Rating>> getRatings(HttpServletRequest request) {
        String username = (String) request.getAttribute("username");
        User user = userRepository.findByUsername(username);

        return ResponseEntity.ok(user.getRatings());
    }
    @GetMapping("{id}/favorites")
    public ResponseEntity<List<String>> getFavoritesById(@PathVariable("id") String userId) {

        User user = userRepository.findById(userId).get();

        List<String> movieDetails = new ArrayList<>();
        for (String id : user.getFavorites()) {
            movieDetails.add(movieService.getMovieById(id));
        }

        return ResponseEntity.ok(movieDetails);
    }

    @GetMapping("{id}/ratings")
    public ResponseEntity<List<Rating>> getRatingsById(@PathVariable("id") String userId) {

        User user = userRepository.findById(userId).get();

        return ResponseEntity.ok(user.getRatings());
    }
    @GetMapping("{id}/friends")
    public List<User> getFriendsById(@PathVariable("id") String userId){
        User user = userRepository.findById(userId).get();
        return userService.getFriends(user.getId());
    }
    @GetMapping("{id}")
    public ResponseEntity<User> getUserById( @PathVariable("id") String id) {

        User user = userRepository.findById(id).get();

        return ResponseEntity.ok(user);
    }


    @PostMapping("friend-invite/{username}")
    public void sendFriendRequest( @PathVariable("username") String invitedUsername, HttpServletRequest request){
        User inviter = userService.findByUsername((String) request.getAttribute("username"));
        User invited = userService.findByUsername(invitedUsername);
        userService.sendFriendRequest(inviter.getId(),invited.getId());
    }
    @PostMapping("friend-accept/{username}")
    public void acceptFriendRequest( @PathVariable("username") String inviterUsername, HttpServletRequest request){
        User accepter = userService.findByUsername((String) request.getAttribute("username"));
        User inviter = userService.findByUsername(inviterUsername);
        userService.acceptFriendRequest(accepter.getId(),inviter.getId());
    }
    @PostMapping("friend-reject/{username}")
    public void rejectFriendRequest( @PathVariable("username") String inviterUsername, HttpServletRequest request){
        User rejecter = userService.findByUsername((String) request.getAttribute("username"));
        User inviter = userService.findByUsername(inviterUsername);
        userService.rejectFriendRequest(rejecter.getId(),inviter.getId());
    }
    @GetMapping("friends")
    public List<User> getFriends(HttpServletRequest request){
        User user = userService.findByUsername((String) request.getAttribute("username"));
        return userService.getFriends(user.getId());
    }
    @GetMapping("friend-requests")
    public List<User> getFriendRequests(HttpServletRequest request){
        User user = userService.findByUsername((String) request.getAttribute("username"));
        return userService.getFriendRequests(user.getId());
    }
    @DeleteMapping("friends/{username}")
    public void removeFriend(@PathVariable("username") String deletedUsername,HttpServletRequest request){
        User user = userService.findByUsername( (String) request.getAttribute("username"));
        User deletedUser = userService.findByUsername(deletedUsername);
        userService.removeFriend(user.getId(), deletedUser.getId());
    }


    @DeleteMapping("/favorite/{movieId}")
    public ResponseEntity<?> removeFavorite(HttpServletRequest request, @PathVariable("movieId") String movieId) {
        String username = (String) request.getAttribute("username");
        User user = userRepository.findByUsername(username);

        if (user == null) {
            return ResponseEntity.status(404).body("Usuario no encontrado");
        }

        List<String> favorites = user.getFavorites();
        if (favorites == null) {
            return ResponseEntity.status(404).body("No hay favoritos para este usuario");
        }

        // Limpiar movieId (quitar espacios)
        String cleanedMovieId = movieId.trim();

        System.out.println("Favoritos antes: " + favorites);
        System.out.println("Intentando eliminar movieId: '" + cleanedMovieId + "'");

        boolean removed = favorites.removeIf(fav -> fav.equals(cleanedMovieId));

        if (removed) {
            userRepository.save(user);
            System.out.println("Favoritos después: " + favorites);
            return ResponseEntity.ok("Película eliminada de favoritos");
        } else {
            System.out.println("No se encontró el movieId en favoritos");
            return ResponseEntity.status(404).body("La película no estaba en favoritos");
        }
    }



}