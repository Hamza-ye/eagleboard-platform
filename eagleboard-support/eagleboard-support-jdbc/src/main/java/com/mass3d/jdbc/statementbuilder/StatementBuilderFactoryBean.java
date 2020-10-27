package com.mass3d.jdbc.statementbuilder;

import static com.google.common.base.Preconditions.checkNotNull;

import com.mass3d.jdbc.StatementBuilder;
import org.hisp.quick.StatementDialect;
import org.springframework.beans.factory.FactoryBean;

public class StatementBuilderFactoryBean
    implements FactoryBean<StatementBuilder>
{
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private final StatementDialect statementDialect;

    public StatementBuilderFactoryBean( StatementDialect statementDialect )
    {
        checkNotNull( statementDialect );
        this.statementDialect = statementDialect;
    }

    private StatementBuilder statementBuilder;

    // -------------------------------------------------------------------------
    // Initialisation
    // -------------------------------------------------------------------------

    public void init()
    {
        if ( statementDialect.equals( StatementDialect.POSTGRESQL ) )
        {
            this.statementBuilder = new PostgreSQLStatementBuilder();
        }
        else if ( statementDialect.equals( StatementDialect.H2 ) )
        {
            this.statementBuilder = new H2StatementBuilder();
        }
        else if ( statementDialect.equals( StatementDialect.HSQL ) )
        {
            this.statementBuilder = new HsqlStatementBuilder();
        }
        else
        {
            throw new RuntimeException( "Unsupported dialect: " + statementDialect.toString() );
        }
    }

    // -------------------------------------------------------------------------
    // FactoryBean implementation
    // -------------------------------------------------------------------------

    @Override
    public StatementBuilder getObject() {
        return statementBuilder;
    }

    @Override
    public Class<StatementBuilder> getObjectType()
    {
        return StatementBuilder.class;
    }

    @Override
    public boolean isSingleton()
    {
        return true;
    }
}
