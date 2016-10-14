package com.eu.habbo.database;

import com.eu.habbo.core.ConfigurationManager;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.PrintWriter;

class DatabasePool
{
    private final Logger log = LoggerFactory.getLogger(DatabasePool.class);
    private HikariDataSource database;

    public boolean getStoragePooling(ConfigurationManager config)
    {
        try
        {
            HikariConfig databaseConfiguration = new HikariConfig();
            databaseConfiguration.setMaximumPoolSize(20);
            databaseConfiguration.setInitializationFailFast(true);
            databaseConfiguration.setJdbcUrl("jdbc:mysql://" + config.getValue("db.hostname", "localhost") + ":" + config.getValue("db.port", "3306") + "/" + config.getValue("db.database", "habbo"));
            databaseConfiguration.addDataSourceProperty("serverName", config.getValue("db.hostname", "localhost"));
            databaseConfiguration.addDataSourceProperty("port", config.getValue("db.port", "3306"));
            databaseConfiguration.addDataSourceProperty("databaseName", config.getValue("db.database", "habbo"));
            databaseConfiguration.addDataSourceProperty("user", config.getValue("db.username"));
            databaseConfiguration.addDataSourceProperty("password", config.getValue("db.password"));
            databaseConfiguration.addDataSourceProperty("dataSource.logger", "com.mysql.jdbc.log.StandardLogger");
            databaseConfiguration.addDataSourceProperty("dataSource.logSlowQueries", "true");
            databaseConfiguration.addDataSourceProperty("dataSource.dumpQueriesOnException", "true");
            databaseConfiguration.addDataSourceProperty("dataSource.logWriter", new PrintWriter(System.out));
            databaseConfiguration.setAutoCommit(true);
            databaseConfiguration.setConnectionTimeout(3400L);
            databaseConfiguration.setValidationTimeout(3399L);
            databaseConfiguration.setLeakDetectionThreshold(4000L);
            databaseConfiguration.setMaxLifetime(30000L);
            databaseConfiguration.setIdleTimeout(10000L);
            //databaseConfiguration.setDriverClassName("com.mysql.jdbc.jdbc2.optional.MysqlDataSource");
            this.database = new HikariDataSource(databaseConfiguration);
        }
        catch (Exception e)
        {
            return false;
        }
        return true;
    }
    
    public HikariDataSource getDatabase()
    {
        return this.database;
    }
}