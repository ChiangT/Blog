package com.chiangt.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class UserVo {
    private Long id;
    private String userName;
    private String nickName;
    private String status;
    private String email;
    private String phonenumber;
    private String sex;
    private String avatar;
    private Long createBy;
    private Date createTime;
    private Long updateBy;
    private Date updateTime;
}
