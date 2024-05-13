package co.edu.javeriana.as.personapp.model.response;

import co.edu.javeriana.as.personapp.model.request.EstudiosRequest;
import java.time.LocalDate;

public class EstudiosResponse extends EstudiosRequest{
    
    private String status;

    public EstudiosResponse(Integer personId, Integer professionId, LocalDate graduationDate, String universityName, String status) {
        super(personId, professionId, graduationDate, universityName);
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
