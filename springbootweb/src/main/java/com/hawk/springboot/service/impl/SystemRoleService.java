package com.hawk.springboot.service.impl;


import com.hawk.springboot.common.BeanUtil;
import com.hawk.springboot.common.PageBean;
import com.hawk.springboot.dal.entity.SystemModule;
import com.hawk.springboot.dal.entity.SystemRole;
import com.hawk.springboot.dal.entity.SystemRoleModule;
import com.hawk.springboot.dal.mapper.SystemRoleMapper;
import com.hawk.springboot.enums.DataStatus;
import com.hawk.springboot.service.ISystemRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by yannfeng on 2016/9/1.
 */
@Service
public class SystemRoleService implements ISystemRoleService {

    @Autowired
    private SystemRoleMapper sysRoleMapper;

    /**
     * 新增或更新系统角色
     * @param role
     * @return
     */
    @Override
    public int saveOrUpdateSystemRole(SystemRole role, Integer optUserId) {
        int rs = 0;
        if(role != null) {
            Integer id = role.getId();
            role.setUpdateUserId(optUserId);
            if(id != null) {

                SystemRole updateSystemRole = sysRoleMapper.getById(id);
                BeanUtil.copyProperties(updateSystemRole, role, true);
                rs = sysRoleMapper.update(updateSystemRole);
            } else {
                role.setCreateUserId(optUserId);
                rs = sysRoleMapper.insert(role);
            }
        }
        return rs;
    }

    /**
     * 删除系统角色
     * @param id
     * @return
     */
    @Override
    public int deleteSystemRole(Integer id) {
        int rs = sysRoleMapper.delete(id);

        if(rs > 0) {
            sysRoleMapper.deleteSystemRoleModuleByRoleId(id);     //级联删除角色模块
        }
        return rs;
    }

    @Override
    public int deleteSystemRole(Integer id, Integer optUserId, boolean real) {

        int rs = 0;
        if(real) {
            rs = sysRoleMapper.delete(id);
        } else {
            SystemRole role = sysRoleMapper.getById(id);
            role.setStatus(DataStatus.DELETE.toString());
            rs = saveOrUpdateRoleAndRoleModule(role, optUserId);
        }

        if(rs > 0) {
            sysRoleMapper.deleteSystemRoleModuleByRoleId(id);     //级联删除角色模块
        }
        return rs;
    }

    /**
     * 查询所有系统角色
     * @return
     */
    @Override
    public List<SystemRole> findAllSystemRole() {
        return sysRoleMapper.queryAll();
    }

    @Override
    public List<SystemRole> findSystemRoleByPage(PageBean page) {
        List<SystemRole> modules = sysRoleMapper.queryByPage(page);
        page.setRowCount(sysRoleMapper.countByFilter(page.getFilters()).intValue());
        return modules;
    }

    @Override
    public List<SystemRole> findAllSystemRoleModules() {
        return sysRoleMapper.queryAllSystemRoleModules();
    }

    @Override
    public SystemRole getSystemRoleModules(Integer id) {
        return sysRoleMapper.getSystemRoleModulesById(id);
    }

    @Override
    public int insertSystemRoleModule(SystemRoleModule roleModule) {
        return sysRoleMapper.insertSystemRoleModule(roleModule);
    }

    @Override
    public int insertSystemRoleModuleBatch(List<SystemRoleModule> roleModules) {
        return sysRoleMapper.insertSystemRoleModuleBatch(roleModules);
    }

    /**
     * 保存更新管理员角色及角色模块
     * @param role
     * @return
     */
    @Override
    public int saveOrUpdateRoleAndRoleModule(SystemRole role, Integer optUserId) {
        int rs = 0;
        if(role != null) {
            List<SystemModule> modules = role.getModules();
            List<SystemRoleModule> roleModules = null;
            rs = saveOrUpdateSystemRole(role, optUserId);
            if(modules != null && role.getId() != null) {

                sysRoleMapper.deleteSystemRoleModuleByRoleId(role.getId());

                roleModules = new ArrayList<SystemRoleModule>();
                for(SystemModule module:modules) {
                    if(module != null && module.getId() != null) {
                        SystemRoleModule roleModule = new SystemRoleModule();
                        roleModule.setRoleId(role.getId());
                        roleModule.setModuleId(module.getId());
                        roleModules.add(roleModule);
                    }
                }
                rs = insertSystemRoleModuleBatch(roleModules);
            }
        }
        return rs;
    }
}
