package pessoal.estudos.quarkus.quarkussocial.rest;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import pessoal.estudos.quarkus.quarkussocial.domain.model.Follower;
import pessoal.estudos.quarkus.quarkussocial.domain.model.User;
import pessoal.estudos.quarkus.quarkussocial.domain.repository.FollowerRepository;
import pessoal.estudos.quarkus.quarkussocial.domain.repository.UserRepository;
import pessoal.estudos.quarkus.quarkussocial.rest.dto.FollowerRequest;
import pessoal.estudos.quarkus.quarkussocial.rest.dto.FollowerResponse;
import pessoal.estudos.quarkus.quarkussocial.rest.dto.FollowersPerUserResponse;

import java.util.stream.Collectors;

@Path("/users/{userId}/followers")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class FollowerResource {

    private FollowerRepository followerRepository;
    private UserRepository userRepository;

    @Inject
    public FollowerResource(FollowerRepository followerRepository, UserRepository userRepository) {
        this.followerRepository = followerRepository;
        this.userRepository = userRepository;
    }

    @PUT
    @Transactional
    public Response followUser(
            @PathParam("userId") Long userId, FollowerRequest followerRequest){

        if(userId.equals(followerRequest.getFollowerId())){

            return Response.status(Response.Status.CONFLICT)
                    .entity("You can't follow yourself.")
                    .build();
        }

        User user = userRepository.findById(userId);
        if(user == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        User follower = userRepository.findById(followerRequest.getFollowerId());
        boolean follows = followerRepository.follows(follower, user);

        if(!follows) {

            var entity = new Follower();
            entity.setUser(user);
            entity.setFollower(follower);

            followerRepository.persist(entity);
        }


        return Response.status(Response.Status.NO_CONTENT).build(); 
    }

    @GET
    public Response listFolloweres(@PathParam("userId") Long userId) {

        User user = userRepository.findById(userId);
        if(user == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        var list = followerRepository.findByUser(userId);
        FollowersPerUserResponse responseObject = new FollowersPerUserResponse();
        responseObject.setFollowersCount(list.size());

        var followersList = list.stream()
                .map(FollowerResponse::new)
                .collect(Collectors.toList());

        responseObject.setContent(followersList);

        return Response.ok(responseObject).build();
    }

    @DELETE
    @Transactional
    public Response unfollowUser(@PathParam("userId") Long userId, @QueryParam("followerId") Long followerId) {
        User user = userRepository.findById(userId);
        if(user == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        followerRepository.deleteByFollowerAndUser(followerId, userId);

        return Response.status(Response.Status.NO_CONTENT).build();
    }
}
