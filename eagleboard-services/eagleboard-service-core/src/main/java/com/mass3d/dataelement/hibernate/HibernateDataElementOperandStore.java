package com.mass3d.dataelement.hibernate;

import java.util.List;
import org.hibernate.SessionFactory;
import com.mass3d.common.hibernate.HibernateIdentifiableObjectStore;
import com.mass3d.dataelement.DataElementOperand;
import com.mass3d.dataelement.DataElementOperandStore;
import com.mass3d.security.acl.AclService;
import com.mass3d.user.CurrentUserService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository( "com.mass3d.dataelement.DataElementOperandStore" )
public class HibernateDataElementOperandStore
    extends HibernateIdentifiableObjectStore<DataElementOperand>
    implements DataElementOperandStore
{
    public HibernateDataElementOperandStore( SessionFactory sessionFactory, JdbcTemplate jdbcTemplate,
        ApplicationEventPublisher publisher, CurrentUserService currentUserService, AclService aclService )
    {
        super( sessionFactory, jdbcTemplate, publisher, DataElementOperand.class, currentUserService, aclService, false );

        transientIdentifiableProperties = true;
    }

    @Override
    public List<DataElementOperand> getAllOrderedName()
    {
        return getQuery( "from DataElementOperand d" ).list();
    }

    @Override
    public List<DataElementOperand> getAllOrderedName( int first, int max )
    {
        return getQuery( "from DataElementOperand d" ).setFirstResult( first ).setMaxResults( max ).list();
    }
}
