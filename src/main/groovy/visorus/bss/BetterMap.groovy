package visorus.bss

import grails.converters.JSON
import grails.web.servlet.mvc.GrailsParameterMap
import org.apache.commons.collections.map.LazyMap
import org.grails.web.json.JSONArray
import org.grails.web.json.JSONObject

import javax.servlet.http.HttpServletRequest

class BetterMap implements Map {

	private static final class Null {

		@Override
		protected final Object clone() {
			return this
		}

		@Override
		boolean equals(Object object) {
			return object == null || object == this
		}

		@Override
		int hashCode() {
			return 0
		}

		@Override
		String toString() {
			return "null"
		}
	}

	private HashMap<String, Object> map
	static final Object NULL = new Null()

	BetterMap() {
		map = new HashMap<>()
	}


	BetterMap(HttpServletRequest request, GrailsParameterMap params) {
		this.map = this.toMap(params)
		if (request.getContentType().contains("application/json")) {
			this.map.putAll(this.toMap((JSONObject) JSON.parse(request)))
		} else {
			throw new Exception("Unidentified ContentType: ${contentType}")
		}
	}

	BetterMap(GrailsParameterMap params) {
		this.map = this.toMap(params)
	}

	BetterMap(HttpServletRequest request) {
		String contentType = request.getContentType()
		if (contentType?.contains("application/json")) {
			this.map = this.toMap((JSONObject) JSON.parse(request))
		} else {
			throw new Exception("Unidentified ContentType: ${contentType}. Is the body empty?")
		}
	}

	BetterMap(JSONObject json) {
		this.map = this.toMap(json)
	}

	BetterMap(Map<?, ?> m) {
		if (m == null) {
			this.map = new HashMap<String, Object>()
		} else {
			this.map = this.toMap(m)
		}
	}

	BetterMap(Object o) {
		if (m == null) {
			this.map = new HashMap<String, Object>()
		} else {
			this.map = this.toMap(o as Map<?, ?>)
		}
	}

	BetterMap(LazyMap m) {
		if (m == null) {
			this.map = new HashMap<String, Object>()
		} else {
			this.map = this.toMap(m)
		}
	}

	BetterMap(JSONArray json) {
		this.map = new HashMap<String, Object>(json.size())

		for (int i = 0; i < json.size(); i++) {

			this.map.put(String.valueOf(i), this.toMap(json.getJSONObject(i)))
		}
	}

	static Object wrap(Object object) {
		try {
			if (object == null) {
				return NULL
			}
			if (object instanceof BetterMap || object instanceof BetterMapList
					|| object instanceof Byte || object instanceof Character
					|| object instanceof Short || object instanceof Integer
					|| object instanceof Long || object instanceof Boolean
					|| object instanceof Float || object instanceof Double
					|| object instanceof String || object instanceof BigInteger
					|| object instanceof BigDecimal || object instanceof Enum) {
				return object
			}
			if (object instanceof LazyMap) {
				return new BetterMap(object)
			}
			if (object instanceof LinkedHashMap) {
				return new BetterMap(object)
			}
			if (object instanceof ArrayList) {
				return new BetterMapList(object)
			}
			if (object instanceof Collection) {
				Collection<?> coll = (Collection<?>) object
				return new BetterMapList(coll)
			}
			if (object instanceof JSONObject) {
				return new BetterMap((JSONObject) object)
			}
			if (object instanceof JSONArray) {
				return new BetterMapList((JSONArray) object)
			}
			throw new MapException("com.test.BetterMap > objeto del tipo ${object.getClass()} no definido en metodo wrap()")
		} catch (Exception ignored) {
			return null
		}
	}

	HashMap<String, Object> toMap(JSONObject json) {
		HashMap<String, Object> results = new HashMap<String, Object>()
		Object value

		Iterator it = json.entrySet().iterator()
		while (it.hasNext()) {
			Entry entry = (Entry) it.next()
			if (entry.getValue() == null || NULL.equals(entry.getValue())) {
				value = null
			} else if (entry.getValue() instanceof LinkedHashMap) {
				value = wrap(entry.getValue())
			} else if (entry.getValue() instanceof JSONObject) {
				value = wrap(entry.getValue())
			} else if (entry.getValue() instanceof JSONArray) {
				value = wrap(entry.getValue())
			} else {
				value = entry.getValue()
			}
			results.put(String.valueOf(entry.getKey()), value)
		}
		return results
	}

