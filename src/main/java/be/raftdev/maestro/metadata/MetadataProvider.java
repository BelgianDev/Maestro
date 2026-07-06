package be.raftdev.maestro.metadata;

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
}
