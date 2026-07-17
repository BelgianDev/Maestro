package be.raftdev.maestro.client.itunes;

import be.raftdev.maestro.metadata.MetadataProvider;
import be.raftdev.maestro.metadata.MetadataService;
import be.raftdev.maestro.metadata.MetadataType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.annotation.Nullable;

import java.util.List;

public record ItunesSearchResponse(int resultCount, List<Result> results) {

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Result(
            // ImplComment: iTunes also provides a censored version of collection and track names, which we don't really need but good to know if ever needed.

            // Common
            String wrapperType,
            String artistName,
            Long artistId,
            String primaryGenreName,

            // ImplComment: iTunes also provides AMG ids, if ever needed.

            // Artist Only
            String artistType,
            Integer primaryGenreId,
            String artistLinkUrl,

            // Track & Album
            String collectionName,
            Long collectionId,

            String artistViewUrl,
            String collectionViewUrl,

            // ImplComment: iTunes also provides artwork URLs for different sizes, but we will only use the 100x100 one.
            String artworkUrl100,

            Integer trackCount,
            String country,
            String releaseDate,

            // Track Only
            String kind,
            Long trackId,
            String trackName,

            String trackViewUrl,
            String previewUrl,

            Integer discCount,
            Integer diskNumber,
            Integer trackNumber,

            Long trackTimeMillis
    ) {

        public @Nullable MetadataType getMetadataType() {
            return switch (this.wrapperType) {
                case "track" -> MetadataType.TRACK;
                case "collection" -> MetadataType.RELEASE;
                case "artist" -> MetadataType.ARTIST;
                default -> null;
            };
        }

        public MetadataService.SearchResult asResult(MetadataProvider provider) {
            if (this.getMetadataType() == null)
                return null;

            return switch (this.getMetadataType()) {
                case ARTIST -> this.asArtistResult(provider);
                case RELEASE -> this.asReleaseResult(provider);
                case TRACK -> this.asTrackResult(provider);
            };
        }

        public MetadataService.SearchResult asArtistResult(MetadataProvider provider) {
            String name = this.artistName();
            String id = String.valueOf(this.artistId());
            String link = this.artistLinkUrl();

            return new MetadataService.SearchResult(provider, id, name, MetadataType.ARTIST, null, link, null);
        }

        public MetadataService.SearchResult asReleaseResult(MetadataProvider provider) {
            String name = this.collectionName();
            String id = String.valueOf(this.collectionId());
            String link = this.collectionViewUrl();
            String assetUrl = this.artworkUrl100();

            return new MetadataService.SearchResult(provider, id, name, MetadataType.RELEASE, null, link, assetUrl);
        }

        public MetadataService.SearchResult asTrackResult(MetadataProvider provider) {
            String name = this.trackName();
            String id = String.valueOf(this.trackId());
            String link = this.trackViewUrl();
            String assetUrl = this.artworkUrl100();

            return new MetadataService.SearchResult(provider, id, name, MetadataType.TRACK, null, link, assetUrl);
        }
    }
}
