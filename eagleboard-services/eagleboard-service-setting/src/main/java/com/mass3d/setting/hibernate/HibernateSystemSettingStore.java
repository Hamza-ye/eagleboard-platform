package com.mass3d.setting.hibernate;

import javax.persistence.criteria.CriteriaBuilder;
import org.hibernate.SessionFactory;
import com.mass3d.hibernate.HibernateGenericStore;
import com.mass3d.setting.SystemSetting;
import com.mass3d.setting.SystemSettingStore;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository( "com.mass3d.setting.SystemSettingStore" )
public class HibernateSystemSettingStore
    extends HibernateGenericStore<SystemSetting> implements SystemSettingStore
{
    public HibernateSystemSettingStore( SessionFactory sessionFactory, JdbcTemplate jdbcTemplate, ApplicationEventPublisher publisher )
    {
        super( sessionFactory, jdbcTemplate, publisher, SystemSetting.class, true );
    }

    @Override
    @Transactional
    public SystemSetting getByNameTx( String name )
    {
        return getByName( name );
    }

    @Override
    public SystemSetting getByName( String name )
    {
        CriteriaBuilder builder = getCriteriaBuilder();

        return getSingleResult( builder, newJpaParameters()
            .addPredicate( root -> builder.equal( root.get( "name" ), name ) ));
    }
}
