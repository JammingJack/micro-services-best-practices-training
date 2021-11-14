package org.openlab.secservice.filters;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.openlab.secservice.JWTUtil;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import sun.plugin.liveconnect.SecurityContextHelper;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

public class JwtAuthorizationFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if(request.getServletPath().equals("/refreshToken")){
            filterChain.doFilter(request, response);
        }else{

            String authorizationToken = request.getHeader("Authorization");
            if(authorizationToken!=null && authorizationToken.startsWith(JWTUtil.PREFREX)){
                try{
                    String jwt = authorizationToken.substring(JWTUtil.PREFREX.length());
                    Algorithm algorithm = Algorithm.HMAC256(JWTUtil.SECRET);
                    JWTVerifier jwtVerifier = JWT.require(algorithm).build();
                    DecodedJWT decodedJWT = jwtVerifier.verify(jwt);
                    String username = decodedJWT.getSubject();
                    String[] roles = decodedJWT.getClaim("roles").asArray(String.class);
                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                            username,
                            null,
                            Arrays
                                    .stream(roles)
                                    .map(role -> new SimpleGrantedAuthority(role))
                                    .collect(Collectors.toList())
                    );
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                    filterChain.doFilter(request, response);
                }
                catch (Exception e){
                    response.setHeader("error-message", e.getMessage());
                    response.sendError(HttpServletResponse.SC_FORBIDDEN);
                }
            }else{
                filterChain.doFilter(request, response);
            }
        }
    }
}
