package be.raftdev.maestro.database.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UuidGenerator;
import org.hibernate.type.SqlTypes;
import tools.jackson.databind.node.JsonNodeFactory;
import tools.jackson.databind.node.ObjectNode;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "tracks")
public class TrackEntity {

    @Id
    @UuidGenerator
    @Column(name = "id", nullable = false, unique = true)
    private UUID identifier;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "track_number", nullable = false)
    private Integer trackNumber;

    @Column(name = "disc_number", nullable = false)
    private Integer discNumber;

    @Column(name = "track_length", nullable = false)
    private Integer trackLength;

    @ManyToOne(optional = false)
    @JoinColumn(name = "artist_id", nullable = false)
    private ArtistEntity artist;

    @ManyToOne(optional = false)
    @JoinColumn(name = "release_id", nullable = false)
    private ReleaseEntity release;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "metadata_provider_data", nullable = false)
    private ObjectNode providerData;

    protected TrackEntity() {}

    public TrackEntity(@NotBlank String name, @Positive int discNumber, @Positive int trackNumber, @Positive int trackLength, @NotNull ArtistEntity artist, @NotNull ReleaseEntity release) {
        this.name = name;
        this.discNumber = discNumber;
        this.trackNumber = trackNumber;
        this.trackLength = trackLength;
        this.artist = artist;
        this.release = release;

        this.providerData = new ObjectNode(JsonNodeFactory.instance);
    }

    public UUID getIdentifier() {
        return identifier;
    }

    public String getName() {
        return name;
    }

    public Integer getTrackNumber() {
        return trackNumber;
    }

    public Integer getDiscNumber() {
        return discNumber;
    }

    public Integer getTrackLength() {
        return trackLength;
    }

    public ArtistEntity getArtist() {
        return artist;
    }

    public ReleaseEntity getRelease() {
        return release;
    }

    public ObjectNode getProviderData() {
        return providerData;
    }

    public void setName(@NotBlank String name) {
        this.name = name;
    }

    public void setTrackNumber(@Positive int trackNumber) {
        this.trackNumber = trackNumber;
    }

    public void setDiscNumber(@Positive int discNumber) {
        this.discNumber = discNumber;
    }

    public void setTrackLength(@Positive int trackLength) {
        this.trackLength = trackLength;
    }

    public void setArtist(@NotNull ArtistEntity artist) {
        this.artist = artist;
    }

    public void setRelease(@NotNull ReleaseEntity release) {
        this.release = release;
    }
}
