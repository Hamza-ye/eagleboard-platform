package com.mass3d.render;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;
import java.util.Date;

public class DeserializeTest
{
    private String a;

    private String b;

    private String c;

    private Date d;

    public DeserializeTest()
    {
    }

    @JsonProperty
    public String getA()
    {
        return a;
    }

    public void setA( String a )
    {
        this.a = a;
    }

    @JsonProperty
    public String getB()
    {
        return b;
    }

    public void setB( String b )
    {
        this.b = b;
    }

    @JsonProperty
    public String getC()
    {
        return c;
    }

    public void setC( String c )
    {
        this.c = c;
    }

    @JsonProperty
    public Date getD()
    {
        return d;
    }

    public void setD( Date d )
    {
        this.d = d;
    }


    @Override
    public String toString()
    {
        return MoreObjects.toStringHelper( this )
            .add( "a", a )
            .add( "b", b )
            .add( "c", c )
            .add( "d", d )
            .toString();
    }
}
