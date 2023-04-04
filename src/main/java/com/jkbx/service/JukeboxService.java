package com.jkbx.service;

import com.jkbx.NotFoundException;
import com.jkbx.dto.JukeboxDto;
import com.jkbx.dto.SettingDto;
import com.jkbx.dto.SettingsDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class JukeboxService {


    private String settingsResourceUrl
            = "http://my-json-server.typicode.com/touchtunes/tech-assignment/settings";

    private String jukeboxResourceUrl
            = "http://my-json-server.typicode.com/touchtunes/tech-assignment/jukes";

    public List<JukeboxDto> fetchSupportedJukeboxes(String settingId, String model, Integer offset, Integer limit) {
        SettingDto settingDto = retrieveSettings(settingId);
        log.info("---> Settings: "+settingDto);
        List<JukeboxDto> supportedJukeboxes = retrieveJukeBoxes(settingDto, model, offset, limit);
        log.info("---> jukeboxes: "+supportedJukeboxes.size());
        return supportedJukeboxes;
    }

    private SettingDto retrieveSettings(String settingId) {
        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<SettingsDto> response
                = restTemplate.getForEntity(settingsResourceUrl, SettingsDto.class);

        return response.getBody()
                .getSettings()
                .stream()
                .filter(settingDto -> !StringUtils.isBlank(settingId) && settingId.equals(settingDto.getId()))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Setting not found"))
                ;

    }

    private List<JukeboxDto> retrieveJukeBoxes(SettingDto settingDto, String model, Integer offset, Integer limit) {
        RestTemplate restTemplate = new RestTemplate();

        long maxLimit = limit != null ? limit : Integer.MAX_VALUE;
        long skip = offset != null ? offset : 0;

        ResponseEntity<JukeboxDto[]> response
                = restTemplate.getForEntity(jukeboxResourceUrl, JukeboxDto[].class);

        return Arrays.stream(response.getBody())
                .filter(jukeboxDto ->
                        jukeboxDto.getComponents().stream()
                                .map(componentDto -> componentDto.getName())
                                .collect(Collectors.toList())
                        .containsAll(settingDto.getRequires()))
                .filter(jukeboxDto -> StringUtils.isBlank(model) || model.equals(jukeboxDto.getModel()))
                .skip(skip)
                .limit(maxLimit)
                .collect(Collectors.toList());
    }
}
