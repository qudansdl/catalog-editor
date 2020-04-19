package com.basicit.html2pdf.json;

/*
Copyright (c) 2002 JSON.org

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

The Software shall be used for Good, not Evil.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
*/

/*
 * Changes Copyright (c) 2006,2007 John Snyders under the same license terms above.
 *
 * Added support for dates. See JSONObject
 *
 * Make it StringTemplate friendly by implementing Collection. This requires adding
 * specific put methods that take JSONObject and JSONArray to avoid confusion with
 * Map and Collection.
 *
 * Want the ability to have ST render JSON text when it sees a JSONArray but
 * ST will prefer to treat it as a Collection iterate the values and render each one.
 * The getJSONString property allows ST to get the serialized string as the JSONString property.
 *
 * Removed JSONObject.NULL. See JSONObject for details.
 *
 * Removed opt* methods. I don't see a need for them since it is now OK to store
 * nulls in the array.
 *
 * Made changes to exception handling
 *
 * TODO remove flexible parsing?
 */
import java.io.IOException;
import java.io.Writer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;

/**
 * A JSONArray is an ordered sequence of values. Its external text form is a
 * string wrapped in square brackets with commas separating the values. The
 * internal form is an object having <code>get</code>
 * methods for accessing the values by index, and <code>put</code> methods for
 * adding or replacing values. The values can be any of these types:
 * <code>Boolean</code>, <code>JSONArray</code>, <code>JSONObject</code>,
 * <code>Number</code>, <code>String</code>, or <code>null</code>.
 * <p>
 * The constructor can convert a JSON text into a Java object. The
 * <code>toString</code> method converts to JSON text.
 * <p>
 * A <code>get</code> method returns a value at the given index.
 * <p>
 * The generic <code>get()</code> method returns an
 * object which you can cast or query for type. There are also typed
 * <code>get</code> methods that do type checking and type coersion for you.
 * <p>
 * The texts produced by the <code>toString</code> methods strictly conform to
 * JSON syntax rules. The constructors are more forgiving in the texts they will
 * accept:
 * <ul>
 * <li>An extra <code>,</code>&nbsp;<small>(comma)</small> may appear just
 *     before the closing bracket.</li>
 * <li>The <code>null</code> value will be inserted when there
 *     is <code>,</code>&nbsp;<small>(comma)</small> elision.</li>
 * <li>Strings may be quoted with <code>'</code>&nbsp;<small>(single
 *     quote)</small>.</li>
 * <li>Strings do not need to be quoted at all if they do not begin with a quote
 *     or single quote, and if they do not contain leading or trailing spaces,
 *     and if they do not contain any of these characters:
 *     <code>{ } [ ] / \ : , = ; #</code> and if they do not look like numbers
 *     and if they are not the reserved words <code>true</code>,
 *     <code>false</code>, or <code>null</code>.</li>
 * <li>Values can be separated by <code>;</code> <small>(semicolon)</small> as
 *     well as by <code>,</code> <small>(comma)</small>.</li>
 * <li>Numbers may have the <code>0-</code> <small>(octal)</small> or
 *     <code>0x-</code> <small>(hex)</small> prefix.</li>
 * <li>Comments written in the slashshlash, slashstar, and hash conventions
 *     will be ignored.</li>
 * </ul>

 * @author JSON.org
 * @version 2
 */
public class JSONArray implements Collection, Iterable {

    /**
     * Cache the SimpleDateFormat if it is used to covert dates.
     */
    private SimpleDateFormat mySimpleDateFormat;


    /**
     * The arrayList where the JSONArray's properties are kept.
     */
    private ArrayList myArrayList;


    /**
     * Construct an empty JSONArray.
     */
    public JSONArray() {
        this.myArrayList = new ArrayList();
    }

