package com.cloudcomputing.config;

import com.cloudcomputing.service.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailsServiceImpl userDetailsService;
    private final DataSource dataSource;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {

        httpSecurity.csrf().disable();
        httpSecurity.authorizeRequests().antMatchers("/", "/login", "/logout").permitAll();
        httpSecurity.authorizeRequests()
                .antMatchers("/")
                .access("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')");
        httpSecurity.authorizeRequests()
                .antMatchers("/userInfo")
                .access("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')");
        httpSecurity.authorizeRequests()
                .antMatchers("/abc")
                .access("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')");
        httpSecurity.authorizeRequests()
                .antMatchers("/registersubjects")
                .access("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')");
        httpSecurity.authorizeRequests()
                .antMatchers("/result")
                .access("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')");
        httpSecurity.authorizeRequests()
                .antMatchers("registersubjects/delete/")
                .access("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')");
        httpSecurity.authorizeRequests()
                .antMatchers("/admin")
                .access("hasRole('ROLE_ADMIN')");
        httpSecurity.authorizeRequests()
                .and()
                .exceptionHandling()
                .accessDeniedPage("/403");
        httpSecurity.authorizeRequests().and().formLogin()
                .loginProcessingUrl("/j_spring_security_check")
                .loginPage("/login")
                .defaultSuccessUrl("/")
                .failureUrl("/login?error=true")
                .usernameParameter("username")
                .passwordParameter("password")
                .and().logout().logoutUrl("/logout").logoutSuccessUrl("/login");
        httpSecurity.authorizeRequests().and()
                .rememberMe().tokenRepository(this.persistentTokenRepository())
                .rememberMeCookieName("rememberme")
                .tokenValiditySeconds(24 * 60 * 60);
    }

    @Bean
    public PersistentTokenRepository persistentTokenRepository() {
        JdbcTokenRepositoryImpl jdbcTokenRepository = new JdbcTokenRepositoryImpl();
        jdbcTokenRepository.setDataSource(dataSource);
        return jdbcTokenRepository;
    }

    @Bean
    public PersistentTokenBasedRememberMeServices getPersistentTokenBasedRememberMeServices() {
        PersistentTokenBasedRememberMeServices persistentTokenBasedRememberMeServices = new PersistentTokenBasedRememberMeServices("rememberme", userDetailsService, this.persistentTokenRepository());
        persistentTokenBasedRememberMeServices.setAlwaysRemember(true);
        return persistentTokenBasedRememberMeServices;
    }
}
