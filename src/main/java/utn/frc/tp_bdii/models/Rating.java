package utn.frc.tp_bdii.models;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Rating {
    private String movieId;
    private int score;
}