package jooq;

import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Objects;


public class JooqConfig {
    static String USERNAME = "postgres";
    static String PASSWORD = "postgres";
    static String URL = "jdbc:postgresql://localhost:5433/ktor";

    public static Result<Record> getDataFromDB(String tableName, String SQLQuery) throws SQLException {
        try (Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD)) {
            DSLContext create = DSL.using(conn, SQLDialect.MYSQL);
            return create.select().from(tableName).where(SQLQuery).fetch();
        }
    }

    public static String getId(String tableName, String SQLQuery) throws SQLException {
        try (Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD)) {
            DSLContext create = DSL.using(conn, SQLDialect.MYSQL);
            return Objects.requireNonNull(create.select().from(tableName).where(SQLQuery).fetch().getFirst().get("id")).toString();
        }
    }
}
