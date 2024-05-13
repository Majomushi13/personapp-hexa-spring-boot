package co.edu.javeriana.as.personapp.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TelefonoRequest {
    private String number;
    private String company;
    private Integer ownerId;
    private String database;  // Campo agregado

    public TelefonoRequest(String number, String company, Integer ownerId) {
        this.number = number;
        this.company = company;
        this.ownerId = ownerId;
    }
}
