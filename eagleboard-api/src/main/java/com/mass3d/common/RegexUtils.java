package com.mass3d.common;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexUtils
{
    /**
     * Return the matches in the given input based on the given pattern and group number.
     * 
     * @param pattern the pattern.
     * @param input the input. If the input is null, an empty set is returned.
     * @param group the group, can be null.
     * @return a set of matches.
     */
    public static Set<String> getMatches( Pattern pattern, String input, Integer group )
    {
        group = group != null ? group : 0;
        
        Set<String> set = new HashSet<>();
        
        if ( input != null )
        {
            Matcher matcher = pattern.matcher( input );
            
            while ( matcher.find() )
            {
                set.add( matcher.group( group ) );
            }
        }
        
        return set;
    }
    
    /**
     * Return the matches in the given input based on the given pattern and group name.
     * 
     * @param pattern the pattern.
     * @param input the input.
     * @param groupName the group name, not null.
     * @return a set of matches.
     */
    public static Set<String> getMatches( Pattern pattern, String input, String groupName )
    {
        Set<String> set = new HashSet<>();
        
        Matcher matcher = pattern.matcher( input );
        
        while ( matcher.find() )
        {
            set.add( matcher.group( groupName ) );
        }
        
        return set;
    }
}
