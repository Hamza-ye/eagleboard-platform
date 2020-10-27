package com.mass3d.fileresource;

import static com.google.common.base.Preconditions.checkNotNull;

import com.mass3d.system.deletion.DeletionHandler;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component( "com.mass3d.fileresource.MessageAttachmentDeletionHandler" )
public class MessageAttachmentDeletionHandler extends DeletionHandler
{
    private final JdbcTemplate jdbcTemplate;

    public MessageAttachmentDeletionHandler( JdbcTemplate jdbcTemplate )
    {
        checkNotNull( jdbcTemplate );
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    protected String getClassName()
    {
        return FileResource.class.getName();
    }

    @Override
    public String allowDeleteFileResource( FileResource fileResource )
    {
        String sql = "SELECT COUNT(*) FROM messageattachments WHERE fileresourceid=" + fileResource.getId();

        int result = jdbcTemplate.queryForObject( sql, Integer.class );

        return result == 0 || fileResource.getStorageStatus() != FileResourceStorageStatus.STORED ? null : ERROR;
    }

    @Override
    public void deleteFileResource( FileResource fileResource )
    {
        String sql = "DELETE FROM messageattachments WHERE fileresourceid=" + fileResource.getId();

        jdbcTemplate.execute( sql );
    }
}
