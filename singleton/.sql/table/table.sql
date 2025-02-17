create table if not exists ts.user
(
    user_id     bigint                             not null
        primary key,
    open_id     varchar(255)                       null,
    nick_name   varchar(255)                       null,
    energy      bigint   default 0                 not null,
    avatar      varchar(255)                       null,
    create_time datetime default CURRENT_TIMESTAMP not null,
    update_time datetime default CURRENT_TIMESTAMP null
);

create table if not exists ts.workflows
(
    workflows_id          int auto_increment
        primary key,
    workflows_category_id int                                                                 null,
    result_type           enum ('VIDEO', 'AUDIO', 'IMAGE', 'MODEL') default 'IMAGE'           not null,
    title                 varchar(255)                                                        not null,
    input_node            varchar(255)                                                        null,
    original_image        varchar(255)                                                        not null,
    now_image             varchar(255)                                                        not null,
    introduced            varchar(255)                                                        not null,
    json                  json                                                                not null,
    energy                int                                       default 1                 not null comment '0未审核 1审核通过 2拒绝审核',
    create_time           datetime                                  default CURRENT_TIMESTAMP not null,
    update_time           datetime                                  default CURRENT_TIMESTAMP not null
);

create table if not exists ts.workflows_category
(
    workflows_category_id int auto_increment
        primary key,
    category_name         varchar(255)                       null,
    create_time           datetime default CURRENT_TIMESTAMP null,
    update_time           datetime default CURRENT_TIMESTAMP null
);

create table if not exists ts.workflows_comments
(
    workflows_comments_id int auto_increment
        primary key,
    user_id               bigint                             not null,
    workflows_id          bigint                             not null,
    content               text                               null,
    create_time           datetime default CURRENT_TIMESTAMP not null,
    update_time           datetime default CURRENT_TIMESTAMP not null
);

create index workflows_comments_user_id_index
    on ts.workflows_comments (user_id);

create index workflows_comments_workflows_id_index
    on ts.workflows_comments (workflows_id);

create table if not exists ts.workflows_form
(
    workflows_form_id int auto_increment
        primary key,
    workflows_id      int                                                  null,
    tips              text                                                 null,
    node_key          varchar(255)                                         null,
    node_digital      varchar(255)                                         not null,
    type              enum ('TEXT_PROMPT', 'IMAGE_UPLOAD', 'VIDEO_UPLOAD') null
);

create index workflows_form_workflows_id_index
    on ts.workflows_form (workflows_id);

create table if not exists ts.workflows_works
(
    workflows_works_id int auto_increment
        primary key,
    workflows_id       int                                                                 not null,
    user_id            bigint                                                              null,
    url                varchar(255)                                                        not null,
    result_type        enum ('VIDEO', 'IMAGE', 'AUDIO', 'MODEL') default 'IMAGE'           not null,
    create_time        datetime                                  default CURRENT_TIMESTAMP not null,
    update_time        datetime                                  default CURRENT_TIMESTAMP not null
);

