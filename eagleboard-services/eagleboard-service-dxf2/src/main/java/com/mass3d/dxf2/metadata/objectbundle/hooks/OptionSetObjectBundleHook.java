package com.mass3d.dxf2.metadata.objectbundle.hooks;

import com.mass3d.common.IdentifiableObject;
import com.mass3d.dxf2.metadata.objectbundle.ObjectBundle;
import com.mass3d.option.OptionSet;
import org.springframework.stereotype.Component;

@Component
public class OptionSetObjectBundleHook
    extends AbstractObjectBundleHook
{
    @Override
    public <T extends IdentifiableObject> void postCreate( T persistedObject, ObjectBundle bundle )
    {
        if ( !OptionSet.class.isInstance( persistedObject ) )
        {
            return;
        }

        updateOption( (OptionSet) persistedObject );
    }

    @Override
    public <T extends IdentifiableObject> void postUpdate( T persistedObject, ObjectBundle bundle )
    {
        if ( !OptionSet.class.isInstance( persistedObject ) )
        {
            return;
        }

        updateOption( (OptionSet) persistedObject );
    }

    private void updateOption( OptionSet optionSet )
    {
        if ( optionSet.getOptions() != null && !optionSet.getOptions().isEmpty() )
        {
            return;
        }

        optionSet.getOptions().forEach( option -> {

            if ( option.getOptionSet() == null )
            {
                option.setOptionSet( optionSet );
            }
        } );

        sessionFactory.getCurrentSession().refresh( optionSet );
    }
}
