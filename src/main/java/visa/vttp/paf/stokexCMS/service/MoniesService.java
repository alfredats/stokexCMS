package visa.vttp.paf.stokexCMS.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import visa.vttp.paf.stokexCMS.model.Portfolio;
import visa.vttp.paf.stokexCMS.repo.MoniesRepository;

@Service
public class MoniesService {

    @Autowired
    private MoniesRepository mRepo;

    public List<Portfolio> getPortfolioTimeSeries(String username) {
        List<Portfolio> mList = mRepo.getPortfolioTimeSeries(username);
        if (mList.isEmpty()) { throw new DataAccessException("No records found for username " + username) {};}
        return mList;
    }

    public Portfolio getLatestMonies(String username) {
        return mRepo.getLatestMoniesByUsername(username);
    }
    
}
