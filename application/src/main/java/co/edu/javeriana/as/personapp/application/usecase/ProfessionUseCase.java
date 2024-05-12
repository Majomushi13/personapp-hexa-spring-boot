package co.edu.javeriana.as.personapp.application.usecase;

import java.util.List;

import org.springframework.beans.factory.annotation.Qualifier;

import co.edu.javeriana.as.personapp.application.port.in.ProfessionInputPort;
import co.edu.javeriana.as.personapp.application.port.out.ProfessionOutputPort;
import co.edu.javeriana.as.personapp.common.annotations.UseCase;
import co.edu.javeriana.as.personapp.common.exceptions.NoExistException;
import co.edu.javeriana.as.personapp.domain.Profession;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@UseCase
public class ProfessionUseCase implements ProfessionInputPort {

    private ProfessionOutputPort professionPersistence;

    public ProfessionUseCase(@Qualifier("professionOutputAdapterMaria") ProfessionOutputPort professionPersistence) {
        this.professionPersistence = professionPersistence;
    }

    @Override
    public void setPersistence(ProfessionOutputPort professionPersistence) {
        this.professionPersistence = professionPersistence;
    }

    @Override
    public Profession create(Profession profession) {
        log.debug("Creating profession in Application Domain");
        return professionPersistence.save(profession);
    }

    @Override
    public Profession edit(Integer id, Profession profession) throws NoExistException {
        Profession oldProfession = professionPersistence.findById(id);
        if (oldProfession != null)
            return professionPersistence.save(profession);
        throw new NoExistException("The profession with id " + id + " does not exist in the database, cannot be edited");
    }

    @Override
    public Boolean drop(Integer id) throws NoExistException {
        Profession oldProfession = professionPersistence.findById(id);
        if (oldProfession != null)
            return professionPersistence.delete(id);
        throw new NoExistException("The profession with id " + id + " does not exist in the database, cannot be dropped");
    }

    @Override
    public List<Profession> findAll() {
        log.info("Fetching all professions from database");
        return professionPersistence.findAll();
    }

    @Override
    public Profession findOne(Integer id) throws NoExistException {
        Profession profession = professionPersistence.findById(id);
        if (profession != null)
            return profession;
        throw new NoExistException("The profession with id " + id + " does not exist in the database, cannot be found");
    }

    @Override
    public Integer count() {
        return findAll().size();
    }
}
