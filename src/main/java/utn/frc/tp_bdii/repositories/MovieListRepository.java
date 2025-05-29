package utn.frc.tp_bdii.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import utn.frc.tp_bdii.models.MovieList;

@Repository
public interface MovieListRepository extends MongoRepository<MovieList, String> {
}
