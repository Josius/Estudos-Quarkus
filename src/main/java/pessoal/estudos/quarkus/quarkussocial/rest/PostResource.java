package pessoal.estudos.quarkus.quarkussocial.rest;

import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.panache.common.Sort;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.hibernate.validator.cfg.defs.PositiveDef;
import org.jboss.logging.annotations.Pos;
import pessoal.estudos.quarkus.quarkussocial.domain.model.Post;
import pessoal.estudos.quarkus.quarkussocial.domain.model.User;
import pessoal.estudos.quarkus.quarkussocial.domain.repository.PostRepository;
import pessoal.estudos.quarkus.quarkussocial.domain.repository.UserRepository;
import pessoal.estudos.quarkus.quarkussocial.rest.dto.CreatePostRequest;
import pessoal.estudos.quarkus.quarkussocial.rest.dto.PostResponse;

import java.util.List;
import java.util.stream.Collectors;

@Path("/users/{userId}/posts")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class PostResource {

    private UserRepository userRepository;
    private PostRepository postRepository;

    @Inject
    public PostResource(
            UserRepository userRepository,
            PostRepository postRepository) {

        this.userRepository = userRepository;
        this.postRepository = postRepository;
    }
    @POST
    @Transactional
    public Response savePost(
            @PathParam("userId") Long userId, CreatePostRequest request) {

        User user = userRepository.findById(userId);
        if(user == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        Post post = new Post();
        post.setText(request.getText());
        post.setUser(user);

        postRepository.persist(post);

        return Response.status(Response.Status.CREATED).build();
    }

    @GET
    public Response listPost( @PathParam("userId") Long userId ) {
        User user = userRepository.findById(userId);
        if(user == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        PanacheQuery<Post> query = postRepository.find(
                "user", Sort.by("dateTime", Sort.Direction.Descending), user);
        List<Post> list = query.list();

        var postRespondeList = list.stream()
//                .map(post -> PostResponse.fromEntity(post))
                .map(PostResponse::fromEntity)
                .collect(Collectors.toList());

        return Response.ok(postRespondeList).build();
    }
}
