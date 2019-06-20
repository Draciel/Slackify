package pl.draciel.slackify.domain;

import lombok.*;
import lombok.experimental.Wither;
import pl.draciel.slackify.utility.LocalDateTimeAttributeConverter;

import javax.annotation.Nonnull;
import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Wither
@Builder
@Entity
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@EqualsAndHashCode(exclude = {"id"})
@Setter(value = AccessLevel.PACKAGE)
@Table(name = "add_track_logs")
public class AddTrackLog {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Nonnull
    @Column(name = "requesting_user")
    private String requestingUser;

    @Nonnull
    @Column(name = "slack_user_id")
    private String slackUserId;

    @Nonnull
    @Column(name = "request")
    private String request;

    @Column(name = "uri")
    private String spotifyId;

    @Nonnull
    @Column(name = "added_at")
    @Convert(converter = LocalDateTimeAttributeConverter.class)
    private LocalDateTime addedAt;

    @Nonnull
    @Column(name = "deleted")
    private boolean deleted;

    @Nonnull
    public static AddTrackLog of(@Nonnull String requestingUser, @Nonnull String slackUserId, @Nonnull String request,
                                 String spotifyId, @Nonnull LocalDateTime addingDate) {
        return new AddTrackLog(0, requestingUser, slackUserId, request, spotifyId, addingDate, false);
    }
}
