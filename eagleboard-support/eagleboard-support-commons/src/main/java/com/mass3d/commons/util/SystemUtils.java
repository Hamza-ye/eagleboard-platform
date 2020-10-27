package com.mass3d.commons.util;

import java.util.Arrays;

public class SystemUtils
{
    private static final int FACTOR_MB = 1024 * 1024;

    /**
     * Indicates whether the current thread is running for testing.
     *
     * @return true if test run.
     */
    public static boolean isTestRun( String[] profiles )
    {
        return Arrays.asList( profiles ).contains( "test" );
    }

    public static boolean isAuditTest( String[] profiles )
    {
        return Arrays.asList( profiles ).contains( "test-audit" );
    }

    public static boolean isH2( String[] profiles )
    {
        return Arrays.asList( profiles ).contains( "test-h2" );
    }

    /**
     * Gets the number of CPU cores available to this JVM.
     * @return the number of available CPU cores.
     */
    public static int getCpuCores()
    {
        return Runtime.getRuntime().availableProcessors();
    }

    /**
     * Gets a String containing info of available and used memory of this JVM.
     * @return an info string.
     */
    public static String getMemoryString()
    {
        return "Mem Total in JVM: " + ( Runtime.getRuntime().totalMemory() / FACTOR_MB ) +
            " Free in JVM: " + ( Runtime.getRuntime().freeMemory() / FACTOR_MB ) +
            " Max Limit: " + ( Runtime.getRuntime().maxMemory() / FACTOR_MB );
    }
}
