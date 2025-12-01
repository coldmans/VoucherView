package inu.voucherview.response;

import inu.voucherview.dto.PostDto;
import inu.voucherview.util.Pagination;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class PostListResponse {
    private List<PostDto> posts;
    private Pagination pagination;
}
