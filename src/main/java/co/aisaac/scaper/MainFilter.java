package co.aisaac.scaper;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class MainFilter {
    public MainFilter() {
    }

    boolean statusNew = true;
    boolean declined;
    boolean interested;
    boolean applied;
    boolean interviewing;
    boolean rejected;
    boolean later;

    List<String> getStatuses() {
        List<String> statuses = new ArrayList<>();
        if (statusNew)
            statuses.add("new");
        if (declined)
            statuses.add("declined");
        if (interested)
            statuses.add("interested");
        if (applied)
            statuses.add("applied");
        if (interviewing)
            statuses.add("interviewing");
        if (rejected)
            statuses.add("rejected");
        if (later)
            statuses.add("later");
        return statuses;
    }

    String companySearch = "";

    String searchTerms = "";

    String titleSearch = "";

}
