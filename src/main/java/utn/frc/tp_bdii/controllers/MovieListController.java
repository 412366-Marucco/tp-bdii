package utn.frc.tp_bdii.controllers;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import utn.frc.tp_bdii.dtos.MovieListDTO;
import utn.frc.tp_bdii.models.User;
import utn.frc.tp_bdii.repositories.UserRepository;
import utn.frc.tp_bdii.services.MovieListService;

import java.util.List;

@RestController
@RequestMapping("/api")
public class MovieListController {
    private MovieListService movieListService;
    private UserRepository userRepository;
    public MovieListController(MovieListService movieListService, UserRepository userRepository){
        this.movieListService = movieListService;
        this.userRepository = userRepository;
    }
    @GetMapping("movie-lists")
    public ResponseEntity<List<MovieListDTO>> getAll(){
        return ResponseEntity.ok(movieListService.getAll());
    }
    @GetMapping("movie-lists/me")
    public ResponseEntity<List<MovieListDTO>> getCurrentUser(HttpServletRequest request){
        String username = (String) request.getAttribute("username");
        User user = userRepository.findByUsername(username);

        return ResponseEntity.ok(movieListService.getByUser(user.getId()));
    }
    @GetMapping("movie-lists/{username}")
    public ResponseEntity<List<MovieListDTO>> getByUser(@PathVariable("username") String username){

        User user = userRepository.findByUsername(username);

        return ResponseEntity.ok(movieListService.getByUser(user.getId()));
    }
    @GetMapping("movie-lists/{id}")
    public ResponseEntity<List<MovieListDTO>> getByUserId(@PathVariable("id") String userId){

        User user = userRepository.findById(userId).get();

        return ResponseEntity.ok(movieListService.getByUser(user.getId()));
    }
    @GetMapping("movie-lists/search")
    public ResponseEntity<List<MovieListDTO>> getByName(@RequestParam String query){
        return ResponseEntity.ok(movieListService.getByName(query));
    }
    @GetMapping("movie-lists/most-liked")
    public ResponseEntity<List<MovieListDTO>> getMostLiked(){
        return ResponseEntity.ok(movieListService.getMostLiked());
    }
    @PostMapping("movie-lists")
    public void add(@RequestBody MovieListDTO movieListDTO, HttpServletRequest request){
        String username = (String) request.getAttribute("username");
        User user = userRepository.findByUsername(username);
        movieListDTO.setOwnerId(user.getId());
        movieListService.add(movieListDTO);
    }

    @PutMapping("movie-lists/favorite/{listId}")
    public void favoriteList(@PathVariable("listId") String id, HttpServletRequest request){
        String username = (String) request.getAttribute("username");
        User user = userRepository.findByUsername(username);
        movieListService.likeMovieList(id, user.getId());
    }


}
