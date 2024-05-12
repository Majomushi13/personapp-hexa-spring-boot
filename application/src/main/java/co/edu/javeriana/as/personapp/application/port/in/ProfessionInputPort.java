package co.edu.javeriana.as.personapp.application.port.in;

import java.util.List;

import co.edu.javeriana.as.personapp.application.port.out.ProfessionOutputPort;
import co.edu.javeriana.as.personapp.common.annotations.Port;
import co.edu.javeriana.as.personapp.common.exceptions.NoExistException;
import co.edu.javeriana.as.personapp.domain.Profession;

@Port
public interface ProfessionInputPort {

    public void setPersistence(ProfessionOutputPort professionPersistence);

    public Profession create(Profession profession);

    public Profession edit(Integer id, Profession profession) throws NoExistException;

    public Boolean drop(Integer id) throws NoExistException;

    public List<Profession> findAll();

    public Profession findOne(Integer id) throws NoExistException;

    public Integer count();
}
