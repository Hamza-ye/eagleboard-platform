package com.mass3d.message.hibernate;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.hibernate.query.Query;
import com.mass3d.common.hibernate.HibernateIdentifiableObjectStore;
import com.mass3d.jdbc.StatementBuilder;
import com.mass3d.message.MessageConversation;
import com.mass3d.message.MessageConversationStatus;
import com.mass3d.message.MessageConversationStore;
import com.mass3d.message.UserMessage;
import com.mass3d.security.acl.AclService;
import com.mass3d.user.CurrentUserService;
import com.mass3d.user.User;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

@Repository( "com.mass3d.message.MessageConversationStore" )
public class HibernateMessageConversationStore
    extends HibernateIdentifiableObjectStore<MessageConversation>
    implements MessageConversationStore
{
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private StatementBuilder statementBuilder;

    public HibernateMessageConversationStore( SessionFactory sessionFactory, JdbcTemplate jdbcTemplate,
        ApplicationEventPublisher publisher, CurrentUserService currentUserService, AclService aclService,
        StatementBuilder statementBuilder )
    {
        super( sessionFactory, jdbcTemplate, publisher, MessageConversation.class, currentUserService, aclService, false );

        checkNotNull( statementBuilder );

        this.statementBuilder = statementBuilder;
    }

    // -------------------------------------------------------------------------
    // Implementation methods
    // -------------------------------------------------------------------------

    @Override
    @SuppressWarnings( "unchecked" )
    public List<MessageConversation> getMessageConversations( User user, MessageConversationStatus status,
        boolean followUpOnly, boolean unreadOnly,
        Integer first, Integer max )
    {
        Assert.notNull( user, "User must be specified" );

        getSession().enableFilter( "userMessageUser" ).setParameter( "userid", user.getId() );

        String hql = "from MessageConversation mc " +
            "inner join mc.userMessages as um " +
            "left join mc.user as ui " +
            "left join mc.lastSender as ls ";

        if ( status != null )
        {
            hql += "where status = :status ";
        }

        if ( followUpOnly )
        {
            hql += (status != null ? "and" : "where") + " um.followUp = true ";
        }

        if ( unreadOnly )
        {
            hql += (status != null || followUpOnly ? "and" : "where") + " um.read = false ";
        }

        hql += "order by mc.lastMessage desc ";

        Query query = getQuery( hql );

        if ( status != null )
        {
            query.setParameter( "status", status.name() );
        }

        if ( first != null && max != null )
        {
            query.setFirstResult( first );
            query.setMaxResults( max );
        }

        return (List<MessageConversation>) query.list()
            .stream()
            .map( o -> mapRowToMessageConversations( (Object[]) o ) )
            .collect( Collectors.toList() );
    }

    @Override
    @SuppressWarnings( "unchecked" )
    public List<MessageConversation> getMessageConversations( Collection<String> uids )
    {
        return getSharingCriteria()
            .add( Restrictions.in( "uid", uids ) )
            .list();
    }

    @Override
    public long getUnreadUserMessageConversationCount( User user )
    {
        Assert.notNull( user, "User must be specified" );

        String hql = "select count(*) from MessageConversation m join m.userMessages u where u.user = :user and u.read = false";

        Query<Long> query = getTypedQuery( hql );
        query.setParameter( "user", user );

        return query.uniqueResult();
    }

    @Override
    public int deleteMessages( User sender )
    {
        Assert.notNull( sender, "User must be specified" );

        String sql = "delete from messageconversation_messages where messageid in (" +
            "select messageid from message where userid = " + sender.getId() + ")";

        getSqlQuery( sql ).executeUpdate();

        String hql = "delete Message m where m.sender = :sender";

        return getQuery( hql )
            .setParameter( "sender", sender )
            .executeUpdate();
    }

    @Override
    public int deleteUserMessages( User user )
    {
        Assert.notNull( user, "User must be specified" );

        String sql = "delete from messageconversation_usermessages where usermessageid in (" +
            "select usermessageid from usermessage where userid = " + user.getId() + ")";

        getSqlQuery( sql ).executeUpdate();

        String hql = "delete UserMessage u where u.user = :user";

        return getQuery( hql )
            .setParameter( "user", user )
            .executeUpdate();
    }

    @Override
    public int removeUserFromMessageConversations( User lastSender )
    {
        Assert.notNull( lastSender, "User must be specified" );

        String hql = "update MessageConversation m set m.lastSender = null where m.lastSender = :lastSender";

        return getQuery( hql )
            .setParameter( "lastSender", lastSender )
            .executeUpdate();
    }

    @Override
    public List<UserMessage> getLastRecipients( User user, Integer first, Integer max )
    {
        Assert.notNull( user, "User must be specified" );

        String sql = " select distinct userinfoid, surname, firstname from userinfo uf " +
            "join usermessage um on (uf.userinfoid = um.userid) " +
            "join messageconversation_usermessages mu on (um.usermessageid = mu.usermessageid) " +
            "join messageconversation mc on (mu.messageconversationid = mc.messageconversationid) " +
            "where mc.lastsenderid = " + user.getId();

        sql += " order by userinfoid desc";

        if ( first != null && max != null )
        {
            sql += " " + statementBuilder.limitRecord( first, max );
        }

        return jdbcTemplate.query( sql, ( resultSet, count ) -> {
            UserMessage recipient = new UserMessage();

            recipient.setId( resultSet.getInt( 1 ) );
            recipient.setLastRecipientSurname( resultSet.getString( 2 ) );
            recipient.setLastRecipientFirstname( resultSet.getString( 3 ) );

            return recipient;
        } );
    }

    private MessageConversation mapRowToMessageConversations( Object[] row )
    {
        MessageConversation mc = (MessageConversation) row[0];

        UserMessage um = (UserMessage) row[1];
        User ui = (User) row[2];
        User ls = (User) row[3];

        mc.setRead( um.isRead() );
        mc.setFollowUp( um.isFollowUp() );

        if ( ui != null )
        {
            mc.setUserFirstname( ui.getFirstName() );
            mc.setUserSurname( ui.getSurname() );
        }

        if ( ls != null )
        {
            mc.setLastSenderFirstname( ls.getFirstName() );
            mc.setLastSenderSurname( ls.getSurname() );
        }

        return mc;
    }
}