package tree;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class Trees {
	private Trees() {
	}
	
	public final static class SearchOption<T> {
		private Function<TreeNode<T>, Stream<TreeNode<T>>> function;
		
		private SearchOption(Function<TreeNode<T>, Stream<TreeNode<T>>> function) {
			this.function = function;
		}

		public final static <T> SearchOption<T> depthFirst() {
			return new SearchOption<>(TreeStreams::depthFirst);
		}
		
		public final static <T> SearchOption<T>  breadthFirst() {
			return new SearchOption<>(TreeStreams::breadthFirst);
		}

		Function<TreeNode<T>, Stream<TreeNode<T>>> getFunction() {
			return function;
		}
	}
	
	public static <T> List<T> contents(Collection<? extends TreeNode<T>> treeNodes) {
		Objects.requireNonNull(treeNodes);
		return treeNodes.stream().map(TreeNode<T>::get).collect(Collectors.toList());
	}
	
	public static <T> TreeNode<T> searchFirst(TreeNode<T> treeNode, T searchContent) {
		Objects.requireNonNull(searchContent);
		return searchFirst(treeNode,SearchOption.breadthFirst(), t -> searchContent.equals(t.get()));
	}
	
	public static <T> TreeNode<T> searchFirst(TreeNode<T> treeNode, SearchOption<T> option, Predicate<TreeNode<T>> filter) {
		List<TreeNode<T>> searchAll = searchAll(treeNode, filter);
		if(searchAll == null || searchAll.isEmpty()) {
			return null;
		}
		
		return searchAll.get(0);
	}
	
	public static <T> List<TreeNode<T>> searchAll(TreeNode<T> treeNode, Predicate<TreeNode<T>> filter) {
		return searchAll(treeNode, SearchOption.breadthFirst(),filter);
	}
	
	public static <T> List<TreeNode<T>> searchAll(TreeNode<T> treeNode, SearchOption<T> option,  Predicate<TreeNode<T>> filter) {
		Objects.requireNonNull(treeNode);
		Objects.requireNonNull(filter);
		Objects.requireNonNull(option);
		
		return option.getFunction().apply(treeNode).filter(filter).collect(Collectors.toList());
	}
	
	public static <T> TreeNode<T> root(TreeNode<T> treeNode) {
		return TreeStreams.toParent(treeNode).filter(t -> t.depth() == TreeNode.rootDepth).findFirst().get();
	}
	
	public static <T> List<TreeNode<T>> leaves(TreeNode<T> treeNode){
		return searchAll(treeNode, t->t.isLeaf());
	}
	
	public static <T> List<TreeNode<T>> pathFromRoot(TreeNode<T> treeNode){
		List<TreeNode<T>> parents = TreeStreams.toParent(treeNode).collect(Collectors.toList());
		parents.sort((a, b) -> Integer.compare(a.depth(), b.depth()));
		return parents;
	}
	
}
