package in.ureport.db.repository;

import br.com.ilhasoft.support.db.repository.AbstractRepository;
import in.ureport.models.User;
import in.ureport.models.holders.Login;

/**
 * Created by ilhasoft on 7/9/15.
 */
public interface UserRepository extends AbstractRepository<User> {

    User login(Login login);

}
