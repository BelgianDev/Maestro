package be.raftdev.maestro.database.entity;

import be.raftdev.maestro.metadata.MetadataProvider;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.JdbcTypeCode;

import java.sql.Types;

@Entity
@Table(name = "track_identity",
        uniqueConstraints = {
            @UniqueConstraint(
                    name = "uk_provider_identifier",
                    columnNames = { "provider", "provider_id" }
            )
        }
)
public class TrackIdentity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "provider", nullable = false)
    @JdbcTypeCode(Types.CHAR)
    private String provider;

    @Column(name = "provider_id", nullable = false)
    private String providerId;

    @ManyToOne(optional = false)
    @JoinColumn(name = "reference", nullable = false)
    private TrackEntity track;

    protected TrackIdentity() {
    }

    public TrackIdentity(@NotNull MetadataProvider provider, @NotNull String id, @NotNull TrackEntity entity) {
        this.provider = provider.identifier();
        this.providerId = id;
        this.track = entity;
    }

    public long getId() {
        return this.id;
    }

    public String getProvider() {
        return this.provider;
    }

    public String getProviderId() {
        return this.providerId;
    }

    public TrackEntity getTrack() {
        return this.track;
    }
}
