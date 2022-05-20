package visa.vttp.paf.stokexCMS.controller;

import java.io.StringReader;

import javax.security.sasl.AuthenticationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import visa.vttp.paf.stokexCMS.service.AuthService;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService uSvc;

    @GetMapping("/logout")
    public ResponseEntity<String> logoutResource(
        @RequestHeader("x-session-key") String sessKey) {
        try {
            uSvc.invalidate(sessKey);
        } catch (AuthenticationException ex) {};
        return ResponseEntity.ok().body("{ \"%s\": \"expired\"}".formatted(sessKey));
    }

    @PostMapping("/login")
    public ResponseEntity<String> postAuth(
        @RequestBody String jsonRaw
    ) {
        JsonObject json = Json.createReader(new StringReader(jsonRaw)).readObject();
        String user = json.getString("username");
        String pass = json.getString("password");

        String sessKey;
        try {
            sessKey = uSvc.verifyCredentials(user, pass);
        } catch (AuthenticationException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("{\"error\": \"Incorrect credentials\"}");
        }
        return ResponseEntity.ok().body("{\"sessionKey\": \"%s\" }".formatted(sessKey));
    } 
}
