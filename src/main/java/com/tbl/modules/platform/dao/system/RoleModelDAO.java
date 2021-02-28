package com.tbl.modules.platform.dao.system;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.tbl.modules.platform.entity.system.RoleModel;

import java.util.List;
import java.util.Map;

/**
 * 角色模块DAO
 *
 * @author anss
 * @date 2018-09-15
 */
public interface RoleModelDAO extends BaseMapper<RoleModel> {

    /**
     * 删除
     * @param modelId
     * @author anss
     * @date 2018-09-15
     * @return
     */
    boolean deleteRolemodel(Long modelId);

    /**
     * 保存
     * @author anss
     * @date 2018-09-15
     * @param map
     * @return
     */
    boolean saveRolemodel(Map<String, Object> map);

    /**
     * @author anss
     * @date 2018-09-15
     * @param modelId
     * @return
     */
    List<Map<String, Object>> getRolemodelByModelId(Long modelId);


}
