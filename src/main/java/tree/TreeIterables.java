package tree;

import lombok.experimental.UtilityClass;

import java.util.*;

@UtilityClass
final class TreeIterables {
    static <T> Iterable<TreeNode<T>> toParent(TreeNode<T> treeNode) {
        return () -> new Iterator<>() {
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

    static <T> Iterable<TreeNode<T>> breadthFirst(TreeNode<T> treeNode) {
        return () -> new Iterator<>() {
            final Deque<TreeNode<T>> queue = new ArrayDeque<>(Collections.singletonList(treeNode));

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

    static <T> Iterable<TreeNode<T>> depthFirst(TreeNode<T> treeNode){
        return () -> new Iterator<>() {
            final Deque<TreeNode<T>> queue = new ArrayDeque<>(Collections.singletonList(treeNode));

            @Override
            public boolean hasNext() {
                return !queue.isEmpty();
            }

            @Override
            public TreeNode<T> next() {
                TreeNode<T> next = queue.removeLast();
                List<TreeNode<T>> children = new ArrayList<>(next.children());
                ListIterator<TreeNode<T>> iterator = children.listIterator(children.size());
                while (iterator.hasPrevious()) {
                    queue.addLast(iterator.previous());
                }
                return next;
            }
        };
    }
}
