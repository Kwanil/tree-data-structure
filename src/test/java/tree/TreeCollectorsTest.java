package tree;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TreeCollectorsTest {


    @Test
    void collector() {
        List<VO> list = root1();
        TreeNode<VO> treeNode = list.stream().collect(TreeCollectors.id(VO::getId).parentId(VO::getParentId).toTreeNode());

        assertEquals("root1", treeNode.value().id);
        assertEquals("root1-child1", treeNode.firstChild().value().id);
        assertEquals("root1-child2", treeNode.lastChild().value().id);
        assertEquals("root1-child1_1", treeNode.firstChild().firstChild().value().id);
    }

    @Test
    void collector1() {
        List<VO> list = root1();
        TreeNode<VO> treeNode = list.stream().collect(TreeCollectors.id(VO::getId).parentId(VO::getParentId).root(i -> i.getParentId() == null).toTreeNode());

        assertEquals("root1", treeNode.value().id);
        assertEquals("root1-child1", treeNode.firstChild().value().id);
        assertEquals("root1-child2", treeNode.lastChild().value().id);
        assertEquals("root1-child1_1", treeNode.firstChild().firstChild().value().id);
    }

    @Test
    void collector2() {
        List<VO> list = new ArrayList<>(root1());
        list.addAll(root2());
        List<TreeNode<VO>> treeNodeList = list.stream().collect(TreeCollectors.id(VO::getId).parentId(VO::getParentId).root(i -> i.getParentId() == null).toTreeNodeList());

        assertEquals(2, treeNodeList.size());
        TreeNode<VO> treeNode = treeNodeList.get(0);
        assertEquals("root1", treeNode.value().id);
        assertEquals("root1-child1", treeNode.firstChild().value().id);
        assertEquals("root1-child2", treeNode.lastChild().value().id);
        assertEquals("root1-child1_1", treeNode.firstChild().firstChild().value().id);
        TreeNode<VO> treeNode2 = treeNodeList.get(1);
        assertEquals("root2", treeNode2.value().id);
        assertEquals("root2-child1", treeNode2.firstChild().value().id);
        assertEquals("root2-child2", treeNode2.lastChild().value().id);
        assertEquals("root2-child1_1", treeNode2.firstChild().firstChild().value().id);
    }

    @Test
    void collector3() {
        List<VO> list = new ArrayList<>(root1());
        list.addAll(root2());
        TreeNode<VO> root = list.stream().collect(TreeCollectors.id(VO::getId).parentId(VO::getParentId).root(i -> i.parentId == null).toTreeNode());
        List<TreeNode<VO>> treeNodeList = List.copyOf(root.children());

        assertEquals(2, treeNodeList.size());
        TreeNode<VO> treeNode = treeNodeList.get(0);
        assertEquals("root1", treeNode.value().id);
        assertEquals("root1-child1", treeNode.firstChild().value().id);
        assertEquals("root1-child2", treeNode.lastChild().value().id);
        assertEquals("root1-child1_1", treeNode.firstChild().firstChild().value().id);
        TreeNode<VO> treeNode2 = treeNodeList.get(1);
        assertEquals("root2", treeNode2.value().id);
        assertEquals("root2-child1", treeNode2.firstChild().value().id);
        assertEquals("root2-child2", treeNode2.lastChild().value().id);
        assertEquals("root2-child1_1", treeNode2.firstChild().firstChild().value().id);
    }

    private List<VO> root1() {
        VO root1 = VO.of("root1", null);
        VO child1 = VO.of("root1-child1", "root1");
        VO child2 = VO.of("root1-child2", "root1");
        VO child1_1 = VO.of("root1-child1_1", "root1-child1");
        VO child1_2 = VO.of("root1-child1_2", "root1-child1");
        return List.of(root1, child1, child2, child1_1, child1_2);
    }

    private List<VO> root2() {
        VO root1 = VO.of("root2", null);
        VO child1 = VO.of("root2-child1", "root2");
        VO child2 = VO.of("root2-child2", "root2");
        VO child1_1 = VO.of("root2-child1_1", "root2-child1");
        VO child1_2 = VO.of("root2-child1_2", "root2-child1");
        return List.of(root1, child1, child2, child1_1, child1_2);
    }


    @Getter
    @Setter
    @ToString
    @RequiredArgsConstructor(staticName = "of")
    static class VO {
        private final String id;
        private final String parentId;
    }
}
