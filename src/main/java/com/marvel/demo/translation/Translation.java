package com.marvel.demo.translation;

import com.fasterxml.jackson.annotation.*;
import lombok.ToString;

import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
		"translatedText"
})
@ToString
public class Translation
{
	@JsonProperty("translatedText")
	private String translatedText;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<>();

	@JsonProperty("translatedText")
	public String getTranslatedText()
	{
		return translatedText;
	}

	@JsonProperty("translatedText")
	public void setTranslatedText(String translatedText)
	{
		this.translatedText = translatedText;
	}

	@JsonAnyGetter
	public Map<String, Object> getAdditionalProperties()
	{
		return this.additionalProperties;
	}

	@JsonAnySetter
	public void setAdditionalProperty(String name, Object value)
	{
		this.additionalProperties.put(name, value);
	}

}