package com.jerry.springReview.web.dto;

import org.junit.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class GreetingResponseDtoTest {
    @Test
    public void lombok_function_test() {
        String name = "test";
        int amount = 100;

        GreetingResponseDto dto = new GreetingResponseDto(name, amount);

        assertThat(dto.getName()).isEqualTo(name);
        assertThat(dto.getAmount()).isEqualTo(amount);
    }
}
