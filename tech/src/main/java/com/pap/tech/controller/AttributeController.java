package com.pap.tech.controller;

import com.pap.tech.dto.response.ApiResponse;
import com.pap.tech.entity.Attribute;
import com.pap.tech.service.AttributeService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/attributes")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AttributeController {
    AttributeService attributeService;
    @GetMapping
    public ApiResponse<List<Attribute>> listAttributes() {
        return ApiResponse.<List<Attribute>>builder()
                .result(attributeService.listAttributes())
                .build();
    }
}
