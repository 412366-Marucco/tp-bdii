package utn.frc.tp_bdii.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "movieLists")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MovieList {
    private String id;
    private String name;
    private String description;
    private String ownerId;
    private LocalDateTime postDate;
    private List<String> movies;
    private List<String> tags;

    private List<String> usersLikes;
    //private List<String> comments;

}
