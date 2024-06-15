package co.aisaac.webapp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
@Entity(name = "users")
@Table(name = "users", schema = "job_scraper")
public class User {

    @Id
    @SequenceGenerator(name = "users_id_generator", sequenceName = "users_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "users_id_generator")
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    private String name;

    // the username in our system
    @Column(nullable = false, unique = true)
    private String email;

    // hex encoded password
    @Column(nullable = false)
    private String password;

    // comma separated list of authorities, may or may not be valid
    private String authorities;

    // is the account expired
    @Column(name="account_expired")
    private boolean accountExpired;

    // is the account locked
    @Column(name="account_locked")
    private boolean accountLocked;

    // are the credentials expired, need to be reset?
    @Column(name="credentials_expired")
    private boolean credentialsExpired;

    // is account enabled
    private boolean enabled;


}
