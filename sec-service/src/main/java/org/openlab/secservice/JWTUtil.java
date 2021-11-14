package org.openlab.secservice;

public class JWTUtil {
    public static final String SECRET = "mySecert123";
    public static final String AUTH_HEADER = "Authorization";
    public static final String PREFREX = "Bearer ";
    public static final long EXPIRED_ACCESS_TOKEN = 2*60*1000;
    public static final long EXPIRED_REFRESH_TOKEN = 15*60*1000;
}
