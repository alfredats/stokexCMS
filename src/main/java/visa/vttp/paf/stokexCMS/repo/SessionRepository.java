package visa.vttp.paf.stokexCMS.repo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import static visa.vttp.paf.stokexCMS.repo.Queries.*;
import static visa.vttp.paf.stokexCMS.utils.StokexUtils.dtf;

import java.time.LocalDateTime;

import javax.security.sasl.AuthenticationException;

@Repository
public class SessionRepository {

    @Autowired
    private JdbcTemplate jt;


    public String initSess(String uuid, String user) {
        final int rows = jt.update(SQL_INSERT_SESSION, 
            user,
            uuid,
            LocalDateTime.now(),
            LocalDateTime.now().plusMinutes(15)
            );
        if (rows == 1) {
            return uuid;
        }
        throw new DataAccessException("Error: Failed to update userSessions table with session id %s and username %s".formatted(uuid,user)) {};
    }

    public String invalidateSess(String uuid) throws AuthenticationException {
        return this.extendSess(uuid, LocalDateTime.now());
    }

    public String extendSess(String uuid) throws AuthenticationException {
        return this.extendSess(uuid, LocalDateTime.now().plusMinutes(15));
    }
    public String extendSess(String uuid, LocalDateTime newExpiry) throws AuthenticationException {
        final SqlRowSet rs = jt.queryForRowSet(SQL_GET_SESSION_BY_ID, uuid);
        if (!rs.next()) { throw new AuthenticationException("No such session id"); }
        LocalDateTime exp = LocalDateTime.parse(rs.getString("timestamp_expired"), dtf);
        if (LocalDateTime.now().isAfter(exp)) {
            throw new AuthenticationException("Session expired");
        }
        final int rows = jt.update(SQL_UPDATE_SESSION_EXPIRY_BY_ID,
            newExpiry,
            uuid
        );
        if (rows == 1) {
            return uuid;
        }
        throw new DataAccessException("Error: Failed to extend expiry of session id %s".formatted(uuid)) {};
    }
    
}
