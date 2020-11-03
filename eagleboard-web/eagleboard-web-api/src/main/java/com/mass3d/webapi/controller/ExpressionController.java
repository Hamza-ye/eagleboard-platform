package com.mass3d.webapi.controller;

import com.mass3d.feedback.Status;
import com.mass3d.dxf2.webmessage.DescriptiveWebMessage;
import com.mass3d.expression.ExpressionService;
import com.mass3d.expression.ExpressionValidationOutcome;
import com.mass3d.i18n.I18n;
import com.mass3d.i18n.I18nManager;
import com.mass3d.webapi.mvc.annotation.ApiVersion;
import com.mass3d.common.DhisApiVersion;
import com.mass3d.webapi.service.WebMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.mass3d.expression.ParseType.INDICATOR_EXPRESSION;

@Controller
@RequestMapping( value = "/expressions" )
@ApiVersion( { DhisApiVersion.DEFAULT, DhisApiVersion.ALL } )
public class ExpressionController
{
    @Autowired
    protected WebMessageService webMessageService;

    @Autowired
    private ExpressionService expressionService;

    @Autowired
    private I18nManager i18nManager;

    @RequestMapping( value = "/description", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE )
    public void getExpressionDescription( @RequestParam String expression, HttpServletResponse response )
        throws IOException
    {
        I18n i18n = i18nManager.getI18n();

        ExpressionValidationOutcome result = expressionService.expressionIsValid( expression, INDICATOR_EXPRESSION );

        DescriptiveWebMessage message = new DescriptiveWebMessage();
        message.setStatus( result.isValid() ? Status.OK : Status.ERROR );
        message.setMessage( i18n.getString( result.getKey() ) );

        if ( result.isValid() )
        {
            message.setDescription( expressionService.getExpressionDescription( expression, INDICATOR_EXPRESSION ) );
        }

        webMessageService.sendJson( message, response );
    }
}
