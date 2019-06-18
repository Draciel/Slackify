package pl.draciel.slackify.slack;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.draciel.slackify.domain.User;

import javax.annotation.Nullable;

@Repository
interface UserRepository extends CrudRepository<User, Long> {

    @Nullable
    User findBySlackUserId(String slackUserId);
}
