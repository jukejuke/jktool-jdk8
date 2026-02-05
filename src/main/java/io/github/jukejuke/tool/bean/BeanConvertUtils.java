package io.github.jukejuke.tool.bean;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/*
 * Bean 类型说明
 * -----------------------------------------------------------------------------------------------------------------
 * 类型       全称                中文名称        核心作用                                                        典型场景
 * -----------------------------------------------------------------------------------------------------------------
 * PO/Entity  Persistent Object   持久化对象      与数据库表一一映射，对应单表结构，包含字段 + getter/setter + 无参构造器    数据库表 user → UserEntity/UserPO
 * DTO        Data Transfer Object 数据传输对象    跨层 / 跨系统传输数据（比如 Controller ↔ Service、服务间调用）          前端提交的 UserLoginDTO、接口返回的 UserListDTO
 * VO         View Object          视图对象        专门给前端展示的数据（定制字段、格式）                                前端列表展示的 UserVO（包含 nickName 而非 name）
 * BO         Business Object      业务对象        封装业务逻辑相关数据（多表 / 多 DTO 组合）                            下单场景的 OrderBO（包含用户 + 商品 + 地址）
 * Query      Query Object         查询对象        封装查询条件（分页、筛选）                                          复杂查询参数封装
 * -----------------------------------------------------------------------------------------------------------------
 */

/**
 * 实践示例：
 * Controller层：方法参数统一使用DTO或Query对象，返回结果统一封装为VO对象；
 * Service层：方法参数统一使用DTO或Query对象，返回结果统一封装为VO对象；
 * Dao层：方法参数统一使用Entity或query对象或直接变量，返回结果统一为Entity对象。
 *
 * Model 层 Bean 是核心数据载体，需按用途分为 PO/Entity（数据库映射）、DTO（传输）、VO（展示）、Query（查询）
 */

/**
 * 转换工具类
 * 封装MVC架构中常用的Bean转换方法
 *  
 */
@Slf4j
public class BeanConvertUtils {



    /**
     * 实体类转换为VO
     * @param entity 实体类对象
     * @param voClass VO类
     * @param <E> 实体类类型
     * @param <V> VO类型
     * @return 转换后的VO对象
     */
    public static <E, V> V entityToVO(E entity, Class<V> voClass) {
        try {
            return BeanUtils.copyBean(entity, voClass);
        } catch (Exception e) {
            log.error("Entity to VO convert error: ", e);
            throw new RuntimeException("Entity to VO convert error", e);
        }
    }

    /**
     * VO转换为实体类
     * @param vo VO对象
     * @param entityClass 实体类
     * @param <V> VO类型
     * @param <E> 实体类类型
     * @return 转换后的实体类对象
     */
    public static <V, E> E voToEntity(V vo, Class<E> entityClass) {
        try {
            return BeanUtils.copyBean(vo, entityClass);
        } catch (Exception e) {
            log.error("VO to Entity convert error: ", e);
            throw new RuntimeException("VO to Entity convert error", e);
        }
    }

    /**
     * DTO转换为实体类
     * @param dto DTO对象
     * @param entityClass 实体类
     * @param <D> DTO类型
     * @param <E> 实体类类型
     * @return 转换后的实体类对象
     */
    public static <D, E> E dtoToEntity(D dto, Class<E> entityClass) {
        try {
            return BeanUtils.copyBean(dto, entityClass);
        } catch (Exception e) {
            log.error("DTO to Entity convert error: ", e);
            throw new RuntimeException("DTO to Entity convert error", e);
        }
    }

    /**
     * 实体类转换为DTO
     * @param entity 实体类对象
     * @param dtoClass DTO类
     * @param <E> 实体类类型
     * @param <D> DTO类型
     * @return 转换后的DTO对象
     */
    public static <E, D> D entityToDTO(E entity, Class<D> dtoClass) {
        try {
            return BeanUtils.copyBean(entity, dtoClass);
        } catch (Exception e) {
            log.error("Entity to DTO convert error: ", e);
            throw new RuntimeException("Entity to DTO convert error", e);
        }
    }

