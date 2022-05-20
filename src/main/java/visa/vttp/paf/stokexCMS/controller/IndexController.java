package visa.vttp.paf.stokexCMS.controller;

import javax.security.sasl.AuthenticationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import visa.vttp.paf.stokexCMS.service.AuthService;

@RestController
@RequestMapping("/")
public class IndexController {

    @Autowired
    private AuthService authSvc;

    @GetMapping("/ping")
    public ResponseEntity<String> pingResource(@RequestHeader("x-session-key") String sessKey) {
        String sess;
        try {
            sess = authSvc.verifySess(sessKey);
        } catch (AuthenticationException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("{ \"error\": %s }".formatted(ex.getMessage()));
        }
        return ResponseEntity.ok("{'pong': %s }".formatted(sess));
    }
    
}
