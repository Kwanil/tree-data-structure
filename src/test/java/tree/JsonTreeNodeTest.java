package tree;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class JsonTreeNodeTest {


    @Test
    void copyOf() {
        TreeNode<Department> expected = departmentChart();
        TreeNode<Department> actual = JsonTreeNode.copyOf(expected);

        System.out.println(TreeNodes.toString(actual));

        assertEquals(expected.value(), actual.value());
        assertEquals(TreeNodes.toString(expected), TreeNodes.toString(actual));
        assertEquals(expected.children().size(), actual.children().size());

        List<TreeNode<Department>> expectedChildren = List.copyOf(expected.children());
        List<TreeNode<Department>> actualChildren = List.copyOf(actual.children());

        for (int i = 0; i < expectedChildren.size(); i++) {
            TreeNode<Department> expectedChild = expectedChildren.get(i);
            TreeNode<Department> actualChild = actualChildren.get(i);
            assertEquals(expectedChild.value(), actualChild.value());
        }

        TreeNode<Department> copyOf = MutableTreeNode.copyOf(actual);

        System.out.println(TreeNodes.toString(copyOf));

        assertEquals(expected, copyOf);
        assertEquals(expected.parent(), copyOf.parent());
        assertEquals(expected.value(), copyOf.value());
        assertIterableEquals(expected.children(), copyOf.children());
    }

    @Test
    void json() throws JsonProcessingException {
        TreeNode<Department> expected = JsonTreeNode.copyOf(departmentChart());
        ObjectMapper objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false);

        String json = objectMapper.writeValueAsString(expected);
        System.out.println(json);

        TreeNode<Department> actual = objectMapper.readValue(json, new TypeReference<JsonTreeNode<Department>>() {
        });

        System.out.println(TreeNodes.toString(actual));

        assertEquals(expected, actual);
        assertEquals(expected.parent(), actual.parent());
        assertEquals(expected.value(), actual.value());
        assertIterableEquals(expected.children(), actual.children());
    }

    private TreeNode<Department> departmentChart() {
        List<Department> departments = getDepartments();
        TreeNode<Department> treeNode = departments.stream().collect(TreeCollectors.id(Department::getDepartmentCode).parentId(Department::getUpperDepartmentCode).root(i -> i.getUpperDepartmentCode() == null).toTreeNode());
        System.out.println(TreeNodes.toString(treeNode));
        return treeNode;
    }

    private List<Department> getDepartments() {
        Department d1 = new Department();
        d1.setDepartmentCode("CORP");
        d1.setDepartmentName("Corporation");

        Department d2 = new Department();
        d2.setDepartmentCode("CENTER");
        d2.setDepartmentName("Center");
        d2.setUpperDepartmentCode("CORP");

        Department d3 = new Department();
        d3.setDepartmentCode("TEAM1");
        d3.setDepartmentName("Team1");
        d3.setUpperDepartmentCode("CENTER");

        Department d4 = new Department();
        d4.setDepartmentCode("TEAM2");
        d4.setDepartmentName("Team2");
        d4.setUpperDepartmentCode("CENTER");

        Department d5 = new Department();
        d5.setDepartmentCode("HR");
        d5.setDepartmentName("HumanResource");
        d5.setUpperDepartmentCode("CORP");

        return List.of(d1, d2, d3, d4, d5);
    }

    @Getter
    @Setter
    @EqualsAndHashCode
    @ToString
    static class Department {
        private String departmentCode;
        private String departmentName;
        private String upperDepartmentCode;
    }

}