    /**
     * Construct a JSONArray from a JSONTokener.
     * @param x A JSONTokener
     * @throws JSONException If there is a syntax error.
     */
    public JSONArray(JSONTokener x) {
        this();
        if (x.nextClean() != '[') {
            throw x.syntaxError("A JSONArray text must start with '['");
        }
        if (x.nextClean() == ']') {
            return;
        }
        x.back();
        for (;;) {
            if (x.nextClean() == ',') {
                x.back();
                this.myArrayList.add(null);
            } else {
                x.back();
                this.myArrayList.add(x.nextValue());
            }
            switch (x.nextClean()) {
            case ';':
            case ',':
                if (x.nextClean() == ']') {
                    return;
                }
                x.back();
                break;
            case ']':
                return;
            default:
                throw x.syntaxError("Expected a ',' or ']'");
            }
        }
    }


    /**
     * Construct a JSONArray from a source JSON text.
     * @param string     A string that begins with
     * <code>[</code>&nbsp;<small>(left bracket)</small>
     *  and ends with <code>]</code>&nbsp;<small>(right bracket)</small>.
     *  @throws JSONException If there is a syntax error.
     */
    public JSONArray(String string) {
        this(new JSONTokener(string));
    }

    /**
     * Copy constructor.
     * Construct a JSONArray from another JSONArray.
     * @param ja     The other JSON array to copy. Must not be null
     */
    public JSONArray(JSONArray ja) {
        this.myArrayList = new ArrayList((Collection)ja);
    }

    /**
     * Range constructor.
     * Construct a JSONArray from a sub range of another JSONArray.
     * Copies the range from begin inclusive to end exclusive. begin must be
     * less than or equal to end and both must be >= 0.
     * @param ja     The other JSON array to copy. Must not be null
     * @param begin  The index in ja to start copying from
     * @param end    The index in ja to stop copying before
     * @throws IllegalArgumentException if begin is more than end
     * or either are negative
     */
    public JSONArray(JSONArray ja, int begin, int end) {
        this.myArrayList = new ArrayList();
        if (begin > end || begin < 0 || end < 0)
        {
            throw new IllegalArgumentException();
        }
        for (int i = begin; i < end; i++)
        {
            this.put(ja.get(i));
        }
    }

    /**
     * Construct a JSONArray from a Collection.
     * @param collection     A Collection.
     */
    public JSONArray(Collection collection) {
        this.myArrayList = (collection == null) ?
        	new ArrayList() :
	        new ArrayList(collection);
    }

    /**
     * Parse a date string in JavaScript Date.toUTCString format
     * @param s date string to parse
     * @return date resulting from parsing string
     * @throws ParseException
     */
    private Date stringToDate(String s) throws ParseException {
        SimpleDateFormat sdf = getSimpleDateFormat();
        return sdf.parse(s);
    }

    /**
     * Create and cache a simple date format that matches the
     * format used by JavaScript Date.toUTCString
     * @return a format for converting to and from UTC string format
     */
    private SimpleDateFormat getSimpleDateFormat()
    {
        if (mySimpleDateFormat == null)
        {
            mySimpleDateFormat = new SimpleDateFormat(JSONObject.JS_UTC_DATE_FORMAT);
        }
        return mySimpleDateFormat;
    }

    /**
     * Get the object value associated with an index.
     * @param index The index must be between 0 and length() - 1.
     * @return An object value.
     * @throws IndexOutOfBoundsException if index is out of bounds
     */
    public Object get(int index) {
        Object o = myArrayList.get(index);
        return o;
    }


    /**
     * Get the boolean value associated with an index.
     * The string values "true" and "false" are converted to boolean.
     *
     * @param index The index must be between 0 and length() - 1.
     * @return      The truth.
     * @throws JSONException If the value at the index is not convertable to boolean.
     */
    public boolean getBoolean(int index) {
        Object o = get(index);
        if (o != null) {
            if (o.equals(Boolean.FALSE) ||
                    (o instanceof String &&
                    ((String)o).equalsIgnoreCase("false"))) {
                return false;
            } else if (o.equals(Boolean.TRUE) ||
                    (o instanceof String &&
                    ((String)o).equalsIgnoreCase("true"))) {
                return true;
            }
        }
        throw new JSONException("JSONArray[" + index + "] is not a Boolean.");
    }


