package models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties; //Для игнора неиспользуемых параметров
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserBodyResponseModel {
    String name, job;
}
