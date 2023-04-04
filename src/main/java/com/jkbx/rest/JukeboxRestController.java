package com.jkbx.rest;

import com.jkbx.dto.JukeboxDto;
import com.jkbx.service.JukeboxService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/api/jukebox")
@Slf4j
@RequiredArgsConstructor
public class JukeboxRestController {

    private final JukeboxService jukeboxService;

    @GetMapping("/supported")
    public ResponseEntity<Collection<JukeboxDto>> findAllSupported(
            @RequestParam(value = "settingId", required = true) String settingId,
            @RequestParam(value = "model", required = false) String model,
            @RequestParam(value = "offset", required = false) Integer offset,
            @RequestParam(value = "limit", required = false) Integer limit
    ) {
        try{
            log.info("JukeboxRestController::findAllSupported -> settingId={}, model={}, offset={}, limit={}",
                    settingId, model, offset, limit);

            List<JukeboxDto> supported = jukeboxService.fetchSupportedJukeboxes(settingId, model, offset, limit);

            return ResponseEntity.status(HttpStatus.OK).body(supported);

        } catch (Exception e){
            log.error("Error -> ", e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
