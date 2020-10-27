package com.mass3d.system.help;

import static com.mass3d.commons.util.StreamUtils.ENCODING_UTF8;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Locale;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;

@Slf4j
public class HelpManager
{
    // -------------------------------------------------------------------------
    // HelpManager implementation
    // -------------------------------------------------------------------------

    public static void getHelpContent( OutputStream out, String id, Locale locale )
    {
        try
        {
            ClassPathResource classPathResource = resolveHelpFileResource( locale );

            Source source = new StreamSource( classPathResource.getInputStream(), ENCODING_UTF8 );

            Result result = new StreamResult( out );

            Transformer transformer = getTransformer( "help_stylesheet.xsl" );

            transformer.setParameter( "sectionId", id );

            transformer.transform( source, result );
        }
        catch ( Exception ex )
        {
            throw new RuntimeException( "Failed to get help content", ex );
        }
    }

    public static void getHelpItems( OutputStream out, Locale locale )
    {
        try
        {
            ClassPathResource classPathResource = resolveHelpFileResource( locale );

            Source source = new StreamSource( classPathResource.getInputStream(), ENCODING_UTF8 );

            Result result = new StreamResult( out );

            getTransformer( "helpitems_stylesheet.xsl" ).transform( source, result );
        }
        catch ( Exception ex )
        {
            throw new RuntimeException( "Failed to get help content", ex );
        }
    }

    // -------------------------------------------------------------------------
    // Supportive methods
    // -------------------------------------------------------------------------

    private static Transformer getTransformer( String stylesheetName )
        throws IOException, TransformerConfigurationException
    {
        Source stylesheet = new StreamSource( new ClassPathResource( stylesheetName ).getInputStream(), ENCODING_UTF8 );

        return TransformerFactory.newInstance().newTransformer( stylesheet );
    }

    private static ClassPathResource resolveHelpFileResource( Locale locale )
    {
        String helpFile;
        
        ClassPathResource classPathResource;

        if ( locale != null && locale.getDisplayLanguage() != null )
        {
            helpFile = "help_content_" + locale.getLanguage() + "_" + locale.getCountry() + ".xml";

            log.debug( "Help file: " + helpFile );
        }
        else
        {
            helpFile = "help_content.xml";

            log.debug( "Help file: " + helpFile );
        }

        classPathResource = new ClassPathResource( helpFile );

        if ( !classPathResource.exists() )
        {
            log.warn( "Help file: " + helpFile + " not available on classpath, falling back to defaul" );
            
            helpFile = "help_content.xml";

            classPathResource = new ClassPathResource( helpFile );
        }

        return classPathResource;
    }
}
