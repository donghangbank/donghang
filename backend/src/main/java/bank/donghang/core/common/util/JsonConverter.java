package bank.donghang.core.common.util;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonConverter {
	private static final ObjectMapper objectMapper = new ObjectMapper();

	public static String jsonConvert(Map<String, Object> map) throws JsonProcessingException {
		return objectMapper.writeValueAsString(map);
	}

	public static Map<String, Object> mapConvert(String json) throws JsonProcessingException {
		TypeReference<HashMap<String, Object>> typeReference = new TypeReference<HashMap<String, Object>>() {
		};

		return objectMapper.readValue(json, typeReference);
	}
}
