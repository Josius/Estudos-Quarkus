package pessoal.estudos.quarkus.quarkussocial.rest.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

//public record CreateUserRequest (
//        @NotBlank(message = "Name is required.") String name,
//        @NotNull(message = "Age is required.") Integer age)
//{}

@Data
public class CreateUserRequest {

    @NotBlank(message = "Name is Required")
    private String name;
    @NotNull(message = "Age is Required")
    private Integer age;

}