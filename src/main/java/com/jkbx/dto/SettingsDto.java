package com.jkbx.dto;

import lombok.Data;

import java.util.Collection;

@Data

public class SettingsDto {
    private Collection<SettingDto> settings;
}
