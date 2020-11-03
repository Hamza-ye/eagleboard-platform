package com.mass3d.program;

import com.mass3d.event.EventStatus;
import com.mass3d.hibernate.EnumUserType;

public class EventStatusUserType
    extends EnumUserType<EventStatus>
{
  public  EventStatusUserType()
  {
      super(  EventStatus.class );
  }
}
