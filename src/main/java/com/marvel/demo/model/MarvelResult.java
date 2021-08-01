package com.marvel.demo.model;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
		"data"
})
public class MarvelResult
{
	@JsonProperty("data")
	private MarvelData marvelData;

	@JsonProperty("data")
	public MarvelData getData()
	{
		return marvelData;
	}

	@JsonProperty("data")
	public void setData(MarvelData marvelData)
	{
		this.marvelData = marvelData;
	}
}
