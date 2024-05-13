package co.edu.javeriana.as.personapp.terminal.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudyModelCli {
    private Integer idProf;
    private Integer ccPer;
    private Date fecha;
    private String universidad;
}
