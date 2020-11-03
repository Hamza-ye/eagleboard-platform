package com.mass3d.webapi.controller;

import com.mass3d.dataentryform.DataEntryForm;
import com.mass3d.schema.descriptors.DataEntryFormSchemaDescriptor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping( value = DataEntryFormSchemaDescriptor.API_ENDPOINT )
public class DataEntryFormController
    extends AbstractCrudController<DataEntryForm>
{
}