    /**
     * Get the value associated with an index as a Date.
     *
     * @param index The index must be between 0 and length() - 1.
     * @return      The date value.
     * @throws JSONException if the value is not valid date string.
     */
    public Date getDate(int index) throws JSONException {
        Object o = get(index);
        if (o != null) {
            try {
                if (o instanceof Date) {
                    return (Date)o;
                }
                else {
                    return stringToDate(o.toString());
                }
            } catch (ParseException e) {
                // fall through
            }
        }
        throw new JSONException("JSONObject[" + index +
            "] is not a date string.");
    }

    /**
     * Get the double value associated with an index.
     *
     * @param index The index must be between 0 and length() - 1.
     * @return      The value.
     * @throws   JSONException If the value cannot be converted to a number.
     */
    public double getDouble(int index) {
        Object o = get(index);
        if (o != null) {
            try {
                return o instanceof Number ?
                    ((Number)o).doubleValue() :
                    Double.valueOf((String)o).doubleValue();
            } catch (Exception e) {
                // fall through
            }
        }
        throw new JSONException("JSONArray[" + index +
            "] is not a number.");
    }


    /**
     * Get the int value associated with an index.
     *
     * @param index The index must be between 0 and length() - 1.
     * @return      The value.
     * @throws   JSONException If the value cannot be converted to a number.
     */
    public int getInt(int index) {
        Object o = get(index);
        if (o == null)
        {
            throw new JSONException("JSONObject[" + index +
            "] is not a number.");
        }
        return o instanceof Number ?
                ((Number)o).intValue() : (int)getDouble(index);
    }

    /**
     * Get the JSONArray associated with an index.
     * @param index The index must be between 0 and length() - 1.
     * @return      A JSONArray value.
     * @throws JSONException If the value is not a JSONArray
     */
    public JSONArray getJSONArray(int index) {
        Object o = get(index);
        if (o != null && o instanceof JSONArray) {
            return (JSONArray)o;
        }
        throw new JSONException("JSONArray[" + index +
                "] is not a JSONArray.");
    }


    /**
     * Get the JSONObject associated with an index.
     * @param index subscript
     * @return      A JSONObject value.
     * @throws JSONException If the value is not a JSONObject
     */
    public JSONObject getJSONObject(int index) {
        Object o = get(index);
        if (o != null && o instanceof JSONObject) {
            return (JSONObject)o;
        }
        throw new JSONException("JSONArray[" + index +
            "] is not a JSONObject.");
    }


    /**
     * Get the long value associated with an index.
     *
     * @param index The index must be between 0 and length() - 1.
     * @return      The value.
     * @throws   JSONException If the value cannot be converted to a number.
     */
    public long getLong(int index) {
        Object o = get(index);
        if (o == null)
        {
            throw new JSONException("JSONObject[" + index +
            "] is not a number.");
        }
        return o instanceof Number ?
                ((Number)o).longValue() : (long)getDouble(index);
    }


    /**
     * Get the string associated with an index.
     *
     * @param index The index must be between 0 and length() - 1.
     * @return      A string value.
     */
    public String getString(int index) {
        Object o = get(index);
        if (o == null)
        {
            return("null");
        }
        return o.toString();
    }


    /**
     * Determine if the value is null.
     * @param index The index must be between 0 and length() - 1.
     * @return true if the value at the index is null.
     */
    public boolean isNull(int index) {
        return get(index) == null;
    }


    /**
     * Make a string from the contents of this JSONArray. The
     * <code>separator</code> string is inserted between each element.
     * Warning: This method assumes that the data structure is acyclical.
     * @param separator A string that will be inserted between the elements.
     * @return a string.
     * @throws JSONException If the array contains an invalid number.
     */
    public String join(String separator) {
        int len = length();
        StringBuffer sb = new StringBuffer();

        for (int i = 0; i < len; i += 1) {
            if (i > 0) {
                sb.append(separator);
            }
            sb.append(JSONObject.valueToString(this.myArrayList.get(i)));
        }
        return sb.toString();
    }


    /**
     * Get the number of elements in the JSONArray, including nulls.
     *
     * @return The length (or size).
     */
    public int length() {
        return this.myArrayList.size();
    }


