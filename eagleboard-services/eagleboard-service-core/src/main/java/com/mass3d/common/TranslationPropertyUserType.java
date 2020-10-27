package com.mass3d.common;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import com.mass3d.hibernate.EnumUserType;
import com.mass3d.translation.TranslationProperty;

public class TranslationPropertyUserType
    extends EnumUserType<TranslationProperty> {

  public TranslationPropertyUserType() {
    super(TranslationProperty.class);
  }

  @Override
  public Object nullSafeGet(ResultSet resultSet, String[] names,
      SharedSessionContractImplementor impl, Object owner)
      throws HibernateException, SQLException {
    String name = resultSet.getString(names[0]);
    TranslationProperty result = null;
    if (!resultSet.wasNull()) {
      result = TranslationProperty.fromValue(name);
    }
    return result;
  }
}
