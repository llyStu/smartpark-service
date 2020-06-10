package com.vibe.utils;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class DateYMDSerializer extends JsonSerializer<Date> {
	@Override
	public void serialize(Date date, JsonGenerator jsonGenerator, SerializerProvider arg2)
			throws IOException, JsonProcessingException {
		jsonGenerator.writeString(new SimpleDateFormat("yyyy-MM-dd").format(date));
	}
}
