package com.mass3d.node;

import java.util.List;
import com.mass3d.schema.Property;
import org.springframework.core.Ordered;

public interface Node extends Ordered
{
    /**
     * Name of this node.
     *
     * @return current name of node
     */
    String getName();

    /**
     * Type specifier for this node.
     *
     * @return Node type
     * @see com.mass3d.node.NodeType
     */
    NodeType getType();

    /**
     * Get parent node, or null if this is a top-level node.
     *
     * @return parent or null if node does not have parent
     */
    Node getParent();

    /**
     * @param type Type to check for
     * @return True if node is of this type
     */
    boolean is(NodeType type);

    /**
     * Helper that checks if node is of simple type, useful to checking if
     * you are allowed to add children to this node.
     *
     * @return true if type is simple
     * @see com.mass3d.node.NodeType
     */
    boolean isSimple();

    /**
     * Helper that checks if node is of complex type.
     *
     * @return true if type is complex
     * @see com.mass3d.node.NodeType
     */
    boolean isComplex();

    /**
     * Helper that checks if node is of collection type.
     *
     * @return true if type is collection
     * @see com.mass3d.node.NodeType
     */
    boolean isCollection();

    /**
     * Should this be considered data or metadata.
     *
     * @return True if metadata (like a pager)
     */
    boolean isMetadata();

    /**
     * Namespace for this node. Not all serializers support this, and its up to the
     * NodeSerializer implementation to decide what to do with this.
     *
     * @return namespace
     * @see com.mass3d.node.NodeSerializer
     */
    String getNamespace();

    /**
     * Comment for this node. Not all serializers support this, and its up to the
     * NodeSerializer implementation to decide what to do with this.
     *
     * @return namespace
     * @see com.mass3d.node.NodeSerializer
     */
    String getComment();

    /**
     * The associated property for this node (if any).
     */
    Property getProperty();

    /**
     * Is there a property associated with this Node.
     */
    boolean haveProperty();

    /**
     * Adds a child to this node.
     *
     * @param child Child node to add
     * @return Child node that was added
     */
    <T extends Node> T addChild(T child);

    /**
     * Remove a child from this node.
     *
     * @param child Child node to add
     */
    <T extends Node> void removeChild(T child);

    /**
     * Adds a collection of children to this node.
     *
     * @param children Child nodes to add
     */
    <T extends Node> void addChildren(Iterable<T> children);

    /**
     * Get all child notes associated with this node. Please note that the returned list is a copy
     * of the internal list, and changes to the list will not be reflected in the node.
     *
     * @return List of child nodes associated with this node
     */
    List<Node> getChildren();

    /**
     * @return the unordered children that are associated with this node.
     */
    List<Node> getUnorderedChildren();
}
