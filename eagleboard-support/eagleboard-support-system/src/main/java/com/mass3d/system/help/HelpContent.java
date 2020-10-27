package com.mass3d.system.help;

/**
 * @version $Id: PopupHelpAction.java 08-04-2009 $
 */
public class HelpContent
{
    private String header;

    private String content;

    public HelpContent()
    {   
    }
    
    public String getHeader()
    {
        return header;
    }

    public void setHeader( String header )
    {
        this.header = header;
    }

    public String getContent()
    {
        return content;
    }

    public void setContent( String content )
    {
        this.content = content;
    }
}
