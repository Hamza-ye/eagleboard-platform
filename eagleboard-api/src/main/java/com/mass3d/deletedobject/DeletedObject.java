package com.mass3d.deletedobject;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;
import com.mass3d.common.DxfNamespaces;
import com.mass3d.common.IdentifiableObject;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

@Entity
@Table( name = "deletedobject",
uniqueConstraints = {
    @UniqueConstraint(name = "key_deleted_object_klass_uid",columnNames={"klass", "uid"}) })
@JacksonXmlRootElement( localName = "deletedObject", namespace = DxfNamespaces.DXF_2_0 )
public class DeletedObject
    implements Serializable
{
    /**
     * The database internal identifier for this Object.
     */
    @Id
    @Column( name = "deletedobjectid")
    @GeneratedValue( generator = "deletedobject_sequence" )
    @SequenceGenerator( name = "deletedobject_sequence", sequenceName = "deletedobject_sequence", allocationSize = 1 )
    private long id;

    /**
     * Class of object that was deleted.
     */
    @Column( nullable = false )
    private String klass;

    /**
     * The Unique Identifier for this Object.
     */
    @Column( nullable =  false )
    private String uid;

    /**
     * The unique code for this Object.
     */
    @Column
    private String code;

    /**
     * Date this object was deleted.
     */
    @Column( nullable = false, name = "deleted_at" )
    @Temporal( TemporalType.TIMESTAMP )
    private Date deletedAt = new Date();

    /**
     * User who deleted this object (if available)
     */
    @Column( name = "deleted_by" )
    private String deletedBy;

    protected DeletedObject()
    {
    }

    public DeletedObject( IdentifiableObject identifiableObject )
    {
        Assert.notNull( identifiableObject, "IdentifiableObject is required and can not be null." );
        Assert.notNull( identifiableObject.getUid(), "IdentifiableObject.uid is required and can not be null." );

        this.klass = ClassUtils.getShortName( identifiableObject.getClass() );
        this.uid = identifiableObject.getUid();
        this.code = !StringUtils.isEmpty( identifiableObject.getCode() ) ? identifiableObject.getCode() : null;
    }

    public long getId()
    {
        return id;
    }

    public void setId( long id )
    {
        this.id = id;
    }

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public String getKlass()
    {
        return klass;
    }

    public void setKlass( String klass )
    {
        this.klass = klass;
    }

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public String getUid()
    {
        return uid;
    }

    public void setUid( String uid )
    {
        this.uid = uid;
    }

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public String getCode()
    {
        return code;
    }

    public void setCode( String code )
    {
        this.code = code;
    }

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public Date getDeletedAt()
    {
        return deletedAt;
    }

    public void setDeletedAt( Date deletedAt )
    {
        this.deletedAt = deletedAt;
    }

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public String getDeletedBy()
    {
        return deletedBy;
    }

    public void setDeletedBy( String deletedBy )
    {
        this.deletedBy = deletedBy;
    }

    @Override
    public boolean equals( Object o )
    {
        if ( this == o ) return true;
        if ( o == null || getClass() != o.getClass() ) return false;

        DeletedObject that = (DeletedObject) o;

        return Objects.equal( klass, that.klass ) &&
            Objects.equal( uid, that.uid ) &&
            Objects.equal( code, that.code ) &&
            Objects.equal( deletedAt, that.deletedAt );
    }

    @Override
    public int hashCode()
    {
        return Objects.hashCode( klass, uid, code, deletedAt );
    }


    @Override
    public String toString()
    {
        return MoreObjects.toStringHelper( this )
            .add( "klass", klass )
            .add( "uid", uid )
            .add( "code", code )
            .add( "deletedAt", deletedAt )
            .toString();
    }
}
