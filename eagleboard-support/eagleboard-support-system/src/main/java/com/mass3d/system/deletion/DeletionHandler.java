package com.mass3d.system.deletion;

import com.mass3d.activity.Activity;
import com.mass3d.constant.Constant;
import com.mass3d.dataelement.DataElement;
import com.mass3d.dataelement.DataElementGroup;
import com.mass3d.dataelement.DataElementGroupSet;
import com.mass3d.dataset.DataSet;
import com.mass3d.datavalue.DataValue;
import com.mass3d.deduplication.PotentialDuplicate;
import com.mass3d.document.Document;
import com.mass3d.eventdatavalue.EventDataValue;
import com.mass3d.expression.Expression;
import com.mass3d.fileresource.FileResource;
import com.mass3d.i18n.locale.I18nLocale;
import com.mass3d.indicator.Indicator;
import com.mass3d.indicator.IndicatorGroup;
import com.mass3d.indicator.IndicatorGroupSet;
import com.mass3d.indicator.IndicatorType;
import com.mass3d.interpretation.Interpretation;
import com.mass3d.interpretation.InterpretationComment;
import com.mass3d.message.MessageConversation;
import com.mass3d.metadata.version.MetadataVersion;
import com.mass3d.option.Option;
import com.mass3d.option.OptionGroup;
import com.mass3d.option.OptionGroupSet;
import com.mass3d.option.OptionSet;
import com.mass3d.period.Period;
import com.mass3d.period.RelativePeriods;
import com.mass3d.project.Project;
import com.mass3d.scheduling.JobConfiguration;
import com.mass3d.todotask.TodoTask;
import com.mass3d.user.User;
import com.mass3d.user.UserAuthorityGroup;
import com.mass3d.user.UserCredentials;
import com.mass3d.user.UserGroup;
import com.mass3d.user.UserSetting;
import com.mass3d.version.Version;

/**
 * A DeletionHandler should override methods for objects that, when deleted, will affect the current
 * object in any way. Eg. a DeletionHandler for DataElementGroup should override the
 * deleteDataElement(..) method which should remove the DataElement from all DataElementGroups.
 * Also, it should override the allowDeleteDataElement() method and return a non-null String value
 * if there exists objects that are dependent on the DataElement and are considered not be deleted.
 * The return value could be a hint for which object is denying the delete, like the name.
 */
public abstract class DeletionHandler {

  protected static final String ERROR = "";

  // -------------------------------------------------------------------------
  // Abstract methods
  // -------------------------------------------------------------------------

  protected abstract String getClassName();

  // -------------------------------------------------------------------------
  // Public methods
  // -------------------------------------------------------------------------

//    public void deleteAttribute( Attribute attribute )
//    {
//    }
//
//    public String allowDeleteAttribute( Attribute attribute )
//    {
//        return null;
//    }
//
//    public void deleteAttributeValue( AttributeValue attributeValue )
//    {
//    }
//
//    public String allowDeleteAttributeValue( AttributeValue attributeValue )
//    {
//        return null;
//    }
//
//    public void deleteDataApproval( DataApproval dataApproval )
//    {
//    }
//
//    public String allowDeleteDataApproval( DataApproval dataApproval )
//    {
//        return null;
//    }
//
//    public void deleteDataApprovalLevel( DataApprovalLevel dataApprovalLevel )
//    {
//    }
//
//    public String allowDeleteDataApprovalLevel( DataApprovalLevel dataApprovalLevel )
//    {
//        return null;
//    }
//
//    public void deleteDataApprovalWorkflow( DataApprovalWorkflow workflow )
//    {
//    }
//
//    public String allowDeleteDataApprovalWorkflow( DataApprovalWorkflow workflow )
//    {
//        return null;
//    }
//
//    public void deleteDataApprovalAudit( DataApprovalAudit audit )
//    {
//    }
//
//    public String allowDeleteDataApprovalAudit( DataApprovalAudit audit )
//    {
//        return null;
//    }

  public void deleteDataElement(DataElement dataElement) {
  }

