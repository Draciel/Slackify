package pl.draciel.slackify.slack;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.draciel.slackify.domain.AddTrackLog;

import java.util.List;

@Repository
interface AddTrackLogRepository extends CrudRepository<AddTrackLog, Long> {

    List<AddTrackLog> findBySpotifyId(String spotifyId);
}
