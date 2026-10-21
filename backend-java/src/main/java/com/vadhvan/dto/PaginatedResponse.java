package com.vadhvan.dto;

import java.util.List;

public class PaginatedResponse<T> {
    private boolean success;
    private String message;
    private List<T> data;
    private Pagination pagination;

    public PaginatedResponse() {}

    public PaginatedResponse(boolean success, String message, List<T> data, Pagination pagination) {
        this.success = success;
        this.message = message;
        this.data = data;
        this.pagination = pagination;
    }

    public static class Pagination {
        private long total;
        private int page;
        private int limit;
        private int totalPages;
        private boolean hasNext;
        private boolean hasPrev;

        public Pagination() {}

        public Pagination(long total, int page, int limit, int totalPages, boolean hasNext, boolean hasPrev) {
            this.total = total;
            this.page = page;
            this.limit = limit;
            this.totalPages = totalPages;
            this.hasNext = hasNext;
            this.hasPrev = hasPrev;
        }

        public long getTotal() { return total; }
        public void setTotal(long total) { this.total = total; }
        public int getPage() { return page; }
        public void setPage(int page) { this.page = page; }
        public int getLimit() { return limit; }
        public void setLimit(int limit) { this.limit = limit; }
        public int getTotalPages() { return totalPages; }
        public void setTotalPages(int totalPages) { this.totalPages = totalPages; }
        public boolean isHasNext() { return hasNext; }
        public void setHasNext(boolean hasNext) { this.hasNext = hasNext; }
        public boolean isHasPrev() { return hasPrev; }
        public void setHasPrev(boolean hasPrev) { this.hasPrev = hasPrev; }
    }

    public static <T> PaginatedResponse<T> of(List<T> data, long total, int page, int limit) {
        int totalPages = limit > 0 ? (int) Math.ceil((double) total / limit) : 1;
        Pagination p = new Pagination(total, page, limit, totalPages, (long) page * limit < total, page > 1);
        return new PaginatedResponse<>(true, "Success", data, p);
    }

    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public List<T> getData() { return data; }
    public void setData(List<T> data) { this.data = data; }
    public Pagination getPagination() { return pagination; }
    public void setPagination(Pagination pagination) { this.pagination = pagination; }
}
