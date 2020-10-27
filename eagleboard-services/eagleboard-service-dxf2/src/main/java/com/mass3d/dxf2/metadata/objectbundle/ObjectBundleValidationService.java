package com.mass3d.dxf2.metadata.objectbundle;

import com.mass3d.dxf2.metadata.objectbundle.feedback.ObjectBundleValidationReport;

public interface ObjectBundleValidationService
{
    /**
     * Validate object bundle
     *
     * @param bundle Bundle to validate
     */
    ObjectBundleValidationReport validate(ObjectBundle bundle);
}
