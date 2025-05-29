package utn.frc.tp_bdii.services;

import utn.frc.tp_bdii.dtos.RegisterRequest;
import utn.frc.tp_bdii.models.User;

import java.util.List;

public interface UserService {
    User register(RegisterRequest request);
    User findByUsername(String username);
    void sendFriendRequest(String fromId, String toId);
    void acceptFriendRequest(String accepterId, String inviterId);
    void rejectFriendRequest(String rejecterId, String inviterId);
    List<User> getFriendRequests(String userId);
    List<User> getFriends(String userId);
}
