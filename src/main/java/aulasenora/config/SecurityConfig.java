package aulasenora.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import aulasenora.service.UsuarioDetailsService;
import aulasenora.service.RecaptchaService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

        private final UsuarioDetailsService userDetailsService;
        private final RecaptchaService recaptchaService;

        public SecurityConfig(UsuarioDetailsService userDetailsService, RecaptchaService recaptchaService) {
                this.userDetailsService = userDetailsService;
                this.recaptchaService = recaptchaService;
        }

        @Bean
        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
                http
                                .csrf(csrf -> csrf.disable())
                                .addFilterBefore(new RecaptchaFilter(recaptchaService), UsernamePasswordAuthenticationFilter.class)
                                .authorizeHttpRequests(auth -> auth
                                                // allow registration, login and static resources
                                                .requestMatchers("/", "/register", "/registrar", "/registrar/success",
                                                                "/api/registro",
                                                                "/login", "/css/**", "/js/**", "/img/**",
                                                                "/student/**", "/volunteer/**", "/admin/**") // Abiertos
                                                                                                             // temporalmente
                                                                                                             // para
                                                                                                             // demo del
                                                                                                             // MVP
                                                .permitAll()
                                                // dashbaords específicos según rol
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