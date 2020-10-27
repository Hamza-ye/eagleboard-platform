package com.mass3d.dxf2.metadata.feedback;

public enum ImportReportMode
{
    /**
     * Gives full import report, including object reports for valid objects.
     */
    FULL,

    /**
     * Returns import report where valid object report has been filtered out.
     */
    ERRORS,

    /**
     * Gives full import report, including object reports for valid objects and names (if available).
     */
    DEBUG
}
