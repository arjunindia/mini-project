package io.bootify.mini_project.user;

import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepository extends JpaRepository<User, String> {

    boolean existsByUsernameIgnoreCase(String username);

}
