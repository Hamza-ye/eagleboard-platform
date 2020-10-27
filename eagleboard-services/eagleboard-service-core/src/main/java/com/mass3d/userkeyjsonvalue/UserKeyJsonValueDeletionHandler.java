package com.mass3d.userkeyjsonvalue;

import static com.google.common.base.Preconditions.checkNotNull;

import com.mass3d.system.deletion.DeletionHandler;
import com.mass3d.user.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component("com.mass3d.userkeyjsonvalue.UserKeyJsonValueDeletionHandler")
public class UserKeyJsonValueDeletionHandler
    extends DeletionHandler {

  private final JdbcTemplate jdbcTemplate;

  public UserKeyJsonValueDeletionHandler(JdbcTemplate jdbcTemplate) {
    checkNotNull(jdbcTemplate);

    this.jdbcTemplate = jdbcTemplate;
  }

  @Override
  protected String getClassName() {
    return UserKeyJsonValue.class.getSimpleName();
  }

  @Override
  public void deleteUser(User user) {
    jdbcTemplate.execute("DELETE FROM userkeyjsonvalue WHERE userid = " + user.getId());
  }
}
