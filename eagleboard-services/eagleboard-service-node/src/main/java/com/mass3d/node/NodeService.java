package com.mass3d.node;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import com.mass3d.node.types.CollectionNode;
import com.mass3d.node.types.ComplexNode;
import com.mass3d.node.types.RootNode;

public interface NodeService
{
    /**
     * Find a nodeSerializer that supports contentType or return null.
     *
     * @param contentType NodeSerializer contentType
     * @return NodeSerializer that support contentType, or null if not match was found
     * @see com.mass3d.node.NodeSerializer
     */
    NodeSerializer getNodeSerializer(String contentType);

    /**
     * Write out rootNode to a nodeSerializer that matches the contentType.
     *
     * @param rootNode     RootNode to write
     * @param contentType  NodeSerializer contentType
     * @param outputStream Write to this outputStream
     */
    void serialize(RootNode rootNode, String contentType, OutputStream outputStream);

    /**
     * Find a nodeDeserializer that supports contentType or return null.
     *
     * @param contentType NodeDeserializer contentType
     * @return NodeDeserializer that support contentType, or null if not match was found
     * @see com.mass3d.node.NodeDeserializer
     */
    NodeDeserializer getNodeDeserializer(String contentType);

    /**
     * @param contentType NodeDeserializer contentType
     * @param inputStream Read RootNode from this stream
     * @return RootNode deserialized from inputStream
     */
    RootNode deserialize(String contentType, InputStream inputStream);

    /**
     * Convert a single object to a complex node instance.
     *
     * @param object Object to convert
     * @return Instance of complex node, or null if any issues
     */
    ComplexNode toNode(Object object);

    /**
     * Convert a list of objects to a collection node of complex nodes.
     *
     * @param objects List of objects to convert
     * @return {@link CollectionNode} instance with converted objects
     */
    CollectionNode toNode(List<Object> objects);
}
