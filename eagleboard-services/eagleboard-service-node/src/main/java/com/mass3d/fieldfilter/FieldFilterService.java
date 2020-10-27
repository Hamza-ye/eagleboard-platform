package com.mass3d.fieldfilter;

import java.util.Arrays;
import java.util.List;
import com.mass3d.node.types.CollectionNode;
import com.mass3d.node.types.ComplexNode;

public interface FieldFilterService
{
    List<String> SHARING_FIELDS = Arrays.asList(
        "!user", "!publicAccess", "!userGroupAccesses", "!userAccesses", "!externalAccess" );

    /**
     * Perform inclusion/exclusion on a list of objects.
     */
    ComplexNode toComplexNode(FieldFilterParams params);

    /**
     * Perform inclusion/exclusion on a list of objects.
     */
    CollectionNode toCollectionNode(Class<?> wrapper, FieldFilterParams params);
}
