package com.mass3d.artemis.audit;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;
import com.mass3d.artemis.MessageType;
import com.mass3d.artemis.SerializableMessage;
import com.mass3d.audit.AuditAttributes;
import com.mass3d.audit.AuditScope;
import com.mass3d.audit.AuditType;
import com.mass3d.common.IdentifiableObject;

/**
 * Class for Audit messages, mostly compatible with {@link com.mass3d.audit.Audit}
 * but has some additions relevant only to Artemis messages.
 *
 */
@Data
@Builder( builderClassName = "AuditBuilder" )
@JsonDeserialize( builder = Audit.AuditBuilder.class )
public class Audit implements SerializableMessage
{
    @JsonProperty
    private final AuditType auditType;

    @JsonProperty
    private AuditScope auditScope;

    @JsonProperty
    private LocalDateTime createdAt;

    @JsonProperty
    private String createdBy;

    @JsonProperty
    private String klass;

    @JsonProperty
    private String uid;

    @JsonProperty
    private String code;

    @JsonProperty
    @Builder.Default
    private AuditAttributes attributes = new AuditAttributes();

    /**
     * This property holds the serialized Audited entity: it should not be used during the construction
     * of an instance of this object
     */
    @JsonProperty
    private Object data;

    /**
     * This property should be used when constructing an Audit instance to send to the Audit sub-system
     * The AuditableEntity object allows the AuditManager to serialize the audited entity only if needed
     */
    @JsonIgnore
    private AuditableEntity auditableEntity;

    @Override
    public MessageType getMessageType()
    {
        return MessageType.AUDIT;
    }

    /**
     * Converts the AMQP Audit object to a DAO Audit object.
     * The data property will only be set if data == string.
     * <p>
     * TODO should we just do .toString() if its not a string objects?
     *
     * @return DAO Audit object with data (if data is string).
     */
    public com.mass3d.audit.Audit toAudit()
    {
        com.mass3d.audit.Audit.AuditBuilder auditBuilder = com.mass3d.audit.Audit.builder()
            .auditType( auditType )
            .auditScope( auditScope )
            .createdAt( createdAt )
            .createdBy( createdBy )
            .klass( klass )
            .uid( uid )
            .code( code )
            .attributes( attributes );

        if ( data instanceof String)
        {
            auditBuilder.data( (String) data );
        }

        return auditBuilder.build();
    }

    @JsonPOJOBuilder( withPrefix = "" )
    public static final class AuditBuilder
    {
        private String klass;
        private String uid;
        private String code;

        public AuditBuilder object( Object o )
        {
            if ( o == null )
            {
                return this;
            }

            klass = o.getClass().getName();

            if ( o instanceof IdentifiableObject )
            {
                uid = ((IdentifiableObject) o).getUid();
                code = ((IdentifiableObject) o).getCode();
            }

            return this;
        }
    }

    String toLog()
    {
        return "Audit{" +
            "auditType=" + auditType +
            ", auditScope=" + auditScope +
            ", createdAt=" + createdAt +
            ", createdBy='" + createdBy + '\'' +
            ", klass='" + klass + '\'' +
            ", uid='" + uid + '\'' +
            ", code='" + code + '\'' +
            '}';
    }
}
