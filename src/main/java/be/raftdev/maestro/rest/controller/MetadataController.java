package be.raftdev.maestro.rest.controller;

import be.raftdev.maestro.metadata.MetadataService;
import be.raftdev.maestro.metadata.MetadataType;
import be.raftdev.maestro.rest.ApiResponse;
import be.raftdev.maestro.rest.response.MetadataSearchResultResponse;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

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

    @PostMapping("add")
    public ApiResponse add(@RequestBody AddRequestBody body) {
        UUID identifier = this.metadataService.addMetadata(body.provider, body.identifier, body.type);
        return ApiResponse.success(identifier);
    }

    public record AddRequestBody(@NotBlank String provider, @NotBlank String identifier, @NotNull MetadataType type) {}
}
