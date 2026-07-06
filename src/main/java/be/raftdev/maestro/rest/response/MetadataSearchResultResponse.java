package be.raftdev.maestro.rest.response;

import be.raftdev.maestro.metadata.MetadataService;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.ALWAYS)
public record MetadataSearchResultResponse(int resultCount, List<MetadataService.SearchResult> results) {
    public MetadataSearchResultResponse(List<MetadataService.SearchResult> results) {
        this(results.size(), results);
    }
}