  public String allowDeleteDataElement(DataElement dataElement) {
    return null;
  }

  public void deleteDataElementGroup(DataElementGroup dataElementGroup) {
  }

  public String allowDeleteDataElementGroup(DataElementGroup dataElementGroup) {
    return null;
  }

  public void deleteDataElementGroupSet(DataElementGroupSet dataElementGroupSet) {
  }

  public String allowDeleteDataElementGroupSet(DataElementGroupSet dataElementGroupSet) {
    return null;
  }

//    public void deleteCategory( Category category )
//    {
//    }
//
//    public String allowDeleteCategory( Category category )
//    {
//        return null;
//    }
//
//    public void deleteCategoryOption( CategoryOption categoryOption )
//    {
//    }
//
//    public String allowDeleteCategoryOption( CategoryOption categoryOption )
//    {
//        return null;
//    }
//
//    public void deleteCategoryCombo( CategoryCombo categoryCombo )
//    {
//    }
//
//    public String allowDeleteCategoryCombo( CategoryCombo categoryCombo )
//    {
//        return null;
//    }
//
//    public void deleteCategoryOptionCombo( CategoryOptionCombo categoryOptionCombo )
//    {
//    }
//
//    public String allowDeleteCategoryOptionCombo( CategoryOptionCombo categoryOptionCombo )
//    {
//        return null;
//    }
//
//    public void deleteProgramMessage( ProgramMessage programMessage )
//    {
//    }
//
//    public String allowDeleteProgramMessage( ProgramMessage programMessage )
//    {
//        return null;
//    }

  public void deleteDataSet(DataSet dataSet) {
  }

  public String allowDeleteDataSet(DataSet dataSet) {
    return null;
  }

  public void deleteTodoTask(TodoTask todoTask) {
  }

  public void deleteActivity(Activity activity) {
  }

  public void deleteProject(Project project) {
  }

  public String allowDeleteTodoTask(TodoTask todoTask) {
    return null;
  }

  public String allowDeleteActivity(Activity activity) {
    return null;
  }

  public String allowDeleteProject(Project project) {
    return null;
  }
//    public void deleteSection( Section section )
//    {
//    }
//
//    public String allowDeleteSection( Section section )
//    {
//        return null;
//    }
//
//    public void deleteCompleteDataSetRegistration( CompleteDataSetRegistration registration )
//    {
//    }
//
//    public String allowDeleteCompleteDataSetRegistration( CompleteDataSetRegistration registration )
//    {
//        return null;
//    }

  public void deleteDataValue(DataValue dataValue) {
  }

  public String allowDeleteDataValue(DataValue dataValue) {
    return null;
  }

  public void deleteExpression(Expression expression) {
  }

  public String allowDeleteExpression(Expression expression) {
    return null;
  }

//    public void deleteMinMaxDataElement( MinMaxDataElement minMaxDataElement )
//    {
//    }
//
//    public String allowDeleteMinMaxDataElement( MinMaxDataElement minMaxDataElement )
//    {
//        return null;
//    }

  public void deleteIndicator(Indicator indicator) {
  }

  public String allowDeleteIndicator(Indicator indicator) {
    return null;
  }

  public void deleteIndicatorGroup(IndicatorGroup indicatorGroup) {
  }

  public String allowDeleteIndicatorGroup(IndicatorGroup indicatorGroup) {
    return null;
  }

  public void deleteIndicatorType(IndicatorType indicatorType) {
  }

  public String allowDeleteIndicatorType(IndicatorType indicatorType) {
    return null;
  }

  public void deleteIndicatorGroupSet(IndicatorGroupSet indicatorGroupSet) {
  }

  public String allowDeleteIndicatorGroupSet(IndicatorGroupSet indicatorGroupSet) {
    return null;
  }

  public void deletePeriod(Period period) {
  }

  public String allowDeletePeriod(Period period) {
    return null;
  }

  public void deleteRelativePeriods(RelativePeriods relativePeriods) {
  }

