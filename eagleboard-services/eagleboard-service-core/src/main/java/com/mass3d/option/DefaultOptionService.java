package com.mass3d.option;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.ArrayList;
import java.util.List;
import com.mass3d.common.IdentifiableObjectStore;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service( "com.mass3d.option.OptionService" )
public class DefaultOptionService
    implements OptionService
{
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private IdentifiableObjectStore<OptionSet> optionSetStore;

    private OptionStore optionStore;
    
    private OptionGroupStore optionGroupStore;

    private OptionGroupSetStore optionGroupSetStore;

    public DefaultOptionService(@Qualifier("com.mass3d.option.OptionSetStore") IdentifiableObjectStore<OptionSet> optionSetStore, OptionStore optionStore,
                                OptionGroupStore optionGroupStore, OptionGroupSetStore optionGroupSetStore )
    {
        checkNotNull( optionSetStore );
        checkNotNull( optionStore );
        checkNotNull( optionGroupStore );
        checkNotNull( optionGroupSetStore );

        this.optionSetStore = optionSetStore;
        this.optionStore = optionStore;
        this.optionGroupStore = optionGroupStore;
        this.optionGroupSetStore = optionGroupSetStore;
    }

    // -------------------------------------------------------------------------
    // OptionService implementation
    // -------------------------------------------------------------------------

    // -------------------------------------------------------------------------
    // Option Set
    // -------------------------------------------------------------------------

    @Override
    @Transactional
    public long saveOptionSet( OptionSet optionSet )
    {
        optionSetStore.save( optionSet );

        return optionSet.getId();
    }

    @Override
    @Transactional
    public void updateOptionSet( OptionSet optionSet )
    {
        optionSetStore.update( optionSet );
    }

    @Override
    @Transactional(readOnly = true)
    public OptionSet getOptionSet( long id )
    {
        return optionSetStore.get( id );
    }

    @Override
    @Transactional(readOnly = true)
    public OptionSet getOptionSet( String uid )
    {
        return optionSetStore.getByUid( uid );
    }

    @Override
    @Transactional(readOnly = true)
    public OptionSet getOptionSetByName( String name )
    {
        return optionSetStore.getByName( name );
    }

    @Override
    @Transactional(readOnly = true)
    public OptionSet getOptionSetByCode( String code )
    {
        return optionSetStore.getByCode( code );
    }

    @Override
    @Transactional
    public void deleteOptionSet( OptionSet optionSet )
    {
        optionSetStore.delete( optionSet );
    }

    @Override
    @Transactional(readOnly = true)
    public List<OptionSet> getAllOptionSets()
    {
        return optionSetStore.getAll();
    }

    // -------------------------------------------------------------------------
    // Option
    // -------------------------------------------------------------------------

    @Override
    @Transactional(readOnly = true)
    public List<Option> getOptions( long optionSetId, String key, Integer max )
    {
        List<Option> options;

        if ( key != null || max != null )
        {
            // Use query as option set size might be very high

            options = optionStore.getOptions( optionSetId, key, max );
        }
        else
        {
            // Return all from object association to preserve custom order

            OptionSet optionSet = getOptionSet( optionSetId );

            options = new ArrayList<>( optionSet.getOptions() );
        }

        return options;
    }

    @Override
    @Transactional
    public void updateOption( Option option )
    {
        optionStore.update( option );
    }

    @Override
    @Transactional(readOnly = true)
    public Option getOption( long id )
    {
        return optionStore.get( id );
    }

    @Override
    @Transactional(readOnly = true)
    public Option getOptionByCode( String code )
    {
        return optionStore.getByCode( code );
    }

    @Override
    @Transactional
    public void deleteOption( Option option )
    {
        optionStore.delete( option );
    }

    // -------------------------------------------------------------------------
    // OptionGroup
    // -------------------------------------------------------------------------

    @Override
    @Transactional
    public long saveOptionGroup( OptionGroup group )
    {
        optionGroupStore.save( group );

        return group.getId();
    }

    @Override
    @Transactional
    public void updateOptionGroup( OptionGroup group )
    {
        optionGroupStore.update( group );
    }

    @Override
    @Transactional(readOnly = true)
    public OptionGroup getOptionGroup( long id )
    {
        return optionGroupStore.get( id );
    }

    @Override
    @Transactional(readOnly = true)
    public OptionGroup getOptionGroup( String uid )
    {
        return optionGroupStore.getByUid( uid );
    }

    @Override
    @Transactional
    public void deleteOptionGroup( OptionGroup group )
    {
        optionGroupStore.delete( group );
    }

    @Override
    @Transactional(readOnly = true)
    public List<OptionGroup> getAllOptionGroups()
    {
        return optionGroupStore.getAll();
    }

    // -------------------------------------------------------------------------
    // OptionGroupSet
    // -------------------------------------------------------------------------

    @Override
    @Transactional
    public long saveOptionGroupSet( OptionGroupSet group )
    {
        optionGroupSetStore.save( group );

        return group.getId();
    }

    @Override
    @Transactional
    public void updateOptionGroupSet( OptionGroupSet group )
    {
        optionGroupSetStore.update( group );
    }

    @Override
    @Transactional(readOnly = true)
    public OptionGroupSet getOptionGroupSet( long id )
    {
        return optionGroupSetStore.get( id );
    }

    @Override
    @Transactional(readOnly = true)
    public OptionGroupSet getOptionGroupSet( String uid )
    {
        return optionGroupSetStore.getByUid( uid );
    }

    @Override
    @Transactional
    public void deleteOptionGroupSet( OptionGroupSet group )
    {
        optionGroupSetStore.delete( group );
    }

    @Override
    @Transactional(readOnly = true)
    public List<OptionGroupSet> getAllOptionGroupSets()
    {
        return optionGroupSetStore.getAll();
    }
}
