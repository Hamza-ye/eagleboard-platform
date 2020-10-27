package com.mass3d.interpretation;

import com.mass3d.user.User;
import com.mass3d.user.UserCredentials;
import com.mass3d.user.UserService;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class MentionUtils {

  public static List<Mention> convertUsersToMentions(Set<User> users) {
    List<Mention> mentions = new ArrayList<Mention>();
    for (User user : users) {
      Mention mention = new Mention();
      mention.setCreated(new Date());
      mention.setUsername(user.getUsername());
      mentions.add(mention);
    }
    return mentions;
  }

  public static Set<User> getMentionedUsers(String text, UserService userService) {
    Set<User> users = new HashSet<>();
    Matcher matcher = Pattern.compile("(?:\\s|^)@([\\w+._-]+)").matcher(text);
    while (matcher.find()) {
      String username = matcher.group(1);
      UserCredentials userCredentials = userService.getUserCredentialsByUsername(username);
      if (userCredentials != null) {
        users.add(userCredentials.getUserInfo());
      }
    }
    return users;
  }

  public static List<String> removeCustomFilters(List<String> filters) {
    List<String> mentions = new ArrayList<String>();
    ListIterator<String> filterIterator = filters.listIterator();
    while (filterIterator.hasNext()) {
      String[] filterSplit = filterIterator.next().split(":");
      if (filterSplit[1].equals("in") && filterSplit[0].equals("mentions")) {
        mentions.add(filterSplit[2]);
        filterIterator.remove();
      }
    }
    return mentions;
  }
}
