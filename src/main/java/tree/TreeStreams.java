package tree;

import lombok.experimental.UtilityClass;

import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@UtilityClass
public class TreeStreams {
    public static <T> Stream<TreeNode<T>> toParent(TreeNode<T> treeNode) {
        return StreamSupport.stream(TreeIterables.toParent(treeNode).spliterator(), false);
    }

    public static <T> Stream<TreeNode<T>> breadthFirst(TreeNode<T> treeNode) {
        return StreamSupport.stream(TreeIterables.breadthFirst(treeNode).spliterator(), false);
    }

    public static <T> Stream<TreeNode<T>> depthFirst(TreeNode<T> treeNode) {
        return StreamSupport.stream(TreeIterables.depthFirst(treeNode).spliterator(), false);
    }
}
