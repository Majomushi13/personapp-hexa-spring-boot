package co.edu.javeriana.as.personapp.terminal.mapper;

import co.edu.javeriana.as.personapp.common.annotations.Mapper;
import co.edu.javeriana.as.personapp.domain.Profession;
import co.edu.javeriana.as.personapp.terminal.model.ProfessionModelCli;

@Mapper
public class ProfessionMapperCli {

    public ProfessionModelCli fromDomainToAdapterCli(Profession profession) {
        ProfessionModelCli professionModelCli = new ProfessionModelCli();
        professionModelCli.setId(profession.getIdentification());
        professionModelCli.setNom(profession.getName());
        professionModelCli.setDes(profession.getDescription());
        return professionModelCli;
    }
}
