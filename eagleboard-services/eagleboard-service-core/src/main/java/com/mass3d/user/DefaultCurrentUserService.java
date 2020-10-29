package com.mass3d.user;

import static com.google.common.base.Preconditions.checkNotNull;

import com.mass3d.cache.Cache;
import com.mass3d.cache.CacheProvider;
import com.mass3d.commons.util.SystemUtils;
import com.mass3d.security.spring.AbstractSpringSecurityCurrentUserService;
import java.util.concurrent.TimeUnit;
import javax.annotation.PostConstruct;
import org.hibernate.Hibernate;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.env.Environment;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service for retrieving information about the currently authenticated user.
 * <p>
 * Note that most methods are transactional, except for retrieving current UserInfo.
 */
@Service("com.mass3d.user.CurrentUserService")
public class DefaultCurrentUserService
    extends AbstractSpringSecurityCurrentUserService {

  /**
   * Cache for user IDs. Key is username. Disabled during test phase. Take care not to cache user
   * info which might change during runtime.
   */
  private static Cache<Long> USERNAME_ID_CACHE;

  // -------------------------------------------------------------------------
  // Dependencies
  // -------------------------------------------------------------------------

  private final UserStore userStore;

  private final Environment env;

  private final CacheProvider cacheProvider;

  private final SessionRegistry sessionRegistry;

  public DefaultCurrentUserService(Environment env, CacheProvider cacheProvider,
      @Lazy SessionRegistry sessionRegistry, @Lazy UserStore userStore) {
    checkNotNull(env);
    checkNotNull(cacheProvider);
    checkNotNull(sessionRegistry);
    checkNotNull(userStore);

    this.env = env;
    this.cacheProvider = cacheProvider;
    this.sessionRegistry = sessionRegistry;
    this.userStore = userStore;
  }

  // -------------------------------------------------------------------------
  // CurrentUserService implementation
  // -------------------------------------------------------------------------

  @PostConstruct
  public void init() {
    USERNAME_ID_CACHE = cacheProvider.newCacheBuilder(Long.class)
        .forRegion("userIdCache")
        .expireAfterAccess(1, TimeUnit.HOURS)
        .withInitialCapacity(200)
        .forceInMemory()
        .withMaximumSize(SystemUtils.isTestRun(env.getActiveProfiles()) ? 0 : 4000)
        .build();
  }

  @Override
  @Transactional(readOnly = true)
  public User getCurrentUser() {
    String username = getCurrentUsername();

    if (username == null) {
      return null;
    }

    Long userId = USERNAME_ID_CACHE.get(username, this::getUserId).orElse(null);

    if (userId == null) {
      return null;
    }

    User user = userStore.getUser(userId);

    // TODO: this is pretty ugly way to retrieve auths
//    Hibernate.initialize(user.getUserCredentials().getAllAuthorities());
    user.getUserCredentials().getAllAuthorities();
    return user;
  }

  @Override
  @Transactional(readOnly = true)
  public UserInfo getCurrentUserInfo() {
    String currentUsername = getCurrentUsername();

    if (currentUsername == null) {
      return null;
    }

    Long userId = USERNAME_ID_CACHE.get(currentUsername, this::getUserId).orElse(null);

    if (userId == null) {
      return null;
    }

    return new UserInfo(userId, currentUsername, getCurrentUserAuthorities());
  }

  private Long getUserId(String username) {
    UserCredentials credentials = userStore.getUserCredentialsByUsername(username);

    return credentials != null ? credentials.getId() : null;
  }

  @Override
  @Transactional(readOnly = true)
  public boolean currentUserIsSuper() {
    User user = getCurrentUser();

    return user != null && user.isSuper();
  }

//    @Override
//    @Transactional( readOnly = true )
//    public Set<OrganisationUnit> getCurrentUserOrganisationUnits()
//    {
//        User user = getCurrentUser();
//
//        return user != null ? new HashSet<>( user.getTodoTasks() ) : new HashSet<>();
//    }

  @Override
  @Transactional(readOnly = true)
  public boolean currentUserIsAuthorized(String auth) {
    User user = getCurrentUser();

    return user != null && user.getUserCredentials().isAuthorized(auth);
  }

  @Override
  public UserCredentials getCurrentUserCredentials() {
    return userStore.getUserCredentialsByUsername(getCurrentUsername());
  }
}
