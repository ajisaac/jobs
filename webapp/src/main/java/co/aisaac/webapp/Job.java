package co.aisaac.webapp;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
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
    public String description;
    public String status;

    public String job_site;
    public LocalDate job_posting_date;
}
