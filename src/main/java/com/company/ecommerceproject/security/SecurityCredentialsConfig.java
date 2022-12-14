//package com.company.ecommerceproject.security;
//
//import com.company.ecommerceproject.config.AppConfig;
//import com.company.ecommerceproject.config.JwtConfig;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.http.HttpMethod;
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//
//import javax.servlet.http.HttpServletResponse;
//
//@EnableWebSecurity
//public class SecurityCredentialsConfig extends WebSecurityConfigurerAdapter {
//    @Autowired
//    private UserDetailsService userDetailsService;
//
//    @Autowired
//    private JwtConfig jwtConfig;
//
//    @Autowired
//    private AppConfig appConfig;
//
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//
//        http.csrf().disable();
//
//        // make sure we use stateless session; session won't be used to store user's state.
//        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//                .and()
//                // handle an authorized attempts
//                .exceptionHandling().authenticationEntryPoint((req, rsp, e) -> rsp.sendError(HttpServletResponse.SC_UNAUTHORIZED))
//                .and()
//                // Add a filter to validate user credentials and add token in the response header
//
//                // What's the authenticationManager()?
//                // An object provided by WebSecurityConfigurerAdapter, used to authenticate the user passing user's credentials
//                // The filter needs this auth manager to authenticate the user.
//                .addFilter(new JwtUsernameAndPasswordAuthenticationFilter(authenticationManager(), jwtConfig))
//                .authorizeRequests()
//                // allow all POST requests
//                .antMatchers(HttpMethod.POST, jwtConfig.getUri()).permitAll()
//                // any other requests must be authenticated
//                .anyRequest().authenticated();
//    }
//
//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.userDetailsService(userDetailsService).passwordEncoder(appConfig.passwordEncoder());
//    }
//}
