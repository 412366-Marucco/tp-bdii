package utn.frc.tp_bdii.services;

import utn.frc.tp_bdii.dtos.RegisterRequest;
import utn.frc.tp_bdii.models.User;

public interface UserService {
    User register(RegisterRequest request);
    User findByUsername(String username);
}
