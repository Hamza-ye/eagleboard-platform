package com.mass3d.commons.util;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class CronUtilsTest
{
    @Test
    public void getCronExpresion()
    {
        assertEquals( "1 2 3 4 5 6", CronUtils.getCronExpression( "1", "2", "3", "4", "5", "6" ) );
    }

    @Test
    public void getDailyCronExpression()
    {
        assertEquals( "0 0 0 */1 * *", CronUtils.getDailyCronExpression( 0, 0 ) );
    }

    @Test
    public void getWeeklyCronExpressionForAllWeekdays()
    {
        assertEquals( "0 0 0 * * SUN", CronUtils.getWeeklyCronExpression( 0, 0, 0 ) );
        assertEquals( "0 0 0 * * MON", CronUtils.getWeeklyCronExpression( 0, 0, 1 ) );
        assertEquals( "0 0 0 * * TUE", CronUtils.getWeeklyCronExpression( 0, 0, 2 ) );
        assertEquals( "0 0 0 * * WED", CronUtils.getWeeklyCronExpression( 0, 0, 3 ) );
        assertEquals( "0 0 0 * * THU", CronUtils.getWeeklyCronExpression( 0, 0, 4 ) );
        assertEquals( "0 0 0 * * FRI", CronUtils.getWeeklyCronExpression( 0, 0, 5 ) );
        assertEquals( "0 0 0 * * SAT", CronUtils.getWeeklyCronExpression( 0, 0, 6 ) );
        assertEquals( "0 0 0 * * SUN", CronUtils.getWeeklyCronExpression( 0, 0, 7 ) );
    }

    @Test
    public void getMonthlyCronExpression()
    {
        assertEquals( "0 0 0 15 */1 *", CronUtils.getMonthlyCronExpression( 0, 0, 15 ) );
    }
}
