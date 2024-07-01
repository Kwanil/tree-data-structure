package tree;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import org.apache.commons.collections4.CollectionUtils;

import java.io.Serializable;
import java.util.*;

/**
 * JacksonJson 으로 읽거나 내보내기 가능한 TreeNode
 * @param <T>
 */
public class JsonTreeNode<T> implements Serializable, TreeNode<T> {
    private T value;
    private TreeNode<T> parent;
    private List<TreeNode<T>> children;

    @Override
    public TreeNode<T> parent() {
        return parent;
    }

    @JsonProperty
    @Override
    public T value() {
        return value;
    }

    @JsonProperty
    @Override
    public Collection<? extends TreeNode<T>> children() {
        return Objects.requireNonNullElse(children, Collections.emptyList());
    }

    @JsonSetter
    public void setValue(T value) {
        this.value = value;
    }

    @JsonSetter
    public void setParent(JsonTreeNode<T> parent) {
        this.parent = parent;
    }

    @JsonSetter
    public void setChildren(List<JsonTreeNode<T>> children) {
        Objects.requireNonNull(children);
        children.forEach(c->c.setParent(this));
        this.children = new ArrayList<>(children);
    }

    public static <T> JsonTreeNode<T> copyOf(TreeNode<T> treeNode) {
        Objects.requireNonNull(treeNode);

        if(treeNode instanceof JsonTreeNode) {
            return (JsonTreeNode<T>) treeNode;
        }

        if(treeNode.isRoot()) {
            return deepCopy(treeNode);
        }

        return deepCopy(treeNode.root());

    }

    private static <T> JsonTreeNode<T> deepCopy(TreeNode<T> treeNode) {
        JsonTreeNode<T> node = new JsonTreeNode<>();
        node.setValue(treeNode.value());

        Collection<? extends TreeNode<T>> nodes = treeNode.children();
        if(CollectionUtils.isNotEmpty(nodes)) {
            List<JsonTreeNode<T>> children = new ArrayList<>();
            nodes.forEach(i -> children.add(JsonTreeNode.deepCopy(i)));
            node.setChildren(children);
        }
        return node;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JsonTreeNode<?> node = (JsonTreeNode<?>) o;
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

    @Override
    public String toString() {
        if (value == null) {
            return "";
        }
        return String.valueOf(value);
    }
}
