/*
 * MIT License
 *
 * Copyright (c) 2024 Braydon (Rainnny).
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package cc.restfulmc.api.common;

import lombok.NonNull;
import net.jodah.expiringmap.ExpirationPolicy;
import net.jodah.expiringmap.ExpiringMap;

import javax.annotation.concurrent.ThreadSafe;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * A simple set that expires elements after a certain
 * amount of time, utilizing the {@link ExpiringMap} library.
 *
 * @param <T> The type of element to store within this set
 * @author Braydon
 */
@ThreadSafe
public final class ExpiringSet<T> implements Iterable<T> {
    /**
     * The internal cache for this set.
     */
    @NonNull private final ExpiringMap<T, Long> cache;

    /**
     * The lifetime (in millis) of the elements in this set.
     */
    private final long lifetime;

    public ExpiringSet(@NonNull ExpirationPolicy expirationPolicy, long duration, @NonNull TimeUnit timeUnit) {
        this(expirationPolicy, duration, timeUnit, ignored -> {});
    }

    public ExpiringSet(@NonNull ExpirationPolicy expirationPolicy, long duration, @NonNull TimeUnit timeUnit, @NonNull Consumer<T> onExpire) {
        //noinspection unchecked
        this.cache = ExpiringMap.builder()
                .expirationPolicy(expirationPolicy)
                .expiration(duration, timeUnit)
                .expirationListener((key, ignored) -> onExpire.accept((T) key))
                .build();
        this.lifetime = timeUnit.toMillis(duration); // Get the lifetime in millis
    }

    /**
     * Add an element to this set.
     *
     * @param element the element
     * @return whether the element was added
     */
    public boolean add(@NonNull T element) {
        boolean contains = contains(element); // Does this set already contain the element?
        this.cache.put(element, System.currentTimeMillis() + this.lifetime);
        return !contains;
    }

    /**
     * Get the entry time of an element in this set.
     *
     * @param element the element
     * @return the entry time, -1 if not contained
     */
    public long getEntryTime(@NonNull T element) {
        return contains(element) ? this.cache.get(element) - this.lifetime : -1L;
    }

    /**
     * Check if an element is
     * contained within this set.
     *
     * @param element the element
     * @return whether the element is contained
     */
    public boolean contains(@NonNull T element) {
        Long timeout = this.cache.get(element); // Get the timeout for the element
        return timeout != null && (timeout > System.currentTimeMillis());
    }

    /**
     * Check if this set is empty.
     *
     * @return whether this set is empty
     */
    public boolean isEmpty() {
        return this.cache.isEmpty();
    }

    /**
     * Get the size of this set.
     *
     * @return the size
     */
    public int size() {
        return this.cache.size();
    }

    /**
     * Remove an element from this set.
     *
     * @param element the element
     * @return whether the element was removed
     */
    public boolean remove(@NonNull T element) {
        return this.cache.remove(element) != null;
    }

    /**
     * Clear this set.
     */
    public void clear() {
        this.cache.clear();
    }

    /**
     * Get the elements in this set.
     *
     * @return the elements
     */
    @NonNull
    public Set<T> getElements() {
        return this.cache.keySet();
    }

    /**
     * Returns an iterator over elements of type {@code T}.
     *
     * @return an Iterator.
     */
    @Override @NonNull
    public Iterator<T> iterator() {
        return this.cache.keySet().iterator();
    }
}