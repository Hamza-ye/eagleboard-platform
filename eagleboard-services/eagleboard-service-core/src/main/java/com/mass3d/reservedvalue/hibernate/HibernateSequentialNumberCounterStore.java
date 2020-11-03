package com.mass3d.reservedvalue.hibernate;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.hibernate.SessionFactory;
import com.mass3d.reservedvalue.SequentialNumberCounterStore;
import org.springframework.stereotype.Repository;

@Repository( "com.mass3d.reservedvalue.SequentialNumberCounterStore" )
public class HibernateSequentialNumberCounterStore
    implements
    SequentialNumberCounterStore
{
    protected SessionFactory sessionFactory;

    public HibernateSequentialNumberCounterStore( SessionFactory sessionFactory )
    {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<Integer> getNextValues( String uid, String key, int length )
    {
        int count = (int) sessionFactory.getCurrentSession()
            .createNativeQuery( "SELECT * FROM incrementSequentialCounter(?0, ?1, ?2)" )
            .setParameter( 0, uid )
            .setParameter( 1, key )
            .setParameter( 2, length )
            .uniqueResult();

        return IntStream
            .range( count - length, length + (count - length) ).boxed().collect( Collectors.toList() );
    }

    @Override
    public void deleteCounter( String uid )
    {
        sessionFactory.getCurrentSession().createQuery( "DELETE SequentialNumberCounter WHERE owneruid = :uid" )
            .setParameter( "uid", uid ).executeUpdate();
    }
}
