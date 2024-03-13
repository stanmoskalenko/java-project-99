package hexlet.code.project.controller;

import hexlet.code.project.component.JwtGenerator;
import hexlet.code.project.dto.AuthAcceptor;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/login")
@RequiredArgsConstructor
public class AuthController {

    private final JwtGenerator jwtGenerator;
    private final AuthenticationManager authenticationManager;

    /**
     * Handles the POST request for user login.
     * It authenticates the user with the provided username and password,
     * and if successful, generates a JWT token for the authenticated user.
     *
     * @param acceptor An object of AuthAcceptor class
     *                 containing the username and password for authentication.
     * @return A JWT token as a String if the authentication is successful.
     */
    @PostMapping
    public String login(@Valid @RequestBody AuthAcceptor acceptor) {
        var auth = new UsernamePasswordAuthenticationToken(acceptor.getUsername(), acceptor.getPassword());
        authenticationManager.authenticate(auth);
        return jwtGenerator.generateToken(acceptor.getUsername());
    }

}
