package co.aisaac.scaper;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;

@Getter
@Setter
@Entity(name = "jobs")
public class Job {

	@Id
	private Long id;

	String title;
	String url;
	String company;
	String subtitle;
	String description;
	String status;
}
