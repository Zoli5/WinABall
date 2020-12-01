package com.zszuri.winaball.responses;

import java.util.List;

public class WinnersPageableResponse {
	
	private Integer actialPage;
	private Integer numberOfEntitiesOnPages;
	private Integer totalPages;
	private List<WinnerResponse> responseList;
	
	public WinnersPageableResponse(Integer actialPage, Integer numberOfEntitiesOnPages, Integer totalPages,
			List<WinnerResponse> responseList) {
		super();
		this.actialPage = actialPage;
		this.numberOfEntitiesOnPages = numberOfEntitiesOnPages;
		this.totalPages = totalPages;
		this.responseList = responseList;
	}

	public Integer getActialPage() {
		return actialPage;
	}

	public void setActialPage(Integer actialPage) {
		this.actialPage = actialPage;
	}

	public Integer getNumberOfEntitiesOnPages() {
		return numberOfEntitiesOnPages;
	}

	public void setNumberOfEntitiesOnPages(Integer numberOfEntitiesOnPages) {
		this.numberOfEntitiesOnPages = numberOfEntitiesOnPages;
	}

	public Integer getTotalPages() {
		return totalPages;
	}

	public void setTotalPages(Integer totalPages) {
		this.totalPages = totalPages;
	}

	public List<? extends Object> getResponseList() {
		return responseList;
	}

	public void setResponseList(List<WinnerResponse> responseList) {
		this.responseList = responseList;
	}
}
