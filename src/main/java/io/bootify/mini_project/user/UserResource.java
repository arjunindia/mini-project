package io.bootify.mini_project.user;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = "/api/users", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserResource {

    private final UserService userService;

    public UserResource(final UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        return ResponseEntity.ok(userService.findAll());
    }

    @GetMapping("/{username}")
    public ResponseEntity<UserDTO> getUser(@PathVariable(name = "username") final String username) {
        return ResponseEntity.ok(userService.get(username));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<String> createUser(@RequestBody @Valid final UserDTO userDTO,
            final BindingResult bindingResult) throws MethodArgumentNotValidException {
        if (!bindingResult.hasFieldErrors("username") && userDTO.getUsername() == null) {
            bindingResult.rejectValue("username", "NotNull");
        }
        if (!bindingResult.hasFieldErrors("username") && userService.usernameExists(userDTO.getUsername())) {
            bindingResult.rejectValue("username", "Exists.user.username");
        }
        if (bindingResult.hasErrors()) {
            throw new MethodArgumentNotValidException(new MethodParameter(
                    this.getClass().getDeclaredMethods()[0], -1), bindingResult);
        }
        final String createdUsername = userService.create(userDTO);
        return new ResponseEntity<>(createdUsername, HttpStatus.CREATED);
    }

    @PutMapping("/{username}")
    public ResponseEntity<String> updateUser(@PathVariable(name = "username") final String username,
            @RequestBody @Valid final UserDTO userDTO) {
        userService.update(username, userDTO);
        return ResponseEntity.ok(username);
    }

    @DeleteMapping("/{username}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteUser(@PathVariable(name = "username") final String username) {
        userService.delete(username);
        return ResponseEntity.noContent().build();
    }

}
