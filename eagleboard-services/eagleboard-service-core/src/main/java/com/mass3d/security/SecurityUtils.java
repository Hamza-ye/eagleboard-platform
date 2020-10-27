package com.mass3d.security;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import org.apache.commons.lang3.StringUtils;
import com.mass3d.user.User;
import com.mass3d.user.UserCredentials;
import org.jboss.aerogear.security.otp.Totp;
import org.springframework.util.Assert;

public class SecurityUtils
{
    private static final String APP_NAME_PREFIX = "DHIS 2 ";
    private static final String QR_PREFIX = "https://chart.googleapis.com/chart?chs=200x200&chld=M%%7C0&cht=qr&chl=";

    /**
     * Generates a QR URL using Google chart API.
     *
     * @param appName the name of the DHIS 2 instance.
     * @param user the user to generate the URL for.
     * @return a QR URL.
     */
    public static String generateQrUrl( String appName, User user )
    {
        Assert.notNull( user.getUserCredentials().getSecret(), "User must have a secret" );

        String app = (APP_NAME_PREFIX + StringUtils.stripToEmpty( appName )).replace( " ", "%20" );
        String url = String.format( "otpauth://totp/%s:%s?secret=%s&issuer=%s",
            app, user.getUsername(), user.getUserCredentials().getSecret(), app );

        try
        {
            return QR_PREFIX + URLEncoder.encode( url, StandardCharsets.UTF_8.name() );
        }
        catch ( UnsupportedEncodingException ex )
        {
            throw new RuntimeException( "Failed to encode QR URL", ex );
        }
    }

    /**
     * Verifies that the secret for the given user matches the given code.
     *
     * @param userCredentials the users credentials.
     * @param code the code.
     * @return true if the user secret matches the given code, false if not.
     */
    public static boolean verify( UserCredentials userCredentials, String code )
    {
        Assert.notNull( userCredentials.getSecret(), "User must have a secret" );

        Totp totp = new Totp( userCredentials.getSecret() );

        return totp.verify( code );
    }
}