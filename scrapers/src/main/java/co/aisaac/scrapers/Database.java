package co.aisaac.scrapers;

import co.aisaac.webapp.Job;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class Database {

    private Connection getConnection() throws SQLException {
        String URL = "jdbc:mysql://localhost:3306/db";
        String USER = "root";
        String PASSWORD = "password";
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

        System.out.println("Saving job - " + job.title + " - " + job.company + " - " + job.url);

        if (jobExists(job)) {
            System.out.println("Job already exists");
            return;
        }

        String sql = "INSERT INTO jobs(title, url, company, description, status, " +
                "job_site, job_posting_date) values(?, ?, ?, ?, ?, ?, ?);";

        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, job.title);
            stmt.setString(2, job.url);
            stmt.setString(3, job.company);
            stmt.setString(4, job.description);
            stmt.setString(5, job.status);
            stmt.setString(6, job.job_site);
            stmt.setString(7, job.job_posting_date.toString());
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}