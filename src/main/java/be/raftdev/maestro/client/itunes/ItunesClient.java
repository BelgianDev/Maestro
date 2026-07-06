package be.raftdev.maestro.client.itunes;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;

public interface ItunesClient {

    @GetExchange("/search?media=music")
    ItunesSearchResponse search(@RequestParam String term, @RequestParam(required = false) String entity, @RequestParam int limit);
}
