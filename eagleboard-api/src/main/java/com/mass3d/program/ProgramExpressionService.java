package com.mass3d.program;

import java.util.Collection;
import com.mass3d.dataelement.DataElement;

/**
 * An Expression is the expression of e.g. a validation rule of a program. It
 * consist of a String representation of the rule as well as references to the
 * data elements and program stages included in the expression.
 * <p/>
 * The expression contains references to data elements and program stages on the
 * form:
 * <p/>
 * i) [DE:1.2] where 1 refers to the program stage identifier and 2 refers to
 * the data element identifier.
 * <p/>
 *
 * @version ProgramExpressionService.java 2:59:58 PM Nov 8, 2012 $
 */
public interface ProgramExpressionService
{
    String ID = ProgramExpressionService.class.getName();

    String INVALID_CONDITION = "Expression is not well-formed";
    
    /**
     * Adds an {@link ProgramExpression}
     *
     * @param programExpression The to ProgramExpression add.
     * @return A generated unique id of the added {@link ProgramExpression}.
     */
    long addProgramExpression(ProgramExpression programExpression);

    /**
     * Updates an {@link ProgramExpression}.
     *
     * @param programExpression the ProgramExpression to update.
     */
    void updateProgramExpression(ProgramExpression programExpression);

    /**
     * Deletes a {@link ProgramExpression}.
     *
     * @param programExpression the ProgramExpression to delete.
     */
    void deleteProgramExpression(ProgramExpression programExpression);

    /**
     * Returns a {@link ProgramExpression}.
     *
     * @param id the id of the ProgramExpression to return.
     * @return the ProgramExpression with the given id
     */
    ProgramExpression getProgramExpression(long id);

    /**
     * Get the description of a program expression
     *
     * @param programExpression The expression
     * @return the description of an expression
     */
    String getExpressionDescription(String programExpression);

    /**
     * Get the Data Element collection of a program expression
     *
     * @param programExpression The expression
     * @return the DataElement collection
     */
    Collection<DataElement> getDataElements(String programExpression);

}
