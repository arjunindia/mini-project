package io.bootify.mini_project.events;

import io.bootify.mini_project.user.User;
import org.springframework.data.jpa.repository.JpaRepository;


public interface EventsRepository extends JpaRepository<Events, Integer> {

    Events findFirstByUsername(User user);

}
