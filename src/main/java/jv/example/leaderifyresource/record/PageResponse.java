package jv.example.leaderifyresource.record;

import java.util.List;

public record PageResponse<T>(
        List<T> content,
        boolean empty,
        boolean first,
        boolean last,
        int number,
        int numberOfElements,
        int size,
        long totalElements,
        int totalPages,
        Pageable pageable,
        Sort sort
) {
    public record Pageable(
            long offset,
            int pageNumber,
            int pageSize,
            boolean paged,
            boolean unpaged,
            Sort sort
    ) {}

    public record Sort(
            boolean empty,
            boolean sorted,
            boolean unsorted
    ) {}
}
