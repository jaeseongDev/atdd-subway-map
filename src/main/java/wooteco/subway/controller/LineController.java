package wooteco.subway.controller;

import java.net.URI;
import java.util.List;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import wooteco.subway.controller.dto.request.LineCreateRequestDto;
import wooteco.subway.controller.dto.request.LineEditRequestDto;
import wooteco.subway.controller.dto.request.SectionCreateRequestDto;
import wooteco.subway.controller.dto.response.LineCreateResponseDto;
import wooteco.subway.controller.dto.response.LineFindAllResponseDto;
import wooteco.subway.controller.dto.response.LineFindResponseDto;
import wooteco.subway.service.LineService;

@RestController
@RequestMapping("/lines")
public class LineController {

    private final LineService lineService;

    public LineController(LineService lineService) {
        this.lineService = lineService;
    }

    @PostMapping("")
    public ResponseEntity<LineCreateResponseDto> createLine(
        @RequestBody LineCreateRequestDto lineRequest) {
        LineCreateResponseDto lineResponse = lineService.createLine(lineRequest);
        return ResponseEntity.created(
            URI.create("/lines/" + lineResponse.getId())
        ).body(lineResponse);
    }

    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<LineFindAllResponseDto>> showLines() {
        List<LineFindAllResponseDto> lineResponses = lineService.showLines();
        return ResponseEntity.ok().body(lineResponses);
    }

    @GetMapping(value = "/{lineId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<LineFindResponseDto> showLine(@PathVariable Long lineId) {
        LineFindResponseDto lineResponse = lineService.showLine(lineId);
        return ResponseEntity.ok().body(lineResponse);
    }

    @PostMapping(value = "/{lineId}/sections")
    public ResponseEntity<Void> createLine(@PathVariable Long lineId,
        @RequestBody SectionCreateRequestDto requestDto) {
        lineService.createSectionInLine(
            lineId,
            requestDto.getUpStationId(),
            requestDto.getDownStationId(),
            requestDto.getDistance()
        );
        return ResponseEntity.created(null).build();
    }


    @PutMapping(value = "/{lineId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> editLine(@PathVariable Long lineId,
        @RequestBody LineEditRequestDto request) {
        lineService.editLine(lineId, request);
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).build();
    }

    @DeleteMapping("/{lineId}")
    public ResponseEntity<Void> deleteLine(@PathVariable Long lineId) {
        lineService.deleteLine(lineId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{lineId}/sections")
    public ResponseEntity<Void> deleteSectionInLine(@PathVariable Long lineId,
        @RequestParam("stationId") Long stationId) {
        lineService.deleteSectionInLine(lineId, stationId);
        return ResponseEntity.noContent().build();
    }
}
