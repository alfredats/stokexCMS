package visa.vttp.paf.stokexCMS.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("price.api")
public class PriceProperties {
    /**
     * endpoint & api key for the price api
     */
    private String host;
    private String key = System.getenv("PRICE_API_KEY");

    /**
     * @return String return the host
     */
    public String getHost() {
        return host;
    }

    /**
     * @param host the host to set
     */
    public void setHost(String host) {
        this.host = host;
    }

    /**
     * @return String return the key
     */
    public String getKey() {
        return key;
    }

    /**
     * @param key the key to set
     */
    public void setKey(String key) {
        this.key = key;
    }

}
