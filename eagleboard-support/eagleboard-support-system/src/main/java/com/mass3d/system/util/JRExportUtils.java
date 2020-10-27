package com.mass3d.system.util;

import java.io.OutputStream;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleXlsReportConfiguration;

/**
 * Supports PDF, HMTL and XLS exports.
 * 
 */
public class JRExportUtils
{
    public static final String TYPE_XLS = "xls";
    public static final String TYPE_PDF = "pdf";
    
    /**
     * Export the provided JasperPrint the format given by type.
     *
     * @param type the type to export to. XLS, PDF and HTML are supported.
     * @param out the OutputStream to export to.
     * @param jasperPrint the JasperPrint to export.
     * @throws JRException on export failure.
     */
    public static void export( String type, OutputStream out, JasperPrint jasperPrint )
        throws JRException
    {
        if ( TYPE_XLS.equals( type ) )
        {
            SimpleXlsReportConfiguration config = new SimpleXlsReportConfiguration();
            
            config.setDetectCellType( true );
            config.setRemoveEmptySpaceBetweenRows( true );
            config.setRemoveEmptySpaceBetweenRows( true );
            config.setCollapseRowSpan( true );
            config.setWhitePageBackground( false );
            
            JRXlsExporter exporter = new JRXlsExporter();
            exporter.setExporterInput( new SimpleExporterInput( jasperPrint ) );
            exporter.setExporterOutput( new SimpleOutputStreamExporterOutput( out ) );
            exporter.setConfiguration( config );
            exporter.exportReport();
        }
        else if ( TYPE_PDF.equals( type ) )
        {
            JRPdfExporter exporter = new JRPdfExporter();
            exporter.setExporterInput( new SimpleExporterInput( jasperPrint ) );
            exporter.setExporterOutput( new SimpleOutputStreamExporterOutput( out ) );
            exporter.exportReport();
        }
    }
}


