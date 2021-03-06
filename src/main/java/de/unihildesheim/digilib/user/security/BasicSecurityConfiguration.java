package de.unihildesheim.digilib.user.security;

import de.unihildesheim.digilib.user.Role;
import de.unihildesheim.digilib.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@EnableWebSecurity
public class BasicSecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserRepository userRepository;

    @Value("${jwt.secret}")
    private String jwtSecret;
    @Value("${jwt.issuer}")
    private String jwtIssuer;
    @Value("${jwt.type}")
    private String jwtType;
    @Value("${jwt.audience}")
    private String jwtAudience;

    @Bean
    public UserDetailsService userDetailsService() {
        return new UserDetailsServiceImpl(userRepository);
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().headers().frameOptions().sameOrigin().and().csrf().disable()
                .addFilter(new JwtAuthenticationFilter(authenticationManager(), jwtAudience, jwtIssuer, jwtSecret, jwtType))
                .addFilter(new JwtAuthorizationFilter(authenticationManager(), jwtSecret))
                .authorizeRequests(authorizeRequests ->
                        authorizeRequests
                                .antMatchers("/api/import").hasAnyRole(Role.ADMIN.name())
                                .antMatchers("/api/users**").hasAnyRole(Role.ADMIN.name())
                                .antMatchers("/api/**").hasAnyRole(Role.getAllRoles())
                                .anyRequest().permitAll()
                )
                .httpBasic().realmName("Digilib")
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService()).passwordEncoder(passwordEncoder());
    }
}
