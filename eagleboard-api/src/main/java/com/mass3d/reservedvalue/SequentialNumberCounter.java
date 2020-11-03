package com.mass3d.reservedvalue;

public class SequentialNumberCounter
{
    private int id;

    private String ownerUid;

    private String key;

    private int counter;

    public SequentialNumberCounter()
    {
        counter = 0;
    }

    public SequentialNumberCounter( String ownerUid, String key, int counter )
    {
        this.ownerUid = ownerUid;
        this.key = key;
        this.counter = counter;
    }

    public int getId()
    {
        return id;
    }

    public void setId( int id )
    {
        this.id = id;
    }

    public String getOwnerUid()
    {
        return ownerUid;
    }

    public void setOwnerUid( String ownerUid )
    {
        this.ownerUid = ownerUid;
    }

    public String getKey()
    {
        return key;
    }

    public void setKey( String key )
    {
        this.key = key;
    }

    public int getCounter()
    {
        return counter;
    }

    public void setCounter( int counter )
    {
        this.counter = counter;
    }
}
