package io.bootify.mini_project.events;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = "/api/eventss", produces = MediaType.APPLICATION_JSON_VALUE)
public class EventsResource {

    private final EventsService eventsService;

    public EventsResource(final EventsService eventsService) {
        this.eventsService = eventsService;
    }

    @GetMapping
    public ResponseEntity<List<EventsDTO>> getAllEventss() {
        return ResponseEntity.ok(eventsService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventsDTO> getEvents(@PathVariable(name = "id") final Integer id) {
        return ResponseEntity.ok(eventsService.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Integer> createEvents(@RequestBody @Valid final EventsDTO eventsDTO) {
        final Integer createdId = eventsService.create(eventsDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Integer> updateEvents(@PathVariable(name = "id") final Integer id,
            @RequestBody @Valid final EventsDTO eventsDTO) {
        eventsService.update(id, eventsDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteEvents(@PathVariable(name = "id") final Integer id) {
        eventsService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
