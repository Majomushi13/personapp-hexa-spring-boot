package co.edu.javeriana.as.personapp.terminal.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PhoneModelCli {
    private String number;
    private String company;
    private String ownerName;  
    @Override
    public String toString() {
        return "PhoneModelCli{" +
               "number='" + number + '\'' +
               ", company='" + company + '\'' +
               ", ownerName='" + ownerName + '\'' +
               '}';
    }
}
