package io.bootify.mini_project.events;

import io.bootify.mini_project.user.User;
import io.bootify.mini_project.user.UserRepository;
import io.bootify.mini_project.util.NotFoundException;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class EventsService {

    private final EventsRepository eventsRepository;
    private final UserRepository userRepository;

    public EventsService(final EventsRepository eventsRepository,
            final UserRepository userRepository) {
        this.eventsRepository = eventsRepository;
        this.userRepository = userRepository;
    }

    public List<EventsDTO> findAll() {
        final List<Events> eventses = eventsRepository.findAll(Sort.by("id"));
        return eventses.stream()
                .map(events -> mapToDTO(events, new EventsDTO()))
                .toList();
    }

    public EventsDTO get(final Integer id) {
        return eventsRepository.findById(id)
                .map(events -> mapToDTO(events, new EventsDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Integer create(final EventsDTO eventsDTO) {
        final Events events = new Events();
        mapToEntity(eventsDTO, events);
        return eventsRepository.save(events).getId();
    }

    public void update(final Integer id, final EventsDTO eventsDTO) {
        final Events events = eventsRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(eventsDTO, events);
        eventsRepository.save(events);
    }

    public void delete(final Integer id) {
        eventsRepository.deleteById(id);
    }

    private EventsDTO mapToDTO(final Events events, final EventsDTO eventsDTO) {
        eventsDTO.setId(events.getId());
        eventsDTO.setTime(events.getTime());
        eventsDTO.setEventName(events.getEventName());
        eventsDTO.setDescription(events.getDescription());
        eventsDTO.setImportant(events.getImportant());
        eventsDTO.setUsername(events.getUsername() == null ? null : events.getUsername().getUsername());
        return eventsDTO;
    }

    private Events mapToEntity(final EventsDTO eventsDTO, final Events events) {
        events.setTime(eventsDTO.getTime());
        events.setEventName(eventsDTO.getEventName());
        events.setDescription(eventsDTO.getDescription());
        events.setImportant(eventsDTO.getImportant());
        final User username = eventsDTO.getUsername() == null ? null : userRepository.findById(eventsDTO.getUsername())
                .orElseThrow(() -> new NotFoundException("username not found"));
        events.setUsername(username);
        return events;
    }

}
