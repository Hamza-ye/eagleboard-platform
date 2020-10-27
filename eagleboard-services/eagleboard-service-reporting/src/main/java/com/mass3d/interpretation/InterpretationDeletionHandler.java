package com.mass3d.interpretation;

import java.util.List;
import com.mass3d.system.deletion.DeletionHandler;
import com.mass3d.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component( "com.mass3d.interpretation.InterpretationDeletionHandler" )
public class InterpretationDeletionHandler
    extends DeletionHandler {

  private InterpretationService interpretationService;

  @Override
  protected String getClassName() {
    return Interpretation.class.getSimpleName();
  }

  @Override
  public void deleteUser(User user) {
    List<Interpretation> interpretations = interpretationService.getInterpretations();

    for (Interpretation interpretation : interpretations) {
      if (interpretation.getUser() != null && interpretation.getUser().equals(user)) {
        interpretation.setUser(null);
        interpretationService.updateInterpretation(interpretation);
      }
    }
  }

//    @Override
//    public String allowDeleteMap( Map map )
//    {
//        return interpretationService.countMapInterpretations( map ) == 0 ? null : ERROR;
//    }
//
//    @Override
//    public String allowDeleteChart( Chart chart )
//    {
//        return interpretationService.countChartInterpretations( chart ) == 0 ? null : ERROR;
//    }
//
//    @Override
//    public String allowDeleteReportTable( ReportTable reportTable )
//    {
//        return interpretationService.countReportTableInterpretations( reportTable ) == 0 ? null : ERROR;
//    }
}
