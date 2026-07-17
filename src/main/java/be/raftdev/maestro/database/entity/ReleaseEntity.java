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
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "releases")
public class ReleaseEntity {

    @Id
    @UuidGenerator
    @Column(name = "id", nullable = false, unique = true)
    private UUID identifier;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "type", nullable = false)
    private ReleaseType type;

    @Column(name = "release_date")
    private LocalDate releaseDate;

    @Column(name = "track_count", nullable = false)
    private Integer trackCount;

    @Column(name = "disc_count", nullable = false)
    private Integer discCount;

    @ManyToOne(optional = false)
    @JoinColumn(name = "artist_id", nullable = false)
    private ArtistEntity artist;

    @OneToMany(
            mappedBy = "release",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<TrackEntity> tracks;

    protected ReleaseEntity() {}

    public ReleaseEntity(@NotBlank String name, @NotNull ReleaseType type, @NotNull LocalDate releaseDate, @Positive int trackCount, @Positive int discCount, @NotNull ArtistEntity artist) {
        this.name = name;
        this.type = type;
        this.releaseDate = releaseDate;
        this.trackCount = trackCount;
        this.discCount = discCount;
        this.artist = artist;
    }

    public UUID getIdentifier() {
        return identifier;
    }

    public String getName() {
        return name;
    }

    public ReleaseType getType() {
        return type;
    }

    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    public ArtistEntity getArtist() {
        return artist;
    }

    public Integer getTrackCount() {
        return trackCount;
    }

    public Integer getDiscCount() {
        return discCount;
    }

    public void setName(@NotNull String name) {
        this.name = name;
    }

    public void setType(@NotNull ReleaseType type) {
        this.type = type;
    }

    public void setReleaseDate(@NotNull LocalDate releaseDate) {
        this.releaseDate = releaseDate;
    }

    public void setArtist(@NotNull ArtistEntity artist) {
        this.artist = artist;
    }

    public void setTrackCount(@Positive int trackCount) {
        this.trackCount = trackCount;
    }

    public void setDiscCount(@Positive int discCount) {
        this.discCount = discCount;
    }

    public enum ReleaseType {
        ALBUM,
        EP,
        SINGLE,
        BROADCAST
    }
}
