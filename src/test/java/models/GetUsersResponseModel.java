package models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class GetUsersResponseModel {
    @JsonProperty("per_page")
    int perPage;
    @JsonProperty("total_pages")
    int totalPages;
    int page, total;

    Support support;
    List<DataInfo> data;

    @Data
    public static class DataInfo {
        int id;
        @JsonProperty("first_name")
        String firstName;
        @JsonProperty("last_name")
        String lastName;
        String email, avatar;
    }

    @Data
    public static class Support {
        String url, text;
    }
}
