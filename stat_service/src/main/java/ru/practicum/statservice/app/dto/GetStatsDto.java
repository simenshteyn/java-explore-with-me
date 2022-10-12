package ru.practicum.statservice.app.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetStatsDto {
    private String start;
    private String end;
    private List<String> uris;
    private Boolean unique;
}
