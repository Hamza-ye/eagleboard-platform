package com.mass3d.dxf2.metadata.objectbundle.hooks;

import java.util.HashSet;
import java.util.Set;
import com.mass3d.common.IdentifiableObject;
import com.mass3d.dataelement.DataElement;
import com.mass3d.dataelement.DataElementOperand;
import com.mass3d.dataset.Section;
import com.mass3d.dxf2.metadata.objectbundle.ObjectBundle;
import com.mass3d.util.ObjectUtils;
import org.springframework.stereotype.Component;

@Component
public class SectionObjectBundleHook extends AbstractObjectBundleHook
{
    @Override
    public void preUpdate( IdentifiableObject object, IdentifiableObject persistedObject, ObjectBundle bundle  )
    {
        if ( !Section.class.isInstance( persistedObject ) )
        {
            return;
        }

        Section section = ( Section ) object;

        Set<DataElementOperand> returnGreyFields = new HashSet<>();

        for ( DataElementOperand greyField : section.getGreyedFields() )
        {
            boolean exist = false;

            for ( DataElement de : section.getDataElements() )
            {
                if ( !ObjectUtils.allNonNull( de.getUid(), greyField.getDataElement() ) )
                {
                    continue;
                }

                if ( de.getUid().equals( greyField.getDataElement().getUid() ) )
                {
                    exist = true;
                    break;
                }
            }

            if ( exist )
            {
                returnGreyFields.add( greyField );
            }
        }

        section.setGreyedFields( returnGreyFields );
    }
}
