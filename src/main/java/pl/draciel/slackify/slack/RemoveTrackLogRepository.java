package pl.draciel.slackify.slack;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import pl.draciel.slackify.domain.RemoveTrackLog;

@Repository
interface RemoveTrackLogRepository extends CrudRepository<RemoveTrackLog, Long> {

}
