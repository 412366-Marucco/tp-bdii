package utn.frc.tp_bdii.dtos;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class MovieListDTO {
    private String id;
    private String name;
    private String description;
    private String ownerId;
    private LocalDateTime postDate;
    private List<String> movies;
    private List<String> tags;

    private List<String> usersLikes;
}
