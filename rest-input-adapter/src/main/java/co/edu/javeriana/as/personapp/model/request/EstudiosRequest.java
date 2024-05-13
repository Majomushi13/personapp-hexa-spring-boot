package co.edu.javeriana.as.personapp.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EstudiosRequest {
    private Integer personId; 
    private Integer professionId; 
    private LocalDate graduationDate;
    private String universityName;
}
