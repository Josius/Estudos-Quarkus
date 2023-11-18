package pessoal.estudos.quarkus.quarkussocial.rest.dto;

import lombok.Data;

@Data
public class FollowerRequest {
    private Long followerId;
}
//public record FollowerRequest (Long followerId){
//}
