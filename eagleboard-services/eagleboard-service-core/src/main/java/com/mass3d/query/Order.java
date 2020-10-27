package com.mass3d.query;

import com.google.common.base.MoreObjects;
import java.util.Date;
import java.util.Objects;
import javax.annotation.Nonnull;
import com.mass3d.schema.Property;
import com.mass3d.system.util.ReflectionUtils;

public class Order
{
    private Direction direction;

    private boolean ignoreCase;

    private Property property;

    public Order( Property property, Direction direction )
    {
        this.property = property;
        this.direction = direction;
    }

    public Order ignoreCase()
    {
        this.ignoreCase = true;
        return this;
    }

    public boolean isAscending()
    {
        return Direction.ASCENDING == direction;
    }

    public boolean isIgnoreCase()
    {
        return ignoreCase;
    }

    public Property getProperty()
    {
        return property;
    }

    public boolean isPersisted()
    {
        return property.isPersisted() && property.isSimple();
    }

    public boolean isNonPersisted()
    {
        return !property.isPersisted() && property.isSimple();
    }

    public int compare( Object lside, Object rside )
    {
        Object o1 = ReflectionUtils.invokeMethod( lside, property.getGetterMethod() );
        Object o2 = ReflectionUtils.invokeMethod( rside, property.getGetterMethod() );

        if ( o1 == o2 )
        {
            return 0;
        }

        // for null values use the same order like PostgreSQL in order to have same effect like DB ordering
        // (NULLs are greater than other values)
        if ( o1 == null || o2 == null )
        {
            if ( o1 == null )
            {
                return isAscending() ? 1 : -1;
            }
            return isAscending() ? -1 : 1;
        }

        if ( String.class.isInstance( o1 ) && String.class.isInstance( o2 ) )
        {
            String value1 = ignoreCase ? ((String) o1).toLowerCase() : (String) o1;
            String value2 = ignoreCase ? ((String) o2).toLowerCase() : (String) o2;

            return isAscending() ? value1.compareTo( value2 ) : value2.compareTo( value1 );
        }
        if ( Boolean.class.isInstance( o1 ) && Boolean.class.isInstance( o2 ) )
        {
            return isAscending() ? ((Boolean) o1).compareTo( (Boolean) o2 ) : ((Boolean) o2).compareTo( (Boolean) o1 );
        }
        else if ( Integer.class.isInstance( o1 ) && Integer.class.isInstance( o2 ) )
        {
            return isAscending() ? ((Integer) o1).compareTo( (Integer) o2 ) : ((Integer) o2).compareTo( (Integer) o1 );
        }
        else if ( Float.class.isInstance( o1 ) && Float.class.isInstance( o2 ) )
        {
            return isAscending() ? ((Float) o1).compareTo( (Float) o2 ) : ((Float) o2).compareTo( (Float) o1 );
        }
        else if ( Double.class.isInstance( o1 ) && Double.class.isInstance( o2 ) )
        {
            return isAscending() ? ((Double) o1).compareTo( (Double) o2 ) : ((Double) o2).compareTo( (Double) o1 );
        }
        else if ( Date.class.isInstance( o1 ) && Date.class.isInstance( o2 ) )
        {
            return isAscending() ? ((Date) o1).compareTo( (Date) o2 ) : ((Date) o2).compareTo( (Date) o1 );
        }
        else if ( Enum.class.isInstance( o1 ) && Enum.class.isInstance( o2 ) )
        {
            return isAscending() ? String.valueOf( o1 ).compareTo( String.valueOf( o2 ) ) : String.valueOf( o2 ).compareTo( String
                .valueOf( o1 ) );
        }

        return 0;
    }

    public static Order asc( Property property )
    {
        return new Order( property, Direction.ASCENDING );
    }

    public static Order iasc( Property property )
    {
        return new Order( property, Direction.ASCENDING ).ignoreCase();
    }

    public static Order desc( Property property )
    {
        return new Order( property, Direction.DESCENDING );
    }

    public static Order idesc( Property property )
    {
        return new Order( property, Direction.DESCENDING ).ignoreCase();
    }

    public static Order from( String direction, Property property )
    {
        switch ( direction )
        {
            case "asc":
                return Order.asc( property );
            case "iasc":
                return Order.iasc( property );
            case "desc":
                return Order.desc( property );
            case "idesc":
                return Order.idesc( property );
            default:
                return Order.asc( property );
        }
    }

    @Override
    public int hashCode()
    {
        return Objects.hash( direction, ignoreCase, property );
    }

    @Override
    public boolean equals( Object obj )
    {
        if ( this == obj )
        {
            return true;
        }

        if ( obj == null || getClass() != obj.getClass() )
        {
            return false;
        }

        final Order other = (Order) obj;

        return Objects.equals( this.direction, other.direction )
            && Objects.equals( this.ignoreCase, other.ignoreCase )
            && Objects.equals( this.property, other.property );
    }

    /**
     * @return the order string (e.g. <code>name:iasc</code>).
     */
    @Nonnull
    public String toOrderString()
    {
        final StringBuilder sb = new StringBuilder( property.getName() );
        sb.append( ':' );
        if ( isIgnoreCase() )
        {
            sb.append( 'i' );
        }
        sb.append( isAscending() ? "asc" : "desc" );
        return sb.toString();
    }

    @Override
    public String toString()
    {
        return MoreObjects.toStringHelper( this )
            .add( "direction", direction )
            .add( "ignoreCase", ignoreCase )
            .add( "property", property )
            .toString();
    }
}
