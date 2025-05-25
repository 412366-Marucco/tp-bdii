package utn.frc.tp_bdii.models;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "users")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    private String id;

    private String username;
    private String email;
    private String password;

    private String role = "USER";

    private List<String> favorites = new ArrayList<>();
    private List<String> watchlist = new ArrayList<>();
    private List<Rating> ratings = new ArrayList<>();
}
