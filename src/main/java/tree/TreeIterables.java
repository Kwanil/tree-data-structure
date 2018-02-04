package tree;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;

final class TreeIterables {

	private TreeIterables() {
		throw new UnsupportedOperationException("initialize error");
	}
	
	final static <T> Iterable<TreeNode<T>> toParent(TreeNode<T> treeNode) {
		return () -> new Iterator<TreeNode<T>>() {
			TreeNode<T> node = treeNode;

			@Override
			public boolean hasNext() {
				return node != null;
			}
			
			@Override
			public TreeNode<T> next() {
				if (node == null) {
					throw new NoSuchElementException();
				}
				
				TreeNode<T> next = node;
				node = next.parent();
				return next;
			}
		};
	}
	
	final static <T> Iterable<TreeNode<T>> breadthFirst(TreeNode<T> treeNode) {
		return () -> new Iterator<TreeNode<T>>() {
			Deque<TreeNode<T>> queue = new ArrayDeque<>(Arrays.asList(treeNode));
			
			@Override
			public boolean hasNext() {
				return !queue.isEmpty();
			}

			@Override
			public TreeNode<T> next() {
				if (queue.isEmpty()) {
					throw new NoSuchElementException();
				}
				TreeNode<T> next = queue.removeFirst();
				next.children().forEach(queue::addLast);
				return next;
			}
		};
	}
	
	final static <T> Iterable<TreeNode<T>> depthFirst(TreeNode<T> treeNode){
		return () -> new Iterator<TreeNode<T>>() {
			Deque<TreeNode<T>> queue = new ArrayDeque<>(Arrays.asList(treeNode));
			
			@Override
			public boolean hasNext() {
				return !queue.isEmpty();
			}

			@Override
			public TreeNode<T> next() {
				TreeNode<T> next = queue.removeLast();
				List<TreeNode<T>> children = next.children();
				ListIterator<TreeNode<T>> iterator = children.listIterator(children.size());
				while (iterator.hasPrevious()) {
					queue.addLast(iterator.previous());
				}
				return next;
			}
		};
	}
}
