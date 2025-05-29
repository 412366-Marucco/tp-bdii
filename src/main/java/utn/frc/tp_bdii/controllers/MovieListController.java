package utn.frc.tp_bdii.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import utn.frc.tp_bdii.dtos.MovieListDTO;
import utn.frc.tp_bdii.services.MovieListService;

import java.util.List;

@RestController
@RequestMapping("/api")
public class MovieListController {
    private MovieListService movieListService;
    public MovieListController(MovieListService movieListService){
        this.movieListService = movieListService;
    }
    @GetMapping("movie-lists")
    public ResponseEntity<List<MovieListDTO>> getAll(){
        return ResponseEntity.ok(movieListService.getAll());
    }
    @GetMapping("movie-lists/{id}")
    public ResponseEntity<List<MovieListDTO>> getByUser(@PathVariable("id") String userId){
        return ResponseEntity.ok(movieListService.getByUser(userId));
    }
    @GetMapping("movie-lists/most-liked")
    public ResponseEntity<List<MovieListDTO>> getMostLiked(){
        return ResponseEntity.ok(movieListService.getMostLiked());
    }
    @PostMapping("movie-lists")
    public void add(@RequestBody MovieListDTO movieListDTO){
        movieListService.add(movieListDTO);
    }


}
