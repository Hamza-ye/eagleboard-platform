package com.mass3d.sms.config.views;

/**
 *
 * Json view to keep confidential parameters from exposing through API and make sure their availability
 * while de-serialisation.
 *
 */
public class SmsConfigurationViews
{
    public static class Public
    {
    }

    public static class Internal extends Public
    {
    }
}
