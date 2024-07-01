package tree;

import org.apache.commons.collections4.CollectionUtils;

import java.util.Collection;
import java.util.Objects;

public interface MutableTreeNode<T> extends TreeNode<T> {

    default void add(TreeNode<T> node) {
        if(node != null && node.parent() == this) {
            add(node, childCount() - 1);
        } else {
            add(node, childCount());
        }
    }

    void add(TreeNode<T> child, int index);

    void remove(int index);

    default void remove(TreeNode<T> node) {
        if (node == null) {
            throw new IllegalArgumentException("argument is null");
        }

        if (!isChild(node)) {
            throw new IllegalArgumentException("argument is not a child.");
        }
        remove(indexOf(node));
    }

    void setValue(T value);

    default void removeFromParent() {
        MutableTreeNode<T> parent = (MutableTreeNode<T>) parent();
        if (parent != null) {
            parent.remove(this);
        }
    }

    void setParent(TreeNode<T> parent);


    static <T> MutableTreeNode<T> copyOf(TreeNode<T> treeNode) {
        Objects.requireNonNull(treeNode);

        if(treeNode instanceof MutableTreeNode) {
            return (MutableTreeNode<T>) treeNode;
        }

        if(treeNode.isRoot()) {
            return deepCopy(treeNode);
        }

        return deepCopy(treeNode.root());

    }

    private static <T> MutableTreeNode<T> deepCopy(TreeNode<T> treeNode) {
        MutableTreeNode<T> node = MutableTreeNode.create(treeNode.value());
        Collection<? extends TreeNode<T>> nodes = treeNode.children();
        if(CollectionUtils.isNotEmpty(nodes)) {
            nodes.forEach(i -> node.add(MutableTreeNode.deepCopy(i)));
        }
        return node;
    }

    static <T> MutableTreeNode<T> create(T value) {
        return new DefaultMutableTreeNode<>(value);
    }

    static <T> MutableTreeNode<T> empty() {
        return DefaultMutableTreeNode.empty();
    }
}
