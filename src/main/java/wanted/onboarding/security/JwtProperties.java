package wanted.onboarding.security;

public interface JwtProperties {

    String SECRET = "eyvUXTmpbXwnMJDdGtNVcvDPqRUHQxKfklwjkgj";
    String TOKEN_PREFIX = "Bearer ";
    String HEADER_STRING = "Authorization";
    long EXPIRATION_TIME = 30 * 60 * 1000L;
}
