package ru.practicum.explorewithme.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import ru.practicum.explorewithme.validator.OffsetBasedPageRequest;

import java.util.List;
import java.util.Optional;

@Component
public class PersistentUserStorage implements UserStorage {
    private final UserRepository userRepository;

    @Autowired
    public PersistentUserStorage(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Optional<User> addUser(User user) {
        try {
            return Optional.of(userRepository.save(user));
        } catch (Exception ex) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<User> getUserById(Long userId) {
        return userRepository.findById(userId);
    }

    @Override
    public Optional<User> removeUserById(Long userId) {
        Optional<User> user = userRepository.findById(userId);
        user.ifPresent(u -> userRepository.deleteById(u.getId()));
        return user;
    }

    @Override
    public List<User> getUsersByIdIn(List<Long> ids, int from, int size) {
        Pageable pageable = new OffsetBasedPageRequest(from, size);
        return userRepository.findByIdIn(ids, pageable);
    }
}
