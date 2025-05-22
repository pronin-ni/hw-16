package models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class UserByIdResponseModel {
    private DataField data;

    @Data
    public static class DataField {
        private int id;
        private String email;
        private String first_name;
        private String last_name;
        private String avatar;
    }
}