    /**
     * Append a boolean value. This increases the array's length by one.
     *
     * @param value A boolean value.
     * @return this.
     */
    public JSONArray put(boolean value) {
        put(value ? Boolean.TRUE : Boolean.FALSE);
        return this;
    }


    /**
     * Put a value in the JSONArray, where the value will be a
     * JSONArray which is produced from a Collection.
     * @param value	A Collection value.
     * @return		this.
     * @throws JSONException If a value is not finite.
     */
    public JSONArray put(Collection value) {
        put(new JSONArray(value));
        return this;
    }

    /**
     * Put a JSONArray value in the JSONArray.
     * @param value The array.
     * @return      this.
     */
    public JSONArray put(JSONArray value) {
        this.myArrayList.add(value);
        return this;
    }

    /**
     * Append a double value. This increases the array's length by one.
     *
     * @param value A double value.
     * @return this.
     * @throws JSONException if the value is not finite.
     */
    public JSONArray put(double value) {
        Double d = new Double(value);
        put(d);
        return this;
    }


    /**
     * Append an int value. This increases the array's length by one.
     *
     * @param value An int value.
     * @return this.
     */
    public JSONArray put(int value) {
        put(new Integer(value));
        return this;
    }


    /**
     * Append an long value. This increases the array's length by one.
     *
     * @param value A long value.
     * @return this.
     */
    public JSONArray put(long value) {
        put(new Long(value));
        return this;
    }


    /**
     * Put a value in the JSONArray, where the value will be a
     * JSONObject which is produced from a Map.
     * @param value	A Map value.
     * @return		this.
     */
    public JSONArray put(Map value) {
        put(new JSONObject(value));
        return this;
    }

    /**
     * Put a JSONObject value in the JSONArray.
     * @param value The object.
     * @return      this.
     */
    public JSONArray put(JSONObject value) {
        this.myArrayList.add(value);
        return this;
    }

    /**
     * Append an object value. This increases the array's length by one.
     * @param value An object value.  The value should be a
     *  Boolean, Double, Integer, JSONArray, JSONObject, Long, or String, or the
     *  JSONObject.NULL object.
     * @return this.
     */
    public JSONArray put(Object value) {
        JSONObject.testValidity(value);
        this.myArrayList.add(value);
        return this;
    }


    /**
     * Put or replace a boolean value in the JSONArray. If the index is greater
     * than the length of the JSONArray, then null elements will be added as
     * necessary to pad it out.
     * @param index The subscript.
     * @param value A boolean value.
     * @return this.
     * @throws IndexOutOfBoundsException If the index is negative.
     */
    public JSONArray put(int index, boolean value) {
        put(index, value ? Boolean.TRUE : Boolean.FALSE);
        return this;
    }


    /**
     * Put a value in the JSONArray, where the value will be a
     * JSONArray which is produced from a Collection.
     * @param index The subscript.
     * @param value	A Collection value.
     * @return		this.
     * @throws IndexOutOfBoundsException If the index is negative.
     * @throws JSONException If a value is not finite.
     */
    public JSONArray put(int index, Collection value) {
        put(index, new JSONArray(value));
        return this;
    }

    /**
     * Put a JSONArray value in the JSONArray.
     * @param index The subscript.
     * @param value The array.
     * @return      this.
     * @throws IndexOutOfBoundsException If the index is negative.
     */
    public JSONArray put(int index, JSONArray value) {
        put(index, (Object)value);
        return this;
    }


    /**
     * Put or replace a double value. If the index is greater than the length of
     *  the JSONArray, then null elements will be added as necessary to pad
     *  it out.
     * @param index The subscript.
     * @param value A double value.
     * @return this.
     * @throws IndexOutOfBoundsException If the index is negative.
     * @throws JSONException If a value is not finite.
     */
    public JSONArray put(int index, double value) {
        put(index, new Double(value));
        return this;
    }


