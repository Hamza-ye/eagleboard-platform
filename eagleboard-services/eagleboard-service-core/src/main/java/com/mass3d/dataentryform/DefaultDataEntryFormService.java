package com.mass3d.dataentryform;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.apache.commons.text.StringEscapeUtils.escapeHtml3;

import com.google.common.collect.Maps;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import com.mass3d.category.CategoryOptionCombo;
import com.mass3d.common.IdScheme;
import com.mass3d.common.IdentifiableObjectManager;
import com.mass3d.common.IdentifiableObjectUtils;
import com.mass3d.common.ValueType;
import com.mass3d.commons.collection.CachingMap;
import com.mass3d.dataelement.DataElement;
import com.mass3d.dataelement.DataElementOperand;
import com.mass3d.dataelement.DataElementService;
import com.mass3d.dataset.DataSet;
import com.mass3d.i18n.I18n;
import com.mass3d.indicator.Indicator;
import com.mass3d.indicator.IndicatorService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service( "com.mass3d.dataentryform.DataEntryFormService" )
public class DefaultDataEntryFormService
    implements DataEntryFormService
{
    private static final String EMPTY_VALUE_TAG = "value=\"\"";
    private static final String EMPTY_TITLE_TAG = "title=\"\"";
    private static final String TAG_CLOSE = "/>";
    private static final String EMPTY = "";

    // ------------------------------------------------------------------------
    // Dependencies
    // ------------------------------------------------------------------------

    private final DataEntryFormStore dataEntryFormStore;

    private final IdentifiableObjectManager idObjectManager;

    private final DataElementService dataElementService;

    private final IndicatorService indicatorService;

    public DefaultDataEntryFormService( DataEntryFormStore dataEntryFormStore,
        IdentifiableObjectManager idObjectManager, DataElementService dataElementService,
        IndicatorService indicatorService )
    {
        checkNotNull( dataEntryFormStore );
        checkNotNull( idObjectManager );
        checkNotNull( dataElementService );
        checkNotNull( indicatorService );

        this.dataEntryFormStore = dataEntryFormStore;
        this.idObjectManager = idObjectManager;
        this.dataElementService = dataElementService;
        this.indicatorService = indicatorService;
    }

    // ------------------------------------------------------------------------
    // Implemented Methods
    // ------------------------------------------------------------------------

    @Override
    @Transactional
    public long addDataEntryForm( DataEntryForm dataEntryForm )
    {
        dataEntryForm.setFormat( DataEntryForm.CURRENT_FORMAT );
        dataEntryFormStore.save( dataEntryForm );
        return dataEntryForm.getId();
    }

    @Override
    @Transactional
    public void updateDataEntryForm( DataEntryForm dataEntryForm )
    {
        dataEntryFormStore.update( dataEntryForm );
    }

    @Override
    @Transactional
    public void deleteDataEntryForm( DataEntryForm dataEntryForm )
    {
        dataEntryFormStore.delete( dataEntryForm );
    }

    @Override
    @Transactional(readOnly = true)
    public DataEntryForm getDataEntryForm( long id )
    {
        return dataEntryFormStore.get( id );
    }

    @Override
    @Transactional(readOnly = true)
    public DataEntryForm getDataEntryFormByName( String name )
    {
        return dataEntryFormStore.getDataEntryFormByName( name );
    }

    @Override
    @Transactional(readOnly = true)
    public List<DataEntryForm> getAllDataEntryForms()
    {
        return dataEntryFormStore.getAll();
    }

    @Override
    @Transactional(readOnly = true)
    public String prepareDataEntryFormForSave( String htmlCode )
    {
        if ( htmlCode == null )
        {
            return null;
        }

        StringBuffer sb = new StringBuffer();

        Matcher inputMatcher = INPUT_PATTERN.matcher( htmlCode );

        while ( inputMatcher.find() )
        {
            // -----------------------------------------------------------------
            // Remove value and title tags from the HTML code
            // -----------------------------------------------------------------

            String dataElementCode = inputMatcher.group();

            Matcher valueTagMatcher = VALUE_TAG_PATTERN.matcher( dataElementCode );
            Matcher titleTagMatcher = TITLE_TAG_PATTERN.matcher( dataElementCode );

            if ( valueTagMatcher.find() && valueTagMatcher.groupCount() > 0 )
            {
                dataElementCode = dataElementCode.replace( valueTagMatcher.group( 1 ), EMPTY );
            }

            if ( titleTagMatcher.find() && titleTagMatcher.groupCount() > 0 )
            {
                dataElementCode = dataElementCode.replace( titleTagMatcher.group( 1 ), EMPTY );
            }

            inputMatcher.appendReplacement( sb, dataElementCode );
        }

        inputMatcher.appendTail( sb );

        return sb.toString();
    }

    @Override
    @Transactional(readOnly = true)
    public String prepareDataEntryFormForEdit( DataEntryForm dataEntryForm, DataSet dataSet, I18n i18n )
    {
        // ------------------------------------------------------------------------
        // Only called for creation of CustomDataForm
        // ------------------------------------------------------------------------

        //TODO HTML encode names

        if ( dataEntryForm == null || !dataEntryForm.hasForm() || dataSet == null )
        {
            return null;
        }

        CachingMap<String, CategoryOptionCombo> optionComboMap = new CachingMap<>();

        optionComboMap.putAll( IdentifiableObjectUtils.getUidObjectMap( dataSet.getDataElementOptionCombos() ) );

        StringBuffer sb = new StringBuffer();

        Matcher inputMatcher = INPUT_PATTERN.matcher( dataEntryForm.getHtmlCode() );

        while ( inputMatcher.find() )
        {
            String inputHtml = inputMatcher.group();

            Matcher identifierMatcher = IDENTIFIER_PATTERN.matcher( inputHtml );
            Matcher dataElementTotalMatcher = DATAELEMENT_TOTAL_PATTERN.matcher( inputHtml );
            Matcher indicatorMatcher = INDICATOR_PATTERN.matcher( inputHtml );

            String displayValue = null;
            String displayTitle = null;

            if ( identifierMatcher.find() && identifierMatcher.groupCount() > 0 )
            {
                String dataElementId = identifierMatcher.group( 1 );
                DataElement dataElement = dataElementService.getDataElement( dataElementId );

                String optionComboId = identifierMatcher.group( 2 );

                CategoryOptionCombo categoryOptionCombo = optionComboMap.get( optionComboId, () -> idObjectManager.getObject( CategoryOptionCombo.class, IdScheme.UID, optionComboId ) );

                String optionComboName = categoryOptionCombo != null ? escapeHtml3( categoryOptionCombo.getName() ) : "[ " + i18n.getString( "cat_option_combo_not_exist" ) + " ]";

                StringBuilder title = dataElement != null ?
                    new StringBuilder( "title=\"" ).append( dataElementId ).append( " - " ).
                        append( escapeHtml3( dataElement.getDisplayName() ) ).append( " - " ).append( optionComboId ).append( " - " ).
                        append( optionComboName ).append( " - " ).append( dataElement.getValueType() ).append( "\"" ) : new StringBuilder();

                displayValue = dataElement != null ? "value=\"[ " + escapeHtml3( dataElement.getDisplayName() ) + " " + optionComboName + " ]\"" : "[ " + i18n.getString( "data_element_not_exist" ) + " ]";
                displayTitle = dataElement != null ? title.toString() : "[ " + i18n.getString( "dataelement_not_exist" ) + " ]";
            }
            else if ( dataElementTotalMatcher.find() && dataElementTotalMatcher.groupCount() > 0 )
            {
                String dataElementId = dataElementTotalMatcher.group( 1 );
                DataElement dataElement = dataElementService.getDataElement( dataElementId );

                displayValue = dataElement != null ? "value=\"[ " + escapeHtml3( dataElement.getDisplayName() ) + " ]\"" : "[ " + i18n.getString( "data_element_not_exist" ) + " ]";
                displayTitle = dataElement != null ? "title=\"" + escapeHtml3( dataElement.getDisplayName() ) + "\"" : "[ " + i18n.getString( "data_element_not_exist" ) + " ]";
            }
            else if ( indicatorMatcher.find() && indicatorMatcher.groupCount() > 0 )
            {
                String indicatorId = indicatorMatcher.group( 1 );
                Indicator indicator = indicatorService.getIndicator( indicatorId );

                displayValue = indicator != null ? "value=\"[ " + escapeHtml3( indicator.getDisplayName() ) + " ]\"" : "[ " + i18n.getString( "indicator_not_exist" ) + " ]";
                displayTitle = indicator != null ? "title=\"" + escapeHtml3( indicator.getDisplayName() ) + "\"" : "[ " + i18n.getString( "indicator_not_exist" ) + " ]";
            }

            // -----------------------------------------------------------------
            // Insert name of data element operand as value and title
            // -----------------------------------------------------------------

            if ( displayValue == null || displayTitle == null )
            {
                log.warn( "Ignoring invalid form markup: '" + inputHtml + "'" );
                continue;
            }

            inputHtml = inputHtml.contains( EMPTY_VALUE_TAG ) ? inputHtml.replace( EMPTY_VALUE_TAG, displayValue ) : inputHtml.replace( TAG_CLOSE, (displayValue + TAG_CLOSE) );
            inputHtml = inputHtml.contains( EMPTY_TITLE_TAG ) ? inputHtml.replace( EMPTY_TITLE_TAG, displayTitle ) : inputHtml.replace( TAG_CLOSE, (displayTitle + TAG_CLOSE) );

            inputMatcher.appendReplacement( sb, inputHtml );
        }

        inputMatcher.appendTail( sb );

        return sb.toString();
    }

    @Override
    @Transactional(readOnly = true)
    public String prepareDataEntryFormForEntry( DataEntryForm dataEntryForm, DataSet dataSet, I18n i18n )
    {
        //TODO HTML encode names

        if ( dataEntryForm == null || !dataEntryForm.hasForm() || dataSet == null )
        {
            return null;
        }

        // ---------------------------------------------------------------------
        // Inline javascript/html to add to HTML before output
        // ---------------------------------------------------------------------
        
        List<String> compulsoryDataElementOperands = dataSet.getCompulsoryDataElementOperands().stream().map( DataElementOperand::getDimensionItem ).collect(  Collectors
            .toList() );        

        Map<String, DataElement> dataElementMap = Maps.uniqueIndex( dataSet.getDataElements(), de -> de.getUid() );

        CachingMap<String, CategoryOptionCombo> optionComboMap = new CachingMap<>();

        optionComboMap.putAll( IdentifiableObjectUtils.getUidObjectMap( dataSet.getDataElementOptionCombos() ) );

        int i = 1;

        StringBuffer sb = new StringBuffer();

        Matcher inputMatcher = INPUT_PATTERN.matcher( dataEntryForm.getHtmlCode() );

        while ( inputMatcher.find() )
        {
            // -----------------------------------------------------------------
            // Get HTML input field code
            // -----------------------------------------------------------------

            String inputHtml = inputMatcher.group();

            Matcher identifierMatcher = IDENTIFIER_PATTERN.matcher( inputHtml );
            Matcher dataElementTotalMatcher = DATAELEMENT_TOTAL_PATTERN.matcher( inputHtml );
            Matcher indicatorMatcher = INDICATOR_PATTERN.matcher( inputHtml );

            if ( identifierMatcher.find() && identifierMatcher.groupCount() > 0 )
            {
                String dataElementId = identifierMatcher.group( 1 );
                String optionComboId = identifierMatcher.group( 2 );

                DataElement dataElement = dataElementMap.get( dataElementId );

                if ( dataElement == null )
                {
                    return i18n.getString( "dataelement_with_id" ) + ": " + dataElementId + " " + i18n.getString( "does_not_exist_in_data_set" );
                }

                CategoryOptionCombo categoryOptionCombo = optionComboMap.get( optionComboId, () -> idObjectManager.getObject( CategoryOptionCombo.class, IdScheme.UID, optionComboId ) );

                if ( categoryOptionCombo == null )
                {
                    return i18n.getString( "category_option_combo_with_id" ) + ": " + optionComboId + " " + i18n.getString( "does_not_exist_in_data_set" );
                }

//                if ( dataSet.isDataElementDecoration() && dataElement.hasDescription() )
//                {
//                    String titleTag = " title=\"" + escapeHtml3( dataElement.getDisplayDescription() ) + "\" ";
//                    inputHtml = inputHtml.replaceAll( "title=\".*?\"", "" ).replace( TAG_CLOSE, titleTag + TAG_CLOSE );
//                }

                String appendCode = "", inputFieldId = dataElementId + "-" + optionComboId;
                
                ValueType valueType = dataElement.getValueType();
                
                String required = compulsoryDataElementOperands.contains( dataElementId + "." + optionComboId ) ? "required=\"required\"" : "";

                if ( ValueType.BOOLEAN == valueType )
                {
                    inputHtml = inputHtml.replaceAll(inputHtml, TAG_CLOSE);
                    
                    appendCode += "<label>";
                    appendCode += "<input type=\"radio\" class=\"entryselect\"" + required + " name=\"" + inputFieldId + "-val\"  id=\"" + inputFieldId + "-val\" tabindex=\"" + i++ + "\" value=\"true\">";
                    appendCode += i18n.getString( "yes" );
                    appendCode += "</label>";
                    
                    appendCode += "<label>";
                    appendCode += "<input type=\"radio\" class=\"entryselect\"" +  required + " name=\"" + inputFieldId + "-val\" " +
                        " id=\"" + inputFieldId + "-val\" tabindex=\"" + i++ + "\" value=\"false\">";
                    appendCode += i18n.getString( "no" );
                    appendCode += "</label>";

                    appendCode += "<img class=\"commentlink\" id=\"" + inputFieldId +"-comment\" " +
                                    "src=\"../images/comment.png\" title=\"View " + "comment\" style=\"cursor: pointer;\"" + TAG_CLOSE;
                }
                else if ( ValueType.TRUE_ONLY == valueType )
                {
                    appendCode += " name=\"entrytrueonly\" class=\"entrytrueonly\"" +  required + "type=\"checkbox\" tabindex=\"" + i++ + "\"" + TAG_CLOSE;
                }
                else if ( dataElement.hasOptionSet() )
                {
                    appendCode += " name=\"entryoptionset\""+ required  + " class=\"entryoptionset\" tabindex=\"" + i++ + "\"" + TAG_CLOSE;
                    appendCode += "<img class=\"commentlink\" id=\"" + inputFieldId +"-comment\" " +
                        "src=\"../images/comment.png\" title=\"View " +
                        "comment\" style=\"cursor: pointer;\"" + TAG_CLOSE;
                }
                else if ( ValueType.LONG_TEXT == valueType )
                {
                    inputHtml = inputHtml.replace( "input", "textarea" );

                    appendCode += " name=\"entryfield\"" + required  + " class=\"entryfield entryarea\" tabindex=\"" + i++ + "\"" + "></textarea>";
                }
                else if ( valueType.isFile() )
                {
                    inputHtml = inputHtml.replace( "input", "div" );

                    appendCode += " class=\"entryfileresource\" tabindex=\"" + i++ + "\">" +
                                    "<input " + required + " class=\"entryfileresource-input\" id=\"input-"+ inputFieldId + "-val\">" +
                                    "<div class=\"upload-field\">" +
                                        "<div class=\"upload-fileinfo\">" +
                                            "<div class=\"upload-fileinfo-size\"></div>" +
                                            "<div class=\"upload-fileinfo-name\"></div>" +
                                        "</div>" +
                                        "<div class=\"upload-progress\">" +
                                            "<div class=\"upload-progress-bar\"></div>" +
                                            "<div class=\"upload-progress-info\"></div>" +
                                        "</div>" +
                                    "</div>" +
                                    "<div class=\"upload-button-group\">" +
                                        "<button class=\"upload-button\"></button>" +
                                    "</div>" +
                                    "<input type=\"file\" style=\"display: none;\">" +
                                "</div>";
                }
                else if ( ValueType.TIME == valueType ) 
                {
                    appendCode += " type=\"time\" name=\"entrytime\"" + required + " class=\"entrytime\" tabindex=\"" + i++ + "\" id=\""+ inputFieldId + "\">";
                }
                else if ( ValueType.DATETIME == valueType )
                {
                    appendCode += " type=\"text\" name=\"entryfield\"" + required + " class=\"entryfield\" tabindex=\"" + i++ + "\">&nbsp;";
                    appendCode += "<input type=\"time\" name=\"entrytime\"" +  required + " class=\"entrytime\" tabindex=\"" + i++ + "\" id=\""+
                        inputFieldId +"-time" +"\">";
                }
                else if ( ValueType.URL == valueType )
                {
                    appendCode += " type=\"url\" name=\"entryfield\"" +  required + " class=\"entryfield\" tabindex=\"" + i++ + "\"" + TAG_CLOSE;
                }
                else
                {
                    appendCode += " type=\"text\" name=\"entryfield\"" + required + " class=\"entryfield\" tabindex=\"" + i++ + "\"" + TAG_CLOSE;
                }

                inputHtml = inputHtml.replace( TAG_CLOSE, appendCode );

                inputHtml += "<span id=\"" + dataElement.getUid() + "-dataelement\" style=\"display:none\">" + dataElement.getFormNameFallback() + "</span>";
                inputHtml += "<span id=\"" + categoryOptionCombo.getUid() + "-optioncombo\" style=\"display:none\">" + categoryOptionCombo.getName() + "</span>";
            }
            else if ( dataElementTotalMatcher.find() && dataElementTotalMatcher.groupCount() > 0 )
            {
                inputHtml = inputHtml.replace( TAG_CLOSE, " type=\"text\" class=\"dataelementtotal\"" + TAG_CLOSE );
            }
            else if ( indicatorMatcher.find() && indicatorMatcher.groupCount() > 0 )
            {
                inputHtml = inputHtml.replace( TAG_CLOSE, " type=\"text\" class=\"indicator\"" + TAG_CLOSE );
            }

            inputMatcher.appendReplacement( sb, inputHtml );
        }

        inputMatcher.appendTail( sb );

        return sb.toString();
    }

    @Override
    @Transactional(readOnly = true)
    public Set<DataElement> getDataElementsInDataEntryForm( DataSet dataSet )
    {
        if ( dataSet == null || !dataSet.hasDataEntryForm() )
        {
            return null;
        }

        Map<String, DataElement> dataElementMap = Maps.uniqueIndex( dataSet.getDataElements(), de -> de.getUid() );

        Set<DataElement> dataElements = new HashSet<>();

        Matcher inputMatcher = INPUT_PATTERN.matcher( dataSet.getDataEntryForm().getHtmlCode() );

        while ( inputMatcher.find() )
        {
            String inputHtml = inputMatcher.group();

            Matcher identifierMatcher = IDENTIFIER_PATTERN.matcher( inputHtml );
            Matcher dataElementTotalMatcher = DATAELEMENT_TOTAL_PATTERN.matcher( inputHtml );

            DataElement dataElement = null;

            if ( identifierMatcher.find() && identifierMatcher.groupCount() > 0 )
            {
                String dataElementId = identifierMatcher.group( 1 );
                dataElement = dataElementMap.get( dataElementId );
            }
            else if ( dataElementTotalMatcher.find() && dataElementTotalMatcher.groupCount() > 0 )
            {
                String dataElementId = dataElementTotalMatcher.group( 1 );
                dataElement = dataElementMap.get( dataElementId );
            }

            if ( dataElement != null )
            {
                dataElements.add( dataElement );
            }
        }

        return dataElements;
    }
}