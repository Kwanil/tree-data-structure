package tree;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.BiPredicate;
import java.util.stream.Collectors;

public class RecursiveTreeGenerator implements TreeGenerator {
	
	@Override
	public <T extends AdjacentNode<Key>, Key> TreeNode<T> create(List<T> contents, T root) {
		Objects.requireNonNull(contents);
		Objects.requireNonNull(root);
		TreeNode<T> rootNode = new TreeNode<T>(root);
		ChildAdder<T> childAdder = new ChildAdder<T>(rootNode, contents, Arrays.asList(rootNode.get()), true);
		childAdder.remainFilter = (remain, use) -> !Objects.equals(remain.getKey(), use.getKey());
		childAdder.childAddFilter = (tree, usedList, content) -> Objects.equals(tree.get().getKey(),
				content.getParentKey());
		addChildRecursively(childAdder);
		return rootNode;
	}

	@Override
	public <T extends NestedSetNode> TreeNode<T> create(List<T> contents, T root) {
		Objects.requireNonNull(contents);
		Objects.requireNonNull(root);
		contents.sort((o1, o2) -> Long.compare(o1.getLeft(), o2.getLeft()));
		TreeNode<T> rootNode = new TreeNode<T>(root);

		ChildAdder<T> childAdder = new ChildAdder<T>(rootNode, contents, Arrays.asList(rootNode.get()), true);
		childAdder.remainFilter = (remain, use) -> remain.getLeft() > use.getLeft()
				&& remain.getRight() < use.getRight();
		childAdder.childAddFilter = (tree, usedList, content) -> {
			boolean fir = tree.get().getLeft() < content.getLeft() && tree.get().getRight() > content.getRight();
			boolean sec = usedList.stream()
					.filter(used -> used.getLeft() < content.getLeft() && used.getRight() > content.getRight())
					.count() == 0;
			return fir && sec;
		};
		addChildRecursively(childAdder);
		return rootNode;
	}

	private <T> void addChildRecursively(ChildAdder<T> childAdder) {
		List<T> remains = childAdder.remainContents.stream()
				.filter(remain -> childAdder.usedContents.stream()
						.filter(use -> childAdder.remainFilter.test(remain, use)).count() > 0)
				.collect(Collectors.toList());
		if (remains.size() > 0) {
			List<TreeNode<T>> trees = childAdder.isRoot ? Arrays.asList(childAdder.treeNode)
					: childAdder.treeNode.getSubTrees();
			for (TreeNode<T> tree : trees) {
				List<T> used = new ArrayList<T>();
				for (T content : remains) {
					if (childAdder.childAddFilter.test(tree, used, content)) {
						tree.addChild(content);
						used.add(content);
					}
				}
				ChildAdder<T> subAdder = new ChildAdder<T>(tree, remains, used, false);
				subAdder.remainFilter = childAdder.remainFilter;
				subAdder.childAddFilter = childAdder.childAddFilter;
				addChildRecursively(subAdder);
			}
		}
	}

	static class ChildAdder<T> {
		TreeNode<T> treeNode;
		List<T> remainContents;
		List<T> usedContents;
		boolean isRoot;
		BiPredicate<T, T> remainFilter;
		TriPredicate<TreeNode<T>, List<T>, T> childAddFilter;

		public ChildAdder(TreeNode<T> treeNode, List<T> remainContents, List<T> usedContents, boolean isRoot) {
			this.treeNode = treeNode;
			this.remainContents = remainContents;
			this.usedContents = usedContents;
			this.isRoot = isRoot;
		}
	}

	interface TriPredicate<A, B, C> {
		boolean test(A a, B b, C c);
	}
}