package com.github.fbascheper.quarkus.zerocode.extension.step.sql;

import com.github.fbascheper.quarkus.zerocode.extension.step.AbstractSqlTestStepExecutor;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;
import java.util.Map;

/**
 * A {@link AbstractSqlTestStepExecutor} used to verify Villain data.
 *
 * @author Erik-Berndt Scheper
 * @since 01-12-2020
 */
public class VillainSqlTestStepExecutor extends AbstractSqlTestStepExecutor {

    @Inject
    @Named("jdbc.driver.class")
    private String dbDriverClassName;

    @Inject
    @Named("villain.jdbc.url")
    private String dbHostUrl;

    @Inject
    @Named("villain.jdbc.username")
    private String dbUserName;

    @Inject
    @Named("villain.jdbc.password")
    private String dbPassword;

    @Override
    public Map<String, List<Map<String, Object>>> execute(Map<String, Object> arguments) {
        return super.execute(arguments);
    }

    @Override
    public String getDriverClassName() {
        return dbDriverClassName;
    }

    @Override
    public String getJdbcUrl() {
        return dbHostUrl;
    }

    @Override
    public String getDbUserName() {
        return dbUserName;
    }

    @Override
    public String getDbPassword() {
        return dbPassword;
    }
}
