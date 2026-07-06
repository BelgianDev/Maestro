package be.raftdev.maestro.metadata.provider;

import be.raftdev.maestro.client.itunes.ItunesClient;
import be.raftdev.maestro.client.itunes.ItunesSearchResponse;
import be.raftdev.maestro.metadata.MetadataProvider;
import be.raftdev.maestro.metadata.MetadataService;
import be.raftdev.maestro.metadata.MetadataType;
import be.raftdev.maestro.util.LogUtils;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;

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
            MetadataType type = result.getMetadataType();
            if (type == null)
                LOGGER.error("Unknown wrapper type '{}': {}", result.wrapperType(), result);

            MetadataService.SearchResult searchResult = switch (type) {
                case ARTIST -> this.handleArtist(result);
                case ALBUM -> this.handleAlbum(result);
                case TRACK -> this.handleTrack(result);
            };

            context.addResult(searchResult);
        }
    }

    public MetadataService.SearchResult handleArtist(ItunesSearchResponse.Result result) {
        String name = result.artistName();
        String id = String.valueOf(result.artistId());
        String link = result.artistLinkUrl();

        return new MetadataService.SearchResult(this, id, name, MetadataType.ARTIST, null, link, null);
    }

    public MetadataService.SearchResult handleAlbum(ItunesSearchResponse.Result result) {
        String name = result.collectionName();
        String id = String.valueOf(result.collectionId());
        String link = result.collectionViewUrl();
        String assetUrl = result.artworkUrl100();

        return new MetadataService.SearchResult(this, id, name, MetadataType.ALBUM, null, link, assetUrl);
    }

    public MetadataService.SearchResult handleTrack(ItunesSearchResponse.Result result) {
        String name = result.trackName();
        String id = String.valueOf(result.trackId());
        String link = result.trackViewUrl();
        String assetUrl = result.artworkUrl100();

        return new MetadataService.SearchResult(this, id, name, MetadataType.TRACK, null, link, assetUrl);
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
            case ALBUM -> "album";
            case ARTIST -> "musicArtist";
            case TRACK -> "song";
        };
    }

}
