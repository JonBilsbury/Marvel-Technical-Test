package com.marvel.demo.character;


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
	private Data data;

	@JsonProperty("data")
	public Data getData()
	{
		return data;
	}

	@JsonProperty("data")
	public void setData(Data data)
	{
		this.data = data;
	}
}
