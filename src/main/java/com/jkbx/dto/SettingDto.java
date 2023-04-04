package com.jkbx.dto;

import lombok.Data;
import lombok.ToString;

import java.util.Collection;

@Data
@ToString
public class SettingDto {
    private String id;
    private Collection<String> requires;
}
