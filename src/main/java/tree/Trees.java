package tree;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public final class Trees {
	private Trees() {
		throw new UnsupportedOperationException("Init");
	}

	public static <T> List<T> contents(Collection<? extends TreeNode<T>> treeNodes) {
		Objects.requireNonNull(treeNodes);
		return treeNodes.stream().map(TreeNode::get).collect(Collectors.toList());
	}
	
	public static <T> Optional<TreeNode<T>> searchFirst(TreeNode<T> treeNode, T searchContent) {
		Objects.requireNonNull(searchContent);
		return searchFirst(treeNode,TreeSearchOption.breadthFirst(), t -> searchContent.equals(t.get()));
	}
	
	public static <T> Optional<TreeNode<T>> searchFirst(TreeNode<T> treeNode, TreeSearchOption<T> option, Predicate<TreeNode<T>> filter) {
		return searchAll(treeNode, filter).stream().findFirst();
	}
	
	public static <T> List<TreeNode<T>> searchAll(TreeNode<T> treeNode, Predicate<? super TreeNode<T>> filter) {
		return searchAll(treeNode, TreeSearchOption.breadthFirst(),filter);
	}
	
	public static <T> List<TreeNode<T>> searchAll(TreeNode<T> treeNode, TreeSearchOption<T> option,  Predicate<? super TreeNode<T>> filter) {
		Objects.requireNonNull(treeNode);
		Objects.requireNonNull(filter);
		Objects.requireNonNull(option);
		return option.getFunction().apply(treeNode).filter(filter).collect(Collectors.toList());
	}
	
	public static <T> Optional<TreeNode<T>> root(TreeNode<T> treeNode) {
		return TreeStreams.toParent(treeNode).filter(TreeNode::isRoot).findFirst();
	}
	
	public static <T> List<TreeNode<T>> leaves(TreeNode<T> treeNode){
		return searchAll(treeNode, TreeNode::isLeaf);
	}
	
	public static <T> List<TreeNode<T>> pathFromRoot(TreeNode<T> treeNode){
		return TreeStreams.toParent(treeNode).sorted(Comparator.comparing(TreeNode::depth)).collect(Collectors.toList());
	}
	
}
