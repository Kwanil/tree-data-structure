package tree;

import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Stream;

public class TreeVisitOption<T> {
    private final Function<TreeNode<T>, Stream<TreeNode<T>>> function;

    private TreeVisitOption(Function<TreeNode<T>, Stream<TreeNode<T>>> function) {
        this.function = Objects.requireNonNull(function);
    }

    public static <T> TreeVisitOption<T> depthFirst() {
        return new TreeVisitOption<>(TreeStreams::depthFirst);
    }

    public static <T> TreeVisitOption<T> breadthFirst() {
        return new TreeVisitOption<>(TreeStreams::breadthFirst);
    }

    Function<TreeNode<T>, Stream<TreeNode<T>>> getFunction() {
        return function;
    }
}
