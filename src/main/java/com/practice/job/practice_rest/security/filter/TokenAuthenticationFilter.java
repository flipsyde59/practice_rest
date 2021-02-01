package com.practice.job.practice_rest.security.filter;

import com.practice.job.practice_rest.controller.ClientController;
import com.practice.job.practice_rest.model.User;
import com.practice.job.practice_rest.security.token.GetTokenServiceImpl;
import com.practice.job.practice_rest.service.user.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Base64;
import java.util.Locale;

import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.web.filter.GenericFilterBean;


public class TokenAuthenticationFilter extends GenericFilterBean {//AbstractAuthenticationProcessingFilter

    private final TokenAuthenticationManager authenticationManager;

    private final TokenAuthenticationEntryPoint authenticationEntryPoint;
    @Autowired
    private UserRepository userRepository;

    private final String HEADER_TOKEN = "token";
    private final String HEADER_AUTH = "Authorization";
    Logger logger = LoggerFactory.getLogger(TokenAuthenticationFilter.class);
    ;

    public TokenAuthenticationFilter(TokenAuthenticationManager authenticationManager, TokenAuthenticationEntryPoint authenticationEntryPoint) {
        this.authenticationManager = authenticationManager;
        this.authenticationEntryPoint = authenticationEntryPoint;
    }
//    public TokenAuthenticationFilter() {
//        super("/**");
//        setAuthenticationSuccessHandler((request, response, authentication) ->
//        {
//            SecurityContextHolder.getContext().setAuthentication(authentication);
//            request.getRequestDispatcher(request.getServletPath()+request.getPathInfo()).forward(request, response);
//        });
//        setAuthenticationFailureHandler((request, response, authenticationException) -> response.getOutputStream().print(authenticationException.getMessage()));
//    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
        if (httpServletRequest.getRequestURI().length() != 1 && httpServletRequest.getRequestURI().substring(0, 5).equalsIgnoreCase("/rest")) {
            try {
                String token = httpServletRequest.getHeader(HEADER_TOKEN);
                if (token == null || token.equalsIgnoreCase("")) {
                    logger.info("Header " + HEADER_TOKEN + " is not found.");
                    String authCreds = httpServletRequest.getHeader(HEADER_AUTH);
                    if (authCreds == null || authCreds.equalsIgnoreCase("")) {
                        logger.info("Header " + HEADER_AUTH + " is not found.");
                        throw new BadCredentialsException("Header " + HEADER_TOKEN + " and " + HEADER_AUTH + " is not found.", null);
                    }
                    byte[] result = Base64.getDecoder().decode(authCreds.substring(6));
                    String userCredsString = new String(result);
                    String[] userCreds = userCredsString.split(":");
                    logger.info("Creating token for user: " + userCreds[0] + " start");
                    String t = authenticationManager.createToken(userCreds[0], userCreds[1]);
                    if (t.toLowerCase(Locale.ROOT).contains("error")) {
                        logger.info("Token not create, error: " + t);
                        throw new BadCredentialsException("Token not create, error: " + t, null);
                    }
                    logger.info("Token for user: " + userCreds[0] + " created");
                    token = t;
                }

                Authentication authentication = authenticationManager.authenticate(new TokenAuthentication(token));

                SecurityContextHolder.getContext().setAuthentication(authentication);

                filterChain.doFilter(servletRequest, servletResponse);
            } catch (AuthenticationException ex) {
                authenticationEntryPoint.commence(httpServletRequest, httpServletResponse, ex);
            }
        } else {
            filterChain.doFilter(servletRequest, servletResponse);
        }
    }
}
//    @Override
//    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
//            throws AuthenticationException {
//        String token = request.getHeader("token");
//        if (token == null)
//            token = request.getParameter("token");
//        if (token == null) {
//            return null;
//        }
//        TokenAuthentication tokenAuthentication = new TokenAuthentication(token);
//        return getAuthenticationManager().authenticate(tokenAuthentication);
//    }
//
//    @Override
//    public void doFilter(ServletRequest req, ServletResponse res,
//                         FilterChain chain) throws IOException, ServletException {
//        super.doFilter(req, res, chain);
//    }}
