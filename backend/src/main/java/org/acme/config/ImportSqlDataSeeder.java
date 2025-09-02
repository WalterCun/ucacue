package org.acme.config;

import io.quarkus.logging.Log;
import jakarta.annotation.Priority;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.transaction.Transactional.TxType;
import jakarta.ws.rs.Produces;
import org.hibernate.Session;

import io.quarkus.runtime.StartupEvent;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * Loads SQL statements from import.sql at application startup and executes them
 * when the target domain tables are empty (first run after model generation).
 * This avoids dropping or recreating the schema and works with
 * quarkus.hibernate-orm.database.generation=update.
 */
@ApplicationScoped
public class ImportSqlDataSeeder {

    @Inject
    EntityManager entityManager;

    @Transactional(value = TxType.REQUIRED)
    public void onStart(@Observes StartupEvent ev) {
        try {
            if (!shouldSeed()) {
                Log.info("Data seeding skipped: target tables already contain data");
                return;
            }
            List<String> statements = loadSqlStatements("import.sql");
            if (statements.isEmpty()) {
                Log.warn("import.sql found but contains no executable statements");
                return;
            }
            executeStatements(statements);
            Log.info("Data seeding from import.sql completed successfully");
        } catch (Exception e) {
            Log.error("Failed to seed data from import.sql", e);
        }
    }

    private boolean shouldSeed() {
        try {
            // Seed if both colegios and carreras tables are empty (or do not exist yet)
            Number c1 = tryCount("colegios");
            Number c2 = tryCount("carreras");
            // If either table does not exist (null), allow seeding so schema can be created and data loaded later
            if (c1 == null || c2 == null) {
                return true;
            }
            return c1.longValue() == 0L && c2.longValue() == 0L;
        } catch (Exception e) {
            Log.warn("Could not determine target tables row count; allowing data seeding. Reason: " + e.getMessage());
            return true;
        }
    }

    private Number tryCount(String table) {
        try {
            Query q = entityManager.createNativeQuery("SELECT COUNT(*) FROM " + table);
            return (Number) q.getSingleResult();
        } catch (Exception ex) {
            // Table missing or error
            return null;
        }
    }

    private List<String> loadSqlStatements(String resourceName) throws IOException {
        InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(resourceName);
        if (is == null) {
            Log.warn("Resource not found: " + resourceName + "; skipping data seeding");
            return List.of();
        }
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
            StringBuilder current = new StringBuilder();
            List<String> statements = new ArrayList<>();
            String line;
            while ((line = reader.readLine()) != null) {
                String trimmed = line.trim();
                // Remove comments starting with --
                if (trimmed.startsWith("--") || trimmed.isEmpty()) {
                    continue;
                }
                current.append(line).append('\n');
                if (trimmed.endsWith(";")) {
                    String stmt = current.toString().trim();
                    // Remove trailing semicolon to avoid issues with some drivers
                    if (stmt.endsWith(";")) {
                        stmt = stmt.substring(0, stmt.length() - 1);
                    }
                    if (!stmt.isBlank()) {
                        statements.add(stmt);
                    }
                    current.setLength(0);
                }
            }
            // Add leftover statement if file didn't end with semicolon
            String leftover = current.toString().trim();
            if (!leftover.isBlank()) {
                statements.add(leftover);
            }
            return statements;
        }
    }

    private void executeStatements(List<String> statements) {
        Session session = entityManager.unwrap(Session.class);
        for (String sql : statements) {
            try {
                session.createNativeQuery(sql).executeUpdate();
            } catch (Exception e) {
                Log.error("Error executing SQL statement during seeding: " + sql, e);
                throw e;
            }
        }
    }
}
