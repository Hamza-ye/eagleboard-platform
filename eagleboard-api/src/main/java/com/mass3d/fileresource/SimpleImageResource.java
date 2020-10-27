package com.mass3d.fileresource;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * This is simple class to represent the list of possible static image
 * resources.
 */
@XmlRootElement
public class SimpleImageResource
{
    private Map<String, String> images;

    @JsonProperty
    public Map<String, String> getImages()
    {
        return images;
    }

    public void setImages( final Map<String, String> images )
    {
        this.images = images;
    }

    public void addImage( final String type, final String path )
    {
        if ( images == null )
        {
            images = new HashMap<>();
        }
        images.put( type, path );
    }

    @Override
    public boolean equals( Object other )
    {
        if ( this == other )
        {
            return true;
        }

        if ( other == null || getClass() != other.getClass() )
        {
            return false;
        }

        SimpleImageResource that = (SimpleImageResource) other;

        return Objects.equals( this.images, that.images );
    }

    @Override
    public int hashCode()
    {
        return Objects.hash( images );
    }

    @Override
    public String toString()
    {
        return "SimpleImageResource{" + "images=" + images + '}';
    }
}
