package com.mass3d.audit;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRawValue;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;

/**
 * Class for Audit message persistence, meant to be a complete replacement for
 * our existing audit hibernate classes.
 *
 */
@Data
@Builder
public class Audit implements Serializable
{
    private Long id;

    /**
     * Type of audit.
     */
    @JsonProperty
    private final AuditType auditType;

    /**
     * Scope of audit.
     */
    @JsonProperty
    private final AuditScope auditScope;

    /**
     * When audit was done. Necessary since there might be delayed from when the Audit
     * is put on the queue, to when its actually persisted.
     */
    @JsonProperty
    private final LocalDateTime createdAt;

    /**
     * Who initiated the audit action.
     */
    @JsonProperty
    private final String createdBy;

    /**
     * Name of klass being audited. Only required if linked directly to an object.
     */
    @JsonProperty
    private String klass;

    /**
     * UID of object being audited, implies required for klass.
     */
    @JsonProperty
    private String uid;

    /**
     * Code of object being audited, implies required for klass.
     */
    @JsonProperty
    private String code;

    /**
     * Additional attributes attached to Audit. Stored as JSONB in the database.
     */
    @JsonProperty
    @Builder.Default
    private AuditAttributes attributes = new AuditAttributes();

    /**
     * GZipped payload. Exposed as raw json, make sure that the payload is decompressed first.
     */
    @JsonRawValue
    private final String data;
}
