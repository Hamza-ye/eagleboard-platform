package com.mass3d.sms.incoming;

/**
 * Enumeration representing available SMS message encodings.
 */
public enum SmsMessageEncoding
{
    /**
     * 7 bit encoding - standard GSM alphabet.
     */
    ENC7BIT,
    /**
     * 8 bit encoding.
     */
    ENC8BIT,
    /**
     * UCS2 (Unicode) encoding.
     */
    ENCUCS2,
    /**
     * Custom encoding. Currently just defaults to 7-bit.
     */
    ENCCUSTOM
}