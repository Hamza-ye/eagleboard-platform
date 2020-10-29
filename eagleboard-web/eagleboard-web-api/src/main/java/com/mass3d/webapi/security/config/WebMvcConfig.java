package com.mass3d.webapi.security.config;

import com.google.common.collect.ImmutableMap;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import com.mass3d.common.Compression;
import com.mass3d.node.DefaultNodeService;
import com.mass3d.node.NodeService;
import com.mass3d.webapi.mvc.CurrentUserHandlerMethodArgumentResolver;
import com.mass3d.webapi.mvc.CurrentUserInfoHandlerMethodArgumentResolver;
import com.mass3d.webapi.mvc.CustomRequestMappingHandlerMapping;
import com.mass3d.webapi.mvc.DhisApiVersionHandlerMethodArgumentResolver;
import com.mass3d.webapi.mvc.messageconverter.CsvMessageConverter;
import com.mass3d.webapi.mvc.messageconverter.ExcelMessageConverter;
import com.mass3d.webapi.mvc.messageconverter.JsonMessageConverter;
import com.mass3d.webapi.mvc.messageconverter.JsonPMessageConverter;
import com.mass3d.webapi.mvc.messageconverter.PdfMessageConverter;
import com.mass3d.webapi.mvc.messageconverter.RenderServiceMessageConverter;
import com.mass3d.webapi.mvc.messageconverter.XmlMessageConverter;
import com.mass3d.webapi.service.ContextService;
import com.mass3d.webapi.view.CustomPathExtensionContentNegotiationStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Primary;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.web.accept.ContentNegotiationManager;
import org.springframework.web.accept.FixedContentNegotiationStrategy;
import org.springframework.web.accept.HeaderContentNegotiationStrategy;
import org.springframework.web.accept.ParameterContentNegotiationStrategy;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.DelegatingWebMvcConfiguration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.http.MediaType.parseMediaType;

@Configuration
@Order( 1000 )
@ComponentScan( basePackages = { "com.mass3d" } )
@EnableGlobalMethodSecurity( prePostEnabled = true )
@EnableWebMvc
@Slf4j
public class WebMvcConfig extends DelegatingWebMvcConfiguration
{
    @Autowired
    public CurrentUserHandlerMethodArgumentResolver currentUserHandlerMethodArgumentResolver;

    @Autowired
    public CurrentUserInfoHandlerMethodArgumentResolver currentUserInfoHandlerMethodArgumentResolver;

    @Autowired
    private ContextService contextService;

//    @Autowired
//    DhisApiVersionHandlerMethodArgumentResolver dhisApiVersionHandlerMethodArgumentResolver;

//    @Autowired
//    NodeService nodeService;

    @Bean( "multipartResolver" )
    public MultipartResolver multipartResolver()
    {
        return new CommonsMultipartResolver();
    }

    @Bean
    public DhisApiVersionHandlerMethodArgumentResolver dhisApiVersionHandlerMethodArgumentResolver()
    {
        return new DhisApiVersionHandlerMethodArgumentResolver();
    }

    @Override
    public void addArgumentResolvers( List<HandlerMethodArgumentResolver> resolvers )
    {
        resolvers.add( dhisApiVersionHandlerMethodArgumentResolver() );
        resolvers.add( currentUserHandlerMethodArgumentResolver );
        resolvers.add( currentUserInfoHandlerMethodArgumentResolver );
    }

    @Bean
    public MethodSecurityExpressionHandler methodSecurityExpressionHandler()
    {
        DefaultMethodSecurityExpressionHandler expressionHandler = new DefaultMethodSecurityExpressionHandler();
        expressionHandler.setDefaultRolePrefix( "" );
        return expressionHandler;
    }

    @Bean
    public NodeService nodeService()
    {
        return new DefaultNodeService();
    }

    @Bean
    public RenderServiceMessageConverter renderServiceMessageConverter()
    {
        return new RenderServiceMessageConverter();
    }

