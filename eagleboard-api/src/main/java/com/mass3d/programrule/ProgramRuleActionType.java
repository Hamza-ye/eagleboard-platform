package com.mass3d.programrule;

import static com.mass3d.programrule.ProgramRuleActionEvaluationTime.*;
import static com.mass3d.programrule.ProgramRuleActionEvaluationTime.ON_COMPLETE;
import static com.mass3d.programrule.ProgramRuleActionEvaluationTime.ON_DATA_ENTRY;
import static com.mass3d.programrule.ProgramRuleActionEvaluationTime.getAll;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import java.util.Set;

public enum ProgramRuleActionType
{
    DISPLAYTEXT( "displaytext" ),
    DISPLAYKEYVALUEPAIR( "displaykeyvaluepair" ),
    HIDEFIELD( "hidefield" ),
    HIDESECTION( "hidesection" ),
    HIDEPROGRAMSTAGE( "hideprogramstage"),
    ASSIGN( "assign", ON_DATA_ENTRY, ON_COMPLETE ),
    SHOWWARNING( "showwarning" ),
    WARNINGONCOMPLETE( "warningoncomplete" ),
    SHOWERROR( "showerror" ),
    ERRORONCOMPLETE( "erroroncomplete" ),
    CREATEEVENT( "createevent" ),
    SETMANDATORYFIELD( "setmandatoryfield", ON_DATA_ENTRY ),
    SENDMESSAGE( "sendmessage", ON_DATA_ENTRY, ON_COMPLETE ),
    SCHEDULEMESSAGE( "schedulemessage", ON_DATA_ENTRY, ON_COMPLETE ),
    HIDEOPTION( "hideoption" ),
    SHOWOPTIONGROUP( "showoptiongroup" ),
    HIDEOPTIONGROUP( "hideoptiongroup" );

    final String value;

    final Set<ProgramRuleActionEvaluationTime> whenToRun;

    private static final Set<ProgramRuleActionType> IMPLEMENTED_ACTIONS =
        new ImmutableSet.Builder<ProgramRuleActionType>().add( SENDMESSAGE, SCHEDULEMESSAGE, ASSIGN ).build(); // Actions having back end implementation

    private static final Set<ProgramRuleActionType> DATA_LINKED_TYPES = new ImmutableSet.Builder<ProgramRuleActionType>().add( HIDEFIELD, SETMANDATORYFIELD, HIDEOPTION,
        HIDEOPTIONGROUP, SHOWOPTIONGROUP ).build(); // Actions associated with DataElement Or TrackedEntityAttribute

    private static final Set<ProgramRuleActionType> NOTIFICATION_LINKED_TYPES =
        new ImmutableSet.Builder<ProgramRuleActionType>().add( SENDMESSAGE, SCHEDULEMESSAGE ).build(); // Actions associated with NotificationTemplate

    ProgramRuleActionType( String value )
    {
        this.value = value;
        this.whenToRun = getAll();
    }

    ProgramRuleActionType( String value, ProgramRuleActionEvaluationTime... whenToRun )
    {
        this.value = value;
        this.whenToRun = Sets.newHashSet( whenToRun );
    }

    public static ProgramRuleActionType fromValue( String value )
    {
        for ( ProgramRuleActionType type : ProgramRuleActionType.values() )
        {
            if ( type.value.equalsIgnoreCase( value ) )
            {
                return type;
            }
        }

        return null;
    }

    public boolean isImplementable()
    {
        return IMPLEMENTED_ACTIONS.contains( this );
    }

    public static Set<ProgramRuleActionType> getImplementedActions()
    {
        return IMPLEMENTED_ACTIONS;
    }

    public static Set<ProgramRuleActionType> getDataLinkedTypes()
    {
        return DATA_LINKED_TYPES;
    }

    public static Set<ProgramRuleActionType> getNotificationLinkedTypes()
    {
        return NOTIFICATION_LINKED_TYPES;
    }
}
