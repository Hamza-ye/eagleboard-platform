package com.mass3d.commons.comparator;

import java.io.File;
import java.util.Comparator;

/**
 * Comparator which compares Files based on their last modification time.
 *
 */
public class FileLastModifiedComparator
    implements Comparator<File>
{
    @Override
    public int compare( File o1, File o2 )
    {
        return (int) (o1.lastModified() - o2.lastModified());
    }
}
