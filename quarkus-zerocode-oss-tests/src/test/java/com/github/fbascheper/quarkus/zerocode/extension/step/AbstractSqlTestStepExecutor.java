package com.github.fbascheper.quarkus.zerocode.extension.step;

import org.slf4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.util.ClassUtils;

import javax.sql.DataSource;
import java.lang.reflect.InvocationTargetException;
import java.sql.Driver;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * A generic implementation of the {@link ZeroCodeTestStepExecutor} to perform SQL verificationStep.
 *
 * @author Erik-Berndt Scheper
 * @see <a href="https://github.com/authorjapps/zerocode/wiki/Sample-DB-SQL-Executor">Example from </a>
 * @since 01-12-2020
 */
public abstract class AbstractSqlTestStepExecutor implements ZeroCodeTestStepExecutor<Map<String, Object>, Map<String, List<Map<String, Object>>>> {

    private static final String RESULTS_KEY = "rows";

    private final Logger logger = getLogger(this.getClass());

    @Override
    public Map<String, List<Map<String, Object>>> execute(Map<String, Object> arguments) {
        String sql = (String) arguments.get("query");

        @SuppressWarnings({"unchecked", "rawtypes"})
        Map<String, Object> sqlParameters = (Map) arguments.get("params");

        logger.info("DB - Executing SQL query: {}, parameters = {}", sql, sqlParameters);
        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValues(sqlParameters);

        List<Map<String, Object>> recordsList = fetchDbRecords(sql, namedParameters);

        // -------------------------------------------------------
        // Put all the fetched rows into nice JSON key and return.
        // -- This make it better to assert SIZE etc in the steps.
        // -- You can choose any key.
        // -------------------------------------------------------
        Map<String, List<Map<String, Object>>> dbRecordsMap = new HashMap<>();
        dbRecordsMap.put(RESULTS_KEY, recordsList);

        return dbRecordsMap;
    }

    private List<Map<String, Object>> fetchDbRecords(String simpleSql, MapSqlParameterSource namedParameters) {
        try {
            var result = getJdbcTemplate().query(simpleSql, namedParameters, new MapSqlParameterRowMapper());
            logger.debug("Received result = {}", result);
            return result;
        } catch (DataAccessException daEx) {
            logger.error("Exception in query", daEx);
            throw daEx;
        }
    }

    private NamedParameterJdbcTemplate getJdbcTemplate() {
        // Build dataSource & JDBC template from the host properties file
        final Class<?> driverClass = ClassUtils.resolveClassName(getDriverClassName(), this.getClass().getClassLoader());
        try {
            Driver driver = (Driver) Objects.requireNonNull(ClassUtils.getConstructorIfAvailable(driverClass)).newInstance();
            final DataSource dataSource = new SimpleDriverDataSource(driver, getJdbcUrl(), getDbUserName(), getDbPassword());

            return new NamedParameterJdbcTemplate(dataSource);

        } catch (InstantiationException | IllegalAccessException | InvocationTargetException ex) {
            logger.error("Failed to create JDBC template", ex);
            throw new IllegalStateException("Failed to create JDBC template", ex);
        }
    }

    /**
     * A Spring JDBC {@link RowMapper}-implementation to map a ResultSet to a Map.
     */
    private static class MapSqlParameterRowMapper implements RowMapper<Map<String,Object>> {

        @Override
        public Map<String, Object> mapRow(ResultSet resultSet, int rowNum) throws SQLException {
            Map<String, Object> aRowColumnValue = new HashMap<>();

            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();

            for (int j = 1; j <= columnCount; j++) {
                String columnName = metaData.getColumnName(j).toUpperCase();
                Object columnValue = resultSet.getObject(columnName);

                aRowColumnValue.put(columnName, columnValue);
            }

            return aRowColumnValue;
        }
    }

    /**
     * @return the class name of the JDBC driver
     */
    public abstract String getDriverClassName();

    /**
     * @return the JDBC URL
     */
    public abstract String getJdbcUrl();

    /**
     * @return the username to connect to the database
     */
    public abstract String getDbUserName();

    /**
     * @return the password to connect to the database
     */
    public abstract String getDbPassword();
}
