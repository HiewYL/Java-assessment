CREATE TABLE t_user_profile (
    id bigint NOT NULL,
    first_name varchar(100),
    last_name varchar(100),
    home_address_1 varchar(100),
    home_address_2 varchar(100),
    email varchar(50),
    phone varchar(15),
    sts_cd varchar(10) NOT NULL,
    ver_nbr int4 NOT NULL,
    created_time timestamptz NOT NULL,
    CONSTRAINT pk_user_profile PRIMARY KEY(id)
);
