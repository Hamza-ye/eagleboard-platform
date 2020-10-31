package com.mass3d.trackedentitycomment;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.mass3d.common.BaseIdentifiableObject;
import com.mass3d.common.DxfNamespaces;

@JacksonXmlRootElement( localName = "trackedEntityComment", namespace = DxfNamespaces.DXF_2_0 )
public class TrackedEntityComment
    extends BaseIdentifiableObject
{
    private String commentText;

    private String creator;

    // -------------------------------------------------------------------------
    // Constructor
    // -------------------------------------------------------------------------

    public TrackedEntityComment()
    {
    }

    public TrackedEntityComment( String commentText, String creator )
    {
        this.commentText = commentText;
        this.creator = creator;
    }

    // -------------------------------------------------------------------------
    // Getters/Setters
    // -------------------------------------------------------------------------

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public String getCommentText()
    {
        return commentText;
    }

    public void setCommentText( String commentText )
    {
        this.commentText = commentText;
    }

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public String getCreator()
    {
        return creator;
    }

    public void setCreator( String creator )
    {
        this.creator = creator;
    }
}
