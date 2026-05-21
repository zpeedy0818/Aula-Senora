package aulasenora.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import aulasenora.service.UsuarioDetailsService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

        private final UsuarioDetailsService userDetailsService;

        public SecurityConfig(UsuarioDetailsService userDetailsService) {
                this.userDetailsService = userDetailsService;
        }

        @Bean
        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
                http
                                .csrf(csrf -> csrf.disable())
                                .headers(headers -> headers.frameOptions(frame -> frame.sameOrigin()))
                                .authorizeHttpRequests(auth -> auth
                                                // allow registration, login and static resources
                                                .requestMatchers("/", "/register", "/registrar", "/registrar/success",
                                                                 "/api/registro", "/login-success", "/debug-users", "/debug-login-as", "/debug-voluntarios",
                                                                 "/login", "/error", "/css/**", "/js/**", "/img/**",
                                                                 "/auth/**", "/recursos/**", "/uploads/**",
                                                                 "/student/**", "/volunteer/**", "/admin/**")
                                                .permitAll()
                                                // dashboards específicos según rol
                                                .requestMatchers("/dashboard-admin").hasRole("ADMIN")
                                                .requestMatchers("/dashboard-voluntario").hasRole("VOLUNTARIO")
                                                .requestMatchers("/dashboard-estudiante").hasRole("ESTUDIANTE")
                                                .anyRequest().authenticated())
                                .formLogin(form -> form
                                                .loginPage("/login")
                                                .defaultSuccessUrl("/dashboard", true)
                                                .permitAll())
                                .logout(logout -> logout
                                                .logoutUrl("/logout")
                                                .logoutSuccessUrl("/login?logout")
                                                .permitAll())
                                .userDetailsService(userDetailsService);

                return http.build();
        }

        @Bean
        public PasswordEncoder passwordEncoder() {
                return new BCryptPasswordEncoder();
        }
}