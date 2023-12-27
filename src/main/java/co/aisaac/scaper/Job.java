package co.aisaac.scaper;

import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Transient;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity(name = "jobs")
public class Job {

    @Id
    private Long id;

    String title;
    String url;
    String company;
    @Transient
    List<String> companyStatuses = new ArrayList<>();
    String subtitle;
    String description;
    String status;

    @Column(name = "search_term")
    String searchTerm;

    String location;
    String job_site;
    LocalDateTime job_posting_date;
}
