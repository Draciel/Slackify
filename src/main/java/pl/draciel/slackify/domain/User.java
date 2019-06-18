package pl.draciel.slackify.domain;

import lombok.*;
import lombok.experimental.Wither;

import javax.annotation.Nonnull;
import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Data
@Wither
@Builder
@Entity
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@EqualsAndHashCode(exclude = {"id"})
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Nonnull
    @Column(name = "username")
    private String username;

    @Nonnull
    @Column(name = "slack_user_id")
    private String slackUserId;

    @Nonnull
    public static User of(@Nonnull final String username, @Nonnull final String slackUserId) {
        return new User(0, username, slackUserId);
    }
}
