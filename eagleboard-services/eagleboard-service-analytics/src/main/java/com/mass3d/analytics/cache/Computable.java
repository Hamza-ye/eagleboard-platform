package com.mass3d.analytics.cache;

/**
 * Responsible for exposing methods responsible for computing a value based on
 * the rules defined by the implementer.
 */
public interface Computable
{
    /**
     * Returns a long value based in the implementation rules.
     *
     * @return the calculated value.
     */
    long compute();
}
