package pessoal.estudos.quarkus.quarkussocial.rest;

import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;
import pessoal.estudos.quarkus.quarkussocial.rest.dto.CreateUserRequest;

@Path("/users")
public class UserResource {

    @POST
    public Response createUser(CreateUserRequest userRequest) {
        return Response.ok().build();
    }
}
