/* package co.edu.javeriana.as.personapp.mapper;

import co.edu.javeriana.as.personapp.common.annotations.Mapper;
import co.edu.javeriana.as.personapp.domain.Profession;
import co.edu.javeriana.as.personapp.model.request.ProfesionRequest;
import co.edu.javeriana.as.personapp.model.response.ProfesionResponse;

@Mapper
public class ProfesionMapperRest {

    // Método para convertir una solicitud en una entidad de dominio
    public Profession fromAdapterToDomain(ProfesionRequest request) {
        if (request == null) {
            return null;
        }
        return new Profession(
            request.getIdentification(),
            request.getName(),
            request.getDescription()
        );
    }

    // Método para convertir una entidad de dominio en una respuesta
    public ProfesionResponse fromDomainToAdapterRest(Profession profesion) {
        if (profesion == null) {
            return null;
        }
        return new ProfesionResponse(
            profesion.getIdentification(),
            profesion.getName(),
            profesion.getDescription(),
            "OK"  // Supongamos que siempre retornamos "OK" como estado
        );
    }
}
 */