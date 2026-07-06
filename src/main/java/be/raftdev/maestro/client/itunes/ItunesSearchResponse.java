package be.raftdev.maestro.client.itunes;

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
                case "collection" -> MetadataType.ALBUM;
                case "artist" -> MetadataType.ARTIST;
                default -> null;
            };
        }
    }
}
