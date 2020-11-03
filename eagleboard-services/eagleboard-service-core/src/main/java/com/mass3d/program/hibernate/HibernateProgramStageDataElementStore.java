package com.mass3d.program.hibernate;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.persistence.criteria.CriteriaBuilder;
import org.hibernate.SessionFactory;
import com.mass3d.common.hibernate.HibernateIdentifiableObjectStore;
import com.mass3d.dataelement.DataElement;
import com.mass3d.program.ProgramStage;
import com.mass3d.program.ProgramStageDataElement;
import com.mass3d.program.ProgramStageDataElementStore;
import com.mass3d.security.acl.AclService;
import com.mass3d.user.CurrentUserService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository( "com.mass3d.program.ProgramStageDataElementStore" )
public class HibernateProgramStageDataElementStore
    extends HibernateIdentifiableObjectStore<ProgramStageDataElement>
    implements ProgramStageDataElementStore
{
    public HibernateProgramStageDataElementStore( SessionFactory sessionFactory, JdbcTemplate jdbcTemplate,
        ApplicationEventPublisher publisher, CurrentUserService currentUserService, AclService aclService )
    {
        super( sessionFactory, jdbcTemplate, publisher, ProgramStageDataElement.class, currentUserService, aclService, false );
    }

    @Override
    public ProgramStageDataElement get( ProgramStage programStage, DataElement dataElement )
    {
        CriteriaBuilder builder = getCriteriaBuilder();

        return getSingleResult( builder, newJpaParameters()
            .addPredicate( root -> builder.equal( root.get( "programStage" ), programStage ) )
            .addPredicate( root -> builder.equal( root.get( "dataElement" ), dataElement ) ) );
    }

    @Override
    public Map<String, Set<String>> getProgramStageDataElementsWithSkipSynchronizationSetToTrue()
    {
        final String sql = "select ps.uid as ps_uid, de.uid as de_uid from programstagedataelement psde " +
            "join programstage ps on psde.programstageid = ps.programstageid " +
            "join dataelement de on psde.dataelementid = de.dataelementid " +
            "where psde.programstageid in (select distinct ( programstageid ) from programstageinstance psi where psi.lastupdated > psi.lastsynchronized) " +
            "and psde.skipsynchronization = true";

        final Map<String, Set<String>> psdesWithSkipSync = new HashMap<>();
        jdbcTemplate.query( sql, rs -> {
            String programStageUid = rs.getString( "ps_uid" );
            String dataElementUid = rs.getString( "de_uid" );

            psdesWithSkipSync.computeIfAbsent( programStageUid, p -> new HashSet<>() ).add( dataElementUid );
        });

        return psdesWithSkipSync;
    }
}
