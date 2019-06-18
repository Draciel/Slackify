package pl.draciel.slackify.domain;

import lombok.*;
import lombok.experimental.Wither;
import pl.draciel.slackify.utility.LocalDateTimeAttributeConverter;

import javax.annotation.Nonnull;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.ZonedDateTime;

@Data
@Wither
@Entity
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@EqualsAndHashCode(exclude = {"id"})
@Table(name = "remove_track_logs")
public class RemoveTrackLog {

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

    @Nonnull
    @Column(name = "removed_at")
    @Convert(converter = LocalDateTimeAttributeConverter.class)
    private ZonedDateTime removedAt;

    @Nonnull
    public static RemoveTrackLog of(@Nonnull final String requestingUser, @Nonnull final String slackUserId,
                                    @Nonnull final String request, @Nonnull final ZonedDateTime removingDate) {
        return new RemoveTrackLog(0, requestingUser, slackUserId, request, removingDate);
    }
}
