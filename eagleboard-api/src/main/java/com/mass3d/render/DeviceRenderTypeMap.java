package com.mass3d.render;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import java.util.LinkedHashMap;
import com.mass3d.common.DxfNamespaces;

/**
 * This class represents the relationship between a RenderingType and a RenderDevice. A RenderDevice can have one RenderType.
 * @param <T> an object wrapping an enum representing options for rendering a specific object
 */
@JacksonXmlRootElement( localName = "renderType", namespace = DxfNamespaces.DXF_2_0 )
public class DeviceRenderTypeMap<T>
    extends LinkedHashMap<RenderDevice, T>
{
}
