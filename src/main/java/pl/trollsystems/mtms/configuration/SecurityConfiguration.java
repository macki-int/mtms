package pl.trollsystems.mtms.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import pl.trollsystems.mtms.repository.UserRepository;

@Configuration
public class SecurityConfiguration {
    @Autowired
    private UserRepository userRepository;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeRequests((authorizeRequests) -> authorizeRequests
//                                .anyRequest().hasAuthority("ADMIN_ROLE")
//                                .anyRequest().hasRole("USER")
//                                .antMatchers("/rawreadouts/**").permitAll()
                                .anyRequest().authenticated()
                )

                .cors(Customizer.withDefaults())
//                .csrf((csrf) -> csrf.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()))
                .csrf().disable()
                .httpBasic(Customizer.withDefaults());

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOrigin("http://localhost:8080");
        configuration.addAllowedOrigin("http://mtms.cba.pl");
        configuration.addAllowedMethod("*");
        configuration.addAllowedHeader("*");
        configuration.setAllowCredentials(true);

        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public UserDetailsService UserDetailsService() {
        UserDetails trollsystems = User.withUsername("trollsystems")
                .password("{bcrypt}")
                .roles("ADMIN")
                .build();
        UserDetails mtms_piezo = User.withUsername("mtms_piezo")
                .password("{bcrypt}")
                .roles("ADMIN")
                .build();
        UserDetails naster = User.withUsername("naster")
                .password("{bcrypt}")
                .roles("ADMIN")
                .build();
        UserDetails saintgobain = User.withUsername("saintgobain")
                .password("{bcrypt}")
                .roles("ADMIN")
                .build();
        UserDetails mtms_ftp = User.withUsername("mtms_ftp")
                .password("{bcrypt}")
                .roles("USER")
                .build();
        return new InMemoryUserDetailsManager(trollsystems, mtms_piezo, naster, saintgobain, mtms_ftp);
    }


}
