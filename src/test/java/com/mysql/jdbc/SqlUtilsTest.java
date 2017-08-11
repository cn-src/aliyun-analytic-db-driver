package com.mysql.jdbc;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author zhangpeng
 */
public class SqlUtilsTest
{
    @Test
    public void fix() throws Exception
    {
        assertEquals("SELECT * FROM demo t", SqlUtils.fix("SELECT t.* FROM demo t"));
        assertEquals("SELECT * FROM demo t", SqlUtils.fix("SELECT * FROM demo t"));
        assertEquals("SELECT * FROM demo t WHERE a='a' AND b='b'", SqlUtils.fix("SELECT t.* FROM demo t WHERE a='a' AND b='b'"));
        assertEquals("SELECT * FROM demo t", SqlUtils.fix("select t.* FROM demo t"));
        assertEquals("SELECT * FROM demo t\n WHERE a='a' AND b='b'", SqlUtils.fix("SELECT t.* \nFROM demo t\n WHERE a='a' AND b='b'"));
    }
    
}