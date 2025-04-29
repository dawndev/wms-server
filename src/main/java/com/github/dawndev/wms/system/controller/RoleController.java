package com.github.dawndev.wms.system.controller;

import com.github.dawndev.wms.common.annotation.Log;
import com.github.dawndev.wms.common.controller.BaseController;
import com.github.dawndev.wms.common.domain.QueryRequest;
import com.github.dawndev.wms.common.exception.WmsException;
import com.github.dawndev.wms.system.domain.Role;
import com.github.dawndev.wms.system.domain.RoleMenu;
import com.github.dawndev.wms.system.service.RoleMenuServie;
import com.github.dawndev.wms.system.service.RoleService;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.wuwenze.poi.ExcelKit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Validated
@RestController
@RequestMapping("role")
public class RoleController extends BaseController {

    @Autowired
    private RoleService roleService;
    @Autowired
    private RoleMenuServie roleMenuServie;

    private String message;

    @GetMapping
//    @RequiresPermissions("role:view")
    public Map<String, Object> roleList(QueryRequest queryRequest, Role role) {
        return getDataTable(roleService.findRoles(role, queryRequest));
    }

    @GetMapping("check/{roleName}")
    public boolean checkRoleName(@NotBlank(message = "{required}") @PathVariable String roleName) {
        Role result = this.roleService.findByName(roleName);
        return result == null;
    }

    @GetMapping("menu/{roleId}")
    public List<String> getRoleMenus(@NotBlank(message = "{required}") @PathVariable String roleId) {
        List<RoleMenu> list = this.roleMenuServie.getRoleMenusByRoleId(roleId);
        return list.stream().map(roleMenu -> String.valueOf(roleMenu.getMenuId())).collect(Collectors.toList());
    }

    @Log("新增角色: #{#user.id}")
    @PostMapping
//    @RequiresPermissions("role:add")
    public void addRole(@Valid Role role) throws WmsException {
        try {
            this.roleService.createRole(role);
        } catch (Exception e) {
            message = "新增角色失败";
            log.error(message, e);
            throw new WmsException(message);
        }
    }

    @Log("删除角色")
    @DeleteMapping("/{roleIds}")
//    @RequiresPermissions("role:delete")
    public void deleteRoles(@NotBlank(message = "{required}") @PathVariable String roleIds) throws WmsException {
        try {
            String[] ids = roleIds.split(StringPool.COMMA);
            this.roleService.deleteRoles(ids);
        } catch (Exception e) {
            message = "删除角色失败";
            log.error(message, e);
            throw new WmsException(message);
        }
    }

    @Log("修改角色")
    @PutMapping
//    @RequiresPermissions("role:update")
    public void updateRole(Role role) throws WmsException {
        try {
            this.roleService.updateRole(role);
        } catch (Exception e) {
            message = "修改角色失败";
            log.error(message, e);
            throw new WmsException(message);
        }
    }

    @PostMapping("excel")
//    @RequiresPermissions("role:export")
    public void export(QueryRequest queryRequest, Role role, HttpServletResponse response) throws WmsException {
        try {
            List<Role> roles = this.roleService.findRoles(role, queryRequest).getRecords();
            ExcelKit.$Export(Role.class, response).downXlsx(roles, false);
        } catch (Exception e) {
            message = "导出Excel失败";
            log.error(message, e);
            throw new WmsException(message);
        }
    }
}
