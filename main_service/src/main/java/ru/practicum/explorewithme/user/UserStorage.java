package ru.practicum.explorewithme.user;


import java.util.List;
import java.util.Optional;

public interface UserStorage {
    Optional<User> addUser(User user);
    Optional<User> getUserById(Long userId);
    Optional<User> removeUserById(Long userId);
    List<User> getUsersByIdIn(List<Long> ids, int from, int size);
}
