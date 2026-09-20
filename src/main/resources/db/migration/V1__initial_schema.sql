
create table entry (
                       created_at datetime(6) not null,
                       id bigint not null auto_increment,
                       leaderboard_id bigint not null,
                       updated_at datetime(6) not null,
                       description varchar(255),
                       name varchar(255) not null,
                       primary key (id)
) engine=InnoDB;

create table entry_tag (
                           entry_id bigint not null,
                           id bigint not null auto_increment,
                           tag_id bigint not null,
                           primary key (id)
) engine=InnoDB;

create table leaderboard (
                             created_at datetime(6) not null,
                             id bigint not null auto_increment,
                             owner_id bigint not null,
                             updated_at datetime(6) not null,
                             description varchar(255),
                             name varchar(255) not null,
                             primary key (id)
) engine=InnoDB;

create table leaderboard_invitation (
                                        created_at datetime(6) not null,
                                        id bigint not null auto_increment,
                                        leaderboard_id bigint not null,
                                        updated_at datetime(6) not null,
                                        user_id bigint not null,
                                        role enum ('ADMIN','EDITOR','VIEWER') not null,
                                        status enum ('ACCEPTED','DECLINED','PENDING') not null,
                                        primary key (id)
) engine=InnoDB;

create table metric (
                        id bigint not null auto_increment,
                        leaderboard_id bigint not null,
                        description varchar(255),
                        name varchar(255) not null,
                        primary key (id)
) engine=InnoDB;

create table refresh_tokens (
                                revoked bit not null,
                                created_at datetime(6) not null,
                                expires_at datetime(6) not null,
                                id bigint not null auto_increment,
                                updated_at datetime(6) not null,
                                user_id bigint not null,
                                token varchar(255) not null,
                                primary key (id)
) engine=InnoDB;

create table tag (
                     id bigint not null auto_increment,
                     tag_category_id bigint not null,
                     name varchar(255) not null,
                     primary key (id)
) engine=InnoDB;

create table tag_category (
                              id bigint not null auto_increment,
                              leaderboard_id bigint not null,
                              description varchar(255),
                              name varchar(255) not null,
                              primary key (id)
) engine=InnoDB;

create table user (
                      created_at datetime(6) not null,
                      id bigint not null auto_increment,
                      updated_at datetime(6) not null,
                      email varchar(255) not null,
                      password varchar(255) not null,
                      username varchar(255) not null,
                      primary key (id)
) engine=InnoDB;

create table user_leaderboard (
                                  id bigint not null auto_increment,
                                  joined_at datetime(6) not null,
                                  leaderboard_id bigint not null,
                                  user_id bigint not null,
                                  role enum ('ADMIN','EDITOR','VIEWER') not null,
                                  primary key (id)
) engine=InnoDB;

create table user_relationship (
                                   created_at datetime(6) not null,
                                   id bigint not null auto_increment,
                                   updated_at datetime(6) not null,
                                   user_high_id bigint not null,
                                   user_low_id bigint not null,
                                   status enum ('BLOCK_BOTH','BLOCK_HIGH_LOW','BLOCK_LOW_HIGH','FRIENDS','PENDING_HIGH_LOW','PENDING_LOW_HIGH') not null,
                                   primary key (id)
) engine=InnoDB;

create table value (
                       value float(53) not null,
                       entry_id bigint not null,
                       id bigint not null auto_increment,
                       metric_id bigint not null,
                       primary key (id)
) engine=InnoDB;

create table view (
                      base bit not null,
                      id bigint not null auto_increment,
                      user_leaderboard_id bigint not null,
                      name varchar(255) not null,
                      visibility enum ('HIDDEN','SHOW'),
                      primary key (id)
) engine=InnoDB;

create table view_metric_config (
                                    position integer not null,
                                    target float(53),
                                    id bigint not null auto_increment,
                                    metric_id bigint not null,
                                    view_id bigint not null,
                                    numeric_filter enum ('EQUALS','GREATER_OR_EQUAL','GREATER_THAN','LESS_OR_EQUAL','LESS_THAN','NOT_EQUALS'),
                                    sort enum ('ASC','DESC') not null,
                                    visibility enum ('HIDDEN','SHOW') not null,
                                    primary key (id)
) engine=InnoDB;

