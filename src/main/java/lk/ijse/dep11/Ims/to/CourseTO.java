package lk.ijse.dep11.Ims.to;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CourseTO implements Serializable {
    @Null(message = "Id Should be empty")
    private Integer id;
    @NotBlank(message = "Name should not be empty")
    private String name;
    @NotNull(message = "Duration Shouldn't be empty")
    private Integer durationInMonths;
}
