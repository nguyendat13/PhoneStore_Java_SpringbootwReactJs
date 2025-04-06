package com.backend.backend_java.config;

import org.hibernate.collection.spi.PersistentBag;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.spi.MappingContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        // Converter để xử lý PersistentBag -> List
        Converter<PersistentBag, List<?>> persistentBagToListConverter = new Converter<>() {
            @Override
            public List<?> convert(MappingContext<PersistentBag, List<?>> context) {
                return new ArrayList<>(context.getSource());
            }
        };

        modelMapper.addConverter(persistentBagToListConverter);

        return modelMapper;
    }
}
