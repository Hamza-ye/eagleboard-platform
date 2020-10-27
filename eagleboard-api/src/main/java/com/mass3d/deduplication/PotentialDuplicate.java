package com.mass3d.deduplication;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.mass3d.common.BaseIdentifiableObject;
import com.mass3d.schema.PropertyType;
import com.mass3d.schema.annotation.Property;
import com.mass3d.schema.annotation.PropertyRange;

public class PotentialDuplicate
    extends BaseIdentifiableObject
{
    /**
     * teiA represents the UID of a TrackedEntityInstance.
     * teiA is required.
     * teiA is a potential duplicate of teiB.
     * if teiB is null, it indicates a user has flagged teiA as
     * a potential duplicate, without knowing which TrackedEntityInstance
     * it is a duplicate of.
     */
    private String teiA;

    /**
     * teiB represents the UID of a TrackedEntityInstance.
     * teiB is optional.
     * teiB is a potential duplicate of teiA.
     */
    private String teiB;

    /**
     * status represents the state of the PotentialDuplicate.
     * all new Potential duplicates are OPEN by default.
     */
    private DeduplicationStatus status = DeduplicationStatus.OPEN;

    public PotentialDuplicate()
    {
    }

    public PotentialDuplicate( String teiA )
    {
        this.teiA = teiA;
    }

    public PotentialDuplicate( String teiA, String teiB )
    {
        this.teiA = teiA;
        this.teiB = teiB;
    }

    @JsonProperty
    @JacksonXmlProperty
    @Property( value = PropertyType.IDENTIFIER, required = Property.Value.TRUE )
    @PropertyRange( min = 11, max = 11 )
    public String getTeiA()
    {
        return teiA;
    }

    public void setTeiA( String teiA )
    {
        this.teiA = teiA;
    }

    @JsonProperty
    @JacksonXmlProperty
    @Property( value = PropertyType.IDENTIFIER, required = Property.Value.FALSE )
    @PropertyRange( min = 11, max = 11 )
    public String getTeiB()
    {
        return teiB;
    }

    public void setTeiB( String teiB )
    {
        this.teiB = teiB;
    }

    @JsonProperty
    @JacksonXmlProperty
    public DeduplicationStatus getStatus()
    {
        return status;
    }

    public void setStatus( DeduplicationStatus status )
    {
        this.status = status;
    }
}
