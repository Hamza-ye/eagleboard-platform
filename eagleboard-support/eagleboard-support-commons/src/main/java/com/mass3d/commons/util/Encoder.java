package com.mass3d.commons.util;

import org.apache.commons.text.StringEscapeUtils;

/**
 * Utility class for encoding.
 *
 */
public class Encoder
{
    /**
     * HTML-escapes the String representation of the given Object.
     * @param object the Object.
     * @return an HTML-escaped String representation.
     */
    public String htmlEncode( Object object )
    {
        return object != null ? StringEscapeUtils.escapeHtml4( String.valueOf( object ) ) : null;
    }

    /**
     * HTML-escapes the given String.
     * @param object the String.
     * @return an HTML-escaped representation.
     */
    public String htmlEncode( String object )
    {
        return StringEscapeUtils.escapeHtml4( object );
    }

    /**
     * XML-escapes the given String.
     * @param object the String.
     * @return an XML-escaped representation.
     */
    public String xmlEncode( String object )
    {
        return StringEscapeUtils.escapeXml11( object );
    }

    /**
     * JavaScript-escaped the given String.
     * @param object the String.
     * @return a JavaScript-escaped representation.
     */
    public String jsEncode( String object )
    {
        return StringEscapeUtils.escapeEcmaScript( object );
    }

    /**
     * Escaped the given JSON content using Java String rules.
     *
     * Assumes " is used as quote char and not used inside values and does
     * not escape '.
     *
     * @param object the String.
     * @return the escaped representation.
     */
    public String jsonEncode( String object )
    {
        return StringEscapeUtils.escapeJava( object );
    }

    /**
     * JavaScript-escaped the given String.
     *
     * @param object the object.
     * @param quoteChar the quote char.
     * @return the escaped representation.
     *
     * See {@link #jsEncode(String)}.
     * @deprecated quoteChar is ignored.
     */
    @Deprecated
    public String jsEscape( String object, String quoteChar )
    {
        return jsEncode( object );
    }
}