package com.game.common.vo;


import com.game.common.dto.InputDto;
import com.game.common.validate.an.Min;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

public class AdminCollectionVo extends InputDto implements Serializable {

    /**
     * 合集id
     */
    private String id;

    @ApiModelProperty(allowEmptyValue = false, required = true, value = "当前页码")
    @Min(1)
    private int pageNum = 1;

    @ApiModelProperty(allowEmptyValue = false, required = true, value = "页大小")
    @Min(1)
    private int pageSize = 10;

    private int limit=10;//等同pageSize
    private int page=1;//pageNum

    public int getPageNum() {
        return pageNum;
    }
    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }
    public int getPageSize() {
        return pageSize;
    }
    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
    public int getLimit() {
        return limit;
    }
    public void setLimit(int limit) {
        this.limit = limit;
    }
    public int getPage() {
        return page;
    }
    public void setPage(int page) {
        this.page = page;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
