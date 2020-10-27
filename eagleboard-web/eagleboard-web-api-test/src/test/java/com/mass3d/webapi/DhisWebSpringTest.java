package com.mass3d.webapi;

import com.mass3d.DhisConvenienceTest;
import com.mass3d.common.IdentifiableObjectManager;
import com.mass3d.render.RenderService;
import com.mass3d.schema.SchemaService;
import com.mass3d.user.User;
import com.mass3d.user.UserService;
import com.mass3d.webapi.mvc.interceptor.TranslationInterceptor;
import org.junit.Before;
import org.junit.Rule;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.snippet.Snippet;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.filter.ShallowEtagHeaderFilter;

import java.lang.reflect.Method;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;

@RunWith( SpringRunner.class )
@WebAppConfiguration
@ContextConfiguration( classes = { MvcTestConfig.class, WebTestConfiguration.class } )
@ActiveProfiles( "test-h2" )
@Transactional
public abstract class DhisWebSpringTest
    extends DhisConvenienceTest
{
    // MvcTestConfig.class,
//    @Autowired
//    protected FilterChainProxy filterChainProxy;

    @Autowired
    protected WebApplicationContext webApplicationContext;

    @Autowired
    protected IdentifiableObjectManager manager;

    @Autowired
    protected RenderService renderService;

    @Autowired
    protected UserService _userService;

    protected MockMvc mvc;

    @Autowired
    protected SchemaService schemaService;

    @Rule
    public JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation( "target/generated-snippets" );


    @Before
    public void setup()
        throws Exception
    {

        userService = _userService;
        CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();
        characterEncodingFilter.setEncoding( "UTF-8" );
        characterEncodingFilter.setForceEncoding( true );
        mvc = MockMvcBuilders.webAppContextSetup( webApplicationContext )
//            .addFilters( characterEncodingFilter, new ShallowEtagHeaderFilter(), filterChainProxy )

            .apply( documentationConfiguration( this.restDocumentation ) )

            .build();

        executeStartupRoutines();

        setUpTest();
    }

    protected void setUpTest()
        throws Exception
    {
    }

    // -------------------------------------------------------------------------
    // Supportive methods
    // -------------------------------------------------------------------------

    public MockHttpSession getSession( String... authorities )
    {
        SecurityContextHolder.getContext().setAuthentication( getPrincipal( authorities ) );
        MockHttpSession session = new MockHttpSession();

        session.setAttribute(
            HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
            SecurityContextHolder.getContext() );

        return session;
    }

    protected UsernamePasswordAuthenticationToken getPrincipal( String... authorities )
    {
        User user = createAdminUser( authorities );
        List<GrantedAuthority> grantedAuthorities = user.getUserCredentials().getAllAuthorities()
            .stream().map( SimpleGrantedAuthority::new ).collect( Collectors.toList() );

        UserDetails userDetails = new org.springframework.security.core.userdetails.User(
            user.getUserCredentials().getUsername(), user.getUserCredentials().getPassword(), grantedAuthorities );

        return new UsernamePasswordAuthenticationToken(
            userDetails,
            userDetails.getPassword(),
            userDetails.getAuthorities()
        );
    }

    private void executeStartupRoutines()
        throws Exception
    {
        String id = "com.mass3d.system.startup.StartupRoutineExecutor";

        if ( webApplicationContext.containsBean( id ) )
        {
            Object object = webApplicationContext.getBean( id );

            Method method = object.getClass().getMethod( "executeForTesting", new Class[0] );

            method.invoke( object, new Object[0] );
        }
    }

    public RestDocumentationResultHandler documentPrettyPrint( String useCase, Snippet... snippets )
    {
        return document( useCase, preprocessRequest( prettyPrint() ), preprocessResponse( prettyPrint() ), snippets );
    }

    public SchemaService getSchemaService()
    {
        return schemaService;
    }

    public MockMvc getMvc()
    {
        return mvc;
    }

    public IdentifiableObjectManager getManager()
    {
        return manager;
    }
}