	HashMap<String, Object> toMap(Map<?, ?> m) {
		HashMap<String, Object> results = new HashMap<String, Object>()
		Object value

		Iterator it = m.entrySet().iterator()
		while (it.hasNext()) {
			Entry entry = (Entry) it.next()
			if (entry.getValue() == null || NULL.equals(entry.getValue())) {
				value = null
			} else if (entry.getValue() instanceof ArrayList) {
				value = wrap(entry.getValue())
			} else if (entry.getValue() instanceof LinkedHashMap) {
				value = wrap(entry.getValue())
			} else if (entry.getValue() instanceof LazyMap) {
				value = wrap(entry.getValue())
			} else if (entry.getValue() instanceof JSONObject) {
				value = wrap(entry.getValue())
			} else if (entry.getValue() instanceof JSONArray) {
				value = wrap(entry.getValue())
			} else {
				value = entry.getValue()
			}
			results.put(String.valueOf(entry.getKey()), value)
		}
		return results
	}

	BetterMap optObject(String key) {
		Object object = this.opt(key)
		return object instanceof BetterMap ? (BetterMap) object : null
	}

	BetterMapList optArray(String key) {
		Object o = this.opt(key)
		return o instanceof BetterMapList ? (BetterMapList) o : null
	}

	Object get(String key) throws MapException {
		if (key == null) {
			throw new MapException("Null key.")
		}
		Object object = this.opt(key)
		if (object == null) {
			throw new MapException("Valor [" + quote(key) + "] no encontrado.")
		}
		return object
	}

	boolean getBoolean(String key) throws MapException {
		Object o = get(key)
		if (o.equals(Boolean.FALSE) ||
				(o instanceof String &&
						((String) o).equalsIgnoreCase("false"))) {
			return false
		} else if (o.equals(Boolean.TRUE) ||
				(o instanceof String &&
						((String) o).equalsIgnoreCase("true"))) {
			return true
		}
		throw new MapException("valor [" + quote(key) + "] is not a Boolean.")
	}

	boolean optBoolean(String key) {
		return optBoolean(key, false)
	}

	boolean optBoolean(String key, boolean defaultValue) {
		try {
			return getBoolean(key)
		} catch (Exception ignored) {
			return defaultValue
		}
	}

	long getInt(String key) {
		Object o = get(key)
		return o instanceof Number ? ((Number) o).intValue() : (long) getDouble(key)
	}

	int optInt(String key) {
		return this.optInt(key, -1)
	}

	int optInt(String key, int defaultValue) {
		Object val = this.opt(key)
		if (NULL.equals(val)) {
			return defaultValue
		}
		if (val instanceof Number) {
			return ((Number) val).intValue()
		}

		if (val instanceof String) {
			try {
				return new BigDecimal((String) val).intValue()
			} catch (Exception ignored) {
				return defaultValue
			}
		}
		return defaultValue
	}

	long getLong(String key) {
		Object o = get(key)
		return o instanceof Number ? ((Number) o).longValue() : (long) getDouble(key)
	}

	long optLong(String key) {
		return this.optLong(key, -1)
	}

	long optLong(String key, long defaultValue) {
		Object val = this.opt(key)
		if (NULL.equals(val)) {
			return defaultValue
		}
		if (val instanceof Number) {
			return ((Number) val).longValue()
		}

		if (val instanceof String) {
			try {
				return new BigDecimal((String) val).longValue()
			} catch (Exception ignored) {
				return defaultValue
			}
		}
		return defaultValue
	}

	Long getInstance(String key, Object instance = null) throws Exception {
		if (this.optLong(key) != -1)
			return this.optLong(key)
		if (this.optObject(key) != null) {
			BetterMap obj = this.optObject(key)
			if (obj.optLong("id") != -1) {
				return obj.optLong("id")
			}
		}
		if (instance == null)
			throw new ObjectException("No se encuentra ID identificador con key $key")
		return instance.id
	}

	Date optDate(String key, Date defaultValue) {
		Object val = this.opt(key)
		if (NULL.equals(val)) {
			return defaultValue
		}
		if (val instanceof Number) {
			return new Date(((Number) val).longValue())
		}
		if (val instanceof Long) {
			return new Date(((Number) val).longValue())
		}
		if (val instanceof String) {
			try {
				return new Date(val as Long)
			} catch (Exception ignored) {
				return defaultValue
			}
		}
		return defaultValue
	}

	Date optDate(String key, Long defaultValue) {
		Object val = this.opt(key)
		if (NULL.equals(val)) {
			return new Date(defaultValue)
		}
		if (val instanceof Number) {
			return new Date(((Number) val).longValue())
		}
		if (val instanceof Long) {
			return new Date(((Number) val).longValue())
		}
		if (val instanceof String) {
			try {
				return new Date(val as Long)
			} catch (Exception ignored) {
				return new Date(defaultValue)
			}
		}
		return new Date(defaultValue)
	}

	float getFloat(String key) {
		Object o = get(key)
		return o instanceof Number ? ((Number) o).longValue() : (long) getDouble(key)
	}

