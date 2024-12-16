package io.github.cafeteru.gft.prices.infrastructure.adapter.in.api.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorDto {
    private String message;
}
