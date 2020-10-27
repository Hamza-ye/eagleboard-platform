package com.mass3d.fieldfilter;

import com.google.common.collect.Lists;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Component( "com.mass3d.fieldfilter.FieldParser" )
public class DefaultFieldParser implements FieldParser
{
    @Override
    public FieldMap parse( String fields )
    {
        List<String> prefixList = Lists.newArrayList();
        FieldMap fieldMap = new FieldMap();

        StringBuilder builder = new StringBuilder();

        String[] fieldSplit = fields.split( "" );

        for ( int i = 0; i < fieldSplit.length; i++ )
        {
            String c = fieldSplit[i];

            // if we reach a field transformer, parse it out here (necessary to allow for () to be used to handle transformer parameters)
            if ( (c.equals( ":" ) && fieldSplit[i + 1].equals( ":" )) || c.equals( "~" ) || c.equals( "|" ) )
            {
                boolean insideParameters = false;

                for ( ; i < fieldSplit.length; i++ )
                {
                    c = fieldSplit[i];

                    if ( StringUtils.isAlphanumeric( c ) || c.equals( ":" ) || c.equals( "~" ) || c.equals( "|" ) )
                    {
                        builder.append( c );
                    }
                    else if ( c.equals( "(" ) ) // start parameter
                    {
                        insideParameters = true;
                        builder.append( c );
                    }
                    else if ( insideParameters && c.equals( ";" ) ) // allow parameter separator
                    {
                        builder.append( c );
                    }
                    else if ( (insideParameters && c.equals( ")" )) ) // end
                    {
                        builder.append( c );
                        break;
                    }
                    else if ( c.equals( "," ) || ( c.equals( "[" ) && !insideParameters ) ) // rewind and break
                    {
                        i--;
                        break;
                    }
                }
            }
            else if ( c.equals( "," ) )
            {
                putInMap( fieldMap, joinedWithPrefix( builder, prefixList ) );
                builder = new StringBuilder();
            }
            else if ( c.equals( "[" ) || c.equals( "(" ) )
            {
                prefixList.add( builder.toString() );
                builder = new StringBuilder();
            }
            else if ( c.equals( "]" ) || c.equals( ")" ) )
            {
                if ( !builder.toString().isEmpty() )
                {
                    putInMap( fieldMap, joinedWithPrefix( builder, prefixList ) );
                }

                prefixList.remove( prefixList.size() - 1 );
                builder = new StringBuilder();
            }
            else if ( StringUtils.isAlphanumeric( c ) || c.equals( "*" ) || c.equals( ":" ) || c.equals( ";" ) || c.equals( "~" ) || c.equals( "!" )
                || c.equals( "|" ) || c.equals( "{" ) || c.equals( "}" ) )
            {
                builder.append( c );
            }
        }

        if ( !builder.toString().isEmpty() )
        {
            putInMap( fieldMap, joinedWithPrefix( builder, prefixList ) );
        }

        return fieldMap;
    }

    @Override
    public List<String> modifyFilter( Collection<String> fields, Collection<String> excludeFields )
    {
        if ( fields == null )
        {
            fields = new LinkedList<>();
        }

        return fields.stream()
            .map( s -> s.replaceAll( "]", String
                .format( ",%s]", excludeFields.toString().replaceAll( "\\[|\\]", "" ) ) ) )
            .map( s -> s.replaceAll( "\\)", String
                .format( ",%s)", excludeFields.toString().replaceAll( "\\(|\\)", "" ) ) ) )
            .collect( Collectors.toList() );
    }

    private String joinedWithPrefix( StringBuilder builder, List<String> prefixList )
    {
        String prefixes = StringUtils.join( prefixList, "." );
        prefixes = prefixes.isEmpty() ? builder.toString() : (prefixes + "." + builder.toString());
        return prefixes;
    }

    private void putInMap( FieldMap fieldMap, String path )
    {
        if ( StringUtils.isEmpty( path ) )
        {
            return;
        }

        for ( String p : path.split( "\\." ) )
        {
            fieldMap.putIfAbsent( p, new FieldMap() );
            fieldMap = fieldMap.get( p );
        }
    }
}
