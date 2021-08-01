package com.marvel.demo.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
		"id",
		"name",
		"description",
		"thumbnail"
})
public class Result
{

	@JsonProperty("id")
	private String id;
	@JsonProperty("name")
	private String name;
	@JsonProperty("description")
	private String description;
	@JsonProperty("thumbnail")
	private Thumbnail thumbnail;

	@JsonProperty("id")
	public String getId()
	{
		return id;
	}

	@JsonProperty("id")
	public void setId(String id)
	{
		this.id = id;
	}

	@JsonProperty("name")
	public String getName()
	{
		return name;
	}

	@JsonProperty("name")
	public void setName(String name)
	{
		this.name = name;
	}

	@JsonProperty("description")
	public String getDescription()
	{
		return description;
	}

	@JsonProperty("description")
	public void setDescription(String description)
	{
		this.description = description;
	}

	@JsonProperty("thumbnail")
	public Thumbnail getThumbnail()
	{
		return thumbnail;
	}

	@JsonProperty("thumbnail")
	public void setThumbnail(Thumbnail thumbnail)
	{
		this.thumbnail = thumbnail;
	}
}