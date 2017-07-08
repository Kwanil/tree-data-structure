package tree;

import java.util.List;

public interface TreeGenerator {
	<T extends AdjacentNode<Key>, Key> TreeNode<T> create(List<T> contents, T root);
	<T extends NestedSetNode> TreeNode<T> create(List<T> contents, T root);
}
