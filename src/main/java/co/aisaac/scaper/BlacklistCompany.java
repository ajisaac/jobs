package co.aisaac.scaper;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name = "blacklist_companies")
public class BlacklistCompany {

    public BlacklistCompany() {
    }

    public BlacklistCompany(String company) {
        if (company == null || company.isBlank())
            throw new IllegalArgumentException();
        this.name = company;
    }

    @Id
    long id;

    String name;

}
