package com.mass3d.webapi.service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface ContextService
{
    /**
     * Get full path of servlet.
     *
     * @return Full HREF to servlet
     * @see HttpServletRequest
     */
    String getServletPath();

    /**
     * Get HREF to context.
     *
     * @return Full HREF to context (context root)
     * @see HttpServletRequest
     */
    String getContextPath();

    /**
     * Get HREF to Web-API.
     *
     * @return Full HREF to Web-API
     * @see HttpServletRequest
     */
    String getApiPath();

    /**
     * Get active HttpServletRequest
     *
     * @return HttpServletRequest
     */
    HttpServletRequest getRequest();

    /**
     * Returns a list of values from a parameter, if the parameter doesn't exist, it will
     * return a empty list.
     *
     * @param name Parameter to get
     * @return List of parameter values, or empty if not found
     */
    Set<String> getParameterValues(String name);

    /**
     * Get all parameters as a map of key => values, supports more than one pr key (so values is a collection)
     */
    Map<String, List<String>> getParameterValuesMap();
}
