package com.mass3d.dxf2.importsummary;

public enum ImportStatus
{
    SUCCESS( 1 ),
    WARNING( 2 ),
    ERROR( 3 );
    
    private int order;
    
    ImportStatus( int order )
    {
        this.order = order;
    }
    
    public int getOrder()
    {
        return order;
    }
}
