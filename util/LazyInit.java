package com.qin.start;

import java.util.Objects;

public abstract class LazyInit<T> {

    private volatile T field;

    /**
     * Return the value.
     *
     * <p>If the value is still <code>null</code>, the method will block and
     * invoke <code>computeValue()</code>. Calls from other threads will wait
     * until the call from the first thread will complete.
     */
    public T get() {
        T result = field;
        // First check (no locking)
        if (result == null) {
            synchronized (this) {
                // Second check (with locking)
                result = field;
                if (result == null) {
                    field = result = computeValue();
                }
            }
        }
        return result;
    }

    protected abstract T computeValue();

    /**
     * Setter for tests
     */
    public synchronized void set(T value) {
        field = value;
    }

    public static void main(String[] args) {
        LazyInit<String> field1 = new LazyInit<String>() {

            @Override
            protected String computeValue() {
                return "value";
            }
        };
        if (Objects.equals("value", field1.get())) {
            
        }

    }

}
