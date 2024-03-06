package hexlet.code.config;

import hexlet.code.service.user.UserService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

@Configuration
@EnableWebSecurity()
@EnableMethodSecurity(securedEnabled = true)
@AllArgsConstructor
public class SecurityConfig {

    private final PasswordEncoder passwordEncoder;
    private final JwtDecoder jwtDecoder;
    private final UserService userService;

    /**
     * This method creates a SecurityFilterChain bean which is used to configure
     * the security settings for the application.
     * It sets up the URL patterns that require authentication and the ones that are publicly accessible.
     * It also configures the session management policy and the OAuth2 resource server settings.
     *
     * @param http an HttpSecurity instance used to build the security configuration
     * @param introspector a HandlerMappingIntrospector instance used to create MvcRequestMatcher
     * @return a SecurityFilterChain instance with the configured security settings
     * @throws Exception if an error occurs during the configuration
     */
    @Bean
    SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            HandlerMappingIntrospector introspector
    ) throws Exception {
        var mvcMatcherBuilder = new MvcRequestMatcher.Builder(introspector);
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/").permitAll()
                        .requestMatchers("/index.html").permitAll()
                        .requestMatchers("/assets/**").permitAll()
                        .requestMatchers(mvcMatcherBuilder.pattern("api/login")).permitAll()
                        .anyRequest()
                        .authenticated())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .oauth2ResourceServer(rs -> rs.jwt(jwt -> jwt.decoder(jwtDecoder)))
                .httpBasic(Customizer.withDefaults())
                .build();
    }

    /**
     * This method creates an AuthenticationManager bean from the shared
     * AuthenticationManagerBuilder object in the HttpSecurity instance.
     *
     * @param http an HttpSecurity instance from which the AuthenticationManagerBuilder is retrieved
     * @return an AuthenticationManager instance
     * @throws Exception if an error occurs during the creation of the AuthenticationManager
     */
    @Bean
    AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class).build();
    }

    /**
     * This method creates a DaoAuthenticationProvider bean which is used to authenticate users against a data source.
     * It sets the UserDetailsService and the PasswordEncoder to be used by the provider.
     *
     * @param auth an AuthenticationManagerBuilder instance used to build the AuthenticationProvider
     * @return an AuthenticationProvider instance
     */
    @Bean
    AuthenticationProvider daoAuthProvider(AuthenticationManagerBuilder auth) {
        var provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userService);
        provider.setPasswordEncoder(passwordEncoder);
        return provider;
    }

}
