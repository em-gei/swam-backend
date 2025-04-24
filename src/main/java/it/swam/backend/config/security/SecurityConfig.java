package it.swam.backend.config.security;

import static org.apache.commons.lang3.StringUtils.prependIfMissing;
import static org.springframework.security.config.Customizer.withDefaults;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Value("${authentication.user}")
    private String user;
    @Value("${authentication.pwd}")
    private String password;
    @Value("${management.endpoints.web.base-path}")
    private String managementEndpointsBasePath;
    @Value("${management.endpoints.web.exposure.include}")
    private List<String> managementEndpointsList;

    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails userDetails = User
            .withUsername(user)
            .password(passwordEncoder().encode(password))
            .authorities("USER")
            .build();

        return new InMemoryUserDetailsManager(userDetails);
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(requests -> requests
                .requestMatchers(getManagementEndpoints())
                .permitAll()
                .requestMatchers(getSwaggerEndpoints())
                .permitAll()
                .requestMatchers("/users/**")
                .authenticated())
            .httpBasic(basic -> basic.authenticationEntryPoint(new AuthEntryPoint()))
            .cors(withDefaults())
            .csrf(Customizer.withDefaults());
        return http.build();
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
            .components(new Components().addSecuritySchemes(
                "basicAuth",
                new SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("basic")
            ))
            .addSecurityItem(new SecurityRequirement().addList("basicAuth"));
    }

    private String[] getManagementEndpoints() {
        return managementEndpointsList
            .stream()
            .map(m -> managementEndpointsBasePath + prependIfMissing(m, "/"))
            .toList()
            .toArray(new String[managementEndpointsList.size()]);
    }

    private String[] getSwaggerEndpoints() {
        return List
            .of("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html", "/webjars/**", "/swagger-resources/**")
            .toArray(String[]::new);
    }
}
