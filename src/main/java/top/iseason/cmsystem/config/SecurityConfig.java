package top.iseason.cmsystem.config;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.*;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandlerImpl;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import top.iseason.cmsystem.entity.user.BaseUser;
import top.iseason.cmsystem.service.UserService;
import top.iseason.cmsystem.utils.Result;
import top.iseason.cmsystem.utils.ResultCode;

import javax.annotation.Resource;
import java.util.Collections;

@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .formLogin()
                .usernameParameter("username")
                .passwordParameter("password")
                .loginProcessingUrl("/public/login")
                .permitAll()
                .successHandler((request, response, authentication) -> {
                    response.setContentType("text/json;charset=utf-8");
                    response.getWriter().write(Result.of(ResultCode.USER_LOGIN_SUCCESS).toString());
                })
                .failureHandler((request, response, exception) -> {
                    response.setContentType("text/json;charset=utf-8");
                    Result result;
                    if (exception instanceof AccountExpiredException) {
                        //????????????
                        result = Result.of(ResultCode.USER_ACCOUNT_EXPIRED);
                    } else if (exception instanceof BadCredentialsException) {
                        //????????????
                        result = Result.of(ResultCode.USER_CREDENTIALS_ERROR);
                    } else if (exception instanceof CredentialsExpiredException) {
                        //????????????
                        result = Result.of(ResultCode.USER_CREDENTIALS_EXPIRED);
                    } else if (exception instanceof DisabledException) {
                        //???????????????
                        result = Result.of(ResultCode.USER_ACCOUNT_DISABLE);
                    } else if (exception instanceof LockedException) {
                        //????????????
                        result = Result.of(ResultCode.USER_ACCOUNT_LOCKED);
                    } else if (exception instanceof InternalAuthenticationServiceException) {
                        //???????????????
                        result = Result.of(ResultCode.USER_ACCOUNT_NOT_EXIST);
                    } else {
                        //????????????
                        result = Result.of(ResultCode.COMMON_FAIL);
                    }
                    response.getWriter().write(result.toString());
                })
                .and()
                .rememberMe()
                .key("remember")
                .and()
                .logout()
                .logoutUrl("/public/logout")
                .permitAll()
                .logoutSuccessHandler((request, response, authentication) -> {
                    response.setContentType("text/json;charset=utf-8");
                    response.getWriter().write(Result.of(ResultCode.USER_LOGOUT_SUCCESS).toString());
                })
                .deleteCookies("JSESSIONID")
                .and()
                .userDetailsService(userDetailsService())
                .authorizeRequests()
                .antMatchers("/judge/**").hasAnyRole("JUDGE", "ADMIN")
                .antMatchers("/team/**").hasAnyRole("TEAM", "ADMIN")
                .antMatchers("/organization/**").hasAnyRole("ORGANIZATION", "ADMIN")
                .antMatchers("/admin/**").hasRole("ADMIN")
                .antMatchers("/user/**").authenticated()
                .antMatchers("/public/**").permitAll()

//                .antMatchers("/doc.html").permitAll()
//                .antMatchers("/webjars/**").permitAll()
//                .anyRequest()
//                .authenticated()
                .and()
                .exceptionHandling()
                .accessDeniedHandler(new AccessDeniedHandlerImpl())
                .authenticationEntryPoint((request, response, exception) -> {
//                exception.printStackTrace();
                    response.setContentType("text/json;charset=utf-8");
                    response.getWriter().write(Result.of(ResultCode.USER_NOT_LOGIN).toString());
                }).and()
                .csrf().csrfTokenRepository(csrfTokenRepository())
                .and()
                .cors(c -> {
                    CorsConfigurationSource source = request -> {
                        CorsConfiguration config = new CorsConfiguration();
                        config.setAllowedOrigins(Collections.singletonList("*"));
                        config.setAllowedMethods(Collections.singletonList("*"));
                        return config;
                    };
                    c.configurationSource(source);
                });

        return http.build();
    }

    @Resource
    private UserService userService;

    @Bean
    public UserDetailsService userDetailsService() {
        return mail -> {
            BaseUser user = userService.getOne(new QueryWrapper<BaseUser>().eq("mail", mail));
            if (user == null) throw new UsernameNotFoundException(mail);
            return user.toUser();
        };
    }

    @Bean
    public CsrfTokenRepository csrfTokenRepository() {
//        return new CookieCsrfTokenRepository();
        return new HttpSessionCsrfTokenRepository();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
