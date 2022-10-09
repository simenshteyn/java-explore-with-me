package ru.practicum.explorewithme.user;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.explorewithme.user.dto.NewUserDto;

import java.util.List;

@Service
public class UserService {
    private final UserStorage userStorage;
    private final ModelMapper modelMapper;

    @Autowired
    public UserService(UserStorage userStorage, ModelMapper modelMapper) {
        this.userStorage = userStorage;
        this.modelMapper = modelMapper;
    }

    public User createUser(NewUserDto newUserDto) {
        User user = modelMapper.map(newUserDto, User.class);
        return userStorage.addUser(user).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.CONFLICT, "Unable to add user")
        );
    }

    public List<User> getUsersByIds(List<Long> ids, int from, int size) {
        return userStorage.getUsersByIdIn(ids, from, size);
    }

    public User removeUserById(Long userId) {
        return userStorage.removeUserById(userId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find user")
        );
    }
}
