package com.mass3d.dxf2.common;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashCodeGenerator
{
    public static String getHashCode( String value ) throws NoSuchAlgorithmException
    {
        byte[] bytesOfMessage = value.getBytes( StandardCharsets.UTF_8 );
        MessageDigest md = MessageDigest.getInstance( "MD5" );
        byte[] digest = md.digest( bytesOfMessage );

        StringBuilder hexString = new StringBuilder();
        for ( byte aDigest : digest )
        {
            String hex = Integer.toHexString( 0xFF & aDigest );
            if ( hex.length() == 1 )
                hexString.append( '0' );
            hexString.append( hex );
        }
        return hexString.toString();
    }
}
