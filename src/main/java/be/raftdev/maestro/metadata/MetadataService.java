package be.raftdev.maestro.metadata;

import be.raftdev.maestro.database.entity.ArtistEntity;
import be.raftdev.maestro.database.entity.ArtistIdentity;
import be.raftdev.maestro.database.repo.*;
import be.raftdev.maestro.exception.provider.NoSuchProviderException;
import be.raftdev.maestro.util.LogUtils;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.annotation.Nullable;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;

import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

@Service
@Validated
public class MetadataService {
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final Executor REQUEST_EXECUTOR = new ThreadPoolExecutor(0, 6,
            30, TimeUnit.SECONDS,
            new SynchronousQueue<>());

    private final Map<String, MetadataProvider> providers;

    // Entities
    private final ArtistRepository artists;
    private final ReleaseRepository releases;
    private final TrackRepository tracks;

    // Identities
    private final ArtistIdentityRepository artistIdentities;
    private final ReleaseIdentityRepository releaseIdentities;
    private final TrackIdentityRepository trackIdentityRepository;

    public MetadataService(List<MetadataProvider> providers, ArtistRepository artists, ReleaseRepository releases, TrackRepository tracks, ArtistIdentityRepository artistIdentities, ReleaseIdentityRepository releaseIdentities, TrackIdentityRepository trackIdentityRepository) {
        this.providers = providers.stream()
                .collect(Collectors.toMap(MetadataProvider::identifier, provider -> provider));

        this.artists = artists;
        this.releases = releases;
        this.tracks = tracks;

        this.artistIdentities = artistIdentities;
        this.releaseIdentities = releaseIdentities;
        this.trackIdentityRepository = trackIdentityRepository;
    }

    /**
     * Execute a search on all providers.
     *
     * @param ctx search context
     * @param providers providers to search, if none is set, search all providers.
     */
    public void searchMetadata(@NotNull SearchContext ctx, @Nullable List<String> providers) {
        Collection<MetadataProvider> searchProviders =
                (providers == null || providers.isEmpty())
                        ? this.providers.values()
                        : providers.stream()
                        .map(this.providers::get)
                        .filter(Objects::nonNull)
                        .toList();

        LOGGER.info("Starting search for '{}'", ctx.query);

        CompletableFuture<?>[] futures = searchProviders.stream()
                .map(provider -> CompletableFuture.runAsync(() -> provider.search(ctx), REQUEST_EXECUTOR)
                        .whenComplete((ignored, ex) -> {
                            if (ex != null) {
                                LOGGER.error("Provider '{}' failed to search for query '{}': ",
                                        provider.identifier(), ctx.query(), ex);
                            }
                        }))
                .toArray(CompletableFuture[]::new);

        CompletableFuture.allOf(futures).join();

        LOGGER.info("Search done, {} results found.", ctx.results.size());
    }

    @Transactional
    public UUID addMetadata(@NotBlank String providerId, @NotBlank String identifier, @NotNull MetadataType type) {
        MetadataProvider provider = this.providers.get(providerId);
        if (provider == null)
            throw new NoSuchProviderException();

        if (type != MetadataType.ARTIST)
            throw new UnsupportedOperationException("Not supported yet.");

        ArtistEntity entity = provider.queryArtist(identifier);
        ArtistIdentity identity = new ArtistIdentity(provider, identifier, entity);

        this.artists.save(entity);
        this.artistIdentities.save(identity);

        return entity.getIdentifier();
    }

    /**
     * Search context, object created when calling the search endpoint, shared by all providers during search.
     *
     * @implNote Some synchronization is needed on the mutable fields, this object is used by multiple threads.
     */
    @Validated
    public static class SearchContext {
        private final @NotNull String query;
        private final @Nullable MetadataType type;

        private final List<SearchResult> results;

        public SearchContext(@NotNull String query, @Nullable MetadataType type) {
            Assert.notNull(query, "Query may not be null!");
            Assert.hasText(query, "Query cannot be empty!");

            this.query = query;
            this.type = type;
            this.results = new ArrayList<>();
        }

        /**
         * Query of the search.
         *
         * @return query.
         */
        public @NotNull String query() {
            return this.query;
        }

        /**
         * Optional metadata type filter.
         *
         * @return {@code null} if the filter is not set, {@link MetadataType} if a filter is present.
         */
        public @Nullable MetadataType type() {
            return this.type;
        }

        /**
         * Add a result to the search.
         *
         * @param result result to add to the search.
         */
        public synchronized void addResult(@Valid @NotNull SearchResult result) {
            this.results.add(result);
        }

        /**
         * Retrieve the search results.
         *
         * @return an unmodifiable list containing the results.
         */
        public List<SearchResult> getResults() {
            return List.copyOf(this.results);
        }
    }

    /**
     * Represents a search result.
     *
     * @param name display name shown on the UI.
     * @param type type of metadata {@link MetadataType}
     * @param provider provider from which the result comes from.
     * @param providerId identifier of the metadata on the provider side. (ex: iTunes: 960778942 for Nick Leng)
     * @param extraInformation extra information to display to the user, used to help differentiate between different artists / albums with the same name.
     * @param externalUrl external url from the provider, useful when to check whether the artist / album is the one the user wants.
     * @param assetUrl asset url, used to display an asset to the user.
     */
    @JsonInclude(JsonInclude.Include.ALWAYS)
    public record SearchResult(
        @NotBlank String name,
        @NotNull MetadataType type,
        @NotBlank String provider,
        @NotBlank String providerId,
        @Nullable String extraInformation,
        @Nullable String externalUrl,
        @Nullable String assetUrl
    ) {
        public SearchResult(MetadataProvider provider, String id, String name, MetadataType type) {
            this(name, type, provider.identifier(), id, null, null, null);
        }

        public SearchResult(MetadataProvider provider, String id, String name, MetadataType type,
                            String extraInformation, String externalUrl, String assetUrl) {
            this(name, type, provider.identifier(), id, extraInformation, externalUrl, assetUrl);
        }
    }
}
