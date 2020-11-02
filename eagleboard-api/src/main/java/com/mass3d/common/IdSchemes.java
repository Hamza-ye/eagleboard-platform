package com.mass3d.common;

import com.google.common.base.MoreObjects;
import com.mass3d.util.ObjectUtils;

/**
 * Identifier schemes used to map meta data. The general identifier scheme can be overridden by id
 * schemes specific to individual object types. The default id scheme is UID.
 */
public class IdSchemes {

  private IdScheme idScheme = IdScheme.UID;

  // Eagle Board modifications
  private IdScheme dataElementIdScheme;
  private IdScheme DataSetIdScheme;
  private IdScheme todoTaskIdScheme;
  private IdScheme activityIdScheme;
  private IdScheme projectIdScheme;

  private IdScheme categoryOptionComboIdScheme;

  private IdScheme categoryOptionIdScheme;

  private IdScheme orgUnitIdScheme;

  private IdScheme programIdScheme;

  private IdScheme programStageIdScheme;

  private IdScheme trackedEntityIdScheme;

  private IdScheme trackedEntityAttributeIdScheme;

  private IdScheme programStageInstanceIdScheme;

  public IdSchemes() {
  }

  public static String getValue(String uid, String code,
      IdentifiableProperty identifiableProperty) {
    return getValue(uid, code, IdScheme.from(identifiableProperty));
  }

  public static String getValue(String uid, String code, IdScheme idScheme) {
    boolean isId = idScheme.is(IdentifiableProperty.ID) || idScheme.is(IdentifiableProperty.UID);

    return isId ? uid : code;
  }

  public static String getValue(IdentifiableObject identifiableObject,
      IdentifiableProperty identifiableProperty) {
    return getValue(identifiableObject, IdScheme.from(identifiableProperty));
  }

  public static String getValue(IdentifiableObject identifiableObject, IdScheme idScheme) {
    boolean isId = idScheme.is(IdentifiableProperty.ID) || idScheme.is(IdentifiableProperty.UID);

    if (isId) {
      return identifiableObject.getUid();
    } else if (idScheme.is(IdentifiableProperty.CODE)) {
      return identifiableObject.getCode();
    } else if (idScheme.is(IdentifiableProperty.NAME)) {
      return identifiableObject.getName();
    }

    return null;
  }

  public IdScheme getScheme(IdScheme idScheme) {
    return IdScheme.from(ObjectUtils.firstNonNull(idScheme, this.idScheme));
  }

  public IdScheme getIdScheme() {
    return IdScheme.from(idScheme);
  }

  public IdSchemes setIdScheme(String idScheme) {
    this.idScheme = IdScheme.from(idScheme);
    return this;
  }

  //--------------------------------------------------------------------------
  // Eagle Board Object type id schemes
  //--------------------------------------------------------------------------
  public IdScheme getDataElementIdScheme() {
    return getScheme(dataElementIdScheme);
  }

  public IdSchemes setDataElementIdScheme(String idScheme) {
    this.dataElementIdScheme = IdScheme.from(idScheme);
    return this;
  }

  public IdScheme getDataSetIdScheme() {
    return getScheme(DataSetIdScheme);
  }

  public IdSchemes setDataSetIdScheme(String idScheme) {
    this.DataSetIdScheme = IdScheme.from(idScheme);
    return this;
  }

  public IdScheme getTodoTaskIdScheme() {
    return getScheme(todoTaskIdScheme);
  }

  public IdSchemes setTodoTaskIdScheme(String idScheme) {
    this.todoTaskIdScheme = IdScheme.from(idScheme);
    return this;
  }

  //--------------------------------------------------------------------------
  // Object type id schemes
  //--------------------------------------------------------------------------

  public IdScheme getCategoryOptionComboIdScheme() {
    return getScheme(categoryOptionComboIdScheme);
  }

  public IdSchemes setCategoryOptionComboIdScheme(String idScheme) {
    this.categoryOptionComboIdScheme = IdScheme.from(idScheme);
    return this;
  }

  public IdScheme getCategoryOptionIdScheme() {
    return getScheme(categoryOptionIdScheme);
  }

  public IdSchemes setCategoryOptionIdScheme(String idScheme) {
    this.categoryOptionIdScheme = IdScheme.from(idScheme);
    return this;
  }

  public IdScheme getOrgUnitIdScheme() {
    return getScheme(orgUnitIdScheme);
  }

  public IdSchemes setOrgUnitIdScheme(String idScheme) {
    this.orgUnitIdScheme = IdScheme.from(idScheme);
    return this;
  }

  public IdScheme getProgramIdScheme() {
    return getScheme(programIdScheme);
  }

  public IdSchemes setProgramIdScheme(String idScheme) {
    this.programIdScheme = IdScheme.from(idScheme);
    return this;
  }

  public IdScheme getProgramStageIdScheme() {
    return getScheme(programStageIdScheme);
  }

  public IdSchemes setProgramStageIdScheme(String idScheme) {
    this.programStageIdScheme = IdScheme.from(idScheme);
    return this;
  }

  public IdScheme getProgramStageInstanceIdScheme() {
    return getScheme(programStageInstanceIdScheme);
  }

  public IdSchemes setProgramStageInstanceIdScheme(String idScheme) {
    this.programStageInstanceIdScheme = IdScheme.from(idScheme);
    return this;
  }

  public IdScheme getTrackedEntityIdScheme() {
    return getScheme(trackedEntityIdScheme);
  }

  public IdSchemes setTrackedEntityIdScheme(String idScheme) {
    this.trackedEntityIdScheme = IdScheme.from(idScheme);
    return this;
  }

  public IdScheme getTrackedEntityAttributeIdScheme() {
    return getScheme(trackedEntityAttributeIdScheme);
  }

  public IdSchemes setTrackedEntityAttributeIdScheme(String idScheme) {
    this.trackedEntityAttributeIdScheme = IdScheme.from(idScheme);
    return this;
  }

  //--------------------------------------------------------------------------
  // Get value methods
  //--------------------------------------------------------------------------

  public IdScheme getActivityIdScheme() {
    return getScheme(activityIdScheme);
  }

  public IdSchemes setActivityIdScheme(String idScheme) {
    this.activityIdScheme = IdScheme.from(idScheme);
    return this;
  }

  public IdScheme getProjectIdScheme() {
    return getScheme(projectIdScheme);
  }

  public IdSchemes setProjectIdScheme(String idScheme) {
    this.projectIdScheme = IdScheme.from(idScheme);
    return this;
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("idScheme", idScheme)
        .add("dataElementIdScheme", dataElementIdScheme)
        .add("DataSetIdScheme", DataSetIdScheme)
        .add("todoTaskIdScheme", todoTaskIdScheme)
        .add("activityIdScheme", activityIdScheme)
        .add("projectIdScheme", projectIdScheme)

        .add("idScheme", idScheme)
        .add("categoryOptionComboIdScheme", categoryOptionComboIdScheme)
        .add("categoryOptionIdScheme", categoryOptionIdScheme)
        .add("orgUnitIdScheme", orgUnitIdScheme)
        .add("programIdScheme", programIdScheme)
        .add("programStageIdScheme", programStageIdScheme)
        .add("trackedEntityIdScheme", trackedEntityIdScheme)
        .add("trackedEntityAttributeIdScheme", trackedEntityAttributeIdScheme)
        .add("programStageInstanceIdScheme", programStageInstanceIdScheme)
        .toString();
  }
}
