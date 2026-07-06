package be.raftdev.maestro.rest.controller;

import be.raftdev.maestro.metadata.MetadataService;
import be.raftdev.maestro.metadata.MetadataType;
import be.raftdev.maestro.rest.response.MetadataSearchResultResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/metadata/")
public class MetadataController {
    private final MetadataService metadataService;

    public MetadataController(MetadataService metadataService) {
        this.metadataService = metadataService;
    }

    @GetMapping("search")
    public MetadataSearchResultResponse search(@RequestParam String query, @RequestParam(required = false) MetadataType type, @RequestParam(required = false) List<String> providers) {
        MetadataService.SearchContext context = new MetadataService.SearchContext(query, type);
        this.metadataService.searchMetadata(context, providers);
        return new MetadataSearchResultResponse(context.getResults());
    }
}
