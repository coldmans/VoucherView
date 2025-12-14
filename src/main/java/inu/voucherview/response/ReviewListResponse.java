package inu.voucherview.response;

import inu.voucherview.dto.ReviewDto;
import inu.voucherview.util.Pagination;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class ReviewListResponse {
    private final List<ReviewDto> reviews;
    private final Pagination pagination;
}