  public String allowDeleteRelativePeriods(RelativePeriods relativePeriods) {
    return null;
  }

//    public void deletePredictor( Predictor predictor )
//    {
//    }
//
//    public String allowDeletePredictor( Predictor predictor )
//    {
//        return null;
//    }
//
//    public void deletePredictorGroup( PredictorGroup predictorGroup )
//    {
//    }
//
//    public String allowDeletePredictorGroup( PredictorGroup predictorGroup )
//    {
//        return null;
//    }
//
//    public void deleteValidationRule( ValidationRule validationRule )
//    {
//    }

//    public String allowDeleteValidationRule( ValidationRule validationRule )
//    {
//        return null;
//    }
//
//    public void deleteValidationRuleGroup( ValidationRuleGroup validationRuleGroup )
//    {
//    }
//
//    public String allowDeleteValidationRuleGroup( ValidationRuleGroup validationRuleGroup )
//    {
//        return null;
//    }
//
//    public void deleteValidationResult( ValidationResult validationResult )
//    {
//    }
//
//    public String allowDeleteValidationResult( ValidationResult validationResult )
//    {
//        return null;
//    }
//
//    public void deleteDataEntryForm( DataEntryForm form )
//    {
//    }
//
//    public String allowDeleteValidationNotificationTemplate( ValidationNotificationTemplate vrnt )
//    {
//        return null;
//    }
//
//    public void deleteValidationNotificationTemplate( ValidationNotificationTemplate vrnt )
//    {
//    }
//
//    public String allowDeleteDataEntryForm( DataEntryForm form )
//    {
//        return null;
//    }
//
//    public void deleteOrganisationUnit( OrganisationUnit unit )
//    {
//    }
//
//    public String allowDeleteOrganisationUnit( OrganisationUnit unit )
//    {
//        return null;
//    }
//
//    public void deleteOrganisationUnitGroup( OrganisationUnitGroup group )
//    {
//    }
//
//    public String allowDeleteOrganisationUnitGroup( OrganisationUnitGroup group )
//    {
//        return null;
//    }
//
//    public void deleteOrganisationUnitGroupSet( OrganisationUnitGroupSet groupSet )
//    {
//    }
//
//    public String allowDeleteOrganisationUnitGroupSet( OrganisationUnitGroupSet groupSet )
//    {
//        return null;
//    }
//
//    public void deleteOrganisationUnitLevel( OrganisationUnitLevel level )
//    {
//    }
//
//    public String allowDeleteOrganisationUnitLevel( OrganisationUnitLevel level )
//    {
//        return null;
//    }
//
//    public void deleteReport( Report report )
//    {
//    }
//
//    public String allowDeleteReport( Report report )
//    {
//        return null;
//    }

  public void deleteUser(User user) {
  }

  public String allowDeleteUser(User user) {
    return null;
  }

  public void deleteUserCredentials(UserCredentials credentials) {
  }

  public String allowDeleteUserCredentials(UserCredentials credentials) {
    return null;
  }

  public void deleteUserAuthorityGroup(UserAuthorityGroup authorityGroup) {
  }

  public String allowDeleteUserAuthorityGroup(UserAuthorityGroup authorityGroup) {
    return null;
  }

  public String allowDeleteUserGroup(UserGroup userGroup) {
    return null;
  }

  public void deleteUserGroup(UserGroup userGroup) {
  }

  public void deleteUserSetting(UserSetting userSetting) {
  }

  public String allowDeleteUserSetting(UserSetting userSetting) {
    return null;
  }

  public void deleteDocument(Document document) {
  }

  public String allowDeleteDocument(Document document) {
    return null;
  }

//    public void deleteLegend( Legend mapLegend )
//    {
//    }
//
//    public String allowDeleteLegend( Legend mapLegend )
//    {
//        return null;
//    }
//
//    public void deleteLegendSet( LegendSet legendSet )
//    {
//    }
//
//    public String allowDeleteLegendSet( LegendSet legendSet )
//    {
//        return null;
//    }
//
//    public void deleteMap( Map map )
//    {
//    }
//
//    public String allowDeleteMap( Map map )
//    {
//        return null;
//    }
//
//    public void deleteExternalMapLayer( ExternalMapLayer externalMapLayer )
//    {
//    }
//
//    public String allowDeleteExternalMapLayer( ExternalMapLayer externalMapLayer )
//    {
//        return null;
//    }
//
//    public void deleteMapView( MapView mapView )
//    {
//    }
//
//    public String allowDeleteMapView( MapView mapView )
//    {
//        return null;
//    }

