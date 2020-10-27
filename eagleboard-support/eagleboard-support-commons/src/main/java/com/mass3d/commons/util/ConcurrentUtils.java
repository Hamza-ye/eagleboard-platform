package com.mass3d.commons.util;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import lombok.extern.slf4j.Slf4j;

/**
 * Utility class for concurrency operations.
 *
 */
@Slf4j
public class ConcurrentUtils
{
    /**
     * Blocks and waits for all Futures in the given collection to complete.
     *
     * @param futures the collection of Futures.
     */
    public static void waitForCompletion( Collection<Future<?>> futures )
    {
        for ( Future<?> future : futures )
        {
            try
            {
                future.get();
            }
            catch ( ExecutionException ex )
            {
                throw new RuntimeException( "Exception during execution", ex );
            }
            catch ( InterruptedException ex )
            {
                log.warn( "Thread interrupted", ex );

                Thread.currentThread().interrupt();
            }
        }
    }

    /**
     * Returns a {@link Future} which is immediately completed and has its
     * value set to an empty string.
     *
     * @return a future which is immediately completed.
     */
    public static Future<?> getImmediateFuture()
    {
        return CompletableFuture.completedFuture( "" );
    }
}
