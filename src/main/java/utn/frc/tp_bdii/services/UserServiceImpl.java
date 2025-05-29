package utn.frc.tp_bdii.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import utn.frc.tp_bdii.dtos.RegisterRequest;
import utn.frc.tp_bdii.models.User;
import utn.frc.tp_bdii.repositories.UserRepository;
import utn.frc.tp_bdii.services.UserService;

import java.util.ArrayList;
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
        List<String> friends = sendingUser.getFriends();
        if(friends == null){
            friends = new ArrayList<>();
        }
        friends.add(toId);
        sendingUser.setFriends(friends);
        userRepository.save(sendingUser);

    }

    @Override
    public void acceptFriendRequest(String accepterId, String inviterId) {
        if(getFriendRequests(accepterId).contains(userRepository.findById(inviterId).get())){
            Optional<User> opt =userRepository.findById(accepterId);
            User acceptingUser = opt.get();
            List<String> friends = acceptingUser.getFriends();
            if(friends == null){
                friends = new ArrayList<>();
            }
            friends.add(inviterId);

            acceptingUser.setFriends(friends);
            userRepository.save(acceptingUser);
        }

    }

    @Override
    public void rejectFriendRequest(String rejecterId, String inviterId) {
        if(getFriendRequests(rejecterId).contains(userRepository.findById(inviterId).get())){
            Optional<User> opt =userRepository.findById(inviterId);
            User invitingUser = opt.get();
            List<String> friends = invitingUser.getFriends();
            if(friends == null){
                friends = new ArrayList<>();
            }
            friends.remove(rejecterId);

            invitingUser.setFriends(friends);
            userRepository.save(invitingUser);
        }
    }

    @Override
    public void removeFriend(String removerId, String removedId) {
        User remover = userRepository.findById(removerId).get();
        User removed = userRepository.findById(removedId).get();
        if(removed.getFriends() != null && remover.getFriends() != null){
            List<String> removedFriends = removed.getFriends();
            List<String> removerFriends = remover.getFriends();
            removedFriends.remove(removerId);
            removerFriends.remove(removedId);
            remover.setFriends(removerFriends);
            removed.setFriends(removedFriends);
            userRepository.save(remover);
            userRepository.save(removed);
        }

    }

    @Override
    public List<User> getFriendRequests(String userId) {
        return userRepository.findAll().stream().filter(u ->
                u.getFriends() != null && u.getFriends().contains(userId)).toList();
    }

    @Override
    public List<User> getFriends(String userId) {
        return userRepository.findAll().stream().filter( u ->
                u.getFriends() != null &&
                u.getFriends().contains(userId)).toList();
    }


}
