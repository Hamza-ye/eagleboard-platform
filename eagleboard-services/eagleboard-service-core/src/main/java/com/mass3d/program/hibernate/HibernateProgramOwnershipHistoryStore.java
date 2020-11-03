package com.mass3d.program.hibernate;

import org.hibernate.SessionFactory;
import com.mass3d.program.ProgramOwnershipHistory;
import com.mass3d.program.ProgramOwnershipHistoryStore;
import org.springframework.stereotype.Repository;

@Repository( "com.mass3d.program.ProgramOwnershipHistoryStore" )
public class HibernateProgramOwnershipHistoryStore implements ProgramOwnershipHistoryStore
{
    private SessionFactory sessionFactory;

    public HibernateProgramOwnershipHistoryStore( SessionFactory sessionFactory )
    {
        this.sessionFactory = sessionFactory;
    }

    // -------------------------------------------------------------------------
    // ProgramOwnershipHistoryStore implementation
    // -------------------------------------------------------------------------

    @Override
    public void addProgramOwnershipHistory( ProgramOwnershipHistory programOwnershipHistory )
    {
        sessionFactory.getCurrentSession().save( programOwnershipHistory );
    }
}
