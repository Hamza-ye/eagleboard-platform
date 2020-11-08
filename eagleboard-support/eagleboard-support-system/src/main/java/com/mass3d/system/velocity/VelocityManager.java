package com.mass3d.system.velocity;

import java.io.StringWriter;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;
import org.springframework.stereotype.Component;

@Component( "com.mass3d.system.velocity.VelocityManager" )
public class VelocityManager
{
    public static final String CONTEXT_KEY = "object";

    private static final String RESOURCE_LOADER_NAME = "class";
    private static final String VM_SUFFIX = ".vm";
    
    private VelocityEngine velocity;

    public VelocityManager()
    {
        velocity = new VelocityEngine();
        velocity.setProperty( RuntimeConstants.RESOURCE_LOADER, RESOURCE_LOADER_NAME );
        velocity.setProperty( RESOURCE_LOADER_NAME + ".resource.loader.class", ClasspathResourceLoader.class.getName() );
        velocity.setProperty( "runtime.log.logsystem.log4j.logger", "console" );
        velocity.setProperty( "runtime.log", "" );
                
        velocity.init();
    }

    public VelocityEngine getEngine()
    {
        return velocity;
    }

    public String render( String template )
    {
        return render( null, template );
    }
    
    public String render( Object object, String template )
    {
        try
        {
            StringWriter writer = new StringWriter();

            VelocityContext context = new VelocityContext();

            if ( object != null )
            {
                context.put( CONTEXT_KEY, object );
            }

            velocity.getTemplate( template + VM_SUFFIX ).merge( context, writer );

            return writer.toString();

            // TODO include encoder in context
        } 
        catch ( Exception ex )
        {
            throw new RuntimeException( "Failed to merge velocity template", ex );
        }
    }
}
