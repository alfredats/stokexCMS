package visa.vttp.paf.stokexCMS.repo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import static visa.vttp.paf.stokexCMS.repo.Queries.*;


@Repository
public class UsersRepository {

    @Autowired
    private JdbcTemplate jt;

    public boolean verifyCredentials(String user, String password) {
        final SqlRowSet rs = jt.queryForRowSet(SQL_GET_USERNAME_BY_CREDENTIALS, user, password);
        if (!rs.next()) {
            return false;
        }
        return !rs.next();
    }

    /**
     *  
     * @param sessKey
     * @return String[] - 1st element is user's name, 2nd element is user's username
     */
    public String[] getUserDetailsBySessKey(String sessKey) {
        String[] userDetails = new String[2];
        final SqlRowSet rs = jt.queryForRowSet(SQL_GET_USERNAME_BY_SESSION_KEY, sessKey);
        if (!rs.next()) { throw new DataAccessException("No matching user for apiKey: " + sessKey) {}; }
        userDetails[0] = rs.getString("name");
        userDetails[1] = rs.getString("username");
        return userDetails;
    }
    

}
