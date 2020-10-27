package com.mass3d;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation is used within a Docker-based integration test to inject data into the
 * Dockerized Postgresql database.
 * The annotation expects a "path" property to point to the actual SQL file containing the INSERT/UPDATE/DELETE
 * statements to run prior to each test. The file must be present in the classpath (e.g. src/main/resources/)
 * The data file is going to be injected only once per each test class.
 *
 * <pre>{@code
 *
 * @org.junit.experimental.categories.Category( IntegrationTest.class )
 * @IntegrationTestData(path = "sql/mydata.sql")
 * public class DefaultTrackedEntityInstanceStoreTest
 *     extends
 *     IntegrationTestBase
 * {
 *   ...
 * }
 * }</pre>
 *
 *
 */
@Retention( RetentionPolicy.RUNTIME )
@Target( ElementType.TYPE )
public @interface IntegrationTestData
{
    String path();
}