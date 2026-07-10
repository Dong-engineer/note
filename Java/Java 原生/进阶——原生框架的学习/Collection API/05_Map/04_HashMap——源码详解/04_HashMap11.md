```java
/* ------------------------------------------------------------ */
// spliterators

static class HashMapSpliterator<K,V> {
    final HashMap<K,V> map;
    Node<K,V> current;          // current node
    int index;                  // current index, modified on advance/split
    int fence;                  // one past last index
    int est;                    // size estimate
    int expectedModCount;       // for comodification checks

    HashMapSpliterator(HashMap<K,V> m, int origin,
                       int fence, int est,
                       int expectedModCount) {
        this.map = m;
        this.index = origin;
        this.fence = fence;
        this.est = est;
        this.expectedModCount = expectedModCount;
    }

    final int getFence() { // initialize fence and size on first use
        int hi;
        if ((hi = fence) < 0) {
            HashMap<K,V> m = map;
            est = m.size;
            expectedModCount = m.modCount;
            Node<K,V>[] tab = m.table;
            hi = fence = (tab == null) ? 0 : tab.length;
        }
        return hi;
    }

    public final long estimateSize() {
        getFence(); // force init
        return (long) est;
    }
}

static final class KeySpliterator<K,V>
    extends HashMapSpliterator<K,V>
    implements Spliterator<K> {
    KeySpliterator(HashMap<K,V> m, int origin, int fence, int est,
                   int expectedModCount) {
        super(m, origin, fence, est, expectedModCount);
    }

    public KeySpliterator<K,V> trySplit() {
        int hi = getFence(), lo = index, mid = (lo + hi) >>> 1;
        return (lo >= mid || current != null) ? null :
        new KeySpliterator<>(map, lo, index = mid, est >>>= 1,
                             expectedModCount);
    }

    public void forEachRemaining(Consumer<? super K> action) {
        int i, hi, mc;
        if (action == null)
            throw new NullPointerException();
        HashMap<K,V> m = map;
        Node<K,V>[] tab = m.table;
        if ((hi = fence) < 0) {
            mc = expectedModCount = m.modCount;
            hi = fence = (tab == null) ? 0 : tab.length;
        }
        else
            mc = expectedModCount;
        if (tab != null && tab.length >= hi &&
            (i = index) >= 0 && (i < (index = hi) || current != null)) {
            Node<K,V> p = current;
            current = null;
            do {
                if (p == null)
                    p = tab[i++];
                else {
                    action.accept(p.key);
                    p = p.next;
                }
            } while (p != null || i < hi);
            if (m.modCount != mc)
                throw new ConcurrentModificationException();
        }
    }

    public boolean tryAdvance(Consumer<? super K> action) {
        int hi;
        if (action == null)
            throw new NullPointerException();
        Node<K,V>[] tab = map.table;
        if (tab != null && tab.length >= (hi = getFence()) && index >= 0) {
            while (current != null || index < hi) {
                if (current == null)
                    current = tab[index++];
                else {
                    K k = current.key;
                    current = current.next;
                    action.accept(k);
                    if (map.modCount != expectedModCount)
                        throw new ConcurrentModificationException();
                    return true;
                }
            }
        }
        return false;
    }

    public int characteristics() {
        return (fence < 0 || est == map.size ? Spliterator.SIZED : 0) |
            Spliterator.DISTINCT;
    }
}

static final class ValueSpliterator<K,V>
    extends HashMapSpliterator<K,V>
    implements Spliterator<V> {
    ValueSpliterator(HashMap<K,V> m, int origin, int fence, int est,
                     int expectedModCount) {
        super(m, origin, fence, est, expectedModCount);
    }

    public ValueSpliterator<K,V> trySplit() {
        int hi = getFence(), lo = index, mid = (lo + hi) >>> 1;
        return (lo >= mid || current != null) ? null :
        new ValueSpliterator<>(map, lo, index = mid, est >>>= 1,
                               expectedModCount);
    }

    public void forEachRemaining(Consumer<? super V> action) {
        int i, hi, mc;
        if (action == null)
            throw new NullPointerException();
        HashMap<K,V> m = map;
        Node<K,V>[] tab = m.table;
        if ((hi = fence) < 0) {
            mc = expectedModCount = m.modCount;
            hi = fence = (tab == null) ? 0 : tab.length;
        }
        else
            mc = expectedModCount;
        if (tab != null && tab.length >= hi &&
            (i = index) >= 0 && (i < (index = hi) || current != null)) {
            Node<K,V> p = current;
            current = null;
            do {
                if (p == null)
                    p = tab[i++];
                else {
                    action.accept(p.value);
                    p = p.next;
                }
            } while (p != null || i < hi);
            if (m.modCount != mc)
                throw new ConcurrentModificationException();
        }
    }

    public boolean tryAdvance(Consumer<? super V> action) {
        int hi;
        if (action == null)
            throw new NullPointerException();
        Node<K,V>[] tab = map.table;
        if (tab != null && tab.length >= (hi = getFence()) && index >= 0) {
            while (current != null || index < hi) {
                if (current == null)
                    current = tab[index++];
                else {
                    V v = current.value;
                    current = current.next;
                    action.accept(v);
                    if (map.modCount != expectedModCount)
                        throw new ConcurrentModificationException();
                    return true;
                }
            }
        }
        return false;
    }

    public int characteristics() {
        return (fence < 0 || est == map.size ? Spliterator.SIZED : 0);
    }
}

static final class EntrySpliterator<K,V>
    extends HashMapSpliterator<K,V>
    implements Spliterator<Map.Entry<K,V>> {
    EntrySpliterator(HashMap<K,V> m, int origin, int fence, int est,
                     int expectedModCount) {
        super(m, origin, fence, est, expectedModCount);
    }

    public EntrySpliterator<K,V> trySplit() {
        int hi = getFence(), lo = index, mid = (lo + hi) >>> 1;
        return (lo >= mid || current != null) ? null :
        new EntrySpliterator<>(map, lo, index = mid, est >>>= 1,
                               expectedModCount);
    }

    public void forEachRemaining(Consumer<? super Map.Entry<K,V>> action) {
        int i, hi, mc;
        if (action == null)
            throw new NullPointerException();
        HashMap<K,V> m = map;
        Node<K,V>[] tab = m.table;
        if ((hi = fence) < 0) {
            mc = expectedModCount = m.modCount;
            hi = fence = (tab == null) ? 0 : tab.length;
        }
        else
            mc = expectedModCount;
        if (tab != null && tab.length >= hi &&
            (i = index) >= 0 && (i < (index = hi) || current != null)) {
            Node<K,V> p = current;
            current = null;
            do {
                if (p == null)
                    p = tab[i++];
                else {
                    action.accept(p);
                    p = p.next;
                }
            } while (p != null || i < hi);
            if (m.modCount != mc)
                throw new ConcurrentModificationException();
        }
    }

    public boolean tryAdvance(Consumer<? super Map.Entry<K,V>> action) {
        int hi;
        if (action == null)
            throw new NullPointerException();
        Node<K,V>[] tab = map.table;
        if (tab != null && tab.length >= (hi = getFence()) && index >= 0) {
            while (current != null || index < hi) {
                if (current == null)
                    current = tab[index++];
                else {
                    Node<K,V> e = current;
                    current = current.next;
                    action.accept(e);
                    if (map.modCount != expectedModCount)
                        throw new ConcurrentModificationException();
                    return true;
                }
            }
        }
        return false;
    }

    public int characteristics() {
        return (fence < 0 || est == map.size ? Spliterator.SIZED : 0) |
            Spliterator.DISTINCT;
    }
}
```

