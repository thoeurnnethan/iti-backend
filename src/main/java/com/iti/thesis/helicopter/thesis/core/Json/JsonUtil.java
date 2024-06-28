// Source code is decompiled from a .class file using FernFlower decompiler.
package com.iti.thesis.helicopter.thesis.core.Json;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.MissingNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.POJONode;
import com.iti.thesis.helicopter.thesis.util.StringUtil;

public abstract class JsonUtil {
   private static final ObjectMapper objectMapper = new ObjectMapper();

   public JsonUtil() {
   }

   public static ObjectMapper getObjectMapper() {
      return objectMapper;
   }

   public static JsonNodeFactory getNodeFactory() {
      return objectMapper.getNodeFactory();
   }

   public static ObjectNode objectNode() {
      return getNodeFactory().objectNode();
   }

//   public static String toJson(Map<String, Object> map) {
//      StringWriter out = new StringWriter();
//
//      String var3;
//      try {
//         objectMapper.writeValue(out, map);
//         String var2 = out.toString();
//         return var2;
//      } catch (Exception var7) {
//         var3 = "{}";
//      } finally {
//         IOUtil.closeQuietly(out);
//      }
//
//      return var3;
//   }

//   public static String toJson(Object pojo) {
//      return toJson(pojo, false);
//   }

//   public static String toJson(Object pojo, boolean prettyPrint) {
//      StringWriter sw = new StringWriter();
//
//      String var4;
//      try {
//         JsonGenerator jg = objectMapper.getJsonFactory().createJsonGenerator(sw);
//         if (prettyPrint) {
//            jg.useDefaultPrettyPrinter();
//         }
//
//         objectMapper.writeValue(jg, pojo);
//         return sw.toString();
//      } catch (Exception var8) {
//         var4 = "{}";
//      } finally {
//         IOUtil.closeQuietly(sw);
//      }
//
//      return var4;
//   }

	public static <T> T toObject(String json, Class<T> clazz) throws IOException {
		try {
			return objectMapper.readValue(json, clazz);
		} catch (JsonParseException var3) {
			throw var3;
		} catch (JsonMappingException var4) {
			throw var4;
		} catch (IOException var5) {
			throw var5;
		}
	}
	
	public static <T> T toObject(JsonParser jsonParser, Class<T> clazz) throws IOException {
		return objectMapper.readValue(jsonParser, clazz);
	}
	
	public static ObjectNode toObjectNode(Object object) {
		return (ObjectNode)objectMapper.convertValue(object, ObjectNode.class);
	}
	
	public static ObjectNode putValue(ObjectNode node, String name, Object value) {
		if (value instanceof String) {
			node.put(name, String.valueOf(value));
		} else if (value.getClass() == Boolean.class) {
			node.put(name, (Boolean)value);
		} else if (value instanceof Number) {
			Number v = (Number)value;
			if (v.getClass() == Integer.class) {
				node.put(name, v.intValue());
			} else if (v.getClass() == Long.class) {
				node.put(name, v.longValue());
			} else if (v.getClass() == Float.class) {
				node.put(name, v.floatValue());
			} else if (v.getClass() == Double.class) {
				node.put(name, v.doubleValue());
			}
		} else if (value instanceof BigDecimal) {
			node.put(name, (BigDecimal)value);
		} else if (value instanceof byte[]) {
			node.put(name, (byte[])((byte[])value));
		} else if (value instanceof ArrayNode) {
			node.putArray(name).addAll((ArrayNode)value);
		} else if (value instanceof ObjectNode) {
			node.putObject(name).putAll((ObjectNode)value);
		} else {
			node.putPOJO(name, value);
		}
		return node;
	}
	
	public static Object getValue(JsonNode node, String name) {
		Object obj = null;
		JsonNode valueNode = node.path(name);
		if (valueNode != null && !valueNode.isMissingNode() && !valueNode.isNull()) {
			if (valueNode.isBinary()) {
				try {
					byte[] bytes = valueNode.binaryValue();
					obj = bytes;
				} catch (IOException var5) {
					obj = "".toString();
				}
			} else if (valueNode.isTextual()) {
				obj = valueNode.textValue();
			} else if (valueNode.isNumber()) {
				if (valueNode.isInt()) {
					obj = new Integer(valueNode.intValue());
				} else if (valueNode.isLong()) {
					obj = new Long(valueNode.longValue());
				} else if (valueNode.isDouble()) {
					obj = new Double(valueNode.doubleValue());
				}
			} else if (valueNode.isBoolean()) {
				obj = new Boolean(valueNode.booleanValue());
			} else if (valueNode.isArray()) {
				obj = (ArrayNode)valueNode;
			} else if (valueNode.isObject()) {
				obj = (ObjectNode)valueNode;
			} else if (valueNode.isPojo()) {
				obj = ((POJONode)valueNode).getPojo();
			}
		} else {
			obj = "".toString();
		}
	
		return obj;
	}
	
	public static JsonNode find(JsonNode baseNode, String path) {
		if (baseNode == null) {
			return MissingNode.getInstance();
		} else if (baseNode.isMissingNode()) {
			return baseNode;
		} else if (path == null) {
			return baseNode;
		} else {
			path.replaceAll("[/]+$", "");
			path.replaceAll("^[/]+", "");
			return find(baseNode, StringUtil.split(path, "/"));
		}
	}

	public static JsonNode find(JsonNode baseNode, String[] path) {
		if (baseNode == null) {
			return MissingNode.getInstance();
		} else if (path != null && !StringUtil.isBlank(path[0]) && !baseNode.isMissingNode()) {
			if (path.length < 2) {
				return baseNode.path(path[0]);
			} else {
				ArrayList<String> list = new ArrayList(path.length);
				String[] var3 = path;
				int var4 = path.length;
				for(int var5 = 0; var5 < var4; ++var5) {
					String p = var3[var5];
					list.add(p);
				}
				
			return find(baseNode, list);
			}
		} else {
			return baseNode;
		}
	}

	public static JsonNode find(JsonNode baseNode, ArrayList<String> path) {
		try {
			if (baseNode == null) {
				return MissingNode.getInstance();
			} else if (path != null && !path.isEmpty() && !StringUtil.isBlank((CharSequence)path.get(0)) && !baseNode.isMissingNode()) {
				return path.size() < 2 ? baseNode.path((String)path.get(0)) : find(baseNode.path((String)path.remove(0)), path);
			} else {
				return baseNode;
			}
		} catch (Exception var3) {
			return MissingNode.getInstance();
		}
	}

	static {
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		objectMapper.configure(SerializationFeature.WRITE_NULL_MAP_VALUES, false);
		objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
	}
}
