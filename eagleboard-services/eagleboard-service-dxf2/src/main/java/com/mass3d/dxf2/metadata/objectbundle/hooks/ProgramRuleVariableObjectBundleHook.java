package com.mass3d.dxf2.metadata.objectbundle.hooks;

import com.google.common.collect.ImmutableMap;
import java.util.function.Consumer;
import com.mass3d.common.IdentifiableObject;
import com.mass3d.dxf2.metadata.objectbundle.ObjectBundle;
import com.mass3d.programrule.ProgramRuleVariable;
import com.mass3d.programrule.ProgramRuleVariableSourceType;
import org.springframework.stereotype.Component;

@Component
public class ProgramRuleVariableObjectBundleHook extends AbstractObjectBundleHook
{
    private final ImmutableMap<ProgramRuleVariableSourceType, Consumer<ProgramRuleVariable>>
        SOURCE_TYPE_RESOLVER = new ImmutableMap.Builder<ProgramRuleVariableSourceType, Consumer<ProgramRuleVariable>>()
        .put( ProgramRuleVariableSourceType.CALCULATED_VALUE, this::processCalculatedValue )
        .put( ProgramRuleVariableSourceType.DATAELEMENT_CURRENT_EVENT, this::processDataElement )
        .put( ProgramRuleVariableSourceType.DATAELEMENT_NEWEST_EVENT_PROGRAM, this::processDataElement )
        .put( ProgramRuleVariableSourceType.DATAELEMENT_PREVIOUS_EVENT, this::processDataElement )
        .put( ProgramRuleVariableSourceType.DATAELEMENT_NEWEST_EVENT_PROGRAM_STAGE, this::processDataElementWithStage )
        .put( ProgramRuleVariableSourceType.TEI_ATTRIBUTE, this::processTEA )
        .build();

    @Override
    public <T extends IdentifiableObject> void preUpdate( T object, T persistedObject, ObjectBundle bundle )
    {
        if( !ProgramRuleVariable.class.isInstance( object ) ) return;

        ProgramRuleVariable variable = (ProgramRuleVariable) object;

        SOURCE_TYPE_RESOLVER.getOrDefault( variable.getSourceType(), v -> {} ).accept( variable );
    }

    private void processCalculatedValue( ProgramRuleVariable variable )
    {
        variable.setAttribute( null );
        variable.setDataElement( null );
        variable.setProgramStage( null );
    }

    private void processDataElement( ProgramRuleVariable variable )
    {
        variable.setAttribute( null );
        variable.setProgramStage( null );
    }

    private void processTEA( ProgramRuleVariable variable )
    {
        variable.setDataElement( null );
        variable.setProgramStage( null );
    }

    private void processDataElementWithStage( ProgramRuleVariable variable )
    {
        variable.setAttribute( null );
    }
}
