package org.example.jooq;

import org.jooq.*;
import org.jooq.Record;
import org.jooq.impl.DSL;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Objects;


public class JooqConfig {

    public static Result<Record> getDataFromDB(String tableName, String SQLQuery) throws SQLException {
        String userName = "postgres";
        String password = "postgres";
        String url = "jdbc:postgresql://localhost:5433/ktor";

        try (Connection conn = DriverManager.getConnection(url, userName, password)) {
            DSLContext create = DSL.using(conn, SQLDialect.MYSQL);
            return (Result<Record>) create.select().from(tableName).where(SQLQuery).fetch();
        }
    }

    public static String getId(String tableName, String SQLQuery) throws SQLException {
        String userName = "postgres";
        String password = "postgres";
        String url = "jdbc:postgresql://localhost:5433/ktor";

        try (Connection conn = DriverManager.getConnection(url, userName, password)) {
            DSLContext create = DSL.using(conn, SQLDialect.MYSQL);
            return  Objects.requireNonNull(create.select().from(tableName).where(SQLQuery).fetch().getFirst().get("id")).toString();
        }
    }
}
