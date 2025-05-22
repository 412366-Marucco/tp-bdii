package utn.frc.tp_bdii.services;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import utn.frc.tp_bdii.models.User;
import utn.frc.tp_bdii.dtos.RegisterRequest;
import utn.frc.tp_bdii.services.UserService;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final List<User> users = new ArrayList<>();
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @Override
    public User register(RegisterRequest request) {
        User user = new User();
        user.setId(users.size() + 1);
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(encoder.encode(request.getPassword()));
        users.add(user);
        return user;
    }

    @Override
    public User findByUsername(String username) {
        return users.stream()
                .filter(u -> u.getUsername().equals(username))
                .findFirst()
                .orElse(null);
    }
}