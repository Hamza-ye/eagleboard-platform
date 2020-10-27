package com.mass3d.patch;

public interface PatchService
{
  /**
   * Creates a patch by checking the differences between a source object and
   * a target object (given by PatchParams).
   *
   * @param params PatchParams instance containing source and target object
   * @return Patch containing the differences between source and target
   */
  Patch diff(PatchParams params);

  /**
   * Applies given patch on the given object.
   *
   * @param patch  Patch instance (either created manually or by using the diff function)
   * @param target Object to apply the patch to
   */
  void apply(Patch patch, Object target);
}
