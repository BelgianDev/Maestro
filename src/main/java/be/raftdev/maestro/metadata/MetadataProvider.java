package be.raftdev.maestro.metadata;

import be.raftdev.maestro.database.entity.ArtistEntity;
import be.raftdev.maestro.database.entity.ReleaseEntity;
import jakarta.validation.constraints.NotNull;

/**
 * Metadata provider, from where music metadata is retrieved from before being transformed.
 */
public interface MetadataProvider {

    /**
     * Unique identifier of the metadata provider.
     *
     * @return unique identifier of the metadata provider.
     */
    String identifier();

    /**
     * Search request, used when looking for a new release / artist to add.
     *
     * @param context search context contains the query, the filters, results should be added to it.
     */
    void search(@NotNull MetadataService.SearchContext context);

    /**
     * Query an artist and retrieve the metadata.
     *
     * @param id provider identifier.
     *
     * @return resulting artist metadata.
     *
     * @throws be.raftdev.maestro.exception.provider.UnknownMetadataException if the artist cannot be found.
     */
    @NotNull ArtistEntity queryArtist(@NotNull String id);
}
