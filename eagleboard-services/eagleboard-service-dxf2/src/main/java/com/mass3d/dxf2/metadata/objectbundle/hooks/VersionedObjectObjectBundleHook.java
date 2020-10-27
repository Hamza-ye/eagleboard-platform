package com.mass3d.dxf2.metadata.objectbundle.hooks;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import com.mass3d.common.IdentifiableObject;
import com.mass3d.common.VersionedObject;
import com.mass3d.dxf2.metadata.objectbundle.ObjectBundle;
import com.mass3d.option.Option;
import com.mass3d.option.OptionSet;
import org.springframework.stereotype.Component;

/**
 * Handles increase of version for objects such as data set and option set.
 * 
 */
@Component
public class VersionedObjectObjectBundleHook extends AbstractObjectBundleHook
{
    @Override
    public <T extends IdentifiableObject> void preUpdate( T object, T persistedObject, ObjectBundle bundle )
    {
        if ( VersionedObject.class.isInstance( object ) )
        {
            VersionedObject versionObj = (VersionedObject) object;
            int persistedVersion = ( ( VersionedObject ) persistedObject ).getVersion();

            versionObj.setVersion( persistedVersion > versionObj.getVersion() ? persistedVersion :
                persistedVersion < versionObj.getVersion() ? versionObj.getVersion() : versionObj.increaseVersion() );
        }
    }

    @Override
    public <T extends IdentifiableObject> void postCreate( T persistedObject, ObjectBundle bundle )
    {
        VersionedObject versionedObject = null;
        
//        if ( Section.class.isInstance( persistedObject ) )
//        {
//            versionedObject = ((Section) persistedObject).getDataSet();
//        }
//        else
          if ( Option.class.isInstance( persistedObject ) )
        {
            versionedObject = ((Option) persistedObject).getOptionSet();
        }

        if ( versionedObject != null )
        {
            versionedObject.increaseVersion();
            sessionFactory.getCurrentSession().save( versionedObject );
        }
    }

    @Override
    public <T extends IdentifiableObject> void postTypeImport( Class<? extends IdentifiableObject> klass, List<T> objects, ObjectBundle bundle )
    {
//        if ( Section.class.isAssignableFrom( klass ) )
//        {
//            Set<DataSet> dataSets = new HashSet<>();
//            objects.forEach( o ->
//            {
//                DataSet dataSet = ((Section) o).getDataSet();
//
//                if ( dataSet != null && dataSet.getId() > 0 )
//                {
//                    dataSets.add( dataSet );
//                }
//            } );
//
//            dataSets.forEach( ds ->
//            {
//                ds.increaseVersion();
//                sessionFactory.getCurrentSession().save( ds );
//            } );
//        }
//        else
          if ( Option.class.isAssignableFrom( klass ) )
        {
            Set<OptionSet> optionSets = new HashSet<>();

            objects.forEach( o ->
            {
                Option option = (Option) o;

                if ( option.getOptionSet() != null && option.getId() > 0 )
                {
                    optionSets.add( option.getOptionSet() );
                }
            } );

            optionSets.forEach( os ->
            {
                os.increaseVersion();
                sessionFactory.getCurrentSession().save( os );
            } );
        }
    }
}