    /**
     * Put or replace an int value. If the index is greater than the length of
     *  the JSONArray, then null elements will be added as necessary to pad
     *  it out.
     * @param index The subscript.
     * @param value An int value.
     * @return this.
     * @throws IndexOutOfBoundsException If the index is negative.
     */
    public JSONArray put(int index, int value) {
        put(index, new Integer(value));
        return this;
    }


    /**
     * Put or replace a long value. If the index is greater than the length of
     *  the JSONArray, then null elements will be added as necessary to pad
     *  it out.
     * @param index The subscript.
     * @param value A long value.
     * @return this.
     * @throws IndexOutOfBoundsException If the index is negative.
     */
    public JSONArray put(int index, long value) {
        put(index, new Long(value));
        return this;
    }


    /**
     * Put a value in the JSONArray, where the value will be a
     * JSONObject which is produced from a Map.
     * @param index The subscript.
     * @param value	The Map value.
     * @return		this.
     * @throws IndexOutOfBoundsException If the index is negative.
     */
    public JSONArray put(int index, Map value) {
        put(index, new JSONObject(value));
        return this;
    }

    /**
     * Put a JSONObject value in the JSONArray.
     * @param index The subscript.
     * @param value The object.
     * @return      this.
     * @throws IndexOutOfBoundsException If the index is negative.
     */
    public JSONArray put(int index, JSONObject value) {
        put(index, (Object)value);
        return this;
    }

    /**
     * Put or replace an object value in the JSONArray. If the index is greater
     *  than the length of the JSONArray, then null elements will be added as
     *  necessary to pad it out.
     * @param index The subscript.
     * @param value The value to put into the array. The value should be a
     *  Boolean, Double, Integer, JSONArray, JSONObject, Long, or String, or null.
     * @return this.
     * @throws IndexOutOfBoundsException If the index is negative.
     */
    public JSONArray put(int index, Object value)  {
        JSONObject.testValidity(value);
        if (index < 0) {
            throw new IndexOutOfBoundsException();
        }
        if (index < length()) {
            this.myArrayList.set(index, value);
        } else {
            while (index != length()) {
                put((Object)null);
            }
            put(value);
        }
        return this;
    }


    /**
     * Produce a JSONObject by combining a JSONArray of names with the values
     * of this JSONArray.
     * @param names A JSONArray containing a list of key strings. These will be
     * paired with the values.
     * @return A JSONObject, or null if there are no names or if this JSONArray
     * has no values.
     */
    public JSONObject toJSONObject(JSONArray names) {
        if (names == null || names.length() == 0 || length() == 0) {
            return null;
        }
        JSONObject jo = new JSONObject();
        for (int i = 0; i < names.length(); i += 1) {
            jo.put(names.getString(i), get(i));
        }
        return jo;
    }


    /**
     * Make a JSON text of this JSONArray. For compactness, no
     * unnecessary whitespace is added. If it is not possible to produce a
     * syntactically correct JSON text then null will be returned instead. This
     * could occur if the array contains an invalid number.
     * <p>
     * Warning: This method assumes that the data structure is acyclical.
     *
     * @return a printable, displayable, transmittable
     *  representation of the array.
     */
    public String toString() {
        try {
            return '[' + join(",") + ']';
        } catch (Exception e) {
            return null;
        }
    }


    /**
     * Make a prettyprinted JSON text of this JSONArray.
     * Warning: This method assumes that the data structure is acyclical.
     * @param indentFactor The number of spaces to add to each level of
     *  indentation.
     * @return a printable, displayable, transmittable
     *  representation of the object, beginning
     *  with <code>[</code>&nbsp;<small>(left bracket)</small> and ending
     *  with <code>]</code>&nbsp;<small>(right bracket)</small>.
     */
    public String toString(int indentFactor) {
        return toString(indentFactor, 0);
    }


