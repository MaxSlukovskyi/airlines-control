create database airlines;

create table airlines
(
    airline_id   int auto_increment
        primary key,
    airline_name varchar(64) not null
);

create table planes
(
    plane_id               int auto_increment
        primary key,
    plane_name             varchar(64) not null,
    plane_seats_number     int         not null,
    plane_cargo_capacity   int         not null,
    plane_maximum_distance int         not null,
    plane_fuel_consumption int         not null,
    plane_airline_id       int         null,
    plane_type             varchar(64) not null,
    constraint planes_airline_fk
        foreign key (plane_airline_id) references airlines (airline_id)
            on delete set null
);

create table jet_planes
(
    plane_id int         not null,
    jet_type varchar(64) not null,
    constraint jet_planes_fk
        foreign key (plane_id) references planes (plane_id)
            on delete cascade
);

create table passenger_planes
(
    plane_id                    int not null,
    business_class_seats_number int not null,
    first_class_seats_number    int not null,
    economy_class_seats_number  int not null,
    constraint passenger_planes_fk
        foreign key (plane_id) references planes (plane_id)
            on delete cascade
);
