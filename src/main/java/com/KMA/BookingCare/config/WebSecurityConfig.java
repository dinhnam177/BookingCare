package com.KMA.BookingCare.config;

import com.KMA.BookingCare.ServiceImpl.UserDetailsServiceImpl;
import com.KMA.BookingCare.common.JwtUtils;
import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.web.cors.CorsConfiguration;

import java.util.List;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
        // securedEnabled = true,
        // jsr250Enabled = true,
        prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private UserDetailsServiceImpl userDetailsService;
    @Autowired
    private AuthEntryPointJwt unauthorizedHandler;
    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter() {
        return new AuthTokenFilter();
    }
    @Override
    public void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().configurationSource(request -> {
                    var cors = new CorsConfiguration();
                    cors.setAllowedOrigins(List.of("http://192.168.1.5:3000",
                            "http://127.0.0.1:3000","http://localhost:3000",
                            "http://165.232.161.206:3000",
                            "http://18.143.161.64:3000"
                            ,"http://booking-care.online:3000",
                            "https://client.booking-care.online",
                            "https://admin.booking-care.online"));
                    cors.setAllowedMethods(List.of("*"));
                    cors.setAllowedHeaders(List.of("*"));
                    return cors;
                }).and().csrf().disable()
                .exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED).and()
                .authorizeRequests().antMatchers("/api/**").permitAll()
                .antMatchers("/signin").permitAll()
                .antMatchers("/ws").permitAll()
                .antMatchers("/api/payment").permitAll()
                .antMatchers("/api/elasticsearch/**").permitAll()
                .antMatchers("/swagger-ui/**").permitAll()
                .antMatchers("/v3/api-docs/**").permitAll()
                .antMatchers("/api/handbook/deletes").hasAnyRole("ADMIN","USER","DOCTER")
                .antMatchers("/api/handbook/deletes").hasAnyRole("ADMIN","USER","DOCTER")
                .antMatchers(HttpMethod.POST,"/api/handbook").hasAnyRole("ADMIN","DOCTER")
                .antMatchers(HttpMethod.GET,"/api/current-login").authenticated()
                .antMatchers(HttpMethod.GET,"/api/media/check/**").authenticated()
                .antMatchers(HttpMethod.GET,"/api/media/get-by-current-login").authenticated()
                .antMatchers("/**").permitAll()
                .antMatchers("/book/**").authenticated()
                .antMatchers("/admin/**").authenticated()
                .antMatchers("/doctor/**").authenticated()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginProcessingUrl("/login")
                .successHandler(loginSuccessHandler)
                .failureUrl("/register")
                .permitAll().and().logout().logoutSuccessUrl("/login")
                .logoutUrl("/logout")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .permitAll().and().exceptionHandling()
                .accessDeniedPage("/access-deny")
        ;
        http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
    }
    @Autowired
    private LoginSuccessHandler loginSuccessHandler;
}