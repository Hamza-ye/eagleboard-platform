package com.mass3d.dataset;

import java.util.List;

public interface SectionService
{
    String ID = SectionService.class.getName();
    
    /**
     * Adds a Section.
     * 
     * @param section the Section to add.
     * @return the generated identifier.
     */
    long addSection(Section section);

    /**
     * Updates a Section.
     *
     * @param section the Section to update.
     */
    void updateSection(Section section);

    /**
     * Deletes a Section.
     *
     * @param section the Section to delete.
     */
    void deleteSection(Section section);

    /**
     * Retrieves the Section with the given identifier.
     *
     * @param id the identifier of the Section to retrieve.
     * @return the Section.
     */
    Section getSection(long id);

    /**
     * Retrieves the Section with the given identifier (uid).
     *
     * @param uid the identifier of the Section to retrieve.
     * @return the Section.
     */
    Section getSection(String uid);

    /**
     * Retrieves the Section with the given name.
     *
     * @param name the name of the Section to retrieve.
     * @return the Section.
     */
    Section getSectionByName(String name, Integer dataSetId);
    
    /**
     * Retrieves all Sections.
     * 
     * @return a Collection of Sections.
     */
    List<Section> getAllSections();
    
}
