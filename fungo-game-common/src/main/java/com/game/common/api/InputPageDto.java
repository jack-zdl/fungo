package com.game.common.api;

import com.game.common.dto.InputDto;
import com.game.common.validate.an.Min;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.omg.CORBA.PRIVATE_MEMBER;

import javax.validation.constraints.Max;
import java.io.Serializable;

/**
 * @author scm
 */
@SuppressWarnings("serial")
@ApiModel(description = "基础请求参数")
public class InputPageDto extends InputDto implements Serializable {

	@ApiModelProperty(allowEmptyValue = false, required = true, value = "当前页码")
	@Min(1)
	private int pageNum = 1;

	@ApiModelProperty(allowEmptyValue = false, required = true, value = "页大小")
	@Min(1)
	@Max(value = 20,message = "分页查询数目有误")
	private int pageSize = 10;

	@Min(1)
	@Max(value = 20,message = "分页查询数目有误")
	private int limit=10;//等同pageSize
	private int page=1;//pageNum
	private int sort;//排序

	private String filter = "";//过滤查询条件


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
	public Integer getSort() {
		return sort;
	}
	public void setSort(int sort) {
		this.sort = sort;
	}
	public String getFilter() {
		return filter;
	}
	public void setFilter(String filter) {
		this.filter = filter;
	}

}
