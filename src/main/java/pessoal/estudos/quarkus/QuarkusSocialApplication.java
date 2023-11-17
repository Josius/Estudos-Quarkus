package pessoal.estudos.quarkus;

import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.info.Contact;
import org.eclipse.microprofile.openapi.annotations.info.Info;
import org.eclipse.microprofile.openapi.annotations.info.License;

import jakarta.ws.rs.core.Application;

@OpenAPIDefinition(
	info = @Info(
		title = "API Quarkus Social",
		version = "1.0",
		contact = @Contact(
			name = "Josimar",
			url = "sem site - configurações somente para estudos",
			email = "email@email.com"),
		license = @License(
		name = "Apache 2.0",
		url = "https://www.apache.org/licenses/LICENSE-2.0.html")
	)
)
public class QuarkusSocialApplication extends Application {
	
}
