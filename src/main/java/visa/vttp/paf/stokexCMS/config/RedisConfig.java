package visa.vttp.paf.stokexCMS.config;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

@Configuration
public class RedisConfig {
    private static final Logger logger = LoggerFactory.getLogger(RedisConfig.class);

    @Value("${spring.redis.host}")
    private String redisHost;

    @Value("${spring.redis.port}")
    private Optional<Integer> redisPort;

    @Value("${spring.redis.password}")
    private String redisPassword;

    @Bean
    @Scope("singleton")
    public RedisTemplate<String,Object> redisTemplate() {
        final RedisStandaloneConfiguration config =
            new RedisStandaloneConfiguration();

        config.setHostName(redisHost);
        config.setPort(redisPort.get());
        config.setPassword(redisPassword);

        final JedisClientConfiguration jedisClient =
            JedisClientConfiguration.builder().build();
        final JedisConnectionFactory jedisFac =
            new JedisConnectionFactory(config, jedisClient);
        jedisFac.afterPropertiesSet();
        logger.info(String.format("connected to redis at %s:%d", redisHost, redisPort.get()));

        final RedisTemplate<String, Object> template =
            new RedisTemplate<>();
        template.setConnectionFactory(jedisFac);
        logger.info(">>> redisTemplate created");

        return template;
    }
}

