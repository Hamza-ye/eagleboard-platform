package com.mass3d.period;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Collection;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.SessionFactory;
import org.hibernate.StatelessSession;
import com.mass3d.dbms.DbmsUtils;
import com.mass3d.system.startup.TransactionContextStartupRoutine;
import org.springframework.stereotype.Component;

@Slf4j
@Component( "com.mass3d.period.PeriodTypePopulator" )
public class PeriodTypePopulator
    extends TransactionContextStartupRoutine
{
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private final PeriodStore periodStore;

    private final SessionFactory sessionFactory;

    public PeriodTypePopulator( PeriodStore periodStore, SessionFactory sessionFactory )
    {
        checkNotNull( periodStore );
        checkNotNull( sessionFactory );

        this.periodStore = periodStore;
        this.sessionFactory = sessionFactory;
    }

    // -------------------------------------------------------------------------
    // Execute
    // -------------------------------------------------------------------------

    @Override
    public void executeInTransaction()
    {
        List<PeriodType> types = PeriodType.getAvailablePeriodTypes();

        Collection<PeriodType> storedTypes = periodStore.getAllPeriodTypes();

        types.removeAll( storedTypes );

        // ---------------------------------------------------------------------
        // Populate missing
        // ---------------------------------------------------------------------

        StatelessSession session = sessionFactory.openStatelessSession();
        session.beginTransaction();
        try
        {
            types.forEach( type -> {
                session.insert( type );
                log.debug( "Added PeriodType: " + type.getName() );
            });
        }
        catch ( Exception exception )
        {
            exception.printStackTrace();
        }
        finally
        {
            DbmsUtils.closeStatelessSession( session );
        }

        types.forEach( type ->  periodStore.reloadPeriodType( type ) );
    }
}
