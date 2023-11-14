package pessoal.estudos.quarkus.quarkussocial.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pessoal.estudos.quarkus.quarkussocial.domain.model.Follower;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FollowerResponse {
    private Long id;
    private String name;

    public FollowerResponse(Follower follower) {
        this(follower.getId(), follower.getFollower().getName());
    }
}
