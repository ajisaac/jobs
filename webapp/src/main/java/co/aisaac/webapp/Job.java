package co.aisaac.webapp;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Transient;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@ToString
@Getter
@Setter
@Entity(name = "jobs")
public class Job {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    public String title;
    public String url;
    public String company;
    @Transient
    public List<String> companyStatuses = new ArrayList<>();
    public String subtitle;
    public String description;
    public String status;

    @Column(name = "search_term")
    public String searchTerm;

    public String location;
    public String job_site;
    public LocalDateTime job_posting_date;
}
