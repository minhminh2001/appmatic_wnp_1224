package com.whitelabel.app.task;

import java.util.AbstractQueue;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class INITBlockingQueue<E> extends AbstractQueue<E> implements
		BlockingQueue<E>, java.io.Serializable {

	private static final long serialVersionUID = -3818618732483109610L;

	/**
	 * The capacity bound, or Integer.MAX_VALUE if none
	 */
	private final int capacity;

	/**
	 * Current number of elements
	 */
	private final AtomicInteger count = new AtomicInteger();

	/**
	 * Head of linked list. Invariant: head.item == null
	 */
	transient INITNode<E> head;

	/**
	 * Tail of linked list. Invariant: last.next == null
	 */
	private transient INITNode<E> last;

	/**
	 * Lock held by take, poll, etc
	 */
	private final ReentrantLock takeLock = new ReentrantLock();

	/**
	 * Wait queue for waiting takes
	 */
	private final Condition notEmpty = takeLock.newCondition();

	/**
	 * Lock held by put, offer, etc
	 */
	private final ReentrantLock putLock = new ReentrantLock();

	/**
	 * Wait queue for waiting puts
	 */
	private final Condition notFull = putLock.newCondition();

	/**
	 * Signals a waiting take. Called only from put/offer (which do not
	 * otherwise ordinarily lock takeLock.)
	 */
	private void signalNotEmpty() {
		final ReentrantLock takeLock = this.takeLock;
		takeLock.lock();
		try {
			notEmpty.signal();
		} finally {
			takeLock.unlock();
		}
	}

	/**
	 * Signals a waiting put. Called only from take/poll.
	 */
	private void signalNotFull() {
		final ReentrantLock putLock = this.putLock;
		putLock.lock();
		try {
			notFull.signal();
		} finally {
			putLock.unlock();
		}
	}

	private synchronized E opQueue(INITNode<E> INITNode) {
		if (INITNode == null) {
			return _dequeue();
		} else {
			_enqueue(INITNode);
			return null;
		}
	}

	// only invoke in opQueue
	private void _enqueue(INITNode<E> INITNode) {
		boolean added = false;

		INITNode<E> curr = head;
		INITNode<E> temp = null;

		while (curr.next != null) {
			temp = curr.next;
			if (temp.getPriority().ordinal() > INITNode.getPriority().ordinal()) {
				curr.next = INITNode;
				INITNode.next = temp;
				added = true;
				break;
			}
			curr = curr.next;
		}

		if (!added) {
			last = last.next = INITNode;
		}
	}

	// only invoke in opQueue
	private E _dequeue() {
		// assert takeLock.isHeldByCurrentThread();
		// assert head.item == null;
		INITNode<E> h = head;
		INITNode<E> first = h.next;
		h.next = h; // help GC
		head = first;
		E x = first.getValue();
		first.setValue(null);
		return x;
	}

	/**
	 * Locks to prevent both puts and takes.
	 */
	void fullyLock() {
		putLock.lock();
		takeLock.lock();
	}

	/**
	 * Unlocks to allow both puts and takes.
	 */
	void fullyUnlock() {
		takeLock.unlock();
		putLock.unlock();
	}

	public INITBlockingQueue() {
		this(Integer.MAX_VALUE);
	}

	public INITBlockingQueue(int capacity) {
		if (capacity <= 0)
			throw new IllegalArgumentException();
		this.capacity = capacity;
		last = head = new INITNode<E>(null);
	}

	public INITBlockingQueue(Collection<? extends E> c) {
		this(Integer.MAX_VALUE);
		final ReentrantLock putLock = this.putLock;
		putLock.lock(); // Never contended, but necessary for visibility
		try {
			int n = 0;
			for (E e : c) {
				if (e == null)
					throw new NullPointerException();
				if (n == capacity)
					throw new IllegalStateException("Queue full");
				opQueue(new INITNode<E>(e));
				++n;
			}
			count.set(n);
		} finally {
			putLock.unlock();
		}
	}

	public int size() {
		return count.get();
	}

	public int remainingCapacity() {
		return capacity - count.get();
	}

	public void put(E e) throws InterruptedException {
		if (e == null)
			throw new NullPointerException();
		// Note: convention in all put/take/etc is to preset local var
		// holding count negative to indicate failure unless set.
		int c = -1;
		INITNode<E> INITNode = new INITNode<E>(e);
		final ReentrantLock putLock = this.putLock;
		final AtomicInteger count = this.count;
		putLock.lockInterruptibly();
		try {
			while (count.get() == capacity) {
				notFull.await();
			}
			opQueue(INITNode);
			c = count.getAndIncrement();
			if (c + 1 < capacity)
				notFull.signal();
		} finally {
			putLock.unlock();
		}
		if (c == 0)
			signalNotEmpty();
	}

	public boolean offer(E e, long timeout, TimeUnit unit)
			throws InterruptedException {

		if (e == null)
			throw new NullPointerException();
		long nanos = unit.toNanos(timeout);
		int c = -1;
		final ReentrantLock putLock = this.putLock;
		final AtomicInteger count = this.count;
		putLock.lockInterruptibly();
		try {
			while (count.get() == capacity) {
				if (nanos <= 0)
					return false;
				nanos = notFull.awaitNanos(nanos);
			}
			opQueue(new INITNode<E>(e));
			c = count.getAndIncrement();
			if (c + 1 < capacity)
				notFull.signal();
		} finally {
			putLock.unlock();
		}
		if (c == 0)
			signalNotEmpty();
		return true;
	}

	public boolean offer(E e) {
		if (e == null)
			throw new NullPointerException();
		final AtomicInteger count = this.count;
		if (count.get() == capacity)
			return false;
		int c = -1;
		INITNode<E> INITNode = new INITNode<E>(e);
		final ReentrantLock putLock = this.putLock;
		putLock.lock();
		try {
			if (count.get() < capacity) {
				opQueue(INITNode);
				c = count.getAndIncrement();
				if (c + 1 < capacity)
					notFull.signal();
			}
		} finally {
			putLock.unlock();
		}
		if (c == 0)
			signalNotEmpty();
		return c >= 0;
	}

	public E take() throws InterruptedException {
		E x;
		int c = -1;
		final AtomicInteger count = this.count;
		final ReentrantLock takeLock = this.takeLock;
		takeLock.lockInterruptibly();
		try {
			while (count.get() == 0) {
				notEmpty.await();
			}
			x = opQueue(null);
			c = count.getAndDecrement();
			if (c > 1)
				notEmpty.signal();
		} finally {
			takeLock.unlock();
		}
		if (c == capacity)
			signalNotFull();
		return x;
	}

	public E poll(long timeout, TimeUnit unit) throws InterruptedException {
		E x = null;
		int c = -1;
		long nanos = unit.toNanos(timeout);
		final AtomicInteger count = this.count;
		final ReentrantLock takeLock = this.takeLock;
		takeLock.lockInterruptibly();
		try {
			while (count.get() == 0) {
				if (nanos <= 0)
					return null;
				nanos = notEmpty.awaitNanos(nanos);
			}
			x = opQueue(null);
			c = count.getAndDecrement();
			if (c > 1)
				notEmpty.signal();
		} finally {
			takeLock.unlock();
		}
		if (c == capacity)
			signalNotFull();
		return x;
	}

	public E poll() {
		final AtomicInteger count = this.count;
		if (count.get() == 0)
			return null;
		E x = null;
		int c = -1;
		final ReentrantLock takeLock = this.takeLock;
		takeLock.lock();
		try {
			if (count.get() > 0) {
				x = opQueue(null);
				c = count.getAndDecrement();
				if (c > 1)
					notEmpty.signal();
			}
		} finally {
			takeLock.unlock();
		}
		if (c == capacity)
			signalNotFull();
		return x;
	}

	public E peek() {
		if (count.get() == 0)
			return null;
		final ReentrantLock takeLock = this.takeLock;
		takeLock.lock();
		try {
			INITNode<E> first = head.next;
			if (first == null)
				return null;
			else
				return first.getValue();
		} finally {
			takeLock.unlock();
		}
	}

	/**
	 * Unlinks interior ViewNode p with predecessor trail.
	 */
	void unlink(INITNode<E> p, INITNode<E> trail) {
		// assert isFullyLocked();
		// p.next is not changed, to allow iterators that are
		// traversing p to maintain their weak-consistency guarantee.
		p.setValue(null);
		trail.next = p.next;
		if (last == p)
			last = trail;
		if (count.getAndDecrement() == capacity)
			notFull.signal();
	}

	public boolean remove(Object o) {
		if (o == null)
			return false;
		fullyLock();
		try {
			for (INITNode<E> trail = head, p = trail.next; p != null; trail = p, p = p.next) {
				if (o.equals(p.getValue())) {
					unlink(p, trail);
					return true;
				}
			}
			return false;
		} finally {
			fullyUnlock();
		}
	}

	public boolean contains(Object o) {
		if (o == null)
			return false;
		fullyLock();
		try {
			for (INITNode<E> p = head.next; p != null; p = p.next)
				if (o.equals(p.getValue()))
					return true;
			return false;
		} finally {
			fullyUnlock();
		}
	}

	public Object[] toArray() {
		fullyLock();
		try {
			int size = count.get();
			Object[] a = new Object[size];
			int k = 0;
			for (INITNode<E> p = head.next; p != null; p = p.next)
				a[k++] = p.getValue();
			return a;
		} finally {
			fullyUnlock();
		}
	}

	@SuppressWarnings("unchecked")
	public <T> T[] toArray(T[] a) {
		fullyLock();
		try {
			int size = count.get();
			if (a.length < size)
				a = (T[]) java.lang.reflect.Array.newInstance(a.getClass()
						.getComponentType(), size);

			int k = 0;
			for (INITNode<T> p = (INITNode<T>) head.next; p != null; p = p.next)
				a[k++] = (T) p.getValue();
			if (a.length > k)
				a[k] = null;
			return a;
		} finally {
			fullyUnlock();
		}
	}

	public void clear() {
		fullyLock();
		try {
			for (INITNode<E> p, h = head; (p = h.next) != null; h = p) {
				h.next = h;
				p.setValue(null);
			}
			head = last;
			// assert head.item == null && head.next == null;
			if (count.getAndSet(0) == capacity)
				notFull.signal();
		} finally {
			fullyUnlock();
		}
	}

	public int drainTo(Collection<? super E> c) {
		return drainTo(c, Integer.MAX_VALUE);
	}

	public int drainTo(Collection<? super E> c, int maxElements) {
		if (c == null)
			throw new NullPointerException();
		if (c == this)
			throw new IllegalArgumentException();
		if (maxElements <= 0)
			return 0;
		boolean signalNotFull = false;
		final ReentrantLock takeLock = this.takeLock;
		takeLock.lock();
		try {
			int n = Math.min(maxElements, count.get());
			// count.get provides visibility to first n ViewNodes
			INITNode<E> h = head;
			int i = 0;
			try {
				while (i < n) {
					INITNode<E> p = h.next;
					c.add(p.getValue());
					p.setValue(null);
					h.next = h;
					h = p;
					++i;
				}
				return n;
			} finally {
				// Restore invariants even if c.add() threw
				if (i > 0) {
					// assert h.item == null;
					head = h;
					signalNotFull = (count.getAndAdd(-i) == capacity);
				}
			}
		} finally {
			takeLock.unlock();
			if (signalNotFull)
				signalNotFull();
		}
	}

	public Iterator<E> iterator() {
		return new Itr();
	}

	private class Itr implements Iterator<E> {

		private INITNode<E> current;
		private INITNode<E> lastRet;
		private E currentElement;

		Itr() {
			fullyLock();
			try {
				current = head.next;
				if (current != null)
					currentElement = current.getValue();
			} finally {
				fullyUnlock();
			}
		}

		public boolean hasNext() {
			return current != null;
		}

		private INITNode<E> nextViewNode(INITNode<E> p) {
			for (;;) {
				INITNode<E> s = p.next;
				if (s == p)
					return head.next;
				if (s == null || s.getValue() != null)
					return s;
				p = s;
			}
		}

		public E next() {
			fullyLock();
			try {
				if (current == null)
					throw new NoSuchElementException();
				E x = currentElement;
				lastRet = current;
				current = nextViewNode(current);
				currentElement = (current == null) ? null : current.getValue();
				return x;
			} finally {
				fullyUnlock();
			}
		}

		public void remove() {
			if (lastRet == null)
				throw new IllegalStateException();
			fullyLock();
			try {
				INITNode<E> INITNode = lastRet;
				lastRet = null;
				for (INITNode<E> trail = head, p = trail.next; p != null; trail = p, p = p.next) {
					if (p == INITNode) {
						unlink(p, trail);
						break;
					}
				}
			} finally {
				fullyUnlock();
			}
		}
	}

	private void writeObject(java.io.ObjectOutputStream s)
			throws java.io.IOException {

		fullyLock();
		try {
			// Write out any hidden stuff, plus capacity
			s.defaultWriteObject();

			// Write out all elements in the proper order.
			for (INITNode<E> p = head.next; p != null; p = p.next)
				s.writeObject(p.getValue());

			// Use trailing null as sentinel
			s.writeObject(null);
		} finally {
			fullyUnlock();
		}
	}

	/**
	 * Reconstitutes this queue from a stream (that is, deserializes it).
	 */
	private void readObject(java.io.ObjectInputStream s)
			throws java.io.IOException, ClassNotFoundException {
		// Read in capacity, and any hidden stuff
		s.defaultReadObject();

		count.set(0);
		last = head = new INITNode<E>(null);

		// Read in all elements and place in queue
		for (;;) {
			@SuppressWarnings("unchecked")
			E item = (E) s.readObject();
			if (item == null)
				break;
			add(item);
		}
	}
}

class INITNode<T> {
	private boolean valueAsT = false;
	private INITPriorityObject<?> value;
	INITNode<T> next;

	INITNode(T value) {
		setValue(value);
	}

	public INITPriority getPriority() {
		return value.priority;
	}

	@SuppressWarnings("unchecked")
	public T getValue() {
		if (value == null) {
			return null;
		} else if (valueAsT) {
			return (T) value;
		} else {
			return (T) value.obj;
		}
	}

	public void setValue(T value) {
		if (value == null) {
			this.value = null;
		} else if (value instanceof INITPriorityObject) {
			this.value = (INITPriorityObject<?>) value;
			this.valueAsT = true;
		} else {
			this.value = new INITPriorityObject<T>(INITPriority.DEFAULT, value);
		}
	}
}

class INITPriorityObject<E> {

	public final INITPriority priority;
	public final E obj;

	public INITPriorityObject(INITPriority priority, E obj) {
		this.priority = priority == null ? INITPriority.DEFAULT : priority;
		this.obj = obj;
	}
}

enum INITPriority {
	UI_TOP, UI_NORMAL, UI_LOW, DEFAULT, BG_TOP, BG_NORMAL, BG_LOW;
}