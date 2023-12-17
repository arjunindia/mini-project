package io.bootify.mini_project.user;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class UserDTO {

    @Size(max = 255)
    private String username;

    @NotNull
    @Size(max = 255)
    private String password;

}
