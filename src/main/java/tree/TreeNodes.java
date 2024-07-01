package tree;

import lombok.experimental.UtilityClass;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@UtilityClass
public class TreeNodes {

    private static <T> TreeVisitOption<T> defaultVisitOption() {
        return TreeVisitOption.depthFirst();
    }

    public static <T> List<T> values(Collection<? extends TreeNode<T>> treeNodes) {
        Objects.requireNonNull(treeNodes);
        return treeNodes.stream().map(TreeNode::value).collect(Collectors.toList());
    }

    public static <T> Optional<TreeNode<T>> searchFirst(TreeNode<T> treeNode, T searchContent) {
        Objects.requireNonNull(searchContent);
        return searchFirst(treeNode, defaultVisitOption(), t -> searchContent.equals(t.value()));
    }

    public static <T> Optional<TreeNode<T>> searchFirst(TreeNode<T> treeNode, TreeVisitOption<T> option, Predicate<TreeNode<T>> filter) {
        return searchAll(treeNode, option, filter).stream().findFirst();
    }

    public static <T> List<TreeNode<T>> searchAll(TreeNode<T> treeNode, Predicate<TreeNode<T>> filter) {
        return searchAll(treeNode, defaultVisitOption(), filter);
    }

    public static <T> List<TreeNode<T>> searchAll(TreeNode<T> treeNode, TreeVisitOption<T> option, Predicate<TreeNode<T>> filter) {
        Objects.requireNonNull(treeNode);
        Objects.requireNonNull(filter);
        Objects.requireNonNull(option);

        return option.getFunction().apply(treeNode).filter(filter).collect(Collectors.toList());
    }

    public static <T> TreeNode<T> getRoot(TreeNode<T> treeNode) {
        return TreeStreams.toParent(treeNode).filter(TreeNode::isRoot).findFirst().orElseThrow(NoSuchElementException::new);
    }

    public static <T> List<TreeNode<T>> getAllLeaves(TreeNode<T> treeNode){
        return searchAll(treeNode, TreeNode::isLeaf);
    }

    public static <T> List<TreeNode<T>> pathFromRoot(TreeNode<T> treeNode){
        return TreeStreams.toParent(treeNode).sorted(Comparator.comparingInt(TreeNode::depth)).collect(Collectors.toList());
    }

    public static <T> String toString(TreeNode<T> treeNode) {
        return toString(treeNode, defaultVisitOption());
    }

    public static <T> String toString(TreeNode<T> treeNode, TreeVisitOption<T> option) {
        return toString(treeNode, option, "\t", "\n");
    }

    public static <T> String toString(TreeNode<T> treeNode, TreeVisitOption<T> option, String prefixByEachNode, String postfixByEachNode) {
        Objects.requireNonNull(treeNode);
        Objects.requireNonNull(option);
        Objects.requireNonNull(prefixByEachNode);
        Objects.requireNonNull(postfixByEachNode);
        Stream<TreeNode<T>> stream = option.getFunction().apply(treeNode);
        return stream.reduce(new StringBuilder(), (sb, node) -> {
            sb.append(prefixByEachNode.repeat(Math.max(0, node.depth())));
            sb.append(node);
            sb.append(postfixByEachNode);
            return sb;
        }, (left, right) -> left).toString();
    }
}