  public void deleteInterpretation(Interpretation interpretation) {
  }

  public String allowDeleteIntepretation(Interpretation interpretation) {
    return null;
  }

//    public void deleteTrackedEntityInstance( TrackedEntityInstance entityInstance )
//    {
//    }
//
//    public String allowDeleteTrackedEntityInstance( TrackedEntityInstance entityInstance )
//    {
//        return null;
//    }
//
//    public void deleteTrackedEntityComment( TrackedEntityComment entityComment )
//    {
//    }

//    public String allowDeleteTrackedEntityComment( TrackedEntityComment entityComment )
//    {
//        return null;
//    }
//
//    public void deleteTrackedEntityAttribute( TrackedEntityAttribute attribute )
//    {
//    }
//
//    public String allowDeleteTrackedEntityAttribute( TrackedEntityAttribute attribute )
//    {
//        return null;
//    }
//
//    public void deleteTrackedEntityAttributeValue( TrackedEntityAttributeValue attributeValue )
//    {
//    }
//
//    public String allowDeleteTrackedEntityAttributeValue( TrackedEntityAttributeValue attributeValue )
//    {
//        return null;
//    }
//
//    public void deleteRelationship( Relationship relationship )
//    {
//    }
//
//    public String allowDeleteRelationship( Relationship relationship )
//    {
//        return null;
//    }
//
//    public void deleteRelationshipType( RelationshipType relationshipType )
//    {
//    }
//
//    public String allowDeleteRelationshipType( RelationshipType relationshipType )
//    {
//        return null;
//    }
//
//    public void deleteProgram( Program program )
//    {
//    }
//
//    public String allowDeleteProgram( Program program )
//    {
//        return null;
//    }
//
//    public void deleteProgramInstance( ProgramInstance programInstance )
//    {
//    }
//
//    public String allowDeleteProgramInstance( ProgramInstance programInstance )
//    {
//        return null;
//    }
//
//    public void deleteProgramStage( ProgramStage programStage )
//    {
//    }
//
//    public String allowDeleteProgramStage( ProgramStage programStage )
//    {
//        return null;
//    }
//
//    public void deleteProgramStageSection( ProgramStageSection programStageSection )
//    {
//    }
//
//    public String allowDeleteProgramStageSection( ProgramStageSection programStageSection )
//    {
//        return null;
//    }
//
//    public void deleteProgramStageInstance( ProgramStageInstance programStageInstance )
//    {
//    }
//
//    public String allowDeleteProgramStageInstance( ProgramStageInstance programStageInstance )
//    {
//        return null;
//    }
//
//    public void deleteProgramNotificationTemplate( ProgramNotificationTemplate programNotificationTemplate )
//    {
//    }
//
//    public void allowDeleteProgramNotificationTemplate( ProgramNotificationTemplate programNotificationTemplate )
//    {
//    }
//
//    public void deleteProgramRule( ProgramRule programRule )
//    {
//    }
//
//    public String allowDeleteProgramRule( ProgramRule programRule )
//    {
//        return null;
//    }
//
//    public void deleteProgramRuleVariable( ProgramRuleVariable programRuleVariable )
//    {
//    }
//
//    public String allowDeleteProgramRuleVariable( ProgramRuleVariable programRuleVariable )
//    {
//        return null;
//    }
//
//    public void deleteProgramRuleAction( ProgramRuleAction programRuleAction )
//    {
//    }
//
//    public String allowDeleteProgramRuleAction( ProgramRuleAction programRuleAction )
//    {
//        return null;
//    }
//
//    public void deleteProgramStageDataElement( ProgramStageDataElement programStageDataElement )
//    {
//    }
//
//    public String allowDeleteProgramStageDataElement( ProgramStageDataElement programStageDataElement )
//    {
//        return null;
//    }