    /**
     * Make a prettyprinted JSON text of this JSONArray.
     * Warning: This method assumes that the data structure is acyclical.
     * @param indentFactor The number of spaces to add to each level of
     *  indentation.
     * @param indent The indention of the top level.
     * @return a printable, displayable, transmittable
     *  representation of the array.
     */
    String toString(int indentFactor, int indent) {
        int len = length();
        if (len == 0) {
            return "[]";
        }
        int i;
        StringBuffer sb = new StringBuffer("[");
        if (len == 1) {
            sb.append(JSONObject.valueToString(this.myArrayList.get(0),
                    indentFactor, indent));
        } else {
            int newindent = indent + indentFactor;
            sb.append('\n');
            for (i = 0; i < len; i += 1) {
                if (i > 0) {
                    sb.append(",\n");
                }
                for (int j = 0; j < newindent; j += 1) {
                    sb.append(' ');
                }
                sb.append(JSONObject.valueToString(this.myArrayList.get(i),
                        indentFactor, newindent));
            }
            sb.append('\n');
            for (i = 0; i < indent; i += 1) {
                sb.append(' ');
            }
        }
        sb.append(']');
        return sb.toString();
    }


    /**
     * Write the contents of the JSONArray as JSON text to a writer.
     * For compactness, no whitespace is added.
     * <p>
     * Warning: This method assumes that the data structure is acyclical.
     *
     * @return The writer.
     * @throws JSONException that wraps an IOException
     */
    public Writer write(Writer writer) {
        try {
            boolean b = false;
            int     len = length();

            writer.write('[');

            for (int i = 0; i < len; i += 1) {
                if (b) {
                    writer.write(',');
                }
                Object v = this.myArrayList.get(i);
                if (v instanceof JSONObject) {
                    ((JSONObject)v).write(writer);
                } else if (v instanceof JSONArray) {
                    ((JSONArray)v).write(writer);
                } else {
                    writer.write(JSONObject.valueToString(v));
                }
                b = true;
            }
            writer.write(']');
            return writer;
        } catch (IOException e) {
           throw new JSONException(e);
        }
    }

    /**
     * A property wrapper around toString.
     * Allows ST to get at the output of toString.
     * @return serialized JSON string
     */
    public String getJSONString()
    {
        return toString();
    }

    //
    // Collection methods
    //

    // same as put but returns true
    public boolean add(Object o) {
        put(o);
        return true;
    }

    // same as put collection but returns true
    public boolean addAll(Collection c) {
        put(c);
        return true;
    }

    public void clear() {
        this.myArrayList.clear();
    }

    public boolean contains(Object elem) {
        return this.myArrayList.contains(elem);
    }

    public boolean containsAll(Collection c) {
        return this.myArrayList.containsAll(c);
    }

    public boolean isEmpty() {
        return this.myArrayList.isEmpty();
    }

    public Iterator iterator() {
        return this.myArrayList.iterator();
    }

    public boolean remove(Object o) {
        return this.myArrayList.remove(o);
    }

    public boolean removeAll(Collection c) {
        return this.myArrayList.removeAll(c);
    }

    public boolean retainAll(Collection c) {
        return this.myArrayList.retainAll(c);
    }

    // same as length
    public int size() {
        return this.myArrayList.size();
    }

    public Object[] toArray() {
        return this.myArrayList.toArray();
    }

    public Object[] toArray(Object[] a) {
        return this.myArrayList.toArray(a);
    }

    //
    // Object methods
    //

    /**
     * Two JSONArrays are equal if the i'th item in each list is
     * equal (or both are null).
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!this.getClass().equals(obj.getClass())) {
            return false;
        }
        JSONArray ja = (JSONArray) obj;
        if (this.size() != ja.size()) {
            return false;
        }
        for (int i = 0; i < this.size(); i++)
        {
            Object a = this.get(i);
            Object b = ja.get(i);
            if (a == null && b != null)
            {
                return false;
            }
            if (a != null && !a.equals(b)) {
                return false;
            }
        }
        return true;
    }


    /**
     * A simple implementation: sum the hash codes of all the items in the array.
     * Should be OK since it is not likely that these mutable objects
     * will be used as hash keys.
     */
    @Override
    public int hashCode()
    {
        int hash = 0;
        for (int i = 0; i < this.size(); i++)
        {
            Object o = this.get(i);
            if (o != null)
            {
                hash += o.hashCode();
            }
        }
        return hash;
    }

}
