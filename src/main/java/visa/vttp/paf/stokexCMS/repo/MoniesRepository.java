package visa.vttp.paf.stokexCMS.repo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import visa.vttp.paf.stokexCMS.model.Portfolio;
import visa.vttp.paf.stokexCMS.utils.StokexUtils;

import static visa.vttp.paf.stokexCMS.repo.Queries.*;

import java.util.ArrayList;
import java.util.List;

@Repository
public class MoniesRepository {
    
    @Autowired
    private JdbcTemplate jt;

    public Portfolio getLatestMoniesByUsername(String username) {
        final SqlRowSet rs = jt.queryForRowSet(SQL_GET_MONIES_BY_USERNAME, username);
        if (!rs.next()) {
            throw new DataAccessException("Error: No such username(%s) found.".formatted(username)) {};
        }
        return StokexUtils.createPortfolio(rs);
    }

    public List<Portfolio> getPortfolioTimeSeries(String username) {
        List<Portfolio> moniesList = new ArrayList<>();
        final SqlRowSet rs = jt.queryForRowSet(SQL_GET_MONIES_BY_USERNAME, username);
        while (rs.next()) {
            moniesList.add(StokexUtils.createPortfolio(rs));
        }
        return moniesList;
    }
}
