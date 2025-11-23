-- auto-generated definition
create table user
(
    id            bigint auto_increment comment 'Primary Key ID'
        primary key,
    username      varchar(100)                             not null comment 'User Nickname',
    userAccount   varchar(50)                              not null comment 'Login Account',
    avatarUrl     varchar(500)                             null comment 'Avatar URL',
    gender        tinyint        default 0                 null comment 'Gender 0-Unknown 1-Male 2-Female',
    userPassword  varchar(255)                             not null comment 'Encrypted Password',
    phone         varchar(20)                              null comment 'Phone Number',
    usmEmail      varchar(100)                             not null comment 'USM Email (@usm.my)',
    emailVerified tinyint        default 0                 null comment 'Email Verified 0-Not Verified 1-Verified',
    campus        varchar(100)                             null comment 'Campus',
    studentId     varchar(20)                              null comment 'Student ID',
    school        varchar(100)                             null comment 'school',
    balance       decimal(10, 2) default 0.00              null comment 'Account Balance',
    userStatus    int            default 0                 null comment 'User Status 0-Not Verified 1-Normal 2-Disabled',
    createTime    timestamp      default CURRENT_TIMESTAMP not null comment 'Create Time',
    updateTime    timestamp      default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment 'Update Time',
    isDelete      tinyint        default 0                 null comment 'Logical Delete 0-Not Deleted 1-Deleted',
    userRole      int            default 0                 null comment 'User Role 0-Normal User 1-Administrator',
    address       varchar(500)                             null comment 'Default address for express delivery',
    constraint uk_phone
        unique (phone),
    constraint uk_studentId
        unique (studentId),
    constraint uk_userAccount
        unique (userAccount),
    constraint uk_usmEmail
        unique (usmEmail)
)
    comment 'USM Secondhand Trading Platform User Table' engine = InnoDB
                                                         collate = utf8mb4_unicode_ci;

create index idx_campus
    on user (campus);

create index idx_createTime
    on user (createTime);

create index idx_emailVerified
    on user (emailVerified);

create index idx_faculty
    on user (school);

create index idx_userStatus
    on user (userStatus);

create index idx_username
    on user (username);



-- auto-generated definition
create table goods
(
    id          bigint auto_increment
        primary key,
    title       varchar(200)                        not null,
    description text                                null,
    price       decimal(10, 2)                      not null,
    categoryId  bigint                              not null,
    userId      bigint                              not null,
    coverImage  varchar(500)                        null,
    images      json                                null comment 'Product images list',
    `condition` tinyint   default 1                 null comment 'Product condition 1-New 2-Like New 3-Gently Used 4-Used',
    status      tinyint   default 1                 null comment 'Status 1-Available 2-Sold 3-Inactive',
    viewCount   int       default 0                 null,
    likeCount   int       default 0                 null,
    campus      varchar(100)                        null,
    contactType tinyint   default 1                 null comment 'Contact method 1-In-app message 2-Phone 3-Email',
    createTime  timestamp default CURRENT_TIMESTAMP not null,
    updateTime  timestamp default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP,
    isDelete    tinyint   default 0                 null
)
    comment 'Goods Table' engine = InnoDB
                          collate = utf8mb4_unicode_ci;

create index idx_campus
    on goods (campus);

create index idx_categoryId
    on goods (categoryId);

create index idx_createTime
    on goods (createTime);

create index idx_status
    on goods (status);

create index idx_userId
    on goods (userId);


CREATE TABLE `category` (
                            `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT 'Primary Key ID',
                            `name` VARCHAR(100) NOT NULL COMMENT 'Category Name',
                            `status` TINYINT DEFAULT 1 COMMENT 'Status 1-Active 0-Inactive',
                            `create_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                            PRIMARY KEY (`id`),
                            UNIQUE KEY `uk_name` (`name`)
) COMMENT 'Product Category Table';


-- auto-generated definition
create table orders
(
    id             bigint auto_increment comment 'Primary Key ID'
        primary key,
    orderNo        varchar(50)                         not null comment 'Order Number',
    goodsId        bigint                              not null comment 'Goods ID',
    buyerId        bigint                              not null comment 'Buyer User ID',
    sellerId       bigint                              not null comment 'Seller User ID',
    quantity       int       default 1                 null comment 'Quantity',
    totalAmount    decimal(10, 2)                      not null comment 'Total Amount',
    deliveryMethod tinyint   default 1                 null comment 'Delivery Method 1-Pickup 2-Express Delivery',
    pickupLocation varchar(200)                        null comment 'Pickup Location',
    pickupTime     timestamp                           null comment 'Scheduled Pickup Time',
    expressAddress varchar(500)                        null comment 'Express Delivery Address',
    orderStatus    tinyint   default 1                 null comment 'Order Status 1-Pending 2-Paid 3-Shipped 4-Completed 5-Cancelled 6-Refunding',
    paymentMethod  tinyint   default 1                 null comment 'Payment Method 1-Balance 2-Cash',
    paymentTime    timestamp                           null comment 'Payment Time',
    buyerNote      varchar(500)                        null comment 'Buyer Note',
    sellerNote     varchar(500)                        null comment 'Seller Note',
    createTime     timestamp default CURRENT_TIMESTAMP not null comment 'Create Time',
    updateTime     timestamp default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment 'Update Time',
    completeTime   timestamp                           null comment 'Order Completion Time',
    constraint uk_order_no
        unique (orderNo)
)
    comment 'Order Table';

create index idx_buyer_id
    on orders (buyerId);

create index idx_create_time
    on orders (createTime);

create index idx_goods_id
    on orders (goodsId);

create index idx_order_status
    on orders (orderStatus);

create index idx_seller_id
    on orders (sellerId);


-- auto-generated definition
create table category
(
    id         bigint auto_increment comment 'Primary Key ID'
        primary key,
    name       varchar(100)                        not null comment 'Category Name',
    status     tinyint   default 1                 null comment 'Status 1-Active 0-Inactive',
    createTime timestamp default CURRENT_TIMESTAMP not null comment 'Create Time',
    constraint uk_name
        unique (name)
)
    comment 'Product Category Table';



