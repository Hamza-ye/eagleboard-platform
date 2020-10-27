package com.mass3d.system.startup;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

public abstract class TransactionContextStartupRoutine
    extends AbstractStartupRoutine
{
   @Autowired
   private TransactionTemplate transactionTemplate;

   /**
    * Work performed in this method will run inside a transaction context.
    */
   public abstract void executeInTransaction();

   @Override
   public final void execute()
   {
       transactionTemplate.execute( new TransactionCallback<Object>()
       {
           @Override
           public Object doInTransaction( TransactionStatus status )
           {
               executeInTransaction();
               return null;
           }
       } );
   }
}
