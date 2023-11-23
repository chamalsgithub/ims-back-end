package lk.ijse.dep11.Ims.to;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Null;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TeacherTO implements Serializable {
    @Null(message = "Id should be empty")
    private Integer id;

    @NotBlank(message = "Description shoudnt empty")
    private String name;

    @NotBlank(message = "Contact shouldnt be empty")
    private String contact;
}
