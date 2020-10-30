package com.mass3d.dxf2.adx;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.concurrent.Callable;
import org.hibernate.SessionFactory;
import com.mass3d.commons.util.StreamUtils;
import com.mass3d.dbms.DbmsUtils;
import com.mass3d.dxf2.common.ImportOptions;
import com.mass3d.dxf2.datavalueset.DataValueSetService;
import com.mass3d.dxf2.importsummary.ImportStatus;
import com.mass3d.dxf2.importsummary.ImportSummary;
import com.mass3d.scheduling.JobConfiguration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class AdxPipedImporter
    implements Callable<ImportSummary>
{
    public static final int PIPE_BUFFER_SIZE = 4096;

    public static final int TOTAL_MINUTES_TO_WAIT = 5;

    protected PipedInputStream pipeIn;

    private final DataValueSetService dataValueSetService;

    private final ImportOptions importOptions;

    private final JobConfiguration id;

    private final SessionFactory sessionFactory;

    private final Authentication authentication;

    public AdxPipedImporter( DataValueSetService dataValueSetService, ImportOptions importOptions,
        JobConfiguration id, PipedOutputStream pipeOut, SessionFactory sessionFactory ) throws IOException
    {
        this.dataValueSetService = dataValueSetService;
        this.pipeIn = new PipedInputStream( pipeOut, PIPE_BUFFER_SIZE );
        this.importOptions = importOptions;
        this.id = id;
        this.sessionFactory = sessionFactory;
        this.authentication = SecurityContextHolder.getContext().getAuthentication();
    }

    @Override
    public ImportSummary call()
    {
        ImportSummary result = null;
        SecurityContextHolder.getContext().setAuthentication( authentication );
        DbmsUtils.bindSessionToThread( sessionFactory );

        try
        {
            result = dataValueSetService.saveDataValueSet( pipeIn, importOptions, id );
        }
        catch ( Exception ex )
        {
            result = new ImportSummary();
            result.setStatus( ImportStatus.ERROR );
            result.setDescription( "Exception: " + ex.getMessage() );
        }
        finally
        {
            StreamUtils.closeQuietly( pipeIn );
            DbmsUtils.unbindSessionFromThread( sessionFactory );
        }

        return result;
    }
}
