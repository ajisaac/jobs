package co.aisaac.scaper;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;
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
}
