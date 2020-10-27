package com.mass3d.common;

import com.mass3d.user.User;
import java.util.Set;

public interface SubscribableObject
    extends IdentifiableObject
{
  Set<String> getSubscribers();

  boolean isSubscribed();

  boolean subscribe(User user);

  boolean unsubscribe(User user);
}
