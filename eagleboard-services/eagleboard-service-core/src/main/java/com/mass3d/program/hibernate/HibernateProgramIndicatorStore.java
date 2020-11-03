package com.mass3d.program.hibernate;

import java.util.List;
import javax.persistence.criteria.CriteriaBuilder;
import org.hibernate.SessionFactory;
import com.mass3d.common.hibernate.HibernateIdentifiableObjectStore;
import com.mass3d.program.ProgramIndicator;
import com.mass3d.program.ProgramIndicatorStore;
import com.mass3d.security.acl.AclService;
import com.mass3d.user.CurrentUserService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository( "com.mass3d.program.ProgramIndicatorStore" )
public class HibernateProgramIndicatorStore
    extends HibernateIdentifiableObjectStore<ProgramIndicator>
    implements ProgramIndicatorStore
{
    public HibernateProgramIndicatorStore( SessionFactory sessionFactory, JdbcTemplate jdbcTemplate,
        ApplicationEventPublisher publisher, CurrentUserService currentUserService, AclService aclService )
    {
        super( sessionFactory, jdbcTemplate, publisher, ProgramIndicator.class, currentUserService, aclService, true );
    }

    @Override
    public List<ProgramIndicator> getProgramIndicatorsWithNoExpression()
    {
        CriteriaBuilder builder = getCriteriaBuilder();

        return getList( builder, newJpaParameters()
            .addPredicate( root -> builder.isNull( root.get( "expression" ) ) ) );
    }
}
