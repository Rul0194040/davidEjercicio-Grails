package visorus.bss

import org.grails.web.json.JSONArray

import java.lang.reflect.Array

class BetterMapList implements List {

	private final ArrayList<Object> myArrayList

	BetterMapList() {
		this.myArrayList = new ArrayList<Object>()
	}

	BetterMapList(JSONArray json) {
		this.myArrayList = new ArrayList<Object>()
		for (Object o : json) {
			this.myArrayList.add(BetterMap.wrap(o))
		}
	}

	BetterMapList(Collection<?> collection) {
		if (collection == null) {
			this.myArrayList = new ArrayList<Object>()
		} else {
			this.myArrayList = new ArrayList<Object>(collection.size())
			for (Object o : collection) {
				this.myArrayList.add(BetterMap.wrap(o))
			}
		}
	}

	BetterMapList(Object array) throws MapException {
		this()
		if (array.getClass().isArray()) {
			int length = Array.getLength(array)
			this.myArrayList.ensureCapacity(length)
			for (int i = 0; i < length; i += 1) {
				this.push(BetterMap.wrap(Array.get(array, i)))
			}
		} else {
			throw new MapException("JSONArray initial value should be a string or collection or array.")
		}
	}

	@Override
	Iterator<Object> iterator() {
		return this.myArrayList.iterator()
	}

	Object opt(int index) {
		return (index < 0 || index >= this.size()) ? null : this.myArrayList.get(index)
	}

	BetterMap optObject(int index) {
		Object object = this.opt(index)
		return object instanceof BetterMap ? (BetterMap) object : null
	}

	BetterMapList optArray(int index) {
		Object o = this.opt(index)
		return o instanceof BetterMapList ? (BetterMapList) o : null
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
		StringWriter sw = new StringWriter()
		synchronized (sw.getBuffer()) {
			return this.write(sw, indentFactor, 0).toString()
		}
	}

//	Writer write(Writer writer) throws MapException {
//		return this.write(writer, 0, 0)
//	}

	Writer write(Writer writer, int indentFactor, int indent) throws MapException {
		try {
			boolean commanate = false
			int length = this.size()
			writer.write('[')

			if (length == 1) {
				try {
					BetterMap.writeValue(writer, this.myArrayList.get(0), indentFactor, indent)
				} catch (Exception e) {
					throw new MapException("Unable to write JSONArray value at index: 0", e)
				}
			} else if (length != 0) {
				final int newindent = indent + indentFactor

				for (int i = 0; i < length; i += 1) {
					if (commanate) {
						writer.write(',')
					}
					if (indentFactor > 0) {
						writer.write('\n')
					}
					BetterMap.indent(writer, newindent)
					try {
						BetterMap.writeValue(writer, this.myArrayList.get(i), indentFactor, newindent)
					} catch (Exception e) {
						throw new MapException("Unable to write JSONArray value at index: " + i, e)
					}
					commanate = true
				}
				if (indentFactor > 0) {
					writer.write('\n')
				}
				BetterMap.indent(writer, indent)
			}
			writer.write(']')
			return writer
		} catch (IOException e) {
			throw new MapException(e)
		}
	}

	@Override
	int size() {
		return myArrayList.size()
	}

	@Override
	boolean isEmpty() {
		return myArrayList.isEmpty()
	}

	@Override
	boolean contains(Object o) {
		return myArrayList.contains(o)
	}

	@Override
	Object[] toArray() {
		return myArrayList.toArray()
	}

	@Override
	Object[] toArray(Object[] a) {
		return myArrayList.toArray(a)
	}

	@Override
	boolean add(Object o) {
		return myArrayList.add(o)
	}

	@Override
	boolean remove(Object o) {
		return myArrayList.remove(o)
	}

	@Override
	boolean containsAll(Collection c) {
		return myArrayList.containsAll(c)
	}

	@Override
	boolean addAll(Collection c) {
		return myArrayList.addAll(c)
	}

	@Override
	boolean addAll(int index, Collection c) {
		return myArrayList.addAll(index, c)
	}

	@Override
	boolean removeAll(Collection c) {
		return myArrayList.removeAll(c)
	}

	@Override
	boolean retainAll(Collection c) {
		return myArrayList.retainAll(c)
	}

	@Override
	void clear() {
		myArrayList.clear()
	}

	@Override
	Object get(int index) {
		return myArrayList.get(index)
	}

	@Override
	Object set(int index, Object element) {
		return myArrayList.set(index, element)
	}

	@Override
	void add(int index, Object element) {
		myArrayList.add(index, element)
	}

	@Override
	Object remove(int index) {
		return myArrayList.remove(index)
	}

	@Override
	int indexOf(Object o) {
		return myArrayList.indexOf(o)
	}

	@Override
	int lastIndexOf(Object o) {
		return myArrayList.lastIndexOf(o)
	}

	@Override
	ListIterator listIterator() {
		return myArrayList.listIterator()
	}

	@Override
	ListIterator listIterator(int index) {
		return myArrayList.listIterator(index)
	}

	@Override
	List subList(int fromIndex, int toIndex) {
		return myArrayList.subList(fromIndex, toIndex)
	}
}
