package ru.practicum.explorewithme.category;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.explorewithme.validator.OnCreate;
import ru.practicum.explorewithme.validator.OnUpdate;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "categories")
@Getter
@Setter
@NoArgsConstructor
public class Category {
    @Id
    @Column(name = "category_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotNull(message = "Id can't be null", groups = OnUpdate.class)
    private Long id;
    @NotBlank(message = "Name can't be blank", groups = {OnUpdate.class, OnCreate.class})
    @NotNull(message = "Name can't be null", groups = {OnUpdate.class, OnCreate.class})
    @Column(unique = true)
    private String name;
}
