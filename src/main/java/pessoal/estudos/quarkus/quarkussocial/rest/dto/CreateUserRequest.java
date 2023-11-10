package pessoal.estudos.quarkus.quarkussocial.rest.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateUserRequest (
        @NotBlank(message = "Name is required.") String name,
        @NotNull(message = "Age is required.") Integer age)
{}
