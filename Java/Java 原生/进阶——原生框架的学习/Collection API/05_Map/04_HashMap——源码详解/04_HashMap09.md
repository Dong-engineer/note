```java
/* ------------------------------------------------------------ */
// Cloning and serialization
@SuppressWarnings("unchecked")
@Override
public Object clone() {
    HashMap<K,V> result;
    try {
        result = (HashMap<K,V>)super.clone();
    } catch (CloneNotSupportedException e) {
        // this shouldn't happen, since we are Cloneable
        throw new InternalError(e);
    }
    result.reinitialize();
    result.putMapEntries(this, false);
    return result;
}

// These methods are also used when serializing HashSets
final float loadFactor() { return loadFactor; }

final int capacity() {
    return (table != null) ? table.length :
    (threshold > 0) ? threshold :
    DEFAULT_INITIAL_CAPACITY;
}

@java.io.Serial
private void writeObject(java.io.ObjectOutputStream s)
    throws IOException {
    int buckets = capacity();
    // Write out the threshold, loadfactor, and any hidden stuff
    s.defaultWriteObject();
    s.writeInt(buckets);
    s.writeInt(size);
    internalWriteEntries(s);
}

@java.io.Serial
private void readObject(ObjectInputStream s)
    throws IOException, ClassNotFoundException {

    ObjectInputStream.GetField fields = s.readFields();


    float lf = fields.get("loadFactor", 0.75f);
    if (lf <= 0 || Float.isNaN(lf))
        throw new InvalidObjectException("Illegal load factor: " + lf);

    lf = Math.min(Math.max(0.25f, lf), 4.0f);
    HashMap.UnsafeHolder.putLoadFactor(this, lf);

    reinitialize();

    s.readInt();                
    int mappings = s.readInt();
    if (mappings < 0) {
        throw new InvalidObjectException("Illegal mappings count: " + mappings);
    } else if (mappings == 0) {

    } else if (mappings > 0) {
        float fc = (float)mappings / lf + 1.0f;
        int cap = ((fc < DEFAULT_INITIAL_CAPACITY) ?
                   DEFAULT_INITIAL_CAPACITY :
                   (fc >= MAXIMUM_CAPACITY) ?
                   MAXIMUM_CAPACITY :
                   tableSizeFor((int)fc));
        float ft = (float)cap * lf;
        threshold = ((cap < MAXIMUM_CAPACITY && ft < MAXIMUM_CAPACITY) ?
                     (int)ft : Integer.MAX_VALUE);

        SharedSecrets.getJavaObjectInputStreamAccess().checkArray(s, Map.Entry[].class, cap);
        @SuppressWarnings({"rawtypes","unchecked"})
        Node<K,V>[] tab = (Node<K,V>[])new Node[cap];
        table = tab;


        for (int i = 0; i < mappings; i++) {
            @SuppressWarnings("unchecked")
            K key = (K) s.readObject();
            @SuppressWarnings("unchecked")
            V value = (V) s.readObject();
            putVal(hash(key), key, value, false, false);
        }
    }
}

// Support for resetting final field during deserializing
private static final class UnsafeHolder {
    private UnsafeHolder() { throw new InternalError(); }
    private static final jdk.internal.misc.Unsafe unsafe
        = jdk.internal.misc.Unsafe.getUnsafe();
    private static final long LF_OFFSET
        = unsafe.objectFieldOffset(HashMap.class, "loadFactor");
    static void putLoadFactor(HashMap<?, ?> map, float lf) {
        unsafe.putFloat(map, LF_OFFSET, lf);
    }
}
```

