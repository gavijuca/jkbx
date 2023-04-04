package com.jkbx.dto;

import lombok.Data;

import java.util.Collection;

@Data
public class JukeboxDto {
    private String id;
    private String model;
    private Collection<ComponentDto> components;
}
