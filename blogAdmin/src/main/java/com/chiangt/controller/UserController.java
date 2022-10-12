package com.chiangt.controller;

import com.chiangt.domain.ResponseResult;
import com.chiangt.domain.entity.Role;
import com.chiangt.domain.entity.User;
import com.chiangt.domain.vo.UserInfoAndRoleIdsVo;
import com.chiangt.service.RoleService;
import com.chiangt.service.UserService;
import com.chiangt.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/system/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @GetMapping("/list")
    public ResponseResult list(Integer pageNum, Integer pageSize, User user){
        return userService.selectUserPage(pageNum, pageSize, user);
    }

    @PostMapping
    public ResponseResult add(@RequestBody User user){
        return userService.addUser(user);
    }

    @DeleteMapping("/{id}")
    public ResponseResult delete(@PathVariable(value = "id")Long id){
        if(id.equals(SecurityUtils.getUserId())){
            return ResponseResult.errorResult(500, "无法删除当前使用的用户");
        }
        userService.removeById(id);
        return ResponseResult.okResult();
    }

    @GetMapping("/{id}")
    public ResponseResult getInfo(@PathVariable(value = "id")Long id){
        List<Role> roleList = roleService.selectAllRole();
        User user = userService.getById(id);
        List<Long> roleIds = roleService.selectRoleIdByUserId(id);
        UserInfoAndRoleIdsVo userInfoAndRoleIdsVo = new UserInfoAndRoleIdsVo(user, roleList, roleIds);
        return ResponseResult.okResult(userInfoAndRoleIdsVo);
    }

    @PutMapping
    public ResponseResult edit(@RequestBody User user){
        userService.updateUser(user);
        return ResponseResult.okResult();
    }

}
