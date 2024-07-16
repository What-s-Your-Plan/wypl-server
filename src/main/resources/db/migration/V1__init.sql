create table WYPL.LABEL
(
    LABEL_ID    NUMBER(10) generated as identity
        primary key,
    CREATED_AT  TIMESTAMP(6)      not null,
    DELETED_AT  TIMESTAMP(6),
    MODIFIED_AT TIMESTAMP(6)      not null,
    COLOR       VARCHAR2(20 char) not null
        check (color in ('labelRed', 'labelPink', 'labelOrange', 'labelYellow', 'labelGreen', 'labelLeaf', 'labelBlue',
                         'labelSky', 'labelNavy', 'labelIndigo', 'labelPurple', 'labelLavender', 'labelCharcoal',
                         'labelBrown')),
    MEMBER_ID   NUMBER(10)        not null,
    TITLE       VARCHAR2(15 char) not null
)
/

create table WYPL.MEMBER
(
    MEMBER_ID     NUMBER(10) generated as identity
        primary key,
    CREATED_AT    TIMESTAMP(6)      not null,
    DELETED_AT    TIMESTAMP(6),
    MODIFIED_AT   TIMESTAMP(6)      not null,
    BIRTHDAY      DATE,
    COLOR         VARCHAR2(20 char) not null
        check (color in ('labelRed', 'labelPink', 'labelOrange', 'labelYellow', 'labelGreen', 'labelLeaf', 'labelBlue',
                         'labelSky', 'labelNavy', 'labelIndigo', 'labelPurple', 'labelLavender', 'labelCharcoal',
                         'labelBrown')),
    EMAIL         VARCHAR2(50 char) not null
        constraint UK_MBMCQELTY0FBRVXP1Q58DN57T
            unique,
    NICKNAME      VARCHAR2(20 char) not null,
    PROFILE_IMAGE VARCHAR2(100 char),
    TIMEZONE      VARCHAR2(10 char) not null
        check (timezone in ('KOREA', 'WEST_USA', 'EAST_USA', 'ENGLAND'))
)
/

create table WYPL.GROUPS
(
    GROUP_ID    NUMBER(10) generated as identity
        primary key,
    CREATED_AT  TIMESTAMP(6)      not null,
    DELETED_AT  TIMESTAMP(6),
    MODIFIED_AT TIMESTAMP(6)      not null,
    COLOR       VARCHAR2(20 char)
        check (color in ('labelRed', 'labelPink', 'labelOrange', 'labelYellow', 'labelGreen', 'labelLeaf', 'labelBlue',
                         'labelSky', 'labelNavy', 'labelIndigo', 'labelPurple', 'labelLavender', 'labelCharcoal',
                         'labelBrown')),
    NAME        VARCHAR2(20 char) not null,
    OWNER_ID    NUMBER(10)        not null
        constraint FKD7Y6AGDAQ9SKOCLCUA8JT0MG7
            references WYPL.MEMBER
)
/

create table WYPL.MEMBER_GROUP
(
    GROUP_ID    NUMBER(10)        not null
        constraint FKQBL6CYEPSHW3UQJPE24EKE4QU
            references WYPL.GROUPS,
    MEMBER_ID   NUMBER(10)        not null
        constraint FKI9080RFWTRT5JLVIM4MCG4RL4
            references WYPL.MEMBER,
    CREATED_AT  TIMESTAMP(6)      not null,
    DELETED_AT  TIMESTAMP(6),
    MODIFIED_AT TIMESTAMP(6)      not null,
    COLOR       VARCHAR2(20 char) not null
        check (color in ('labelRed', 'labelPink', 'labelOrange', 'labelYellow', 'labelGreen', 'labelLeaf', 'labelBlue',
                         'labelSky', 'labelNavy', 'labelIndigo', 'labelPurple', 'labelLavender', 'labelCharcoal',
                         'labelBrown')),
    STATE       VARCHAR2(20 char) not null
        check (state in ('PENDING', 'ACCEPTED')),
    primary key (GROUP_ID, MEMBER_ID)
)
/