    @Override
    public void configureMessageConverters(
        List<HttpMessageConverter<?>> converters )
    {
        Arrays.stream( Compression.values() )
            .forEach( compression -> converters.add( new JsonMessageConverter( nodeService(), compression ) ) );
        Arrays.stream( Compression.values() )
            .forEach( compression -> converters.add( new XmlMessageConverter( nodeService(), compression ) ) );
        Arrays.stream( Compression.values() )
            .forEach( compression -> converters.add( new CsvMessageConverter( nodeService(), compression ) ) );

        converters.add( new JsonPMessageConverter( nodeService(), contextService ) );
        converters.add( new PdfMessageConverter( nodeService() ) );
        converters.add( new ExcelMessageConverter( nodeService() ) );

        converters.add( new StringHttpMessageConverter() );
        converters.add( new ByteArrayHttpMessageConverter() );
        converters.add( new FormHttpMessageConverter() );

        converters.add( renderServiceMessageConverter() );
    }

    @Primary
    @Bean
    @Override
    public ContentNegotiationManager mvcContentNegotiationManager()
    {
        CustomPathExtensionContentNegotiationStrategy pathExtensionNegotiationStrategy =
            new CustomPathExtensionContentNegotiationStrategy( mediaTypeMap );
        pathExtensionNegotiationStrategy.setUseJaf( false );

        String[] mediaTypes = new String[] { "json", "jsonp", "xml", "png", "xls", "pdf", "csv" };

        ParameterContentNegotiationStrategy parameterContentNegotiationStrategy = new ParameterContentNegotiationStrategy(
            mediaTypeMap.entrySet().stream()
                .filter( x -> ArrayUtils.contains( mediaTypes, x.getKey() ) )
                .collect( Collectors.toMap( Map.Entry::getKey, Map.Entry::getValue ) ) );

        HeaderContentNegotiationStrategy headerContentNegotiationStrategy = new HeaderContentNegotiationStrategy();
        FixedContentNegotiationStrategy fixedContentNegotiationStrategy = new FixedContentNegotiationStrategy(
            MediaType.APPLICATION_JSON );

        return new ContentNegotiationManager(
            Arrays.asList(
                pathExtensionNegotiationStrategy,
                parameterContentNegotiationStrategy,
                headerContentNegotiationStrategy,
                headerContentNegotiationStrategy,
                fixedContentNegotiationStrategy ) );
    }

    @Override
    protected RequestMappingHandlerMapping createRequestMappingHandlerMapping()
    {
        CustomRequestMappingHandlerMapping mapping = new CustomRequestMappingHandlerMapping();
        mapping.setOrder( 0 );
        mapping.setContentNegotiationManager( mvcContentNegotiationManager() );
        return mapping;
    }

    private Map<String, MediaType> mediaTypeMap = new ImmutableMap.Builder<String, MediaType>()
        .put( "json", MediaType.APPLICATION_JSON )
        .put( "json.gz", parseMediaType( "application/json+gzip" ) )
        .put( "json.zip", parseMediaType( "application/json+zip" ) )
        .put( "jsonp", parseMediaType( "application/javascript" ) )
        .put( "xml", MediaType.APPLICATION_XML )
        .put( "xml.gz", parseMediaType( "application/xml+gzip" ) )
        .put( "xml.zip", parseMediaType( "application/xml+zip" ) )
        .put( "png", MediaType.IMAGE_PNG )
        .put( "pdf", MediaType.APPLICATION_PDF )
        .put( "xls", parseMediaType( "application/vnd.ms-excel" ) )
        .put( "xlsx", parseMediaType( "application/vnd.ms-excel" ) )
        .put( "csv", parseMediaType( "application/csv" ) )
        .put( "csv.gz", parseMediaType( "application/csv+gzip" ) )
        .put( "csv.zip", parseMediaType( "application/csv+zip" ) )
        .put( "geojson", parseMediaType( "application/json+geojson" ) )
        .build();
}