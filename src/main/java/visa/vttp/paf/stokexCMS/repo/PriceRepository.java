package visa.vttp.paf.stokexCMS.repo;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import visa.vttp.paf.stokexCMS.model.price.TimeSeries;

@Repository
public interface PriceRepository extends CrudRepository<TimeSeries, String>{}
