package pessoal.estudos.quarkus.quarkussocial.domain.repository;


import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import pessoal.estudos.quarkus.quarkussocial.domain.model.Post;

@ApplicationScoped
public class PostRepository implements PanacheRepository<Post> {

}
