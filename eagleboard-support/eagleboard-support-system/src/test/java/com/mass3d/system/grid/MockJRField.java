package com.mass3d.system.grid;

import net.sf.jasperreports.engine.JRField;
import net.sf.jasperreports.engine.JRPropertiesHolder;
import net.sf.jasperreports.engine.JRPropertiesMap;
import net.sf.jasperreports.engine.JRPropertyExpression;

public class MockJRField
    implements JRField
{
    private String name;

    public MockJRField( String name )
    {
        this.name = name;
    }

    @Override
    public String getDescription()
    {
        return null;
    }

    @Override
    public String getName()
    {
        return name;
    }

    @Override
    public Class<?> getValueClass()
    {
        return null;
    }

    @Override
    public String getValueClassName()
    {
        return null;
    }

    @Override
    public JRPropertyExpression[] getPropertyExpressions()
    {
        return new JRPropertyExpression[0];
    }

    @Override
    public void setDescription( String arg0 )
    {
    }

    @Override
    public JRPropertiesHolder getParentProperties()
    {
        return null;
    }

    @Override
    public JRPropertiesMap getPropertiesMap()
    {
        return null;
    }

    @Override
    public boolean hasProperties()
    {
        return false;
    }

    @Override
    public Object clone()
    {
        return this;
    }
}
