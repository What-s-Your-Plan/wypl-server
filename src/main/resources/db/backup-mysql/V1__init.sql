create table label
(
    label_id    integer                                                                                                                                                                                        not null auto_increment,
    member_id   integer                                                                                                                                                                                        not null,
    created_at  datetime(6)                                                                                                                                                                                    not null,
    deleted_at  datetime(6),
    modified_at datetime(6)                                                                                                                                                                                    not null,
    title       varchar(15)                                                                                                                                                                                    not null,
    color       enum ('labelRed','labelPink','labelOrange','labelYellow','labelGreen','labelLeaf','labelBlue','labelSky','labelNavy','labelIndigo','labelPurple','labelLavender','labelCharcoal','labelBrown') not null,
    primary key (label_id)
) engine = InnoDB;

create table member
(
    birthday      date,
    member_id     integer                                                                                                                                                                                        not null auto_increment,
    created_at    datetime(6)                                                                                                                                                                                    not null,
    deleted_at    datetime(6),
    modified_at   datetime(6)                                                                                                                                                                                    not null,
    nickname      varchar(20)                                                                                                                                                                                    not null,
    email         varchar(50)                                                                                                                                                                                    not null,
    profile_image varchar(100),
    color         enum ('labelRed','labelPink','labelOrange','labelYellow','labelGreen','labelLeaf','labelBlue','labelSky','labelNavy','labelIndigo','labelPurple','labelLavender','labelCharcoal','labelBrown') not null,
    timezone      enum ('KOREA','WEST_USA','EAST_USA','ENGLAND')                                                                                                                                                 not null,
    primary key (member_id)
) engine = InnoDB;

create table member_group
(
    group_id    integer                                                                                                                                                                                        not null,
    member_id   integer                                                                                                                                                                                        not null,
    created_at  datetime(6)                                                                                                                                                                                    not null,
    deleted_at  datetime(6),
    modified_at datetime(6)                                                                                                                                                                                    not null,
    color       enum ('labelRed','labelPink','labelOrange','labelYellow','labelGreen','labelLeaf','labelBlue','labelSky','labelNavy','labelIndigo','labelPurple','labelLavender','labelCharcoal','labelBrown') not null,
    state       enum ('PENDING','ACCEPTED')                                                                                                                                                                    not null,
    primary key (group_id, member_id)
) engine = InnoDB;

create table member_schedule
(
    member_id          integer     not null,
    member_schedule_id integer     not null auto_increment,
    schedule_id        integer     not null,
    created_at         datetime(6) not null,
    deleted_at         datetime(6),
    modified_at        datetime(6) not null,
    primary key (member_schedule_id)
) engine = InnoDB;

create table repetition
(
    day_of_week           BINARY(7),
    repetition_end_date   date,
    repetition_id         integer     not null auto_increment,
    repetition_start_date date,
    week                  integer     not null,
    created_at            datetime(6) not null,
    deleted_at            datetime(6),
    modified_at           datetime(6) not null,
    repetition_cycle      enum ('WEEK','MONTH','YEAR'),
    primary key (repetition_id)
) engine = InnoDB;

create table review
(
    member_schedule_id integer,
    review_id          integer     not null auto_increment,
    created_at         datetime(6) not null,
    deleted_at         datetime(6),
    modified_at        datetime(6) not null,
    title              varchar(50) not null,
    primary key (review_id)
) engine = InnoDB;

create table schedule
(
    group_id      integer,
    label_id      integer,
    repetition_id integer,
    schedule_id   integer                 not null auto_increment,
    created_at    datetime(6)             not null,
    deleted_at    datetime(6),
    end_date      datetime(6)             not null,
    modified_at   datetime(6)             not null,
    start_date    datetime(6)             not null,
    title         varchar(50)             not null,
    description   varchar(255),
    category      enum ('MEMBER','GROUP') not null,
    primary key (schedule_id)
) engine = InnoDB;

create table side_tab
(
    d_day     date,
    member_id integer not null,
    title     varchar(20),
    goal      varchar(60),
    memo      varchar(1000),
    primary key (member_id)
) engine = InnoDB;

create table social_member
(
    member_id      integer         not null,
    oauth_id       varchar(255)    not null,
    oauth_provider enum ('GOOGLE') not null,
    primary key (member_id)
) engine = InnoDB;

create table group_tbl
(
    group_id    integer     not null auto_increment,
    owner_id    integer     not null,
    created_at  datetime(6) not null,
    deleted_at  datetime(6),
    modified_at datetime(6) not null,
    name        varchar(20) not null,
    description varchar(50),
    primary key (group_id)
) engine = InnoDB;

create table todo
(
    is_completed bit default false not null,
    member_id    integer,
    todo_id      integer           not null auto_increment,
    created_at   datetime(6)       not null,
    deleted_at   datetime(6),
    modified_at  datetime(6)       not null,
    content      varchar(765)      not null,
    primary key (todo_id)
) engine = InnoDB;

alter table member
    add constraint UK_mbmcqelty0fbrvxp1q58dn57t unique (email);

alter table member_group
    add constraint FKig0052fud399pxbsu3fxf9lar
        foreign key (group_id)
            references group_tbl (group_id);

alter table member_group
    add constraint FKi9080rfwtrt5jlvim4mcg4rl4
        foreign key (member_id)
            references member (member_id);

alter table member_schedule
    add constraint FKjmxjh6tt9uegmkeb6kqn7xoj5
        foreign key (member_id)
            references member (member_id);

alter table member_schedule
    add constraint FK85h38v1n4lgwdt3xi0mfujew3
        foreign key (schedule_id)
            references schedule (schedule_id);

alter table review
    add constraint FKf62i7g0ky07ac1yyi9uxij02u
        foreign key (member_schedule_id)
            references member_schedule (member_schedule_id);

alter table schedule
    add constraint FKjclesvygb0q6kb7wp9sg9fg43
        foreign key (label_id)
            references label (label_id);

alter table schedule
    add constraint FK3yov0ab7903kth6neavo1sn0m
        foreign key (repetition_id)
            references repetition (repetition_id);

alter table side_tab
    add constraint FKfpj8occ9lg157t785rs51g6ou
        foreign key (member_id)
            references member (member_id);

alter table social_member
    add constraint FKkreqgrxo2y1kx1ftdhkdm7k24
        foreign key (member_id)
            references member (member_id);

alter table group_tbl
    add constraint FKjyrllihwtp18idx6qdnkg24tf
        foreign key (owner_id)
            references member (member_id);

alter table todo
    add constraint FK67o67f2ave0yd2pb137aoh603
        foreign key (member_id)
            references member (member_id);