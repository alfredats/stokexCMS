package visa.vttp.paf.stokexCMS;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import javax.security.sasl.AuthenticationException;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataAccessException;

import io.lettuce.core.KillArgs;
import visa.vttp.paf.stokexCMS.service.AuthService;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class Test_AuthService {

    @Autowired
    private AuthService aSvc;

    private static String test_sessKey;

    @Test
    @org.junit.jupiter.api.Order(1)
    public void verifyCredentials_fail() {
        try {
            aSvc.verifyCredentials("", "");
        } catch (AuthenticationException ex) {
            assertTrue(true);
            return;
        }
        fail("Should throw auth exception due to incorrect credentials");
    }


    @Test
    @org.junit.jupiter.api.Order(2)
    public void verifyCredentials_success() {
        try {
            test_sessKey = aSvc.verifyCredentials("testUser1", "testPassword1");
        } catch (AuthenticationException ex) {
            ex.printStackTrace();
            fail("Failed to authenticate test user, check credentials?");
        }
        if (null == test_sessKey) { fail("Sess Key should not be null; ");}
        assertTrue(test_sessKey.length() == 32, "Sess key should have length 32"); 
    }


    @Test
    @org.junit.jupiter.api.Order(3)
    public void verifySess_fail() {
        try {
            aSvc.verifySess("");
        } catch (AuthenticationException ex) {
            assertTrue(true);
            return;
        }
        fail("Should throw auth exception due to incorrect sessKey");

    }
    
    @Test
    @org.junit.jupiter.api.Order(4)
    public void verifySess_success() {
        try {
            String shouldSame = aSvc.verifySess(test_sessKey);
            assertTrue(shouldSame.equals(test_sessKey), "Did not obtain same sessKey from verifySess()");
        } catch (AuthenticationException ex) {
            ex.printStackTrace();
            fail("Failed to authenticate test session, check sessKey?");
        }
    }

    @Test
    @org.junit.jupiter.api.Order(5) 
    public void getUserDetailsBySessKey_fail() {
        try {
            aSvc.getUserDetailsBySessKey("");
        } catch (DataAccessException ex) {
            assertTrue(true);
            return;
        }
        fail("Should throw auth exception due to incorrect sessKey");
    }
    @Test
    @org.junit.jupiter.api.Order(6) 
    public void getUserDetailsBySessKey_success() {
        String[] testUserDetails;
        try {
            testUserDetails = aSvc.getUserDetailsBySessKey(test_sessKey);
        } catch (DataAccessException ex) {
            ex.printStackTrace();
            fail("Failed to get user details for test session, check sessKey?");
            return;
        }
        assertEquals("Test User1", testUserDetails[0]);
        assertEquals("testUser1", testUserDetails[1]);
    }

    @Test
    @org.junit.jupiter.api.Order(7) 
    public void invalidate_fail() {
        try {
            aSvc.invalidate("");
        } catch (AuthenticationException ex) {
            assertTrue(true);
            return;
        }
        fail("Should throw auth exception due to incorrect sessKey");
    }
    
}
