package com.jrh.biblioteca.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

@Component
public class DatabaseTest implements CommandLineRunner {

    @Autowired
    private DataSource dataSource;

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Testing database connection...");

        try (Connection connection = dataSource.getConnection()) {
            System.out.println("Database connection established.");

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT 1");
            while (resultSet.next()) {
                System.out.println("Result: " + resultSet.getInt(1));
            }
        } catch (Exception e) {
            System.err.println("Error connecting to database: " + e.getMessage());
        }
    }
}
