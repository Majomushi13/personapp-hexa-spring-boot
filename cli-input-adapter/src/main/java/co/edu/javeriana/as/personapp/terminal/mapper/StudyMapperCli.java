package co.edu.javeriana.as.personapp.terminal.mapper;

import java.util.Date;
import java.time.LocalDate;
import java.time.ZoneId;
import co.edu.javeriana.as.personapp.common.annotations.Mapper;
import co.edu.javeriana.as.personapp.domain.Study;
import co.edu.javeriana.as.personapp.terminal.model.StudyModelCli;

@Mapper
public class StudyMapperCli {

    public StudyModelCli fromDomainToAdapterCli(Study study) {
        StudyModelCli studyModelCli = new StudyModelCli();
        studyModelCli.setIdProf(study.getProfession().getIdentification()); 
        
        if (study.getPerson() != null) {
            studyModelCli.setCcPer(study.getPerson().getIdentification());
        }
        
        if (study.getGraduationDate() != null) {
            Date date = Date.from(study.getGraduationDate().atStartOfDay(ZoneId.systemDefault()).toInstant());
            studyModelCli.setFecha(date);
        }
        studyModelCli.setUniversidad(study.getUniversityName());
        return studyModelCli;
    }
    
    
}
