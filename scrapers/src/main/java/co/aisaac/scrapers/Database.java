package co.aisaac.scrapers;

import co.aisaac.webapp.Job;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class Database {

    private final String URL = "jdbc:mysql://localhost:3306/db";
    private final String USER = "root";
    private final String PASSWORD = "password";

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public boolean hrefExists(String href) {
        String sql = "SELECT * FROM jobs WHERE url = ?";

        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, href);
            ResultSet rs = stmt.executeQuery();
            return rs.next();

        } catch (SQLException e) {
            e.printStackTrace();
            return true;

        }
    }

    public boolean jobExists(Job job) {
        if (hrefExists(job.url)) {
            return true;
        }

        String sql = "SELECT * FROM jobs WHERE title = ? AND company = ?";

        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, job.title);
            stmt.setString(2, job.company);
            ResultSet rs = stmt.executeQuery();
            return rs.next();

        } catch (SQLException e) {
            e.printStackTrace();
            return true;

        }
    }

    public void storeJob(Job job) {

        if (jobExists(job)) {
            System.out.println("Job already exists");
            return;
        }

        System.out.println("Storing job " + job.searchTerm + " - " + job.title);

        String sql = "INSERT INTO jobs(title, url, company, subtitle, description, status, search_term, location, " +
                "job_site, job_posting_date) values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";

        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, job.title);
            stmt.setString(2, job.url);
            stmt.setString(3, job.company);
            stmt.setString(4, job.subtitle);
            stmt.setString(5, job.description);
            stmt.setString(6, job.status);
            stmt.setString(7, job.searchTerm);
            stmt.setString(8, job.location);
            stmt.setString(9, job.job_site);
            stmt.setString(10, job.job_posting_date.toLocalDate().toString());
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        // save job todo
    }
}