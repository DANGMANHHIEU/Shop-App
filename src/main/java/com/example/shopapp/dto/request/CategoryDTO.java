package com.example.shopapp.dto.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CategoryDTO {

    @NotEmpty(message = "Category's name cannot be empty")
    private String name;
}
