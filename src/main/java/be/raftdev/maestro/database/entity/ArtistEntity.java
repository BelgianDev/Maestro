package be.raftdev.maestro.database.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UuidGenerator;
import org.hibernate.type.SqlTypes;
import tools.jackson.databind.node.JsonNodeFactory;
import tools.jackson.databind.node.ObjectNode;

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

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "links")
    private List<ArtistLink> links;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "metadata_provider_data", nullable = false)
    private ObjectNode providerData;

    @OneToMany(
            mappedBy = "releases",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<ReleaseEntity> releases;

    protected ArtistEntity() {}

    public ArtistEntity(String name, List<ArtistLink> links) {
        this.name = name;
        this.links = links;
        this.providerData = new ObjectNode(JsonNodeFactory.instance);
    }

    public UUID getIdentifier() {
        return identifier;
    }

    public String getName() {
        return name;
    }

    public List<ArtistLink> getLinks() {
        return links;
    }

    public ObjectNode getProviderData() {
        return providerData;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLinks(List<ArtistLink> links) {
        this.links = links;
    }

    public record ArtistLink(@NotNull String name, @NotNull String url) {
    }
}