	float optFloat(String key) {
		return this.optFloat(key, -1F)
	}

	float optFloat(String key, float defaultValue) {
		Object val = this.opt(key)
		if (NULL.equals(val)) {
			return defaultValue
		}
		if (val instanceof Number) {
			return ((Number) val).floatValue()
		}
		if (val instanceof String) {
			try {
				return Float.parseFloat((String) val)
			} catch (Exception ignored) {
				return defaultValue
			}
		}
		return defaultValue
	}

	Double getDouble(String key) {
		Object o = get(key)
		try {
			return o instanceof Number ? ((Number) o).doubleValue() : Double.parseDouble((String) o)
		} catch (Exception ignored) {
			throw new MapException("[" + quote(key) + "] is not a number.")
		}
	}

	float optDouble(String key) {
		return this.optFloat(key, -1F)
	}

	float optDouble(String key, double defaultValue) {
		Object val = this.opt(key)
		if (NULL.equals(val)) {
			return defaultValue
		}
		if (val instanceof Number) {
			return ((Number) val).doubleValue()
		}
		if (val instanceof String) {
			try {
				return Double.parseDouble((String) val)
			} catch (Exception ignored) {
				return defaultValue
			}
		}
		return defaultValue
	}

	String optString(String key) {
		return this.optString(key, "")
	}

	String optString(String key, String defaultValue) {
		Object object = this.opt(key)
		String value = NULL.equals(object) ? defaultValue : object.toString()
		String[] ignore = ["access_token", "username", "password", "max", "offset", "sort", "order", "id", "desc", "version", "accessToken"]
		if (ignore.contains(key))
			return value
		return value?.trim()
//		return value?.toUpperCase()?.trim()
	}


	Object opt(String key) {
		return key == null ? null : this.map.get(key)
	}

	static String quote(String string) {
		StringWriter sw = new StringWriter()
		synchronized (sw.getBuffer()) {
			try {
				return quote(string, sw).toString()
			} catch (IOException ignored) {
				// will never happen - we are writing to a string writer
				return ""
			}
		}
	}

	static Writer quote(String string, Writer w) throws IOException {
		if (string == null || string.isEmpty()) {
			w.write("\"\"")
			return w
		}

		char b
		char c = 0
		String hhhh
		int i
		int len = string.length()

		w.write('"')
		for (i = 0; i < len; i += 1) {
			b = c
			c = string.charAt(i)
			switch (c) {
				case '\\':
				case '"':
					w.write('\\')
					w.write(c)
					break
				case '/':
					if (b == '<') {
						w.write('\\')
					}
					w.write(c)
					break
				case '\b':
					w.write("\\b")
					break
				case '\t':
					w.write("\\t")
					break
				case '\n':
					w.write("\\n")
					break
				case '\f':
					w.write("\\f")
					break
				case '\r':
					w.write("\\r")
					break
				default:
					if (c < (' ' as char) || (c >= ('\u0080' as char) && c < ('\u00a0' as char))
							|| (c >= ('\u2000' as char) && c < ('\u2100' as char))) {
						w.write("\\u")
						hhhh = Integer.toHexString(c)
						w.write("0000", 0, 4 - hhhh.length())
						w.write(hhhh)
					} else {
						w.write(c)
					}
			}
		}
		w.write('"')
		return w
	}

	@Override
	int size() {
		return this.map.size()
	}

	@Override
	String toString() {
		try {
			return this.toString(0)
		} catch (Exception ignored) {
			return null
		}
	}

	String toString(int indentFactor) throws MapException {
		StringWriter w = new StringWriter()
		synchronized (w.getBuffer()) {
			return this.write(w, indentFactor, 0).toString()
		}
	}

	Writer write(Writer writer) throws MapException {
		return this.write(writer, 0, 0)
	}

	static void testValidity(Object o) throws MapException {
		if (o != null) {
			if (o instanceof Double) {
				if (((Double) o).isInfinite() || ((Double) o).isNaN()) {
					throw new MapException(
							"com.test.BetterMap does not allow non-finite numbers.")
				}
			} else if (o instanceof Float) {
				if (((Float) o).isInfinite() || ((Float) o).isNaN()) {
					throw new MapException(
							"com.test.BetterMap does not allow non-finite numbers.")
				}
			}
		}
	}

	static String numberToString(Number number) throws MapException {
		if (number == null) {
			throw new MapException("Null pointer")
		}
		testValidity(number)

		// Shave off trailing zeros and decimal point, if possible.

		String string = number.toString()
		if (string.indexOf('.') > 0 && string.indexOf('e') < 0
				&& string.indexOf('E') < 0) {
			while (string.endsWith("0")) {
				string = string.substring(0, string.length() - 1)
			}
			if (string.endsWith(".")) {
				string = string.substring(0, string.length() - 1)
			}
		}
		return string
	}

