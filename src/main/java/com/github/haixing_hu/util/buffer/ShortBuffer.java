/*
 * Copyright (c) 2014  Haixing Hu
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package com.github.haixing_hu.util.buffer;

import java.io.Serializable;

import javax.annotation.Nullable;

import com.github.haixing_hu.io.serialize.BinarySerialization;
import com.github.haixing_hu.lang.Argument;
import com.github.haixing_hu.lang.ArrayUtils;
import com.github.haixing_hu.lang.Assignable;
import com.github.haixing_hu.lang.Cloneable;
import com.github.haixing_hu.lang.Comparison;
import com.github.haixing_hu.lang.Equality;
import com.github.haixing_hu.lang.Hash;
import com.github.haixing_hu.lang.Size;
import com.github.haixing_hu.lang.Swapable;
import com.github.haixing_hu.util.expand.ExpansionPolicy;

import static com.github.haixing_hu.lang.Argument.*;
import static com.github.haixing_hu.lang.ArrayUtils.EMPTY_SHORT_ARRAY;

/**
 * A simple auto-expansion buffer of {@code short} values.
 *
 * @author Haixing Hu
 */
public final class ShortBuffer implements Swapable<ShortBuffer>,
    Assignable<ShortBuffer>, Cloneable<ShortBuffer>, Comparable<ShortBuffer>,
    Serializable {

  private static final long serialVersionUID = - 1169453206950184791L;

  static {
    BinarySerialization.register(ShortBuffer.class,
        ShortBufferBinarySerializer.INSTANCE);
  }

  /**
   * The buffer used to store the {@code short} values.
   */
  protected short[] buffer;

  /**
   * The length of the array stored in this {@code short} buffer.
   */
  protected int length;

  /**
   * The expansion policy used by this buffer.
   */
  protected transient ExpansionPolicy expansionPolicy;

  public ShortBuffer() {
    buffer = ArrayUtils.EMPTY_SHORT_ARRAY;
    length = 0;
    expansionPolicy = ExpansionPolicy.getDefault();
  }

  public ShortBuffer(final ExpansionPolicy expansionPolicy) {
    this.buffer = ArrayUtils.EMPTY_SHORT_ARRAY;
    this.length = 0;
    this.expansionPolicy = requireNonNull("expansionPolicy", expansionPolicy);
  }

  public ShortBuffer(final short[] buffer) {
    this.buffer = requireNonNull("buffer", buffer);
    this.length = 0;
    this.expansionPolicy = ExpansionPolicy.getDefault();
  }

  public ShortBuffer(final short[] buffer, final ExpansionPolicy expansionPolicy) {
    this.buffer = requireNonNull("buffer", buffer);
    this.length = 0;
    this.expansionPolicy = requireNonNull("expansionPolicy", expansionPolicy);
  }

  public ShortBuffer(final int bufferSize) {
    requireGreaterEqual("bufferSize", bufferSize, "zero", 0);
    if (bufferSize == 0) {
      this.buffer = EMPTY_SHORT_ARRAY;
    } else {
      this.buffer = new short[bufferSize];
    }
    this.length = 0;
    this.expansionPolicy = ExpansionPolicy.getDefault();
  }

  public ShortBuffer(final int bufferSize, final ExpansionPolicy expansionPolicy) {
    requireGreaterEqual("bufferSize", bufferSize, "zero", 0);
    if (bufferSize == 0) {
      this.buffer = EMPTY_SHORT_ARRAY;
    } else {
      this.buffer = new short[bufferSize];
    }
    this.length = 0;
    this.expansionPolicy = requireNonNull("expansionPolicy", expansionPolicy);
  }

  public ExpansionPolicy getExpansionPolicy() {
    return expansionPolicy;
  }

  public void setExpansionPolicy(final ExpansionPolicy expansionPolicy) {
    this.expansionPolicy = requireNonNull("expansionPolicy", expansionPolicy);
  }

  public long bytes() {
    return Size.REFERENCE + 4 + (buffer.length * 2);
  }

  public short at(final int i) {
    return buffer[i];
  }

  public short[] buffer() {
    return buffer;
  }

  public int length() {
    return length;
  }

  public int capacity() {
    return buffer.length;
  }

  public boolean isEmpty() {
    return length == 0;
  }

  public boolean isFull() {
    return length == buffer.length;
  }

  public void clear() {
    length = 0;
  }

  public int room() {
    return buffer.length - length;
  }

  public void setLength(final int newLength) {
    if (newLength < 0) {
      throw new IndexOutOfBoundsException();
    }
    if (newLength > buffer.length) {
      buffer = expansionPolicy.expand(buffer, length, newLength);
    }
    length = newLength;
  }

  public void append(final short ch) {
    if (length >= buffer.length) {
      buffer = expansionPolicy.expand(buffer, length, length + 1);
    }
    buffer[length++] = ch;
  }

  public void append(@Nullable final short[] array) {
    if ((array == null) || (array.length == 0)) {
      return;
    }
    final int newLength = length + array.length;
    if (newLength > buffer.length) {
      buffer = expansionPolicy.expand(buffer, length, newLength);
    }
    System.arraycopy(array, 0, buffer, length, array.length);
    length = newLength;
  }

  public void append(@Nullable final short[] array, final int off, final int n) {
    if (array == null) {
      return;
    }
    Argument.checkBounds(off, n, array.length);
    if (n == 0) {
      return;
    }
    final int newLength = length + n;
    if (newLength > buffer.length) {
      buffer = expansionPolicy.expand(buffer, length, newLength);
    }
    System.arraycopy(array, off, buffer, length, n);
    length = newLength;
  }

  public void append(@Nullable final ShortBuffer array) {
    if ((array == null) || (array.length == 0)) {
      return;
    }
    final int newLength = length + array.length;
    if (newLength > buffer.length) {
      buffer = expansionPolicy.expand(buffer, length, newLength);
    }
    System.arraycopy(array.buffer, 0, buffer, length, array.length);
    length = newLength;
  }

  public void append(@Nullable final ShortBuffer array, final int off,
      final int n) {
    if (array == null) {
      return;
    }
    Argument.checkBounds(off, n, array.length);
    if (n == 0) {
      return;
    }
    final int newLength = length + n;
    if (newLength > buffer.length) {
      buffer = expansionPolicy.expand(buffer, length, newLength);
    }
    System.arraycopy(array.buffer, off, buffer, length, n);
    length = newLength;
  }

  /**
   * Returns the current within this buffer of the first occurrence of the
   * specified value, starting the search at the specified
   * {@code beginIndex} and finishing at {@code endIndex}. If no such
   * value occurs in this buffer within the specified bounds, {@code -1} is
   * returned.
   * <p>
   * There is no restriction on the value of {@code beginIndex} and
   * {@code endIndex}. If {@code beginIndex} is negative, it has the
   * same effect as if it were zero. If {@code endIndex} is greater than
   * {@link #length()}, it has the same effect as if it were {@link #length()}.
   * If the {@code beginIndex} is greater than the {@code endIndex},
   * {@code -1} is returned.
   *
   * @param value
   *          the value to search for.
   * @param beginIndex
   *          the current to start the search from.
   * @param endIndex
   *          the current to finish the search at.
   * @return the current of the first occurrence of the value in the buffer
   *         within the given bounds, or {@code -1} if the value does not
   *         occur.
   */
  public int indexOf(final short value, int beginIndex, int endIndex) {
    if (beginIndex < 0) {
      beginIndex = 0;
    }
    if (endIndex > length) {
      endIndex = length;
    }
    if (beginIndex > endIndex) {
      return - 1;
    }
    for (int i = beginIndex; i < endIndex; ++i) {
      if (buffer[i] == value) {
        return i;
      }
    }
    return - 1;
  }

  /**
   * Returns the current within this buffer of the last occurrence of the
   * specified value, starting the search at the specified
   * {@code beginIndex} and finishing at {@code endIndex}. If no such
   * value occurs in this buffer within the specified bounds, {@code -1} is
   * returned.
   * <p>
   * There is no restriction on the value of {@code beginIndex} and
   * {@code endIndex}. If {@code beginIndex} is negative, it has the
   * same effect as if it were zero. If {@code endIndex} is greater than
   * {@link #length()}, it has the same effect as if it were {@link #length()}.
   * If the {@code beginIndex} is greater than the {@code endIndex},
   * {@code -1} is returned.
   *
   * @param ch
   *          the value to search for.
   * @param beginIndex
   *          the current to start the search from.
   * @param endIndex
   *          the current to finish the search at. If it is larger than the
   *          length of this buffer, it is treated as the length of this buffer.
   * @return the current of the first occurrence of the value in the buffer
   *         within the given bounds, or {@code -1} if the value does not
   *         occur.
   */
  public int lastIndexOf(final short value, int beginIndex, int endIndex) {
    if (beginIndex < 0) {
      beginIndex = 0;
    }
    if (endIndex > length) {
      endIndex = length;
    }
    if (beginIndex > endIndex) {
      return - 1;
    }
    for (int i = endIndex - 1; i >= beginIndex; --i) {
      if (buffer[i] == value) {
        return i;
      }
    }
    return - 1;
  }

  /**
   * Returns the current within this buffer of the first occurrence of the
   * specified value, starting the search at {@code 0} and finishing at
   * {@link #length()}. If no such value occurs in this buffer within those
   * bounds, {@code -1} is returned.
   *
   * @param value
   *          the value to search for.
   * @return the current of the first occurrence of the character in the buffer,
   *         or {@code -1} if the value does not occur.
   */
  public int indexOf(final short value) {
    return indexOf(value, 0, length);
  }

  /**
   * Returns the current within this buffer of the last occurrence of the
   * specified value, starting the search at {@code 0} and finishing at
   * {@link #length()}. If no such value occurs in this buffer within those
   * bounds, {@code -1} is returned.
   *
   * @param value
   *          the value to search for.
   * @return the current of the first occurrence of the character in the buffer,
   *         or {@code -1} if the value does not occur.
   */
  public int lastIndexOf(final short value) {
    return lastIndexOf(value, 0, length);
  }

  public void compact() {
    if (length == 0) {
      buffer = EMPTY_SHORT_ARRAY;
    } else if (length < buffer.length) {
      final short[] temp = new short[length];
      System.arraycopy(buffer, 0, temp, 0, length);
      buffer = temp;
    }
  }

  public void reserve(final int n, final boolean keepContent) {
    if (buffer.length < n) {
      final short[] newBuffer = new short[n];
      if (keepContent && (length > 0)) {
        System.arraycopy(buffer, 0, newBuffer, 0, length);
      } else {
        length = 0;
      }
      buffer = newBuffer;
    }
  }

  @Override
  public void swap(final ShortBuffer other) {
    final short[] tempBuffer = other.buffer;
    other.buffer = this.buffer;
    this.buffer = tempBuffer;
    final int tempLength = other.length;
    other.length = this.length;
    this.length = tempLength;
  }

  @Override
  public void assign(final ShortBuffer that) {
    if (this != that) {
      length = 0;
      if (buffer.length < that.length) {
        // create a new buffer and abandon the old one
        buffer = new short[that.length];
      }
      System.arraycopy(that.buffer, 0, buffer, 0, that.length);
      length = that.length;
    }
  }

  @Override
  public ShortBuffer clone() {
    final ShortBuffer cloned = new ShortBuffer(buffer.length);
    cloned.length = length;
    System.arraycopy(buffer, 0, cloned.buffer, 0, buffer.length);
    return cloned;
  }

  public short[] toArray() {
    if (length == 0) {
      return ArrayUtils.EMPTY_SHORT_ARRAY;
    } else {
      final short[] result = new short[length];
      System.arraycopy(buffer, 0, result, 0, length);
      return result;
    }
  }

  @Override
  public int hashCode() {
    final int multiplier = 1771;
    int code = 93;
    code = Hash.combine(code, multiplier, length);
    for (int i = 0; i < length; ++i) {
      code = Hash.combine(code, multiplier, buffer[i]);
    }
    return code;
  }

  @Override
  public boolean equals(final Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final ShortBuffer other = (ShortBuffer) obj;
    return (length == other.length)
        && Equality.equals(buffer, 0, other.buffer, 0, length);
  }

  @Override
  public int compareTo(final ShortBuffer other) {
    if (other == null) {
      return + 1;
    } else if (this == other) {
      return 0;
    } else {
      return Comparison.compare(buffer, length, other.buffer, other.length);
    }
  }

  @Override
  public String toString() {
    if (length == 0) {
      return "[]";
    } else {
      final StringBuilder builder = new StringBuilder();
      builder.append('[');
      for (int i = 0; i < length; ++i) {
        builder.append(buffer[i]).append(',');
      }
      // eat the last separator ','
      builder.setLength(builder.length() - 1);
      builder.append(']');
      return builder.toString();
    }
  }
}
