package visa.vttp.paf.stokexCMS.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import visa.vttp.paf.stokexCMS.config.PriceProperties;
import visa.vttp.paf.stokexCMS.model.price.TimeSeries;
import visa.vttp.paf.stokexCMS.repo.PriceRepository;
import visa.vttp.paf.stokexCMS.utils.StokexUtils;

@Service
@EnableConfigurationProperties(PriceProperties.class)
public class PriceService {

    @Autowired
    private PriceRepository pRepo;

    @Autowired
    private PriceProperties priceApi;

    private RestTemplate rt = new RestTemplate();
    private static Map<String,Integer> ttlMap = new HashMap<>();
    public static Set<String> ALLOWED_INTERVALS;

    @PostConstruct
    private void ttl() {
        ttlMap.put("5min",300);
        ttlMap.put("1day", 60*60);
        ttlMap.put("1week", 12*60*60);
        ttlMap.put("1month", 24*60*60);
        ALLOWED_INTERVALS = ttlMap.keySet();
    }

    public Optional<TimeSeries> getIntradayByTicker(String ticker) { return this.getPricesByTicker(ticker, "5min"); }    
    public Optional<TimeSeries> getDailyByTicker(String ticker) { return this.getPricesByTicker(ticker, "1day"); }
    public Optional<TimeSeries> getWeeklyByTicker(String ticker) { return this.getPricesByTicker(ticker, "1week"); }
    public Optional<TimeSeries> getMonthlyByTicker(String ticker) { return this.getPricesByTicker(ticker, "1month"); }

    public Optional<TimeSeries> getPricesByTicker(String ticker, String interval) {
        final String endpoint = "/time_series";

        String uString = UriComponentsBuilder
            .fromUriString(priceApi.getHost() + endpoint)
            .queryParam("symbol", ticker)
            .queryParam("interval", interval)
            .queryParam("type", "Stock")
            .queryParam("apikey", priceApi.getKey())
            .toUriString();
        System.out.println("query string: " + uString);
                        
        RequestEntity<Void> req = RequestEntity
            .get(uString)
            .accept(MediaType.APPLICATION_JSON)
            .build();

        Optional<String> resp = this.exchangeGet(req);
        if (resp.isPresent()) {
            TimeSeries p = StokexUtils.createPrice(resp.get(), interval.equals("5min"));
            p.setTime(ttlMap.get(interval));
            p.setName(ticker + ":" + interval);
            pRepo.save(p);
            return Optional.of(p);
        }

        return Optional.empty();
    }

    private Optional<String> exchangeGet(RequestEntity<Void> req) {
        try {
            ResponseEntity<String> resp = this.rt.exchange(req, String.class);
            if (resp.getStatusCodeValue() == 200) {
                return Optional.of(resp.getBody());
            }
        } catch (RestClientException ex) {
            ex.printStackTrace();
        }
        return Optional.empty();
    }
}
