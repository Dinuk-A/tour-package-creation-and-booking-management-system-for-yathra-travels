package lk.yathra.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebConfig {

    private BCryptPasswordEncoder bCryptPasswordEncoder;

    // WebConfig implements WebMvcConfigurer {
    /*
     * public void addCorsMappings(CorsRegistry registry) {
     * registry.addMapping("/**")
     * .allowedOrigins("*")
     * .allowedMethods("GET", "POST", "PUT", "DELETE")
     * .allowedHeaders("*");
     * } }
     */

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.authorizeHttpRequests((auth) -> {
            auth

                    .requestMatchers("controllerJS/**", "resources/**").permitAll()

                    .requestMatchers("/attractionforweb/**").permitAll()

                    .requestMatchers("/dayplanforweb/**").permitAll()

                    .requestMatchers("/tourpackageforweb/**").permitAll()

                    .requestMatchers("/nationalityforweb/**").permitAll()

                    .requestMatchers("/login").permitAll()

                    .requestMatchers("/error").permitAll()

                    .requestMatchers("/createadmin").permitAll()

                    .requestMatchers("/dashboard").permitAll()

                    .requestMatchers("/yathra").permitAll()

                    .requestMatchers("/edituserinfo").permitAll()

                    .requestMatchers("/emp/**").hasAnyAuthority("Admin", "Manager")

                    .requestMatchers("/user/**").hasAnyAuthority("Admin", "Manager")

                    .requestMatchers("/privilege/**").hasAnyAuthority("Admin", "Manager", "Assistant_Manager")

                    .requestMatchers("/vehicle/**")
                    .hasAnyAuthority("Admin", "Manager", "Assistant_Manager", "Receptionist", "Tour_Agent")

                    .requestMatchers("/attraction/**")
                    .hasAnyAuthority("Admin", "Manager", "Assistant_Manager", "Receptionist", "Tour_Agent")

                    .requestMatchers("/stay/**")
                    .hasAnyAuthority("Admin", "Manager", "Assistant_Manager", "Receptionist", "Tour_Agent")

                    .requestMatchers("/dayplan/**")
                    .hasAnyAuthority("Admin", "Manager", "Assistant_Manager", "Receptionist", "Tour_Agent")

                    .requestMatchers("/lunchhotel/**")
                    .hasAnyAuthority("Admin", "Manager", "Assistant_Manager", "Receptionist", "Tour_Agent")

                    .requestMatchers("/tourpackage/**")
                    .hasAnyAuthority("Admin", "Manager", "Assistant_Manager", "Receptionist", "Tour_Agent")

                    .requestMatchers("/inquiry/**")
                    .hasAnyAuthority("Admin", "Manager", "Assistant_Manager", "Receptionist", "Tour_Agent")

                    .requestMatchers("/inquiryresponse/**")
                    .hasAnyAuthority("Admin", "Manager", "Assistant_Manager")

                    .requestMatchers("/client/**")
                    .hasAnyAuthority("Admin", "Manager", "Assistant_Manager", "Receptionist", "Tour_Agent")

                    .requestMatchers("/booking/**")
                    .hasAnyAuthority("Admin", "Manager", "Assistant_Manager", "Tour_Agent")

                    .requestMatchers("/payment/**").hasAnyAuthority("Admin", "Manager", "Assistant_Manager")

                    .requestMatchers("/expenses/**").hasAnyAuthority("Admin", "Manager", "Assistant_Manager")

                    .requestMatchers("/externalparties/**")
                    .hasAnyAuthority("Admin", "Manager", "Assistant_Manager", "Tour_Agent")

                    // template eka
                    // .requestMatchers("/").hasAnyAuthority("Admin","Manager")
                    // .requestMatchers("emp/**").hasAnyRole("Admin","Manager") mehemath puluwan

                    // default behavior for all requests that are not explicitly matched by earlier
                    // authorization rules.
                    .anyRequest().authenticated(); // "{}" thiyana than wala anthima matcher ekedi ; enna one
        })

                .formLogin((logins) -> {
                    logins

                            .loginPage("/login")

                            .usernameParameter("username") // login form eke usename input field eke NAME eka ,
                            // me text 1 equal wenna one, user entity eke username property ekata

                            .passwordParameter("password")

                            .defaultSuccessUrl("/dashboard", true) // login success nam mekata redirect wenna
                            // log wena hama sarema direct wenne dashboard ekata, thawa option 1k thiyanawa
                            // kalin hitapu page 1ta yanna

                            .failureUrl("/login?error=invalidusernamepassword");
                    // "{}" athule thiyana than wala anthima matcher ekedi ; enna one

                })

                // logout details
                .logout((logout) -> {
                    logout
                            .logoutUrl("/logout") // logout requet url
                            .logoutSuccessUrl("/login"); // logout unama yanna one login page ekata
                })

                // cross referensess block karanawa
                .csrf((csrf) -> csrf.disable() // browser eken nathwa(cross site) wena method walin access karannath
                                               // denawa

                // default enables, api js file walin ajax walin access karannaa one nisa meka
                // disable karanawa
                // naththan findall, push , put kisima service ekak use karanna bari wenawa
                // php nam enebled
                )

                .exceptionHandling((exp) -> exp.accessDeniedPage("/error"));

        return http.build();
    }

    // pw encryption obj ekak
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        bCryptPasswordEncoder = new BCryptPasswordEncoder();
        return bCryptPasswordEncoder;
    }

}
