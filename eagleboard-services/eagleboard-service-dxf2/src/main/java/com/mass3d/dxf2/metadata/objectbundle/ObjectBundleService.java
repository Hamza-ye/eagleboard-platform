package com.mass3d.dxf2.metadata.objectbundle;

import com.mass3d.dxf2.metadata.objectbundle.feedback.ObjectBundleCommitReport;

public interface ObjectBundleService
{
    /**
     * Creates and prepares object bundle.
     *
     * @param params Params object for this bundle.
     * @return Configured ObjectBundle instance
     */
    ObjectBundle create(ObjectBundleParams params);

    /**
     * Commits objects from bundle into persistence store if bundle mode COMMIT is enabled.
     *
     * @param bundle ObjectBundle to commit.
     */
    ObjectBundleCommitReport commit(ObjectBundle bundle);
}
