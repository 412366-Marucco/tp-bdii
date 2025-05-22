package utn.frc.tp_bdii.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import utn.frc.tp_bdii.models.User;

public interface UserRepository extends MongoRepository<User, String> {
    User findByUsername(String username);
}
