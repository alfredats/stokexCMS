package visa.vttp.paf.stokexCMS.repo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import static visa.vttp.paf.stokexCMS.repo.Queries.*;

@Repository
public class UsersRepository {

    @Autowired
    private JdbcTemplate jt;

    public boolean authenticateUserByLogin(String user, String password) {
        final SqlRowSet rs = jt.queryForRowSet(SQL_GET_HASH_BY_LOGIN, user, password);
        if (!rs.next()) {
            return false;
        }
        return !rs.next();
    }

    public String getUsernameByApiKey(String apiKey) {
        final SqlRowSet rs = jt.queryForRowSet(SQL_GET_USERNAME_BY_APIKEY, apiKey);
        if (!rs.next()) { throw new RuntimeException("No matching user for apiKey: " + apiKey); }
        return rs.getString("username");
    }
    

}
