package com.microel.speedtest.common.models;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class OffsetPaginator implements Pageable {
    private final int limit;
    private final int offset;
    private final Sort sort;

    public OffsetPaginator(int limit, int offset, Sort sort) {
        this.sort = sort;
        if (limit < 1) {
            throw new IllegalArgumentException("Limit must not be less than one!");
        }
        if (offset < 0) {
            throw new IllegalArgumentException("Offset index must not be less than zero!");
        }
        this.limit = limit;
        this.offset = offset;
    }

    @Override
    public int getPageNumber() {
        return offset / limit;
    }

    @Override
    public int getPageSize() {
        return limit;
    }

    @Override
    public long getOffset() {
        return offset;
    }

    @Override
    public Sort getSort() {
        return sort;
    }

    @Override
    public Pageable next() {
        return new OffsetPaginator(getPageSize(), (int) (getOffset() + getPageSize()), sort);
    }

    public Pageable previous() {
        return hasPrevious() ?
                new OffsetPaginator(getPageSize(), (int) (getOffset() - getPageSize()), sort) : this;
    }

    @Override
    public Pageable previousOrFirst() {
        return hasPrevious() ? previous() : first();
    }

    @Override
    public Pageable first() {
        return new OffsetPaginator(getPageSize(), 0, sort);
    }

    @Override
    public Pageable withPage(int pageNumber) {
        return new OffsetPaginator(getPageSize(), getPageNumber()*getPageSize(), sort);
    }

    @Override
    public boolean hasPrevious() {
        return offset > limit;
    }
}
