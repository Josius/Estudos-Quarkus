package pessoal.estudos.quarkus.quarkussocial.rest.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.ws.rs.core.Response;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
public class ResponseError {

    public static final int UNPROCESSABLE_ENTITY_STATUS = 422;

    private String message;
    private Collection<FieldError> fieldErrors;

    public static <T> ResponseError createFromValidaton(Set<ConstraintViolation<T>> violations) {

        List<FieldError> errors = violations
                .stream()
                .map(cv -> new FieldError(cv.getPropertyPath().toString(), cv.getMessage()))
                .collect(Collectors.toList());

        String message = "Validation Error";

        var responseError = new ResponseError(message, errors);

        return responseError;
    }

    public Response withStatusCode(int code) {
        return Response.status(code).entity(this).build();
    }
}
