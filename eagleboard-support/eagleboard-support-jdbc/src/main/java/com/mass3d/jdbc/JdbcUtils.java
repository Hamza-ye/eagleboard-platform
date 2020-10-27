package com.mass3d.jdbc;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.jdbc.core.ColumnMapRowMapper;
import org.springframework.jdbc.core.InterruptibleBatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ParameterDisposer;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.RowMapperResultSetExtractor;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

/**
 * JDBC Utility methods
 */
public class JdbcUtils
{
    /**
     * Executes a JDBC batch update using the provided {@see JdbcTemplate}
     *
     * @param jdbcTemplate a JdbcTemplate
     * @param sql the SQL string to be executed
     * @param pss a {@see BatchPreparedStatementSetterWithKeyHolder} containing the binding information
     *
     * @return a int, where each element corresponds to an executed statement
     */
    public static <T> int[] batchUpdateWithKeyHolder( JdbcTemplate jdbcTemplate, final String sql,
        final BatchPreparedStatementSetterWithKeyHolder<T> pss )
    {
        return jdbcTemplate.execute( con -> con.prepareStatement( sql, Statement.RETURN_GENERATED_KEYS ),
            (PreparedStatementCallback<int[]>) ps -> {
                try
                {
                    int batchSize = pss.getBatchSize();
                    InterruptibleBatchPreparedStatementSetter ipss = ( pss instanceof InterruptibleBatchPreparedStatementSetter
                        ? (InterruptibleBatchPreparedStatementSetter) pss
                        : null );
                    int[] result;
                    KeyHolder keyHolder = new GeneratedKeyHolder();

                    try
                    {
                        if ( org.springframework.jdbc.support.JdbcUtils.supportsBatchUpdates( ps.getConnection() ) )
                        {
                            for ( int i = 0; i < batchSize; i++ )
                            {
                                pss.setValues( ps, i );
                                if ( ipss != null && ipss.isBatchExhausted( i ) )
                                    break;
                                ps.addBatch();
                            }
                            result = ps.executeBatch();

                            generatedKeys( ps, keyHolder );
                        }
                        else
                        {
                            List<Integer> rowsAffected = new ArrayList<>();
                            for ( int i = 0; i < batchSize; i++ )
                            {
                                pss.setValues( ps, i );
                                if ( ipss != null && ipss.isBatchExhausted( i ) )
                                {
                                    break;
                                }

                                rowsAffected.add( ps.executeUpdate() );
                                generatedKeys( ps, keyHolder );
                            }

                            result = rowsAffected.stream().mapToInt( Integer::intValue ).toArray();
                        }
                    }
                    finally
                    {
                        pss.setPrimaryKey( keyHolder );
                    }

                    return result;
                }
                finally
                {
                    if ( pss instanceof ParameterDisposer )
                    {
                        ((ParameterDisposer) pss).cleanupParameters();
                    }
                }
            } );
    }

    private static void generatedKeys( PreparedStatement ps, KeyHolder keyHolder )
        throws SQLException
    {
        List<Map<String, Object>> keys = keyHolder.getKeyList();
        ResultSet rs = ps.getGeneratedKeys();
        if ( rs == null )
            return;

        try
        {
            keys.addAll( new RowMapperResultSetExtractor<>( new ColumnMapRowMapper(), 1 ).extractData( rs ) );
        }
        finally
        {

            rs.close();
        }
    }
}
