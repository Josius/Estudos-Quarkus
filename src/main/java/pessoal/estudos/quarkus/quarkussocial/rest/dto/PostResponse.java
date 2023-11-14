package pessoal.estudos.quarkus.quarkussocial.rest.dto;

import lombok.Data;
import pessoal.estudos.quarkus.quarkussocial.domain.model.Post;

import java.time.LocalDateTime;

@Data
public class PostResponse {

    private String text;
    private LocalDateTime dateTime;

    public static PostResponse fromEntity(Post post) {
        var response =  new PostResponse();
        response.setText(post.getText());
        response.setDateTime(post.getDateTime());
        return response;
    }
}
