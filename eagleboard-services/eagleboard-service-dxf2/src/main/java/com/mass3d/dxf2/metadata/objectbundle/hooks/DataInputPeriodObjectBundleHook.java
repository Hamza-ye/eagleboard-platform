package com.mass3d.dxf2.metadata.objectbundle.hooks;

import com.mass3d.common.IdentifiableObject;
import com.mass3d.dataset.DataInputPeriod;
import com.mass3d.dxf2.metadata.objectbundle.ObjectBundle;
import com.mass3d.period.Period;
import com.mass3d.period.PeriodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DataInputPeriodObjectBundleHook
    extends AbstractObjectBundleHook
{
    @Autowired
    private PeriodService periodService;

    @Override
    public void preCreate( IdentifiableObject object, ObjectBundle bundle )
    {
        if ( !DataInputPeriod.class.isInstance( object ) )
        {
            return;
        }

        setPeriod( object );
    }

    @Override
    public void preUpdate( IdentifiableObject object, IdentifiableObject persistedObject, ObjectBundle bundle )
    {
        if ( !DataInputPeriod.class.isInstance( object ) )
        {
            return;
        }

        setPeriod( object );
    }

    private void setPeriod( IdentifiableObject object )
    {
        DataInputPeriod dataInputPeriod = (DataInputPeriod) object;

        Period period = periodService.getPeriod( dataInputPeriod.getPeriod().getIsoDate() );

        dataInputPeriod.setPeriod( period );
        sessionFactory.getCurrentSession().save( period );
    }
}
