package io.github.jukejuke.tool.bean;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

/**
 * ConvertUtils测试类
 */
public class BeanConvertUtilsTest {

    /**
     * 测试实体类
     */
    static class TestEntity {
        private Long id;
        private String name;
        private int age;
        private Date createTime;

        // getter和setter方法
        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        public Date getCreateTime() {
            return createTime;
        }

        public void setCreateTime(Date createTime) {
            this.createTime = createTime;
        }
    }

    /**
     * 测试DTO类
     */
    static class TestDTO {
        private Long id;
        private String name;
        private int age;
        private Date createTime;

        // getter和setter方法
        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        public Date getCreateTime() {
            return createTime;
        }

        public void setCreateTime(Date createTime) {
            this.createTime = createTime;
        }
    }

    /**
     * 测试VO类
     */
    static class TestVO {
        private Long id;
        private String name;
        private int age;
        private Date createTime;

        // getter和setter方法
        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        public Date getCreateTime() {
            return createTime;
        }

        public void setCreateTime(Date createTime) {
            this.createTime = createTime;
        }
    }

    /**
     * 创建测试实体对象
     * @return 测试实体对象
     */
    private TestEntity createTestEntity() {
        TestEntity entity = new TestEntity();
        entity.setId(1L);
        entity.setName("测试实体");
        entity.setAge(25);
        entity.setCreateTime(new Date());
        return entity;
    }

    /**
     * 创建测试DTO对象
     * @return 测试DTO对象
     */
    private TestDTO createTestDTO() {
        TestDTO dto = new TestDTO();
        dto.setId(2L);
        dto.setName("测试DTO");
        dto.setAge(30);
        dto.setCreateTime(new Date());
        return dto;
    }

    /**
     * 创建测试VO对象
     * @return 测试VO对象
     */
    private TestVO createTestVO() {
        TestVO vo = new TestVO();
        vo.setId(3L);
        vo.setName("测试VO");
        vo.setAge(35);
        vo.setCreateTime(new Date());
        return vo;
    }