  public void deleteEventDataValue(EventDataValue eventDataValue) {
  }

  public String allowDeleteEventDataValue(EventDataValue eventDataValue) {
    return null;
  }

//    public void deleteProgramIndicator( ProgramIndicator programIndicator )
//    {
//    }
//
//    public String allowDeleteProgramIndicator( ProgramIndicator programIndicator )
//    {
//        return null;
//    }
//
//    public void deleteProgramIndicatorGroup( ProgramIndicatorGroup programIndicatorGroup )
//    {
//    }
//
//    public String allowDeleteProgramIndicatorGroup( ProgramIndicatorGroup programIndicatorGroup )
//    {
//        return null;
//    }
//
//    public void deleteProgramExpression( ProgramExpression programExpression )
//    {
//    }
//
//    public String allowDeleteProgramExpression( ProgramExpression programExpression )
//    {
//        return null;
//    }

  public void deleteConstant(Constant constant) {
  }

  public String allowDeleteConstant(Constant constant) {
    return null;
  }

  public void deleteOptionSet(OptionSet optionSet) {
  }

  public String allowDeleteOptionSet(OptionSet optionSet) {
    return null;
  }

  public void deleteOptionGroupSet(OptionGroupSet optionGroupSet) {
  }

  public String allowDeleteOptionGroupSet(OptionGroupSet optionGroupSet) {
    return null;
  }

  public void deleteOptionGroup(OptionGroup optionGroup) {
  }

  public String allowDeleteOptionGroup(OptionGroup optionGroup) {
    return null;
  }

  public void deleteOption(Option optionSet) {
  }

  public String allowDeleteOption(Option option) {
    return null;
  }

//    public void deleteLockException( LockException lockException )
//    {
//    }
//
//    public String allowDeleteLockException( LockException lockException )
//    {
//        return null;
//    }

  public void deleteIntepretation(Interpretation interpretation) {
  }

  public String allowDeleteInterpretation(Interpretation interpretation) {
    return null;
  }

  public void deleteI18nLocale(I18nLocale i18nLocale) {
  }

  public String allowDeleteI18nLocale(I18nLocale i18nLocale) {
    return null;
  }

//    public void deleteSqlView( SqlView sqlView )
//    {
//    }
//
//    public String allowDeleteSqlView( SqlView sqlView )
//    {
//        return null;
//    }
//
//    public void deleteDashboard( Dashboard dashboard )
//    {
//    }
//
//    public String allowDeleteDashboard( Dashboard dashboard )
//    {
//        return null;
//    }
//
//    public void deleteDashboardItem( DashboardItem dashboardItem )
//    {
//    }
//
//    public String allowDeleteDashboardItem( DashboardItem dashboardItem )
//    {
//        return null;
//    }
//
//    public void deleteCategoryOptionGroup( CategoryOptionGroup categoryOptionGroup )
//    {
//    }
//
//    public String allowDeleteCategoryOptionGroup( CategoryOptionGroup categoryOptionGroup )
//    {
//        return null;
//    }
//
//    public void deleteCategoryOptionGroupSet( CategoryOptionGroupSet categoryOptionGroupSet )
//    {
//    }
//
//    public String allowDeleteCategoryOptionGroupSet( CategoryOptionGroupSet categoryOptionGroupSet )
//    {
//        return null;
//    }
//
//    public void deleteTrackedEntityType( TrackedEntityType trackedEntityType )
//    {
//    }
//
//    public String allowDeleteTrackedEntityType( TrackedEntityType trackedEntityType )
//    {
//        return null;
//    }
//
//    public void deleteEventReport( EventReport eventReport )
//    {
//    }
//
//    public String allowDeleteEventReport( EventReport eventReport )
//    {
//        return null;
//    }
//
//    public void deleteEventChart( EventChart eventChart )
//    {
//    }
//
//    public String allowDeleteEventChart( EventChart eventChart )
//    {
//        return null;
//    }
//
//    public void deleteOAuth2Client( OAuth2Client oAuth2Client )
//    {
//    }
//
//    public String allowDeleteOAuth2Client( OAuth2Client oAuth2Client )
//    {
//        return null;
//    }
//
//    public void deleteProgramTrackedEntityAttribute( ProgramTrackedEntityAttribute attribute )
//    {
//    }
//
//    public String allowDeleteProgramTrackedEntityAttribute( ProgramTrackedEntityAttribute attribute )
//    {
//        return null;
//    }
//
//    public void deleteProgramTrackedEntityAttributeGroup( ProgramTrackedEntityAttributeGroup color )
//    {
//    }
//
//    public String allowDeleteProgramTrackedEntityAttributeGroup( ProgramTrackedEntityAttributeGroup group )
//    {
//        return null;
//    }
//
//    public void deletePushAnalysis( PushAnalysis pushAnalysis )
//    {
//    }
//
//    public String allowDeletePushAnalysis( PushAnalysis pushAnalysis )
//    {
//        return null;
//    }
//
//    public void deleteDataSetNotificationTemplate( DataSetNotificationTemplate dataSetNotificationTemplate )
//    {
//
//    }
//
//    public String allowDeleteDataSetNotificationTemplate( DataSetNotificationTemplate dataSetNotificationTemplate )
//    {
//        return null;
//    }
//
//    public void deleteSMSCommand( SMSCommand smsCommand )
//    {
//
//    }
//
//    public String allowDeleteSMSCommand( SMSCommand smsCommand )
//    {
//        return null;
//    }

