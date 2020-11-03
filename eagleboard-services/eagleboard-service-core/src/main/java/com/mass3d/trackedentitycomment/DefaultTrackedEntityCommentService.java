package com.mass3d.trackedentitycomment;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service( "com.mass3d.trackedentitycomment.TrackedEntityCommentService" )
public class DefaultTrackedEntityCommentService
    implements TrackedEntityCommentService
{
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private final TrackedEntityCommentStore commentStore;

    public DefaultTrackedEntityCommentService( TrackedEntityCommentStore commentStore )
    {
        checkNotNull( commentStore );

        this.commentStore = commentStore;
    }

    // -------------------------------------------------------------------------
    // Implementation methods
    // -------------------------------------------------------------------------

    @Override
    @Transactional
    public long addTrackedEntityComment( TrackedEntityComment comment )
    {
        commentStore.save( comment );

        return comment.getId();
    }

    @Override
    @Transactional
    public void deleteTrackedEntityComment( TrackedEntityComment comment )
    {
        commentStore.delete( comment );
    }

    @Override
    @Transactional(readOnly = true)
    public boolean trackedEntityCommentExists( String uid )
    {
        return commentStore.exists( uid );
    }

    @Override
    @Transactional(readOnly = true)
    public List<String> filterExistingNotes( List<String> uids )
    {
        return this.commentStore.filterExisting( uids );
    }

    @Override
    @Transactional
    public void updateTrackedEntityComment( TrackedEntityComment comment )
    {
        commentStore.update( comment );
    }

    @Override
    @Transactional(readOnly = true)
    public TrackedEntityComment getTrackedEntityComment( long id )
    {
        return commentStore.get( id );
    }

}
