package com.mass3d.datastatistics.hibernate;

import static com.mass3d.util.DateUtils.asSqlDate;

import com.google.common.collect.Lists;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.hibernate.SessionFactory;
import com.mass3d.analytics.SortOrder;
import com.mass3d.datastatistics.DataStatisticsEvent;
import com.mass3d.datastatistics.DataStatisticsEventStore;
import com.mass3d.datastatistics.DataStatisticsEventType;
import com.mass3d.datastatistics.FavoriteStatistics;
import com.mass3d.hibernate.HibernateGenericStore;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

@Repository( "com.mass3d.datastatistics.DataStatisticsEventStore" )
public class HibernateDataStatisticsEventStore
    extends HibernateGenericStore<DataStatisticsEvent>
    implements DataStatisticsEventStore
{
    public HibernateDataStatisticsEventStore( SessionFactory sessionFactory, JdbcTemplate jdbcTemplate, ApplicationEventPublisher publisher )
    {
        super( sessionFactory, jdbcTemplate, publisher, DataStatisticsEvent.class, false );
    }

    @Override
    public Map<DataStatisticsEventType, Double> getDataStatisticsEventCount( Date startDate, Date endDate )
    {
        Map<DataStatisticsEventType, Double> eventTypeCountMap = new HashMap<>();

        final String sql =
            "select eventtype as eventtype, count(eventtype) as numberofviews " +
            "from datastatisticsevent " +
            "where timestamp between ? and ? " +
            "group by eventtype;";

        PreparedStatementSetter pss = ( ps ) -> {
            int i = 1;
            ps.setDate( i++, asSqlDate( startDate ) );
            ps.setDate( i++, asSqlDate( endDate ) );
        };

        jdbcTemplate.query( sql, pss, ( rs, i ) -> {
            eventTypeCountMap.put( DataStatisticsEventType.valueOf( rs.getString( "eventtype" ) ), rs.getDouble( "numberofviews" ) );
            return eventTypeCountMap;
        } );

        final String totalSql =
            "select count(eventtype) as total " +
            "from datastatisticsevent " +
            "where timestamp between ? and ?;";

        jdbcTemplate.query( totalSql, pss, ( resultSet, i ) -> {
            return eventTypeCountMap.put( DataStatisticsEventType.TOTAL_VIEW, resultSet.getDouble( "total" ) );
        } );

        return eventTypeCountMap;
    }

    @Override
    public List<FavoriteStatistics> getFavoritesData( DataStatisticsEventType eventType, int pageSize, SortOrder sortOrder, String username )
    {
        Assert.notNull( eventType, "Data statistics event type cannot be null" );
        Assert.notNull( sortOrder, "Sort order cannot be null" );

        String sql =
            "select c.uid, views, c.name, c.created from ( " +
            "select favoriteuid as uid, count(favoriteuid) as views " +
            "from datastatisticsevent ";

        if ( username != null )
        {
            sql += "where username = ? ";
        }

        sql +=
            "group by uid) as events " +
            "inner join " + eventType.getTable() + " c on c.uid = events.uid " +
            "order by events.views " + sortOrder.getValue() + " " +
            "limit ?;";

        PreparedStatementSetter pss = ( ps ) -> {
            int i = 1;

            if ( username != null )
            {
                ps.setString( i++, username );
            }

            ps.setInt( i++, pageSize );
        };

        return jdbcTemplate.query( sql, pss, ( rs, i ) -> {
            FavoriteStatistics stats = new FavoriteStatistics();

            stats.setPosition( i + 1 );
            stats.setId( rs.getString( "uid" ) );
            stats.setName( rs.getString( "name" ) );
            stats.setCreated( rs.getDate( "created" ) );
            stats.setViews( rs.getInt( "views" ) );

            return stats;
        } );
    }

    @Override
    public FavoriteStatistics getFavoriteStatistics( String uid )
    {
        String sql =
            "select count(dse.favoriteuid) " +
            "from datastatisticsevent dse " +
            "where dse.favoriteuid = ?;";

        Object[] args = Lists.newArrayList( uid ).toArray();

        Integer views = jdbcTemplate.queryForObject( sql, args, Integer.class );

        FavoriteStatistics stats = new FavoriteStatistics();
        stats.setViews( views );
        return stats;
    }
}