create table view_tag_config (
                                 id bigint not null auto_increment,
                                 tag_category_id bigint not null,
                                 view_id bigint not null,
                                 target varchar(255),
                                 filter enum ('CONTAINS','ENDS_WITH','EQUALS','STARTS_WITH') not null,
                                 primary key (id)
) engine=InnoDB;

alter table entry
    add constraint uk_entry_pair unique (name, leaderboard_id);

alter table leaderboard
    add constraint uk_leaderboard_owner_name unique (owner_id, name);

alter table leaderboard_invitation
    add constraint uk_leaderboard_invitation_user unique (leaderboard_id, user_id);

alter table refresh_tokens
    add constraint UKghpmfn23vmxfu3spu3lfg4r2d unique (token);

alter table tag
    add constraint uk_tag_category_name unique (tag_category_id, name);

alter table user
    add constraint UKob8kqyqqgmefl0aco34akdtpe unique (email);

alter table user
    add constraint uk_username unique (username);

alter table user_leaderboard
    add constraint uk_user_leaderboard_pair unique (user_id, leaderboard_id);

alter table user_relationship
    add constraint uk_user_relationship_pair unique (user_low_id, user_high_id);

alter table value
    add constraint uk_value_pair unique (metric_id, entry_id);

alter table view
    add constraint uk_view_user_leaderboard_id unique (user_leaderboard_id, name);

alter table entry
    add constraint FKh0mt9e624pwgbv94s7h3apy33
        foreign key (leaderboard_id)
            references leaderboard (id);

alter table entry_tag
    add constraint FKf6817de9r89m9d1k0nbf04gg1
        foreign key (entry_id)
            references entry (id);

alter table entry_tag
    add constraint FKqr14bxpjx3dbw6tficox82syb
        foreign key (tag_id)
            references tag (id);

alter table leaderboard
    add constraint FKcmfs7ctrw1lfjryp1fw9jp6dg
        foreign key (owner_id)
            references user (id);

alter table leaderboard_invitation
    add constraint FKaf15kbi6j8flkuaknt2gled55
        foreign key (leaderboard_id)
            references leaderboard (id);

alter table leaderboard_invitation
    add constraint FKb79alhuyt3khdibjoxvjaw6lj
        foreign key (user_id)
            references user (id);

alter table metric
    add constraint FKsoqdcuheiaoa7cqkslky5qc09
        foreign key (leaderboard_id)
            references leaderboard (id);

alter table refresh_tokens
    add constraint FKjwc9veyjcjfkej6rnnbsijfvh
        foreign key (user_id)
            references user (id);

alter table tag
    add constraint FK1cwvxtxoapw1bpqhbnspq0dnv
        foreign key (tag_category_id)
            references tag_category (id);

alter table tag_category
    add constraint FK27aodanb7sc2xxs66urhloo3s
        foreign key (leaderboard_id)
            references leaderboard (id);

alter table user_leaderboard
    add constraint FKh7b2ykaltb0ss8qxaxr9qb35v
        foreign key (leaderboard_id)
            references leaderboard (id);

alter table user_leaderboard
    add constraint FK18alokgeeva1bj4gnpd60grvk
        foreign key (user_id)
            references user (id);

alter table user_relationship
    add constraint FKkfg2vjhv4imxmdblsbnwcxtei
        foreign key (user_high_id)
            references user (id);

alter table user_relationship
    add constraint FKexhln1n9jxqp0ewxjoed8lif9
        foreign key (user_low_id)
            references user (id);

alter table value
    add constraint FKkrcpyji8b3755yjxt9a9q8hjv
        foreign key (entry_id)
            references entry (id);

alter table value
    add constraint FKojr4waja6mruw0jnd0krxn0n6
        foreign key (metric_id)
            references metric (id);

alter table view
    add constraint FKi7f6yyxo1mv20x8uhpptr7ync
        foreign key (user_leaderboard_id)
            references user_leaderboard (id);

alter table view_metric_config
    add constraint FKlp0020cdxcfp5p7lujgi6ppm8
        foreign key (metric_id)
            references metric (id);

alter table view_metric_config
    add constraint FKiqerx5uefsjlfjbglwrfnmqd8
        foreign key (view_id)
            references view (id);

alter table view_tag_config
    add constraint FKa2gbg9jg5rq9diu4qi5c3c7fl
        foreign key (tag_category_id)
            references tag_category (id);

alter table view_tag_config
    add constraint FK2jtnv6hwqrc2l43e2utks35sb
        foreign key (view_id)
            references view (id);
