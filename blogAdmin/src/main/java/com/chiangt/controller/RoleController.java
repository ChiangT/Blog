package com.chiangt.controller;

import com.chiangt.domain.ResponseResult;
import com.chiangt.domain.dto.ChangeRoleStatusDto;
import com.chiangt.domain.entity.Role;
import com.chiangt.domain.vo.PageVo;
import com.chiangt.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/system/role")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @PostMapping
    public ResponseResult add(@RequestBody Role role){
        roleService.insertRole(role);
        return ResponseResult.okResult();
    }

    @GetMapping("/list")
    public ResponseResult list(Integer pageNum, Integer pageSize, Role role){
        PageVo pageVo = roleService.selectRoleList(pageNum, pageSize, role);
        return ResponseResult.okResult(pageVo);
    }

    @PutMapping("/changeStatus")
    public ResponseResult changeStatus(@RequestBody ChangeRoleStatusDto changeRoleStatusDto){
        Role role = new Role();
        role.setId(changeRoleStatusDto.getRoleId());
        role.setStatus(changeRoleStatusDto.getStatus());
        return ResponseResult.okResult(roleService.updateById(role));
    }

    @DeleteMapping("/{id}")
    public ResponseResult delete(@PathVariable(value = "id")Long id){
        roleService.removeById(id);
        return ResponseResult.okResult();
    }

    @GetMapping("/listAllRole")
    public ResponseResult listAllRole(){
        List<Role> roleList = roleService.selectAllRole();
        return ResponseResult.okResult(roleList);
    }
}
