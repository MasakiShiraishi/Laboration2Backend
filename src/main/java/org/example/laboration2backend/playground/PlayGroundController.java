package org.example.laboration2backend.playground;

import lombok.extern.slf4j.Slf4j;
import org.example.laboration2backend.dto.PlaceDto;
import org.example.laboration2backend.entity.Playground;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin
@Slf4j
public class PlayGroundController {

    private final PlaygroundService playgroundService;

    public PlayGroundController(PlaygroundService playgroundService) {
        this.playgroundService = playgroundService;
    }

    @GetMapping("/playgrounds")
    @Retryable(value = {Exception.class}, maxAttempts = 3, backoff = @Backoff(delay = 2000))
    public List<APlace> allPlaygrounds(@RequestParam(required = false) Map<String, String> queryParams) {
        return playgroundService.all().stream()
                .map(p ->
                        new APlace(
                                p.getCoordinate().getPosition().getLat(),
                                p.getCoordinate().getPosition().getLon())
                ).toList();
    }


    @GetMapping("/playgrounds/search")
    @Retryable(value = {Exception.class}, maxAttempts = 3, backoff = @Backoff(delay = 2000))
    public List<PlaceDto> searchPlaygrounds(@RequestParam Double lat,
                                            @RequestParam Double lon,
                                            @RequestParam Double radius) {
        List<Playground> playgrounds = playgroundService.getPlaygroundsWithinRadius(lat, lon, radius);
        return playgrounds.stream() .flatMap(p
                -> playgroundService.getPlaceByPlaygroundId(p.getId()).stream())
                .map(PlaceDto::fromPlace)
                .toList();
    }

    @PostMapping("/playgrounds")
    @Retryable(value = {Exception.class}, maxAttempts = 3, backoff = @Backoff(delay = 2000))
    public ResponseEntity<Void> addPlayground(@RequestBody Location location) {
        Playground p = playgroundService.createNewPlayGround(location.lat(), location.lon());
        return ResponseEntity.created(URI.create("/playgrounds/" + p.getId())).build();
    }

    @Recover
    public ResponseEntity<Void> recover(Exception e) {
        log.error("Error during operation: {}", e.getMessage()); return ResponseEntity.status(500).build(); }
}
