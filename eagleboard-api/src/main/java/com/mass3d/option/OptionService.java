package com.mass3d.option;

import java.util.List;

public interface OptionService
{
    String ID = OptionService.class.getName();

    // -------------------------------------------------------------------------
    // OptionSet
    // -------------------------------------------------------------------------

    long saveOptionSet(OptionSet optionSet);

    void updateOptionSet(OptionSet optionSet);

    OptionSet getOptionSet(long id);

    OptionSet getOptionSet(String uid);

    OptionSet getOptionSetByName(String name);

    OptionSet getOptionSetByCode(String code);

    void deleteOptionSet(OptionSet optionSet);

    List<OptionSet> getAllOptionSets();

    List<Option> getOptions(long optionSetId, String name, Integer max);

    // -------------------------------------------------------------------------
    // Option
    // -------------------------------------------------------------------------

    void updateOption(Option option);

    Option getOption(long id);

    Option getOptionByCode(String code);

    void deleteOption(Option option);

    // -------------------------------------------------------------------------
    // OptionGroup
    // -------------------------------------------------------------------------

    long saveOptionGroup(OptionGroup group);

    void updateOptionGroup(OptionGroup group);

    OptionGroup getOptionGroup(long id);

    OptionGroup getOptionGroup(String uid);

    void deleteOptionGroup(OptionGroup group);

    List<OptionGroup> getAllOptionGroups();

    // -------------------------------------------------------------------------
    // OptionGroupSet
    // -------------------------------------------------------------------------

    long saveOptionGroupSet(OptionGroupSet group);

    void updateOptionGroupSet(OptionGroupSet group);

    OptionGroupSet getOptionGroupSet(long id);

    OptionGroupSet getOptionGroupSet(String uid);

    void deleteOptionGroupSet(OptionGroupSet group);

    List<OptionGroupSet> getAllOptionGroupSets();
}
