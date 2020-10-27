package com.mass3d.dxf2.metadata.objectbundle.hooks;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.ObjectUtils;
import com.mass3d.common.IdentifiableObject;
import com.mass3d.dxf2.metadata.objectbundle.ObjectBundle;
import com.mass3d.feedback.ErrorCode;
import com.mass3d.feedback.ErrorReport;
import com.mass3d.option.Option;
import com.mass3d.option.OptionSet;
import org.springframework.stereotype.Component;

@Component
public class OptionObjectBundleHook
    extends AbstractObjectBundleHook
{
    @Override
    public <T extends IdentifiableObject> List<ErrorReport> validate( T object, ObjectBundle bundle )
    {
        List<ErrorReport> errors = new ArrayList<>();

        if ( !(object instanceof Option) ) return new ArrayList<>();

        final Option option = (Option) object;

        if ( option.getOptionSet() != null )
        {
            OptionSet optionSet = bundle.getPreheat().get( bundle.getPreheatIdentifier(), OptionSet.class, option.getOptionSet() );

            errors.addAll( checkDuplicateOption( optionSet, option ) );
        }

        return errors;
    }

    @Override
    public <T extends IdentifiableObject> void preCreate( T object, ObjectBundle bundle )
    {
        if ( !(object instanceof Option) )
        {
            return;
        }

        final Option option = (Option) object;

        // if the bundle contains also the option set there is no need to add the option here
        // (will be done automatically later and option set may contain raw value already)
        if ( option.getOptionSet() != null && !bundle.containsObject( option.getOptionSet() ) )
        {
            OptionSet optionSet = bundle.getPreheat().get( bundle.getPreheatIdentifier(), OptionSet.class, option.getOptionSet() );

            if ( optionSet != null )
            {
                optionSet.addOption( option );
            }
        }
    }

    /**
     * Check for duplication of Option's name OR code within given OptionSet
     *
     * @param listOptions
     * @param checkOption
     * @return
     */
    private List<ErrorReport> checkDuplicateOption( OptionSet optionSet, Option checkOption )
    {
        List<ErrorReport> errors = new ArrayList<>();

        if ( optionSet == null || optionSet.getOptions().isEmpty() || checkOption == null )
        {
            return errors;
        }

        for ( Option option : optionSet.getOptions() )
        {
            if ( option == null || option.getName() == null || option.getCode() == null )
            {
                continue;
            }

            if ( ObjectUtils.allNotNull( option.getUid(), checkOption.getUid() ) && option.getUid().equals( checkOption.getUid() ) )
            {
                continue;
            }

            if ( option.getName().equals( checkOption.getName() ) || option.getCode().equals( checkOption.getCode() ) )
            {
                errors.add( new ErrorReport( OptionSet.class, ErrorCode.E4028, optionSet.getUid(), option.getUid() ) );
            }
        }

        return errors;
    }
}