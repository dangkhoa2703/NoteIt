package com.noteit.secutity;

import com.noteit.secutity.service.JwtService;
import com.noteit.service.UserServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;


/**
 * Jwt authentication filter.
 */
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    
    private final JwtService jwtService;
    private final UserServiceImpl userServiceImpl;

    /**
     * JWT filter, decode JWT data and hand it to the next security filter.
     *
     * @param request the request
     * @param response the response
     * @param filterChain the filter chain
     */
    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        // extract the token from request header
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String username;

        // if token is invalid -> return null
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // Extract username form jwt, because the jwt is attack to the word "Bearer "
        // the real token start from the 7th character.
        jwt = authHeader.substring(7);
        username = jwtService.extractUsername(jwt);

//      if token contain userDetails and user is not authenticated
//      --> authenticate user
        if( username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = this.userServiceImpl.loadUserByUsername(username);
            if(jwtService.isTokenValid(jwt, userDetails)) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );

//                extends this authentication token with the details of request
                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );

//                update SecurityContextHolder
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }

//            pass data to the next filter
            filterChain.doFilter(request,response);
        }
    }
}