  public void deleteMessageConversation(MessageConversation messageConversation) {
  }

  public String allowDeleteMessageConversation(MessageConversation messageConversation) {
    return null;
  }

  public void deleteJobConfiguration(JobConfiguration jobConfiguration) {
  }

  public String allowDeleteJobConfiguration(JobConfiguration jobConfiguration) {
    return null;
  }

  public String allowDeleteFileResource(FileResource fileResource) {
    return null;
  }

  public void deleteFileResource(FileResource fileResource) {
  }

//    public String allowDeleteTrackedEntityInstanceFilter( TrackedEntityInstanceFilter filter )
//    {
//        return null;
//    }
//
//    public void deleteTrackedEntityInstanceFilter( TrackedEntityInstanceFilter filter )
//    {
//    }

  public String allowDeleteVersion(Version version) {
    return null;
  }

  public void deleteVersion(Version version) {
  }

  public String allowDeleteMetadataVersion(MetadataVersion metadataVersion) {
    return null;
  }

  public void deleteMetadataVersion(MetadataVersion metadataVersion) {
  }

//    public String allowDeleteReservedValue( ReservedValue reservedValue )
//    {
//        return null;
//    }
//
//    public void deleteReservedValue( ReservedValue reservedValue )
//    {
//    }

  public String allowDeletePotentialDuplicate(PotentialDuplicate potentialDuplicate) {
    return null;
  }

  public void deletePotentialDuplicate(PotentialDuplicate potentialDuplicate) {
  }

//    public String allowDeleteVisualization( Visualization visualization )
//    {
//        return null;
//    }
//
//    public void deleteVisualization( Visualization visualization )
//    {
//    }

  public String allowDeleteInterpretationComment(InterpretationComment comment) {
    return null;
  }

  public void deleteInterpretationComment(InterpretationComment comment) {
  }

//    public String allowDeleteIncomingSms( IncomingSms sms )
//    {
//        return null;
//    }
//
//    public void deleteIncomingSms( IncomingSms sms )
//    {
//
//    }
//
//    public String allowDeleteOutboundSms( OutboundSms sms )
//    {
//        return null;
//    }
//
//    public void deleteOutboundSms( OutboundSms sms )
//    {
//
//    }

//    public String allowDeleteReportTable( ReportTable reportTable )
//    {
//        return null;
//    }
//
//    public void deleteReportTable( ReportTable reportTable )
//    {
//
//    }
//
//    public String allowDeleteChart( Chart chart )
//    {
//        return null;
//    }
//
//    public void deleteChart( Chart chart )
//    {
//
//    }
}
