package com.chiangt.domain.vo;

import com.chiangt.domain.entity.Role;
import com.chiangt.domain.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserInfoAndRoleIdsVo {
    private User user;
    private List<Role> roleList;
    private List<Long> roleIds;
}
