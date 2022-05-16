package visa.vttp.paf.stokexCMS.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import visa.vttp.paf.stokexCMS.service.AuthService;

@RestController
@RequestMapping("/")
public class IndexController {

    @Autowired
    private AuthService authSvc;

    @PostMapping
    public ResponseEntity<String> pingResource(@RequestHeader("x-api-key") String apiKey) {
        String username = authSvc.verifyKey(apiKey);
        return ResponseEntity.ok("{'pong': %s }".formatted(username));
    }
    
}
