package cc.restfulmc.api.common;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.RemovalListener;
import lombok.NonNull;

import java.util.Iterator;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * A simple set that expires elements after a certain
 * amount of time, utilizing Google Guava's {@link Cache}.
 *
 * @param <T> The type of element to store within this set
 * @author Braydon
 */
public final class ExpiringSet<T> implements Iterable<T> {
    /**
     * The internal cache for this set.
     */
    @NonNull private final Cache<T, Long> cache;

    /**
     * The lifetime (in millis) of the elements in this set.
     */
    private final long lifetime;

    public ExpiringSet(@NonNull ExpirationPolicy expirationPolicy, long duration, @NonNull TimeUnit timeUnit) {
        this(expirationPolicy, duration, timeUnit, ignored -> {});
    }

    public ExpiringSet(@NonNull ExpirationPolicy expirationPolicy, long duration, @NonNull TimeUnit timeUnit, @NonNull Consumer<T> onExpire) {
        CacheBuilder<Object, Object> builder = CacheBuilder.newBuilder();
        if (expirationPolicy == ExpirationPolicy.CREATED) {
            builder.expireAfterWrite(duration, timeUnit);
        } else {
            builder.expireAfterAccess(duration, timeUnit);
        }
        this.cache = builder
            .removalListener((RemovalListener<T, Long>) notification -> {
                if (notification.wasEvicted()) {
                    onExpire.accept(notification.getKey());
                }
            }).build();
        this.lifetime = timeUnit.toMillis(duration);
    }

    /**
     * Add an element to this set.
     *
     * @param element the element
     * @return whether the element was added
     */
    public boolean add(@NonNull T element) {
        boolean contains = contains(element);
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
        return contains(element) ? Objects.requireNonNull(this.cache.getIfPresent(element)) - this.lifetime : -1L;
    }

    /**
     * Check if an element is
     * contained within this set.
     *
     * @param element the element
     * @return whether the element is contained
     */
    public boolean contains(@NonNull T element) {
        Long timeout = this.cache.getIfPresent(element);
        return timeout != null && (timeout > System.currentTimeMillis());
    }

    /**
     * Check if this set is empty.
     *
     * @return whether this set is empty
     */
    public boolean isEmpty() {
        return this.cache.size() == 0;
    }

    /**
     * Get the size of this set.
     *
     * @return the size
     */
    public long size() {
        return this.cache.size();
    }

    /**
     * Remove an element from this set.
     *
     * @param element the element
     * @return whether the element was removed
     */
    public boolean remove(@NonNull T element) {
        boolean contains = contains(element);
        this.cache.invalidate(element);
        return contains;
    }

    /**
     * Clear this set.
     */
    public void clear() {
        this.cache.invalidateAll();
    }

    /**
     * Get the elements in this set.
     *
     * @return the elements
     */
    @NonNull
    public Set<T> getElements() {
        return this.cache.asMap().keySet();
    }

    /**
     * Returns an iterator over elements of type {@code T}.
     *
     * @return an Iterator.
     */
    @Override @NonNull
    public Iterator<T> iterator() {
        return this.cache.asMap().keySet().iterator();
    }

    /**
     * The expiration policy for elements in this set.
     */
    public enum ExpirationPolicy {
        /**
         * Elements expire after a fixed duration since creation.
         */
        CREATED,

        /**
         * Elements expire after a fixed duration since last access.
         */
        ACCESSED
    }
}
