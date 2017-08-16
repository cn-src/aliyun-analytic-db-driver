package com.mysql.jdbc;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author zhangpeng
 */
public class SqlUtils
{
    private static final Pattern SELECT_PATTERN      = Pattern.compile("\\p{Space}*SELECT\\p{Space}+\\w+\\.\\*\\p{Space}+FROM\\p{Space}+.*", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);
    private static final Pattern SELECT_ITEM_PATTERN = Pattern.compile("SELECT\\p{Space}+\\w+\\.\\*\\p{Space}+FROM", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);
    
    private SqlUtils() {}
    
    /**
     * 针对分析型数据库不支持的别名加星号的语句(例：SELECT d.*)去掉别名
     */
    public static String fix(final String sql)
    {
        if(null == sql || sql.length() <=0){
            return sql;
        }
        final Matcher selectMatcher = SELECT_PATTERN.matcher(sql);
        if (selectMatcher.find())
        {
            return SELECT_ITEM_PATTERN.matcher(sql).replaceFirst("SELECT * FROM");
        }
        return sql;
    }
}
