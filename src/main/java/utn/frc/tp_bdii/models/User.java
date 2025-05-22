package utn.frc.tp_bdii.models;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "users") // indica que se guarda en Mongo
public class User {

    @Id
    private String id; // Mongo genera este _id automáticamente

    private String username;
    private String email;
    private String password;
}
