package prototype;

import com.rahel.lxblog.dto.RegistrationRequest;
import com.rahel.lxblog.entity.BlogUser;

public class BlogUserPrototype {
	
	public static BlogUser aUser() {
	BlogUser u = new BlogUser();
	u.setId(1);
	u.setEmail("test_email");
	u.setPassword("test_password");
	u.setFirst_name("first_name");
	u.setLast_name("last_name");
	return u;
	}
	
	public static RegistrationRequest aRequest() {
		RegistrationRequest rValid = new RegistrationRequest();
		rValid.setEmail("email@example.com");
		rValid.setPassword("rrpassword");
		rValid.setFirst_name("rrfirst_name");
		rValid.setLast_name("rrlast_name");
		return rValid;
	}
	
	public static RegistrationRequest anInvalidRequest() {
		RegistrationRequest rValid = new RegistrationRequest();
		rValid.setEmail("plainaddress");
		rValid.setPassword("rrpassword");
		rValid.setFirst_name("rrfirst_name");
		rValid.setLast_name("rrlast_name");
		return rValid;
	}
}
