package be.raftdev.maestro.database.entity;

import be.raftdev.maestro.metadata.MetadataProvider;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.JdbcTypeCode;

import java.sql.Types;

@Entity
@Table(name = "artist_identity",
        uniqueConstraints = {
            @UniqueConstraint(
                    name = "uk_provider_identifier",
                    columnNames = { "provider", "provider_id" }
            )
        }
)
public class ArtistIdentity {

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
    private ArtistEntity artist;

    protected ArtistIdentity() {
    }

    public ArtistIdentity(@NotNull MetadataProvider provider, @NotNull String id, @NotNull ArtistEntity entity) {
        this.provider = provider.identifier();
        this.providerId = id;
        this.artist = entity;
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

    public ArtistEntity getArtist() {
        return this.artist;
    }
}
