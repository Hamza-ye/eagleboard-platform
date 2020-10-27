package com.mass3d.datavalue;

import com.mass3d.dataelement.DataElement;
import java.util.List;
import com.mass3d.common.AuditType;
import com.mass3d.todotask.TodoTask;
import com.mass3d.period.Period;

public interface DataValueAuditService {

  String ID = DataValueAuditService.class.getName();

  /**
   * Adds a DataValueAudit.
   *
   * @param dataValueAudit the DataValueAudit to add.
   */
  void addDataValueAudit(DataValueAudit dataValueAudit);

  /**
   * Deletes all data value audits for the given todoTask.
   *
   * @param todoTask the todoTask.
   */
  void deleteDataValueAudits(TodoTask todoTask);

  /**
   * Deletes all data value audits for the given data field.
   *
   * @param dataElement the data field.
   */
  void deleteDataValueAudits(DataElement dataElement);

  /**
   * Returns all DataValueAudits for the given DataValue.
   *
   * @param dataValue the DataValue to get DataValueAudits for.
   * @return a list of DataValueAudits which match the given DataValue, or an empty collection if
   * there are no matches.
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
}