	static final Writer writeValue(Writer writer, Object value, int indentFactor, int indent) throws MapException, IOException {
		if (value == null || value.equals(null)) {
			writer.write("null")
			/**} else if (value instanceof JSONString) {Object o
			 try {o = ((JSONString) value).toJSONString()} catch (Exception ignored) {throw new JSONException(e)}writer.write(o != null ? o.toString() : quote(value.toString()))
			 */
		} else if (value instanceof Number) {
			// not all Numbers may match actual JSON Numbers. i.e. fractions or Imaginary
			final String numberAsString = numberToString((Number) value)
			try {
				// Use the BigDecimal constructor for its parser to validate the format.
				@SuppressWarnings("unused")
				BigDecimal testNum = new BigDecimal(numberAsString)
				// Close enough to a JSON number that we will use it unquoted
				writer.write(numberAsString)
			} catch (NumberFormatException ignored) {
				// The Number value is not a valid JSON number.
				// Instead we will quote it as a string
				quote(numberAsString, writer)
			}
		} else if (value instanceof Boolean) {
			writer.write(value.toString())
		} else if (value instanceof Enum<Object>) {
			writer.write(quote(((Enum<?>) value).name()))
		} else if (value instanceof BetterMap) {
			((BetterMap) value).write(writer, indentFactor, indent)
		} else if (value instanceof BetterMapList) {
			((BetterMapList) value).write(writer, indentFactor, indent)
		} else if (value instanceof Map) {
			Map<?, ?> map = (Map<?, ?>) value
			new BetterMap(map).write(writer, indentFactor, indent)
		} else if (value instanceof Collection) {
			Collection<?> coll = (Collection<?>) value
			new BetterMapList(coll).write(writer, indentFactor, indent)
		} else if (value.getClass().isArray()) {
			new BetterMapList(value).write(writer, indentFactor, indent)
		} else {
			quote(value.toString(), writer)
		}
		return writer
	}

	static final void indent(Writer writer, int indent) throws IOException {
		for (int i = 0; i < indent; i += 1) {
			writer.write(' ')
		}
	}

	Writer write(Writer writer, int indentFactor, int indent) throws MapException {
		try {
			boolean commanate = false
			final int length = this.size()
			writer.write('{')

			if (length == 1) {
				final Entry<String, ?> entry = this.map.entrySet().iterator().next()
				final String key = entry.getKey()
				writer.write(quote(key))
				writer.write(':')
				if (indentFactor > 0) {
					writer.write(' ')
				}
				try {
					writeValue(writer, entry.getValue(), indentFactor, indent)
				} catch (Exception ignored) {
					throw new MapException("Unable to write JSONObject value for key: " + key, ignored)
				}
			} else if (length != 0) {
				final int newindent = indent + indentFactor
				for (final Entry<String, ?> entry : this.map.entrySet()) {
					if (commanate) {
						writer.write(',')
					}
					if (indentFactor > 0) {
						writer.write('\n')
					}
					this.indent(writer, newindent)
					final String key = entry.getKey()
					writer.write(quote(key))
					writer.write(':')
					if (indentFactor > 0) {
						writer.write(' ')
					}
					try {
						writeValue(writer, entry.getValue(), indentFactor, newindent)
					} catch (Exception ignored) {
						throw new MapException("Unable to write JSONObject value for key: " + key, ignored)
					}
					commanate = true
				}
				if (indentFactor > 0) {
					writer.write('\n')
				}
				this.indent(writer, indent)
			}
			writer.write('}')
			return writer
		} catch (IOException ignored) {
			throw new MapException(ignored)
		}
	}

	@Override
	boolean isEmpty() {
		return map.isEmpty()
	}

	@Override
	boolean containsKey(Object key) {
		return map.containsKey(key)
	}

	@Override
	boolean containsValue(Object value) {
		return map.containsValue(value)
	}

	@Override
	Object get(Object key) {
		return map.get(key)
	}

	@Override
	Object put(Object key, Object value) {
		return map.put(String.valueOf(key), value)
	}

	Object put(Object key, Map value) {
		return map.put(String.valueOf(key), new BetterMap(value))
	}

	@Override
	Object remove(Object key) {
		return map.remove(key)
	}

	@Override
	void putAll(Map m) {
		map.putAll(m)
	}

	@Override
	void clear() {
		map.clear()
	}

	@Override
	Set keySet() {
		return map.keySet()
	}

	@Override
	Collection values() {
		return map.values()
	}

	@Override
	Set<Entry> entrySet() {
		return map.entrySet()
	}
}
