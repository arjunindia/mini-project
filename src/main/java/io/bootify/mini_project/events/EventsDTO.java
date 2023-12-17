package io.bootify.mini_project.events;

import jakarta.validation.constraints.Size;
import java.time.OffsetDateTime;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;


@Getter
@Setter
public class EventsDTO {

    private Integer id;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
    private OffsetDateTime time;

    @Size(max = 255)
    private String eventName;

    private String description;

    private Boolean important;

    @Size(max = 255)
    private String username;

}
