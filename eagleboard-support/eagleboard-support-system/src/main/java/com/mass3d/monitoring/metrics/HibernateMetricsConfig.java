package com.mass3d.monitoring.metrics;

import static com.mass3d.external.conf.ConfigurationKey.MONITORING_HIBERNATE_ENABLED;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.binder.jpa.HibernateMetrics;
import java.util.Collections;
import java.util.Map;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceException;
import org.apache.commons.lang.StringUtils;
import org.hibernate.SessionFactory;
import com.mass3d.external.conf.ConfigurationKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

@Configuration
@Conditional( HibernateMetricsConfig.HibernateMetricsEnabledCondition.class )
public class HibernateMetricsConfig
{
    private static final String ENTITY_MANAGER_FACTORY_SUFFIX = "entityManagerFactory";

    @Autowired
    public void bindEntityManagerFactoriesToRegistry( Map<String, EntityManagerFactory> entityManagerFactories,
        MeterRegistry registry )
    {
        entityManagerFactories
            .forEach( ( name, factory ) -> bindEntityManagerFactoryToRegistry( name, factory, registry ) );
    }

    private void bindEntityManagerFactoryToRegistry( String beanName, EntityManagerFactory entityManagerFactory,
        MeterRegistry registry )
    {
        String entityManagerFactoryName = getEntityManagerFactoryName( beanName );
        try
        {
            new HibernateMetrics( entityManagerFactory.unwrap( SessionFactory.class ), entityManagerFactoryName,
                Collections.emptyList() ).bindTo( registry );
        }
        catch ( PersistenceException ex )
        {
            // Continue
        }
    }

    /**
     * Get the name of an {@link EntityManagerFactory} based on its
     * {@code beanName}.
     *
     * @param beanName the name of the {@link EntityManagerFactory} bean
     * @return a name for the given entity manager factory
     */
    private String getEntityManagerFactoryName( String beanName )
    {
        if ( beanName.length() > ENTITY_MANAGER_FACTORY_SUFFIX.length()
            && StringUtils.endsWithIgnoreCase( beanName, ENTITY_MANAGER_FACTORY_SUFFIX ) )
        {
            return beanName.substring( 0, beanName.length() - ENTITY_MANAGER_FACTORY_SUFFIX.length() );
        }
        return beanName;
    }

    static class HibernateMetricsEnabledCondition
        extends
        MetricsEnabler
    {
        @Override
        protected ConfigurationKey getConfigKey()
        {
            return MONITORING_HIBERNATE_ENABLED;
        }
    }
}
