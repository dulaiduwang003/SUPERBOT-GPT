package com.cn.common.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
@TableName(value = "blog_article")
public class BlogArticle {

  @TableId(type = IdType.AUTO)
  private Long blogArticleId;

  private Long blogCategoryId;

  private String title;

  private String cover;

  private String content;

  @TableField(fill = FieldFill.INSERT)
  private LocalDateTime createTime;

  @TableField(fill = FieldFill.INSERT_UPDATE)
  private LocalDateTime updateTime;


}
