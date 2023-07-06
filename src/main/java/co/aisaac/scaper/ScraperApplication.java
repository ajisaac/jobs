package co.aisaac.scaper;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@SpringBootApplication
public class ScraperApplication {

    @Bean
    public SecurityWebFilterChain securityWebFilterChain( ServerHttpSecurity http) {
        return http.authorizeExchange()
                .pathMatchers("/actuator/**").permitAll()
                .anyExchange().authenticated()
                .and().build();
    }

    public static void main(String[] args) {
        SpringApplication.run(ScraperApplication.class, args);
    }

}
