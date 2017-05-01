package at.sw2017.swess17.application;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

/**
 * Created by mRozwold on 01.05.2017.
 */

public class TaskList<Task> extends ArrayList<Task> {


    public TaskList(int initialCapacity) {
        super(initialCapacity);
    }

    public TaskList() {
        super();
    }

    public TaskList(@NonNull Collection<? extends Task> c) {
        super(c);
    }

    @Override
    public void trimToSize() {
        super.trimToSize();
    }

    @Override
    public void ensureCapacity(int minCapacity) {
        super.ensureCapacity(minCapacity);
    }

    @Override
    public int size() {
        return super.size();
    }

    @Override
    public boolean isEmpty() {
        return super.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return super.contains(o);
    }

    @Override
    public int indexOf(Object o) {
        return super.indexOf(o);
    }

    @Override
    public int lastIndexOf(Object o) {
        return super.lastIndexOf(o);
    }

    @Override
    public Object clone() {
        return super.clone();
    }

    @Override
    public Object[] toArray() {
        return super.toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return super.toArray(a);
    }

    @Override
    public Task get(int index) {
        return super.get(index);
    }

    @Override
    public Task set(int index, Task element) {
        return super.set(index, element);
    }

    @Override
    public boolean add(Task task) {
        return super.add(task);
    }

    @Override
    public void add(int index, Task element) {
        super.add(index, element);
    }

    @Override
    public Task remove(int index) {
        return super.remove(index);
    }

    @Override
    public boolean remove(Object o) {
        return super.remove(o);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return super.containsAll(c);
    }

    @Override
    public void clear() {
        super.clear();
    }

    @Override
    public String toString() {
        return super.toString();
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
    }

    @Override
    public boolean addAll(Collection<? extends Task> c) {
        return super.addAll(c);
    }

    @Override
    public boolean addAll(int index, Collection<? extends Task> c) {
        return super.addAll(index, c);
    }

    @Override
    protected void removeRange(int fromIndex, int toIndex) {
        super.removeRange(fromIndex, toIndex);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return super.removeAll(c);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return super.retainAll(c);
    }

    @NonNull
    @Override
    public ListIterator<Task> listIterator(int index) {
        return super.listIterator(index);
    }

    @NonNull
    @Override
    public ListIterator<Task> listIterator() {
        return super.listIterator();
    }

    @NonNull
    @Override
    public Iterator<Task> iterator() {
        return super.iterator();
    }

    @Override
    public List<Task> subList(int fromIndex, int toIndex) {
        return super.subList(fromIndex, toIndex);
    }

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public void forEach(Consumer<? super Task> action) {
        super.forEach(action);
    }

    @Override
    public Spliterator<Task> spliterator() {
        return super.spliterator();
    }

    @Override
    public Stream<Task> stream() {
        return null;
    }

    @Override
    public Stream<Task> parallelStream() {
        return null;
    }

    @Override
    public boolean removeIf(Predicate<? super Task> filter) {
        return super.removeIf(filter);
    }

    @Override
    public void replaceAll(UnaryOperator<Task> operator) {
        super.replaceAll(operator);
    }

    @Override
    public void sort(Comparator<? super Task> c) {
        super.sort(c);
    }
}