    /**
     * 创建测试实体列表
     * @return 测试实体列表
     */
    private List<TestEntity> createTestEntityList() {
        List<TestEntity> entityList = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            TestEntity entity = new TestEntity();
            entity.setId((long) (i + 1));
            entity.setName("测试实体" + (i + 1));
            entity.setAge(25 + i);
            entity.setCreateTime(new Date());
            entityList.add(entity);
        }
        return entityList;
    }

    /**
     * 创建测试DTO列表
     * @return 测试DTO列表
     */
    private List<TestDTO> createTestDTOList() {
        List<TestDTO> dtoList = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            TestDTO dto = new TestDTO();
            dto.setId((long) (i + 1));
            dto.setName("测试DTO" + (i + 1));
            dto.setAge(30 + i);
            dto.setCreateTime(new Date());
            dtoList.add(dto);
        }
        return dtoList;
    }

    /**
     * 创建测试VO列表
     * @return 测试VO列表
     */
    private List<TestVO> createTestVOList() {
        List<TestVO> voList = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            TestVO vo = new TestVO();
            vo.setId((long) (i + 1));
            vo.setName("测试VO" + (i + 1));
            vo.setAge(35 + i);
            vo.setCreateTime(new Date());
            voList.add(vo);
        }
        return voList;
    }

    /**
     * 测试实体类转换为VO
     */
    @Test
    public void testEntityToVO() {
        TestEntity entity = createTestEntity();
        TestVO vo = BeanConvertUtils.entityToVO(entity, TestVO.class);
        assertNotNull(vo);
        assertEquals(entity.getId(), vo.getId());
        assertEquals(entity.getName(), vo.getName());
        assertEquals(entity.getAge(), vo.getAge());
        assertEquals(entity.getCreateTime(), vo.getCreateTime());
    }

    /**
     * 测试VO转换为实体类
     */
    @Test
    public void testVoToEntity() {
        TestVO vo = createTestVO();
        TestEntity entity = BeanConvertUtils.voToEntity(vo, TestEntity.class);
        assertNotNull(entity);
        assertEquals(vo.getId(), entity.getId());
        assertEquals(vo.getName(), entity.getName());
        assertEquals(vo.getAge(), entity.getAge());
        assertEquals(vo.getCreateTime(), entity.getCreateTime());
    }

    /**
     * 测试DTO转换为实体类
     */
    @Test
    public void testDtoToEntity() {
        TestDTO dto = createTestDTO();
        TestEntity entity = BeanConvertUtils.dtoToEntity(dto, TestEntity.class);
        assertNotNull(entity);
        assertEquals(dto.getId(), entity.getId());
        assertEquals(dto.getName(), entity.getName());
        assertEquals(dto.getAge(), entity.getAge());
        assertEquals(dto.getCreateTime(), entity.getCreateTime());
    }

    /**
     * 测试实体类转换为DTO
     */
    @Test
    public void testEntityToDTO() {
        TestEntity entity = createTestEntity();
        TestDTO dto = BeanConvertUtils.entityToDTO(entity, TestDTO.class);
        assertNotNull(dto);
        assertEquals(entity.getId(), dto.getId());
        assertEquals(entity.getName(), dto.getName());
        assertEquals(entity.getAge(), dto.getAge());
        assertEquals(entity.getCreateTime(), dto.getCreateTime());
    }

    /**
     * 测试DTO转换为VO
     */
    @Test
    public void testDtoToVO() {
        TestDTO dto = createTestDTO();
        TestVO vo = BeanConvertUtils.dtoToVO(dto, TestVO.class);
        assertNotNull(vo);
        assertEquals(dto.getId(), vo.getId());
        assertEquals(dto.getName(), vo.getName());
        assertEquals(dto.getAge(), vo.getAge());
        assertEquals(dto.getCreateTime(), vo.getCreateTime());
    }

    /**
     * 测试VO转换为DTO
     */
    @Test
    public void testVoToDTO() {
        TestVO vo = createTestVO();
        TestDTO dto = BeanConvertUtils.voToDTO(vo, TestDTO.class);
        assertNotNull(dto);
        assertEquals(vo.getId(), dto.getId());
        assertEquals(vo.getName(), dto.getName());
        assertEquals(vo.getAge(), dto.getAge());
        assertEquals(vo.getCreateTime(), dto.getCreateTime());
    }

    /**
     * 测试实体类列表转换为VO列表
     */
    @Test
    public void testEntityListToVOList() {
        List<TestEntity> entityList = createTestEntityList();
        List<TestVO> voList = BeanConvertUtils.entityListToVOList(entityList, TestVO.class);
        assertNotNull(voList);
        assertEquals(entityList.size(), voList.size());
        for (int i = 0; i < entityList.size(); i++) {
            TestEntity entity = entityList.get(i);
            TestVO vo = voList.get(i);
            assertEquals(entity.getId(), vo.getId());
            assertEquals(entity.getName(), vo.getName());
            assertEquals(entity.getAge(), vo.getAge());
            assertEquals(entity.getCreateTime(), vo.getCreateTime());
        }
    }

    /**
     * 测试VO列表转换为实体类列表
     */
    @Test
    public void testVoListToEntityList() {
        List<TestVO> voList = createTestVOList();
        List<TestEntity> entityList = BeanConvertUtils.voListToEntityList(voList, TestEntity.class);
        assertNotNull(entityList);
        assertEquals(voList.size(), entityList.size());
        for (int i = 0; i < voList.size(); i++) {
            TestVO vo = voList.get(i);
            TestEntity entity = entityList.get(i);
            assertEquals(vo.getId(), entity.getId());
            assertEquals(vo.getName(), entity.getName());
            assertEquals(vo.getAge(), entity.getAge());
            assertEquals(vo.getCreateTime(), entity.getCreateTime());
        }
    }

    /**
     * 测试DTO列表转换为实体类列表
     */
    @Test
    public void testDtoListToEntityList() {
        List<TestDTO> dtoList = createTestDTOList();
        List<TestEntity> entityList = BeanConvertUtils.dtoListToEntityList(dtoList, TestEntity.class);
        assertNotNull(entityList);
        assertEquals(dtoList.size(), entityList.size());
        for (int i = 0; i < dtoList.size(); i++) {
            TestDTO dto = dtoList.get(i);
            TestEntity entity = entityList.get(i);
            assertEquals(dto.getId(), entity.getId());
            assertEquals(dto.getName(), entity.getName());
            assertEquals(dto.getAge(), entity.getAge());
            assertEquals(dto.getCreateTime(), entity.getCreateTime());
        }
    }

    /**
     * 测试实体类列表转换为DTO列表
     */
    @Test
    public void testEntityListToDTOList() {
        List<TestEntity> entityList = createTestEntityList();
        List<TestDTO> dtoList = BeanConvertUtils.entityListToDTOList(entityList, TestDTO.class);
        assertNotNull(dtoList);
        assertEquals(entityList.size(), dtoList.size());
        for (int i = 0; i < entityList.size(); i++) {
            TestEntity entity = entityList.get(i);
            TestDTO dto = dtoList.get(i);
            assertEquals(entity.getId(), dto.getId());
            assertEquals(entity.getName(), dto.getName());
            assertEquals(entity.getAge(), dto.getAge());
            assertEquals(entity.getCreateTime(), dto.getCreateTime());
        }
    }

    /**
     * 测试DTO列表转换为VO列表
     */
    @Test
    public void testDtoListToVOList() {
        List<TestDTO> dtoList = createTestDTOList();
        List<TestVO> voList = BeanConvertUtils.dtoListToVOList(dtoList, TestVO.class);
        assertNotNull(voList);
        assertEquals(dtoList.size(), voList.size());
        for (int i = 0; i < dtoList.size(); i++) {
            TestDTO dto = dtoList.get(i);
            TestVO vo = voList.get(i);
            assertEquals(dto.getId(), vo.getId());
            assertEquals(dto.getName(), vo.getName());
            assertEquals(dto.getAge(), vo.getAge());
            assertEquals(dto.getCreateTime(), vo.getCreateTime());
        }
    }

    /**
     * 测试VO列表转换为DTO列表
     */
    @Test
    public void testVoListToDTOList() {
        List<TestVO> voList = createTestVOList();
        List<TestDTO> dtoList = BeanConvertUtils.voListToDTOList(voList, TestDTO.class);
        assertNotNull(dtoList);
        assertEquals(voList.size(), dtoList.size());
        for (int i = 0; i < voList.size(); i++) {
            TestVO vo = voList.get(i);
            TestDTO dto = dtoList.get(i);
            assertEquals(vo.getId(), dto.getId());
            assertEquals(vo.getName(), dto.getName());
            assertEquals(vo.getAge(), dto.getAge());
            assertEquals(vo.getCreateTime(), dto.getCreateTime());
        }
    }

    /**
     * 测试空列表转换
     */
    @Test
    public void testEmptyListConversion() {
        List<TestEntity> emptyEntityList = new ArrayList<>();
        List<TestVO> voList = BeanConvertUtils.entityListToVOList(emptyEntityList, TestVO.class);
        assertNotNull(voList);
        assertTrue(voList.isEmpty());

        List<TestVO> emptyVoList = null;
        List<TestEntity> entityList = BeanConvertUtils.voListToEntityList(emptyVoList, TestEntity.class);
        assertNotNull(entityList);
        assertTrue(entityList.isEmpty());
    }

    /**
     * 测试空对象转换
     */
    @Test
    public void testNullObjectConversion() {
        TestEntity nullEntity = null;
        TestVO vo = BeanConvertUtils.entityToVO(nullEntity, TestVO.class);
        assertNull(vo);
    }
}