create table WYPL.REPETITION
(
    REPETITION_ID         NUMBER(10) generated as identity
        primary key,
    CREATED_AT            TIMESTAMP(6) not null,
    DELETED_AT            TIMESTAMP(6),
    MODIFIED_AT           TIMESTAMP(6) not null,
    DAY_OF_WEEK           NUMBER(10),
    REPETITION_CYCLE      VARCHAR2(255 char)
        check (repetition_cycle in ('WEEK', 'MONTH', 'YEAR')),
    REPETITION_END_DATE   DATE,
    REPETITION_START_DATE DATE,
    WEEK                  NUMBER(10)
)
/

create table WYPL.SCHEDULE
(
    SCHEDULE_ID   NUMBER(10) generated as identity
        primary key,
    CREATED_AT    TIMESTAMP(6)       not null,
    DELETED_AT    TIMESTAMP(6),
    MODIFIED_AT   TIMESTAMP(6)       not null,
    CATEGORY      VARCHAR2(255 char) not null
        check (category in ('MEMBER', 'GROUP')),
    DESCRIPTION   VARCHAR2(255 char),
    END_DATE      TIMESTAMP(6)       not null,
    GROUP_ID      NUMBER(10),
    START_DATE    TIMESTAMP(6)       not null,
    TITLE         VARCHAR2(50 char)  not null,
    LABEL_ID      NUMBER(10)
        constraint FKJCLESVYGB0Q6KB7WP9SG9FG43
            references WYPL.LABEL,
    REPETITION_ID NUMBER(10)
        constraint FK3YOV0AB7903KTH6NEAVO1SN0M
            references WYPL.REPETITION
)
/

create table WYPL.MEMBER_SCHEDULE
(
    MEMBER_SCHEDULE_ID NUMBER(10) generated as identity
        primary key,
    CREATED_AT         TIMESTAMP(6) not null,
    DELETED_AT         TIMESTAMP(6),
    MODIFIED_AT        TIMESTAMP(6) not null,
    WRITE_REVIEW       NUMBER(1)    not null
        check (write_review in (0, 1)),
    MEMBER_ID          NUMBER(10)   not null
        constraint FKJMXJH6TT9UEGMKEB6KQN7XOJ5
            references WYPL.MEMBER,
    SCHEDULE_ID        NUMBER(10)   not null
        constraint FK85H38V1N4LGWDT3XI0MFUJEW3
            references WYPL.SCHEDULE
)
/

create table WYPL.REVIEW
(
    REVIEW_ID          NUMBER(10) generated as identity
        primary key,
    CREATED_AT         TIMESTAMP(6)      not null,
    DELETED_AT         TIMESTAMP(6),
    MODIFIED_AT        TIMESTAMP(6)      not null,
    TITLE              VARCHAR2(50 char) not null,
    MEMBER_SCHEDULE_ID NUMBER(10)
        constraint FKF62I7G0KY07AC1YYI9UXIJ02U
            references WYPL.MEMBER_SCHEDULE
)
/

create table WYPL.SIDE_TAB
(
    MEMBER_ID NUMBER(10) not null
        primary key
        constraint FKFPJ8OCC9LG157T785RS51G6OU
            references WYPL.MEMBER,
    TITLE     VARCHAR2(20 char),
    D_DAY     DATE,
    GOAL      VARCHAR2(60 char),
    MEMO      VARCHAR2(1000 char)
)
/

create table WYPL.SOCIAL_MEMBER
(
    MEMBER_ID      NUMBER(10)         not null
        primary key
        constraint FKKREQGRXO2Y1KX1FTDHKDM7K24
            references WYPL.MEMBER,
    OAUTH_ID       VARCHAR2(255 char) not null,
    OAUTH_PROVIDER VARCHAR2(255 char) not null
        check (oauth_provider in ('GOOGLE'))
)
/

create table WYPL.TODO
(
    TODO_ID      NUMBER(10) generated as identity
        primary key,
    CREATED_AT   TIMESTAMP(6)       not null,
    DELETED_AT   TIMESTAMP(6),
    MODIFIED_AT  TIMESTAMP(6)       not null,
    CONTENT      VARCHAR2(765 char) not null,
    IS_COMPLETED NUMBER(1)          not null
        check (is_completed in (0, 1)),
    MEMBER_ID    NUMBER(10)
        constraint FK67O67F2AVE0YD2PB137AOH603
            references WYPL.MEMBER
)
/