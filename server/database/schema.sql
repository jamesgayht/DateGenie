/* drop database if exists dategenie;  */
use railway;
drop table if exists users; 
drop table if exists restaurant_reviews; 
drop table if exists restaurants; 
drop table if exists attraction_reviews; 
drop table if exists attractions; 

/* create database dategenie;  */
/* use dategenie;  */

create table users (
    user_id int auto_increment primary key, 
    username varchar(32) not null, 
    email varchar(128) not null,
    password varchar(128) not null
);

/* insert into customers (name, email, password)
VALUES 
("fred", "fredflintstone@bedrock.com", <hashCode>) */

create table restaurants (
    restaurant_uuid varchar(128) primary key,
    restaurant_name varchar(128) not null, 
    type varchar(128) not null,
    description text not null, 
    image_uuid varchar(128) not null, 
    cuisine varchar(128) not null, 
    latitude DOUBLE not null, 
    longitude DOUBLE not null, 
    image_url varchar(256) not null, 
    pricing varchar(128) not null    
);

create table restaurant_reviews (
    reviews_id int auto_increment primary key, 
    author_name varchar(128) not null, 
    rating DOUBLE not null, 
    review text not null, 
    review_date date not null,
    restaurant_uuid varchar(128) not null,
    foreign key (restaurant_uuid) references restaurants(restaurant_uuid)
);

create table attractions (
    attraction_uuid varchar(128) primary key,
    attraction_name varchar(128) not null, 
    type varchar(128) not null, 
    description text not null, 
    image_uuid varchar(128) not null, 
    latitude DOUBLE not null, 
    longitude DOUBLE not null, 
    image_url varchar(256) not null, 
    pricing varchar(128) not null
);

create table attraction_reviews (
    reviews_id int auto_increment primary key, 
    author_name varchar(128) not null, 
    rating DOUBLE not null, 
    review text not null, 
    review_date date not null,
    attraction_uuid varchar(128) not null,
    foreign key (attraction_uuid) references attractions(attraction_uuid)
);