package utn.frc.tp_bdii.controllers;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/protected")
public class ProtectedController {

    @GetMapping("/me")
    public ResponseEntity<String> me(HttpServletRequest request) {
        String username = (String) request.getAttribute("username");
        return ResponseEntity.ok("Hola " + username + ", tu token es válido 👌");
    }
}
