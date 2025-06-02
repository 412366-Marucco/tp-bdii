package utn.frc.tp_bdii.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import utn.frc.tp_bdii.dtos.MovieListDTO;
import utn.frc.tp_bdii.models.MovieList;
import utn.frc.tp_bdii.repositories.MovieListRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class MovieListService {

    private MovieListRepository movieListRepository;
    public MovieListService(MovieListRepository repository){
        this.movieListRepository = repository;
    }
    public void add(MovieListDTO movieListDTO){
        movieListDTO.setPostDate(LocalDateTime.now());
        movieListRepository.save(toEntity(movieListDTO));

    }
    public List<MovieListDTO> getAll(){
        return movieListRepository.findAll().stream()
                .map(this::toDTO).toList();
    }
    public List<MovieListDTO> getByUser(String userId){
        List<MovieListDTO> list = new java.util.ArrayList<>(movieListRepository.findAll().stream()
                .filter(l -> l.getOwnerId().equals(userId))
                .map(this::toDTO).toList());
        list.sort((ml1,ml2) -> ml2.getPostDate().compareTo(ml1.getPostDate()));
        return list;
    }

    public List<MovieListDTO> getMostLiked(){
        List<MovieListDTO> list = new java.util.ArrayList<>(movieListRepository.findAll().stream().map(this::toDTO).toList());
        list.sort( (ml1,ml2) -> ml2.getUsersLikes().size() - ml1.getUsersLikes().size());
        return list.stream().limit(20).toList();
    }
    public List<MovieListDTO> getByName(String query){
        List<MovieListDTO> list = new java.util.ArrayList<>(movieListRepository.findAll().stream()
                .filter(ml -> ml.getName().contains(query))
                .map(this::toDTO).toList());
        list.sort( (ml1,ml2) -> ml2.getUsersLikes().size() - ml1.getUsersLikes().size());
        return list.stream().limit(20).toList();
    }
    public void likeMovieList(String listId, String userId){
        MovieList list = getById(listId);
        List<String> likes = list.getUsersLikes();
        if(likes == null) likes = new ArrayList<>();
        likes.add(userId);
        list.setUsersLikes(likes);
        movieListRepository.save(list);
    }
    public MovieList getById(String id){
        return movieListRepository.findById(id).get();
    }
    public MovieList toEntity(MovieListDTO dto){
        return MovieList.builder()
                .id(dto.getId())
                .name(dto.getName())
                .description(dto.getDescription())
                .ownerId(dto.getOwnerId())
                .movies(dto.getMovies())
                .postDate(dto.getPostDate())
                .tags(dto.getTags())
                .usersLikes(dto.getUsersLikes()).build();
    }
    public MovieListDTO toDTO(MovieList ent){
        return MovieListDTO.builder()
                .id(ent.getId())
                .name(ent.getName())
                .description(ent.getDescription())
                .ownerId(ent.getOwnerId())
                .movies(ent.getMovies())
                .postDate(ent.getPostDate())
                .tags(ent.getTags())
                .usersLikes(ent.getUsersLikes()).build();
    }


}
