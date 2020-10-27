package com.mass3d.datavalue;

import com.mass3d.dataelement.DataElement;
import java.util.List;
import com.mass3d.common.AuditType;
import com.mass3d.todotask.TodoTask;
import com.mass3d.period.Period;

public interface DataValueAuditStore {

  String ID = DataValueAuditStore.class.getName();

  /**
   * Adds a DataValueAudit.
   *
   * @param dataValueAudit the DataValueAudit to add.
   */
  void addDataValueAudit(DataValueAudit dataValueAudit);

  /**
   * Deletes all data value audits for the given todoTask.
   *
   * @param todoTask the organisation unit.
   */
  void deleteDataValueAudits(TodoTask todoTask);

  /**
   * Deletes all data value audits for the given data field.
   *
   * @param dataElement the data element.
   */
  void deleteDataValueAudits(DataElement dataElement);

//    /**
//     * Deletes all data value audits for the given organisation unit.
//     *
//     * @param organisationUnit the organisation unit.
//     */
//    void deleteDataValueAudits(OrganisationUnit organisationUnit);
//
//    /**
//     * Deletes all data value audits for the given data element.
//     *
//     * @param dataElement the data element.
//     */
//    void deleteDataValueAudits(DataElement dataElement);

  /**
   * Returns all DataValueAudits which match the DataElement, Period, OrganisationUnit and
   * CategoryOptionCombo of the given DataValue.
   *
   * @param dataValue the DataValue to get DataValueAudits for.
   * @return a list of DataValueAudits which match the DataElement Period, OrganisationUnit and
   * CategoryOptionCombo of the given DataValue, or an empty list if no DataValueAudits match.
   */
  List<DataValueAudit> getDataValueAudits(DataValue dataValue);

  /**
   * Returns all DataValueAudits which match the given DataElement, Period, OrganisationUnit and
   * CategoryOptionCombo.
   *
   * @param dataElements the DataElement of the DataValueAudits.
   * @param periods the Period of the DataValueAudits.
   * @param todoTasks the todoTask of the DataValueAudits.
   * @return a list of DataValueAudits which match the given DataElement, Period, OrganisationUnit
   * and CategoryOptionCombo, or an empty list if no DataValueAudits match.
   */
  List<DataValueAudit> getDataValueAudits(List<DataElement> dataElements, List<Period> periods,
      List<TodoTask> todoTasks, AuditType auditType);

  List<DataValueAudit> getDataValueAudits(List<DataElement> dataElements, List<Period> periods,
      List<TodoTask> todoTasks, AuditType auditType, int first, int max);

  int countDataValueAudits(List<DataElement> dataElements, List<Period> periods,
      List<TodoTask> todoTasks, AuditType auditType);

//    /**
//     * Returns all DataValueAudits which match the given DataElement, Period,
//     * OrganisationUnit and CategoryOptionCombo.
//     *
//     * @param dataElements         the DataElement of the DataValueAudits.
//     * @param periods              the Period of the DataValueAudits.
//     * @param organisationUnits    the OrganisationUnit of the DataValueAudits.
//     * @param categoryOptionCombo  the CategoryOptionCombo of the DataValueAudits.
//     * @param attributeOptionCombo the attribute option combo.
//     * @return a list of DataValueAudits which match the given DataElement, Period,
//     * OrganisationUnit and CategoryOptionCombo, or an empty list
//     * if no DataValueAudits match.
//     */
//    List<DataValueAudit> getDataValueAudits(List<DataElement> dataElements, List<Period> periods,
//        List<OrganisationUnit> organisationUnits,
//        CategoryOptionCombo categoryOptionCombo, CategoryOptionCombo attributeOptionCombo,
//        AuditType auditType);
//
//    List<DataValueAudit> getDataValueAudits(List<DataElement> dataElements, List<Period> periods,
//        List<OrganisationUnit> organisationUnits,
//        CategoryOptionCombo categoryOptionCombo, CategoryOptionCombo attributeOptionCombo,
//        AuditType auditType, int first, int max);
//
//    int countDataValueAudits(List<DataElement> dataElements, List<Period> periods,
//        List<OrganisationUnit> organisationUnits,
//        CategoryOptionCombo categoryOptionCombo, CategoryOptionCombo attributeOptionCombo,
//        AuditType auditType);
}
