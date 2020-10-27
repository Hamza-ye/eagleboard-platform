package com.mass3d.system.util;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.function.Predicate;
import com.mass3d.common.IdentifiableObject;

public class PredicateUtils
{
    public static final Predicate<Field> idObjectCollections = new CollectionWithTypePredicate( IdentifiableObject.class );

    private static class CollectionPredicate
        implements Predicate<Field>
    {
        @Override
        public boolean test( Field field )
        {
            return Collection.class.isAssignableFrom( field.getType() );
        }
    }

    private static class CollectionWithTypePredicate
        implements Predicate<Field>
    {
        private CollectionPredicate collectionPredicate = new CollectionPredicate();

        private Class<?> type;

        CollectionWithTypePredicate( Class<?> type )
        {
            this.type = type;
        }

        @Override
        public boolean test( Field field )
        {
            if ( collectionPredicate.test( field ) )
            {
                ParameterizedType parameterizedType = (ParameterizedType) field.getGenericType();
                Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();

                if ( actualTypeArguments.length > 0 )
                {
                    if ( type.isAssignableFrom( (Class<?>) actualTypeArguments[0] ) )
                    {
                        return true;
                    }
                }
            }

            return false;
        }
    }
}
