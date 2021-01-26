package com.practice.job.practice_rest.security.filter;

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

import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.web.filter.GenericFilterBean;


public class TokenAuthenticationFilter extends GenericFilterBean {//AbstractAuthenticationProcessingFilter

    private final AuthenticationManager authenticationManager;

    private final AuthenticationEntryPoint authenticationEntryPoint;

    private final String HEADER = "token";

    public TokenAuthenticationFilter(AuthenticationManager authenticationManager, AuthenticationEntryPoint authenticationEntryPoint) {
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
                String headerValue = httpServletRequest.getHeader(HEADER);
                if (headerValue == null || headerValue.equalsIgnoreCase("")) {
                    throw new BadCredentialsException("Header " + HEADER + " is not found.", null);
                }

                Authentication authentication = authenticationManager.authenticate(new TokenAuthentication(headerValue));

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
