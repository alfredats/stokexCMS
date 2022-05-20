package visa.vttp.paf.stokexCMS.service;

import java.util.UUID;

import javax.security.sasl.AuthenticationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import visa.vttp.paf.stokexCMS.repo.SessionRepository;
import visa.vttp.paf.stokexCMS.repo.UsersRepository;

@Service
public class AuthService {

    @Autowired
    private UsersRepository uRepo;

    @Autowired
    private SessionRepository sessRepo;

    private synchronized String generateSessKey(String user) {
        String uuid = UUID.randomUUID().toString();
        uuid = uuid.replaceAll("-", "");
        sessRepo.initSess(uuid, user);
        return uuid;
    }

    public String verifySess(String sessKey) throws AuthenticationException {
        try {
            return sessRepo.extendSess(sessKey);
        } catch (AuthenticationException ex) {
            throw new AuthenticationException("Error: Failed to verify session with id " + sessKey);
        }
    }

    /**
     * Given username and password, return a session key for use
     * @param user : String - username
     * @param pass : String - password
     * @return String - session key that lasts for 15 minutes
     */
    public String verifyCredentials(String user, String pass) throws AuthenticationException {
        if(!uRepo.verifyCredentials(user,pass)) {
            throw new AuthenticationException("Error: Failed to authenticate user { %s }".formatted(user));
        }
        return this.generateSessKey(user);
    }

    public void invalidate(String sessKey) throws AuthenticationException {
        sessRepo.invalidateSess(sessKey);
    }

    public String[] getUserDetailsBySessKey(String sessKey) {
        return uRepo.getUserDetailsBySessKey(sessKey);
    }
    
}