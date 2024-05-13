package co.edu.javeriana.as.personapp.application.port.in;

import java.util.List;
import co.edu.javeriana.as.personapp.application.port.out.ProfessionOutputPort;
import co.edu.javeriana.as.personapp.common.exceptions.NoExistException;
import co.edu.javeriana.as.personapp.domain.Profession;

public interface ProfessionInputPort {

    void setPersistence(ProfessionOutputPort professionPersistence);

    Profession create(Profession profession);

    Profession edit(Integer id, Profession profession) throws NoExistException;

    Boolean drop(Integer id) throws NoExistException;

    List<Profession> findAll();

    Profession findOne(Integer id) throws NoExistException;

    Integer count();
}
