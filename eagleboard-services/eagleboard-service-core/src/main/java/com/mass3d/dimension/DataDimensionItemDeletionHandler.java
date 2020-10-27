package com.mass3d.dimension;

import static com.google.common.base.Preconditions.checkNotNull;

import com.mass3d.common.DataDimensionItem;
import com.mass3d.system.deletion.DeletionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component("com.mass3d.dimension.DataDimensionItemDeletionHandler")
public class DataDimensionItemDeletionHandler
    extends DeletionHandler {
  // -------------------------------------------------------------------------
  // Dependencies
  // -------------------------------------------------------------------------

  private final JdbcTemplate jdbcTemplate;

  public DataDimensionItemDeletionHandler( JdbcTemplate jdbcTemplate )
  {
    checkNotNull( jdbcTemplate );
    this.jdbcTemplate = jdbcTemplate;
  }

  // -------------------------------------------------------------------------
  // DeletionHandler implementation
  // -------------------------------------------------------------------------

  @Override
  protected String getClassName()
  {
    return DataDimensionItem.class.getSimpleName();
  }

//  @Override
//  public String allowDeleteCategoryOptionCombo( CategoryOptionCombo optionCombo )
//  {
//    String sql = "SELECT COUNT(*) FROM datadimensionitem where dataelementoperand_categoryoptioncomboid="
//        + optionCombo.getId();
//
//    return jdbcTemplate.queryForObject( sql, Integer.class ) == 0 ? null : ERROR;
//  }
}