    /**
     * DTO转换为VO
     * @param dto DTO对象
     * @param voClass VO类
     * @param <D> DTO类型
     * @param <V> VO类型
     * @return 转换后的VO对象
     */
    public static <D, V> V dtoToVO(D dto, Class<V> voClass) {
        try {
            return BeanUtils.copyBean(dto, voClass);
        } catch (Exception e) {
            log.error("DTO to VO convert error: ", e);
            throw new RuntimeException("DTO to VO convert error", e);
        }
    }

    /**
     * VO转换为DTO
     * @param vo VO对象
     * @param dtoClass DTO类
     * @param <V> VO类型
     * @param <D> DTO类型
     * @return 转换后的DTO对象
     */
    public static <V, D> D voToDTO(V vo, Class<D> dtoClass) {
        try {
            return BeanUtils.copyBean(vo, dtoClass);
        } catch (Exception e) {
            log.error("VO to DTO convert error: ", e);
            throw new RuntimeException("VO to DTO convert error", e);
        }
    }

    /**
     * 实体类列表转换为VO列表
     * @param entityList 实体类列表
     * @param voClass VO类
     * @param <E> 实体类类型
     * @param <V> VO类型
     * @return 转换后的VO列表
     */
    public static <E, V> List<V> entityListToVOList(List<E> entityList, Class<V> voClass) {
        List<V> voList = new ArrayList<>();
        if (entityList != null && !entityList.isEmpty()) {
            for (E entity : entityList) {
                voList.add(entityToVO(entity, voClass));
            }
        }
        return voList;
    }

    /**
     * VO列表转换为实体类列表
     * @param voList VO列表
     * @param entityClass 实体类
     * @param <V> VO类型
     * @param <E> 实体类类型
     * @return 转换后的实体类列表
     */
    public static <V, E> List<E> voListToEntityList(List<V> voList, Class<E> entityClass) {
        List<E> entityList = new ArrayList<>();
        if (voList != null && !voList.isEmpty()) {
            for (V vo : voList) {
                entityList.add(voToEntity(vo, entityClass));
            }
        }
        return entityList;
    }

    /**
     * DTO列表转换为实体类列表
     * @param dtoList DTO列表
     * @param entityClass 实体类
     * @param <D> DTO类型
     * @param <E> 实体类类型
     * @return 转换后的实体类列表
     */
    public static <D, E> List<E> dtoListToEntityList(List<D> dtoList, Class<E> entityClass) {
        List<E> entityList = new ArrayList<>();
        if (dtoList != null && !dtoList.isEmpty()) {
            for (D dto : dtoList) {
                entityList.add(dtoToEntity(dto, entityClass));
            }
        }
        return entityList;
    }

    /**
     * 实体类列表转换为DTO列表
     * @param entityList 实体类列表
     * @param dtoClass DTO类
     * @param <E> 实体类类型
     * @param <D> DTO类型
     * @return 转换后的DTO列表
     */
    public static <E, D> List<D> entityListToDTOList(List<E> entityList, Class<D> dtoClass) {
        List<D> dtoList = new ArrayList<>();
        if (entityList != null && !entityList.isEmpty()) {
            for (E entity : entityList) {
                dtoList.add(entityToDTO(entity, dtoClass));
            }
        }
        return dtoList;
    }

    /**
     * DTO列表转换为VO列表
     * @param dtoList DTO列表
     * @param voClass VO类
     * @param <D> DTO类型
     * @param <V> VO类型
     * @return 转换后的VO列表
     */
    public static <D, V> List<V> dtoListToVOList(List<D> dtoList, Class<V> voClass) {
        List<V> voList = new ArrayList<>();
        if (dtoList != null && !dtoList.isEmpty()) {
            for (D dto : dtoList) {
                voList.add(dtoToVO(dto, voClass));
            }
        }
        return voList;
    }

    /**
     * VO列表转换为DTO列表
     * @param voList VO列表
     * @param dtoClass DTO类
     * @param <V> VO类型
     * @param <D> DTO类型
     * @return 转换后的DTO列表
     */
    public static <V, D> List<D> voListToDTOList(List<V> voList, Class<D> dtoClass) {
        List<D> dtoList = new ArrayList<>();
        if (voList != null && !voList.isEmpty()) {
            for (V vo : voList) {
                dtoList.add(voToDTO(vo, dtoClass));
            }
        }
        return dtoList;
    }
}