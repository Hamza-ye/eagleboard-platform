package com.mass3d.startup;

import com.mass3d.dataelement.DataElement;
import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.mass3d.constant.Constant;
import com.mass3d.constant.ConstantService;
import com.mass3d.dataelement.DataElementService;
import com.mass3d.expression.Expression;
import com.mass3d.expression.ExpressionService;
import com.mass3d.indicator.Indicator;
import com.mass3d.indicator.IndicatorService;
import com.mass3d.system.startup.TransactionContextStartupRoutine;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Upgrades indicator formulas, expressions (for validation rules) and custom
 * data entry forms from using identifiers to using uids.
 *
 */
public class ExpressionUpgrader
    extends TransactionContextStartupRoutine
{
    private static final String OLD_OPERAND_EXPRESSION = "\\[(\\d+)\\.?(\\d*)\\]";
    private static final String OLD_CONSTANT_EXPRESSION = "\\[C(\\d+?)\\]";

    private static final Pattern OLD_OPERAND_PATTERN = Pattern.compile( OLD_OPERAND_EXPRESSION );
    private static final Pattern OLD_CONSTANT_PATTERN = Pattern.compile( OLD_CONSTANT_EXPRESSION );

    private static final Log log = LogFactory.getLog( ExpressionUpgrader.class );

//    @Autowired
//    private DataEntryFormService dataEntryFormService;

    @Autowired
    private DataElementService dataElementService;

//    @Autowired
//    private CategoryService categoryService;

    @Autowired
    private IndicatorService indicatorService;

    @Autowired
    private ConstantService constantService;

    @Autowired
    private ExpressionService expressionService;

    @Override
    public void executeInTransaction()
    {
        upgradeIndicators();
        upgradeExpressions();
//        upgradeDataEntryForms();
    }

    private void upgradeIndicators()
    {
        Collection<Indicator> indicators = indicatorService.getAllIndicators();

        for ( Indicator indicator : indicators )
        {
            String numerator = upgradeExpression( indicator.getNumerator() );
            String denominator = upgradeExpression( indicator.getDenominator() );

            if ( numerator != null || denominator != null )
            {
                indicator.setNumerator( numerator );
                indicator.setDenominator( denominator );
                indicatorService.updateIndicator( indicator );
            }
        }
    }

    private void upgradeExpressions()
    {
        Collection<Expression> expressions = expressionService.getAllExpressions();

        for ( Expression expression : expressions )
        {
            String expr = upgradeExpression( expression.getExpression() );

            if ( expr != null )
            {
                expression.setExpression( expr );
                expressionService.updateExpression( expression );
            }
        }
    }

    private String upgradeExpression( String expression )
    {
        if ( expression == null || expression.trim().isEmpty() )
        {
            return null;
        }

        boolean changes = false;

        StringBuffer sb = new StringBuffer();

        try
        {
            // -----------------------------------------------------------------
            // Constants
            // -----------------------------------------------------------------

            Matcher matcher = OLD_CONSTANT_PATTERN.matcher( expression );

            while ( matcher.find() )
            {
                Constant constant = constantService.getConstant( Integer.parseInt( matcher.group( 1 ) ) );
                String replacement = "C{" + constant.getUid() + "}";
                matcher.appendReplacement( sb, replacement );
                changes = true;
            }

            matcher.appendTail( sb );
            expression = sb.toString();

            // -----------------------------------------------------------------
            // Operands
            // -----------------------------------------------------------------

            matcher = OLD_OPERAND_PATTERN.matcher( expression );
            sb = new StringBuffer();

            while ( matcher.find() )
            {
                DataElement de = dataElementService.getDataElement( Integer.parseInt( matcher.group( 1 ) ) );
                String replacement = "#{" + de.getUid();

//                if ( matcher.groupCount() == 2 && matcher.group( 2 ) != null && !matcher.group( 2 ).trim().isEmpty() )
//                {
//                    CategoryOptionCombo coc = categoryService.getCategoryOptionCombo( Integer.parseInt( matcher.group( 2 ) ) );
//                    replacement += "." + coc.getUid();
//                }

                replacement += "}";
                matcher.appendReplacement( sb, replacement );
                changes = true;
            }

            matcher.appendTail( sb );
            expression = sb.toString();
        }
        catch ( Exception ex )
        {
            log.error( "Failed to upgrade expression: " + expression );
            log.error( ex ); // Log and continue
        }

        if ( changes )
        {
            log.info( "Upgraded expression: " + expression );
        }

        return changes ? expression : null;
    }

//    private void upgradeDataEntryForms()
//    {
//        Collection<DataEntryForm> forms = dataEntryFormService.getAllDataEntryForms();
//
//        for ( DataEntryForm form : forms )
//        {
//            if ( DataEntryForm.CURRENT_FORMAT > form.getFormat() && form.getHtmlCode() != null && !form.getHtmlCode().trim().isEmpty() )
//            {
//                try
//                {
//                    // ---------------------------------------------------------
//                    // Identifiers
//                    // ---------------------------------------------------------
//
//                    Matcher matcher = IDENTIFIER_PATTERN.matcher( form.getHtmlCode() );
//                    StringBuffer sb = new StringBuffer();
//
//                    while ( matcher.find() )
//                    {
//                        DataElement de = dataElementService.getDataElement( Integer.parseInt( matcher.group( 1 ) ) );
//                        CategoryOptionCombo coc = categoryService.getCategoryOptionCombo( Integer.parseInt( matcher.group( 2 ) ) );
//                        String replacement = "id=\"" + de.getUid() + "-" + coc.getUid() + "-val\"";
//                        matcher.appendReplacement( sb, replacement );
//                    }
//
//                    matcher.appendTail( sb );
//                    form.setHtmlCode( sb.toString() );
//
//                    // ---------------------------------------------------------
//                    // Data element totals
//                    // ---------------------------------------------------------
//
//                    matcher = DATAELEMENT_TOTAL_PATTERN.matcher( form.getHtmlCode() );
//                    sb = new StringBuffer();
//
//                    while ( matcher.find() )
//                    {
//                        DataElement de = dataElementService.getDataElement( Integer.parseInt( matcher.group( 1 ) ) );
//                        String replacement = "dataelementid=\"" + de.getUid() + "\"";
//                        matcher.appendReplacement( sb, replacement );
//                    }
//
//                    matcher.appendTail( sb );
//                    form.setHtmlCode( sb.toString() );
//
//                    // ---------------------------------------------------------
//                    // Indicators
//                    // ---------------------------------------------------------
//
//                    matcher = INDICATOR_PATTERN.matcher( form.getHtmlCode() );
//                    sb = new StringBuffer();
//
//                    while ( matcher.find() )
//                    {
//                        Indicator in = indicatorService.getIndicator( Integer.parseInt( matcher.group( 1 ) ) );
//                        String replacement = "indicatorid=\"" + in.getUid() + "\"";
//                        matcher.appendReplacement( sb, replacement );
//                    }
//
//                    matcher.appendTail( sb );
//                    form.setHtmlCode( sb.toString() );
//
//                    // ---------------------------------------------------------
//                    // Update format and save
//                    // ---------------------------------------------------------
//
//                    form.setFormat( DataEntryForm.CURRENT_FORMAT );
//                    dataEntryFormService.updateDataEntryForm( form );
//
//                    log.info( "Upgraded custom data entry form: " + form.getName() );
//                }
//                catch ( Exception ex )
//                {
//                    log.error( "Upgrading custom data entry form failed: " + form.getName() );
//                    log.error( ex ); // Log and continue
//                }
//            }
//        }
//    }
}
