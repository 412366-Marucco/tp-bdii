package utn.frc.tp_bdii.dtos;

import lombok.Data;

@Data
public class RegisterRequest {
    public String username;
    public String email;
    public String password;
}