package tree;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collector;

import static org.junit.Assert.assertEquals;

public class TreeCollectorsTest {

    List<VO> voList = Arrays.asList(
            vo(1, 0),
            vo(2, 1),
            vo(3, 1),
            vo(4, 1),
            vo(5, 2),
            vo(6, 2),
            vo(7, 2),
            vo(8, 3),
            vo(9, 3),
            vo(10, 4),
            vo(11, 4),
            vo(12, 6)
    );
    private VO vo(int id, int parentId) {
        return VO.of(id, parentId);
    }

    @Test
    public void testTreeCollector() throws Exception {
        Collector<VOEx, ?, VOEx> toTree = TreeCollectors.<VOEx,Integer>tree()
                .id(VOEx::getId)
                .parentId(VOEx::getParentId)
                .childAppend((vo, list) -> vo.setChildren(list))
                .root(vo -> vo.getParentId() == 0)
                .toTree();

        VOEx vo = voList.stream().map(i->new VOEx(i.id,i.parentId)).collect(toTree);
        System.out.println(vo);

        assertEquals(vo.getId(), 1);

        assertEquals(vo.children.size(), 3);

        assertEquals(vo.children.get(0).children.size(), 3);
    }

    @Test
    public void testTreeNodeCollector() throws Exception {
        Collector<VO, ?, TreeNode<VO>> toTreeNode = TreeCollectors.<VO, Integer>treeNode()
                .id(VO::getId)
                .parentId(VO::getParentId)
                .root(vo -> vo.getParentId() == 0)
                .toTree();

        TreeNode<VO> treeNode = voList.stream().collect(toTreeNode);
        System.out.println(treeNode);

        assertEquals(treeNode.get().id, 1);

        assertEquals(treeNode.children().size(), 3);

        assertEquals(treeNode.children().get(0).children().size(), 3);
    }


    static class VO {
        private int id;
        private int parentId;

        public static VO of(int id, int parentId) {
            return new VO(id, parentId);
        }

        private VO(int id, int parentId) {
            this.id = id;
            this.parentId = parentId;
        }

        public int getId() {
            return id;
        }

        public int getParentId() {
            return parentId;
        }


        @Override
        public String toString() {
            return "VO{" +
                    "id=" + id +
                    ", parentId=" + parentId +
                    '}';
        }
    }

    static class VOEx extends VO {
        private List<VOEx> children;

        public static VOEx of(int id, int parentId) {
            return new VOEx(id, parentId);
        }

        private VOEx(int id, int parentId) {
            super(id, parentId);
        }

        public List<VOEx> getChildren() {
            return children;
        }

        public void setChildren(List<VOEx> children) {
            this.children = children;
        }

        @Override
        public String toString() {
            return "VO{" +
                    "id=" + getId() +
                    ", parentId=" + getParentId() +
                    ", children=" + children +
                    '}';
        }
    }
}