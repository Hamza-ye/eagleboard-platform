package com.mass3d.preheat;

import java.util.List;
import java.util.Map;
import java.util.Set;
import com.mass3d.common.IdentifiableObject;

public interface PreheatService
{
    /**
     * Preheat a set of pre-defined classes. If size == 0, then preheat all metadata classes automatically.
     *
     * @param params Params for preheating
     */
    Preheat preheat(PreheatParams params);

    /**
     * Validate PreheatParams.
     *
     * @param params PreheatParams
     */
    void validate(PreheatParams params);

    /**
     * Scan object and collect all references (both id object and collections with id objects).
     *
     * @param object Object to scan
     * @return Maps classes to collections of identifiers
     */
    Map<PreheatIdentifier, Map<Class<? extends IdentifiableObject>, Set<String>>> collectReferences(
        Object object);

    Map<Class<?>, Map<String, Map<String, Object>>> collectObjectReferences(Object object);

    /**
     * Scan objects and collect unique values (used to verify object properties with unique=true)
     *
     * @param objects Objects to scan
     * @return Klass -> Property.name -> Value -> UID
     */
    Map<Class<? extends IdentifiableObject>, Map<String, Map<Object, String>>> collectUniqueness(
        PreheatIdentifier identifier,
        Map<Class<? extends IdentifiableObject>, List<IdentifiableObject>> objects);

    /**
     * Connects id object references on a given object using a given identifier + a preheated Preheat cache.
     *
     * @param object     Object to connect to
     * @param preheat    Preheat Cache to use
     * @param identifier Use this identifier type to attach references
     */
    void connectReferences(Object object, Preheat preheat, PreheatIdentifier identifier);

    /**
     * Preheat object, and connect back references.
     *
     * @param object Object to refresh.
     */
    void refresh(IdentifiableObject object);
}
