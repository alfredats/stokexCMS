package visa.vttp.paf.stokexCMS.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import visa.vttp.paf.stokexCMS.repo.UsersRepository;

@Service
public class AuthService {

    @Autowired
    private UsersRepository uRepo;

    public String verifyKey(String apiKey) {
        return uRepo.getUsernameByApiKey(apiKey);
    }
    
}