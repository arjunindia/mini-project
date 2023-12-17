package io.bootify.mini_project.user;

import io.bootify.mini_project.events.Events;
import io.bootify.mini_project.events.EventsRepository;
import io.bootify.mini_project.util.NotFoundException;
import io.bootify.mini_project.util.WebUtils;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class UserService {

    private final UserRepository userRepository;
    private final EventsRepository eventsRepository;

    public UserService(final UserRepository userRepository,
            final EventsRepository eventsRepository) {
        this.userRepository = userRepository;
        this.eventsRepository = eventsRepository;
    }

    public List<UserDTO> findAll() {
        final List<User> users = userRepository.findAll(Sort.by("username"));
        return users.stream()
                .map(user -> mapToDTO(user, new UserDTO()))
                .toList();
    }

    public UserDTO get(final String username) {
        return userRepository.findById(username)
                .map(user -> mapToDTO(user, new UserDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public String create(final UserDTO userDTO) {
        final User user = new User();
        mapToEntity(userDTO, user);
        user.setUsername(userDTO.getUsername());
        return userRepository.save(user).getUsername();
    }

    public void update(final String username, final UserDTO userDTO) {
        final User user = userRepository.findById(username)
                .orElseThrow(NotFoundException::new);
        mapToEntity(userDTO, user);
        userRepository.save(user);
    }

    public void delete(final String username) {
        userRepository.deleteById(username);
    }

    private UserDTO mapToDTO(final User user, final UserDTO userDTO) {
        userDTO.setUsername(user.getUsername());
        userDTO.setPassword(user.getPassword());
        return userDTO;
    }

    private User mapToEntity(final UserDTO userDTO, final User user) {
        user.setPassword(userDTO.getPassword());
        return user;
    }

    public boolean usernameExists(final String username) {
        return userRepository.existsByUsernameIgnoreCase(username);
    }

    public String getReferencedWarning(final String username) {
        final User user = userRepository.findById(username)
                .orElseThrow(NotFoundException::new);
        final Events usernameEvents = eventsRepository.findFirstByUsername(user);
        if (usernameEvents != null) {
            return WebUtils.getMessage("user.events.username.referenced", usernameEvents.getId());
        }
        return null;
    }

}
