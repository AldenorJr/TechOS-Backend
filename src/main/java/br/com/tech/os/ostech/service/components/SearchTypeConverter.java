package br.com.tech.os.ostech.service.components;

import br.com.tech.os.ostech.enums.SearchType;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
public class SearchTypeConverter implements Converter<String, SearchType> {

    @Override
    public SearchType convert(@NonNull String source) {
        return SearchType.fromValue(source);
    }
}