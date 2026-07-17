package be.raftdev.maestro.database.entity;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UuidGenerator;
import org.hibernate.type.SqlTypes;
import org.springframework.boot.context.properties.bind.DefaultValue;
import tools.jackson.databind.node.JsonNodeFactory;
import tools.jackson.databind.node.ObjectNode;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "artist")
public class ArtistEntity {

    @Id
    @UuidGenerator
    @Column(name = "id", nullable = false, unique = true)
    private UUID identifier;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "links")
    private List<ArtistLink> links;

    @OneToMany(
            mappedBy = "artist",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<ReleaseEntity> releases;

    protected ArtistEntity() {}

    public ArtistEntity(String name, @Nullable String description, List<ArtistLink> links) {
        this.name = name;
        this.description = description;
        this.links = new ArrayList<>(links);
    }

    public UUID getIdentifier() {
        return identifier;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public List<ArtistLink> getLinks() {
        return links;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLinks(List<ArtistLink> links) {
        this.links = links;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public record ArtistLink(@NotNull String name, @NotNull String url) {
    }
}
