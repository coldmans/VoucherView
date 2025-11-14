package inu.voucherview.util;

import lombok.Getter;

@Getter
public class Pagination {
    private int page; // 현재 페이지 번호
    private int limit; // 페이지 당 보여줄 개수
    private int totalCount; // DB의 전체 데이터 개수

    private int totalPages; // 전체페이지 수
    private int offset; // DB 쿼리용 OFFSET

    private int displayPageCount = 10; // 화면 하단에 보여줄 페이지 번호 개수
    private int startPage;
    private int endPage;

    private boolean hasPrevious;
    private boolean hasNext;


    /**
     * 생성자: 페이지 계산에 필요한 모든 값을 받아서
     * 나머지 값들을 모두 계산합니다.
     */

    public Pagination(int page, int limit, int totalCount){
        this.page = (page <= 0) ? 1 : page;
        this.limit = (limit <= 0) ? 10 : limit;
        this.totalCount = totalCount;

        this.offset = (this.page - 1) * this.limit;

        if(this.totalCount == 0){
            this.totalPages = 1;
        } else {
            this.totalPages = (int) Math.ceil((double) this.totalCount / this.limit);
        }

        this.endPage = (int) (Math.ceil((double) this.page / this.displayPageCount)) * this.displayPageCount;
        this.startPage = (this.endPage - this.displayPageCount) + 1;

        if(this.endPage > this.totalPages){
            this.endPage = this.totalPages;
        }

        this.hasPrevious = (this.page > 1);
        this.hasNext = (this.page < this.totalPages);

    }

}
