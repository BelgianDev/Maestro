package be.raftdev.maestro.metadata.provider;

import be.raftdev.maestro.client.itunes.ItunesClient;
import be.raftdev.maestro.client.itunes.ItunesSearchResponse;
import be.raftdev.maestro.database.entity.ArtistEntity;
import be.raftdev.maestro.exception.provider.UnknownMetadataException;
import be.raftdev.maestro.metadata.MetadataProvider;
import be.raftdev.maestro.metadata.MetadataService;
import be.raftdev.maestro.metadata.MetadataType;
import be.raftdev.maestro.util.LogUtils;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * iTunes (Apple) metadata provider, using the free API for basic meta-data resolving
 */
@Component
public class ItunesMetadataProvider implements MetadataProvider {
    private static final Logger LOGGER = LogUtils.getLogger();
    private final ItunesClient itunesClient;

    private ItunesMetadataProvider(ItunesClient itunesClient) {
        this.itunesClient = itunesClient;
    }

    @Override
    public String identifier() {
        return "apple-itunes";
    }

    @Override
    public void search(@NotNull MetadataService.SearchContext context) {
        String mediaType = fromMetadataTypeToMediaType(context.type());

        ItunesSearchResponse response = this.itunesClient.search(context.query(), mediaType, 200);
        LOGGER.info("Retrieved {} results from iTunes", response.resultCount());

        for (ItunesSearchResponse.Result result : response.results()) {
            MetadataService.SearchResult searchResult = result.asResult(this);
            if (searchResult == null)
                LOGGER.error("Unknown wrapper type '{}': {}", result.wrapperType(), result);

            context.addResult(searchResult);
        }
    }

    @Override
    public ArtistEntity queryArtist(String id) {
        ItunesSearchResponse response = this.itunesClient.lookupArtist(id);
        ItunesSearchResponse.Result result = response.results().stream().filter(resultCandidate ->
                resultCandidate.getMetadataType() == MetadataType.ARTIST).findFirst().orElse(null);

        if (result == null)
            throw new UnknownMetadataException("Could not find iTunes artist with id '"+ id +"'");

        ArtistEntity.ArtistLink link = new ArtistEntity.ArtistLink("Apple Music", result.artistLinkUrl());
        return new ArtistEntity(result.artistName(), null, List.of(link));
    }

    /**
     * Transforms the {@link MetadataType} to the iTunes media type used to query the API.
     *
     * @param type metadata type to convert.
     *
     * @return the type used to query the iTunes API, or {@code null} if the type is not supported, or the input is null.
     */
    private @Nullable String fromMetadataTypeToMediaType(@Nullable MetadataType type) {
        if (type == null)
            return null;

        return switch (type) {
            case RELEASE -> "album";
            case ARTIST -> "musicArtist";
            case TRACK -> "song";
        };
    }
}
