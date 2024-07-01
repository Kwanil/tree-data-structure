package tree;

import org.apache.commons.collections4.CollectionUtils;

import java.util.*;

public class DefaultMutableTreeNode<T> implements MutableTreeNode<T> {
    static final DefaultMutableTreeNode<?> EMPTY = new DefaultMutableTreeNode<>();

    private T value;
    private TreeNode<T> parent;
    private List<TreeNode<T>> children;

    DefaultMutableTreeNode() {
        this(null);
    }

    DefaultMutableTreeNode(T value) {
        this.value = value;
    }

    @Override
    public void add(TreeNode<T> child, int index) {
        Objects.requireNonNull(child, "new child is null");

        if (isAncestor(child)) {
            throw new IllegalArgumentException("new child is an ancestor");
        }

        MutableTreeNode<T> oldParent = (MutableTreeNode<T>) child.parent();
        if (oldParent != null) {
            oldParent.remove(child);
        }
        ((MutableTreeNode<T>) child).setParent(this);
        if (children == null) {
            children = new ArrayList<>();
        }
        children.add(index, child);
    }

    @Override
    public void remove(int index) {
        MutableTreeNode<T> child = (MutableTreeNode<T>) childAt(index);
        children.remove(index);
        child.setParent(null);
    }

    @Override
    public void setValue(T value) {
        this.value = value;
    }

    @Override
    public void setParent(TreeNode<T> parent) {
        this.parent = parent;
    }

    @Override
    public TreeNode<T> childAt(int index) {
        if (children == null) {
            throw new ArrayIndexOutOfBoundsException("node has no children");
        }
        return children.get(index);
    }

    @Override
    public TreeNode<T> parent() {
        return parent;
    }

    @Override
    public T value() {
        return value;
    }

    @Override
    public Collection<? extends TreeNode<T>> children() {
        return Objects.requireNonNullElse(children, Collections.emptyList());
    }

    @Override
    public String toString() {
        if (value == null) {
            return "";
        }
        return String.valueOf(value);
    }

    @SuppressWarnings("unchecked")
    public static <T> DefaultMutableTreeNode<T> empty() {
        return (DefaultMutableTreeNode<T>) EMPTY;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DefaultMutableTreeNode<?> node = (DefaultMutableTreeNode<?>) o;
        List<TreeNode<T>> emptyList = Collections.emptyList();
        boolean equalsValue = Objects.equals(value, node.value);
        boolean equalsParent = parent != null && node.parent != null ? Objects.equals(parent.value(), node.parent.value()) : Objects.equals(parent, node.parent);
        boolean equalCollection = CollectionUtils.isEqualCollection(Objects.requireNonNullElse(this.children, emptyList), Objects.requireNonNullElse(node.children, emptyList));
        return equalsValue && equalsParent && equalCollection;
    }

    @Override
    public int hashCode() {
        List<T> childList = Objects.requireNonNullElse(children, Collections.<TreeNode<T>>emptyList()).stream().map(TreeNode::value).toList();
        return Objects.hash(value, parent != null ? parent.value(): null, childList);
    }
}
