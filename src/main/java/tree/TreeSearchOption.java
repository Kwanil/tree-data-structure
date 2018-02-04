package tree;

import java.util.function.Function;
import java.util.stream.Stream;

public class TreeSearchOption<T> {
    private Function<TreeNode<T>, Stream<TreeNode<T>>> function;

    private TreeSearchOption(Function<TreeNode<T>, Stream<TreeNode<T>>> function) {
        this.function = function;
    }

    public final static <T> TreeSearchOption<T> depthFirst() {
        return new TreeSearchOption<>(TreeStreams::depthFirst);
    }

    public final static <T> TreeSearchOption<T> breadthFirst() {
        return new TreeSearchOption<>(TreeStreams::breadthFirst);
    }

    public final static <T> TreeSearchOption<T> toParent() {
        return new TreeSearchOption<>(TreeStreams::toParent);
    }

    public Function<TreeNode<T>, Stream<TreeNode<T>>> getFunction() {
        return function;
    }
}
