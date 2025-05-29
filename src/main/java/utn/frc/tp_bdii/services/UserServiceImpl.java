package utn.frc.tp_bdii.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import utn.frc.tp_bdii.dtos.RegisterRequest;
import utn.frc.tp_bdii.models.User;
import utn.frc.tp_bdii.repositories.UserRepository;
import utn.frc.tp_bdii.services.UserService;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @Override
    public User register(RegisterRequest request) {
        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(encoder.encode(request.getPassword()));

        // Asignar "USER" por defecto
        user.setRole("USER");

        return userRepository.save(user); // ✅ guarda en Mongo Atlas
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username); // ✅ busca desde Mongo
    }

    @Override
    public void sendFriendRequest(String fromId, String toId) {
        Optional<User> opt1 = userRepository.findById(fromId);
        User sendingUser = opt1.get();
        sendingUser.getFriends().add(toId);
        userRepository.save(sendingUser);

    }

    @Override
    public void acceptFriendRequest(String accepterId, String inviterId) {
        if(getFriendRequests(accepterId).contains(userRepository.findById(inviterId).get())){
            Optional<User> opt =userRepository.findById(accepterId);
            User acceptingUser = opt.get();
            acceptingUser.getFriends().add(inviterId);
            userRepository.save(acceptingUser);
        }

    }

    @Override
    public void rejectFriendRequest(String rejecterId, String inviterId) {
        if(getFriendRequests(rejecterId).contains(userRepository.findById(inviterId).get())){
            Optional<User> opt =userRepository.findById(inviterId);
            User invitingUser = opt.get();
            invitingUser.getFriends().remove(inviterId);
            userRepository.save(invitingUser);
        }
    }

    @Override
    public List<User> getFriendRequests(String userId) {
        return userRepository.findAll().stream().filter(u -> u.getFriends().contains(userId)).toList();
    }

    @Override
    public List<User> getFriends(String userId) {
        return userRepository.findAll().stream().filter( u -> u.getFriends().contains(userId)).toList();
    }


}
