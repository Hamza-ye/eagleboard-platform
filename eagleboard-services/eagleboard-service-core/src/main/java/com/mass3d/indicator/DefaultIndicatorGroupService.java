package com.mass3d.indicator;

import com.mass3d.common.hibernate.HibernateIdentifiableObjectStore;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service( "com.mass3d.indicator.IndicatorGroupService" )
public class DefaultIndicatorGroupService implements IndicatorGroupService
{
    private HibernateIdentifiableObjectStore<IndicatorGroup> indicatorGroupStore;

    public DefaultIndicatorGroupService(
        @Qualifier( "com.mass3d.indicator.IndicatorGroupStore" ) HibernateIdentifiableObjectStore<IndicatorGroup> indicatorGroupStore )
    {
        this.indicatorGroupStore = indicatorGroupStore;
    }

    @Override
    public IndicatorGroup getIndicatorGroupByUid( String uid )
    {
        return indicatorGroupStore.getByUid( uid );
    }
}
