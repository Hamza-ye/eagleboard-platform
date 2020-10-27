package com.mass3d.hibernate.jsonb.type;

import com.fasterxml.jackson.databind.JavaType;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Properties;
import com.mass3d.render.DeviceRenderTypeMap;
import com.mass3d.render.RenderDevice;
import com.mass3d.render.type.RenderingObject;

public class JsonDeviceRenderTypeMap extends JsonBinaryType
{
    private Class<? extends RenderingObject> renderType;

    @Override
    protected JavaType getResultingJavaType( Class<?> returnedClass )
    {
        return MAPPER.getTypeFactory().constructType( DeviceRenderTypeMap.class );
    }

    @Override
    protected Object convertJsonToObject( String content )
    {
        try
        {
            LinkedHashMap<RenderDevice, LinkedHashMap<String, String>> map = reader.readValue( content );
            return convertMapToObject( map );
        }
        catch ( IOException | IllegalAccessException | InstantiationException e )
        {
            throw new RuntimeException( e );
        }
    }

    @Override
    public void setParameterValues( Properties parameters )
    {
        super.setParameterValues( parameters );

        final String renderType = (String) parameters.get( "renderType" );

        if ( renderType == null )
        {
            throw new IllegalArgumentException(
                String.format( "Required parameter '%s' is not configured", "renderType" ) );
        }

        try
        {
            this.renderType = (Class<? extends RenderingObject>) classForName( renderType );
        }
        catch ( ClassNotFoundException e )
        {
            throw new IllegalArgumentException( "Class: " + renderType + " is not a known class type." );
        }
    }

    private <T extends RenderingObject> DeviceRenderTypeMap<T> convertMapToObject( LinkedHashMap<RenderDevice, LinkedHashMap<String, String>> map ) throws IllegalAccessException, InstantiationException {
        DeviceRenderTypeMap deviceRenderTypeMap = new DeviceRenderTypeMap<>();
        for( RenderDevice renderDevice : map.keySet() )
        {
            LinkedHashMap<String, String> renderObjectMap = map.get( renderDevice );
            RenderingObject renderingObject = renderType.newInstance();
            renderingObject.setType( Enum
                .valueOf( renderingObject.getRenderTypeClass(), renderObjectMap.get( RenderingObject._TYPE ) ) );
            deviceRenderTypeMap.put(  renderDevice , renderingObject );
        }
        return deviceRenderTypeMap;
    }
}
