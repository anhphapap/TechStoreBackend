package com.pap.tech.service;

import com.pap.tech.entity.Attribute;
import com.pap.tech.repository.AttributeRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AttributeService {
    AttributeRepository attributeRepository;
    public List<Attribute> listAttributes() {
        return attributeRepository.findAll();
    }
}
