package com.mass3d.artemis.audit.configuration;

import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.collect.ImmutableMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import com.mass3d.audit.AuditScope;
import com.mass3d.audit.AuditType;
import com.mass3d.external.conf.ConfigurationKey;
import com.mass3d.external.conf.DhisConfigurationProvider;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

/**
 * Configures the Audit Matrix based on configuration properties from dhis.conf
 * <p>
 * This configurator uses properties with prefix "audit.". Each property prefixed with "audit."
 * must match the (lowercase) name of an {@see AuditScope} and must contain a semi-colon list of valid
 * {@see AuditType} names: (READ;UPDATE;...).
 * <p>
 * Example:
 * <p>
 * audit.tracker=CREATE;READ;UPDATE;DELETE
 * <p>
 * Misspelled entries are ignored, and the specific type is set to false.
 * Missing {@see AuditScope} are replaced with all-false types.
 * To disable Auditing completely, simply do not declare any audit.* property in dhis.conf
 *
 */
@Component
@DependsOn( "dhisConfigurationProvider" )
public class AuditMatrixConfigurer
{
    private final DhisConfigurationProvider config;
    private final static String PROPERTY_PREFIX = "audit.";
    private final static String AUDIT_TYPE_STRING_SEPAR = ";";

    /**
     * Default Audit configuration: CREATE, UPDATE and DELETE operations are audited by default.
     * Other Audit types have to be explicitly enabled by the user
     */
    private static final Map<AuditType, Boolean> DEFAULT_AUDIT_CONFIGURATION = ImmutableMap.<AuditType, Boolean>builder()
        .put( AuditType.CREATE, true )
        .put( AuditType.UPDATE, true )
        .put( AuditType.DELETE, true )
        .put( AuditType.READ, false )
        .put( AuditType.SEARCH, false )
        .put( AuditType.SECURITY, false )
        .build();

    public AuditMatrixConfigurer( DhisConfigurationProvider dhisConfigurationProvider )
    {
        checkNotNull( dhisConfigurationProvider );

        this.config = dhisConfigurationProvider;
    }

    public Map<AuditScope, Map<AuditType, Boolean>> configure()
    {
        Map<AuditScope, Map<AuditType, Boolean>> matrix = new HashMap<>();

        for ( AuditScope value : AuditScope.values() )
        {
            Optional<ConfigurationKey> confKey = ConfigurationKey.getByKey( PROPERTY_PREFIX + value.name().toLowerCase() );

            if ( confKey.isPresent() && !StringUtils.isEmpty( config.getProperty( confKey.get() ) ) )
            {
                String[] configuredTypes = config.getProperty( confKey.get() ).split( AUDIT_TYPE_STRING_SEPAR );

                Map<AuditType, Boolean> matrixAuditTypes = new HashMap<>();

                for ( AuditType auditType : AuditType.values() )
                {
                    matrixAuditTypes.put( auditType, ArrayUtils.contains( configuredTypes, auditType.name() ) );
                }

                matrix.put( value, matrixAuditTypes );

            }
            else
            {
                matrix.put( value, DEFAULT_AUDIT_CONFIGURATION );
            }
        }

        return matrix;
    }
}
