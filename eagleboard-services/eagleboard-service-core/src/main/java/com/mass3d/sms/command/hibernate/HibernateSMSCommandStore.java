package com.mass3d.sms.command.hibernate;

import java.util.List;
import javax.persistence.criteria.CriteriaBuilder;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import com.mass3d.common.hibernate.HibernateIdentifiableObjectStore;
import com.mass3d.dataset.DataSet;
import com.mass3d.query.JpaQueryUtils;
import com.mass3d.security.acl.AclService;
import com.mass3d.sms.command.SMSCommand;
import com.mass3d.sms.parse.ParserType;
import com.mass3d.user.CurrentUserService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository( "com.mass3d.sms.command.hibernate.SMSCommandStore" )
public class HibernateSMSCommandStore
    extends HibernateIdentifiableObjectStore<SMSCommand> implements SMSCommandStore
{
    public HibernateSMSCommandStore( SessionFactory sessionFactory, JdbcTemplate jdbcTemplate,
        ApplicationEventPublisher publisher, CurrentUserService currentUserService, AclService aclService )
    {
        super( sessionFactory, jdbcTemplate, publisher, SMSCommand.class, currentUserService, aclService, true );
    }

    @Override
    public List<SMSCommand> getJ2MESMSCommands()
    {
        CriteriaBuilder builder = getCriteriaBuilder();

        return getList( builder, newJpaParameters()
            .addPredicate( root -> builder.equal( root.get( "parserType" ), ParserType.J2ME_PARSER ) ) );
    }

    @Override
    public SMSCommand getSMSCommand( String commandName, ParserType parserType )
    {
        CriteriaBuilder builder = getCriteriaBuilder();

        List<SMSCommand> list = getList( builder, newJpaParameters()
            .addPredicate( root -> builder.equal( root.get( "parserType" ), parserType ) )
            .addPredicate( root -> JpaQueryUtils.stringPredicateIgnoreCase( builder, root.get( "name" ), commandName, JpaQueryUtils.StringSearchMode.ANYWHERE ) ) );

        if ( list != null && !list.isEmpty() )
        {
            return  list.get( 0 );
        }

        return null;
    }

    @Override
    public int countDataSetSmsCommands( DataSet dataSet )
    {
        Query<Long> query = getTypedQuery( "select count(distinct c) from SMSCommand c where c.dataset=:dataSet" );
        query.setParameter( "dataSet", dataSet );
        // TODO rename data set property

        return  query.getSingleResult().intValue();
    }
}
