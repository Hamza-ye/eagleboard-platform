package com.mass3d.audit;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import lombok.Value;

@Data
@Builder
public class AuditQuery
{
    /**
     * This narrows the search scope for audits by types.
     */
    @Builder.Default
    private Set<AuditType> auditType = new HashSet<>();

    /**
     * This narrows the search scope for audits by scopes.
     */
    @Builder.Default
    private Set<AuditScope> auditScope = new HashSet<>();

    /**
     * This narrows the search scope for audits, the class name should be fully qualified.
     * <p>
     * TODO should it be fully qualified? what about refactors? what about duplicate class names if we don't do it?
     */
    @Builder.Default
    private Set<String> klass = new HashSet<>();

    /**
     * This narrows the search scope by search by a list of UIDs. This binds an AND relationship with klass,
     * and a OR relationship with code.
     */
    @Builder.Default
    private Set<String> uid = new HashSet<>();

    /**
     * This narrows the search scope by search by a list of codes. This binds an AND relationship with klass,
     * and a OR relationship with uid.
     */
    @Builder.Default
    private Set<String> code = new HashSet<>();

    /**
     * This narrows the search by filtering records base on the values of {@link AuditAttributes}
     */
    @Builder.Default
    private AuditAttributes auditAttributes = new AuditAttributes();

    /**
     * From/To dates to query from.
     */
    private Range range;

    static Range range( LocalDateTime from )
    {
        return Range.builder().from( from ).build();
    }

    static Range range( LocalDateTime from, LocalDateTime to )
    {
        return Range.builder().from( from ).to( to ).build();
    }

    @Value
    @Builder( access = AccessLevel.PRIVATE )
    public static class Range
    {
        /**
         * From date to fetch audits from.
         */
        private @NonNull LocalDateTime from;

        /**
         * To date to fetch audits from.
         */
        private LocalDateTime to;
    }
}
