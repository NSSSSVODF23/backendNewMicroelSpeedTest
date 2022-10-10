package com.microel.speedtest.common.models.filters;

import com.microel.speedtest.common.models.OffsetPaginator;
import com.microel.speedtest.common.models.QueryLimit;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Getter
@Setter
public class MatchingFactory {

    public static <T> Example<T> standardExample(T matchingObject) {
        return Example.of(matchingObject, ExampleMatcher.matching().withIgnoreNullValues().withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING).withIgnoreCase());
    }

    public static Pageable paginator(QueryLimit queryLimit, Sort sort) {
        return new OffsetPaginator(queryLimit.getLimit(),queryLimit.getOffset(), sort);
    }
}
