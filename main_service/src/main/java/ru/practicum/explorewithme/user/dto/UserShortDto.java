package ru.practicum.explorewithme.user.dto;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class UserShortDto {
    @NonNull
    private Long id;
    @NonNull
    private String name;
}
