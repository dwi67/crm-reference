CREATE TABLE customer
(
    id INT PRIMARY KEY NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
    name VARCHAR(255) DEFAULT NULL,
    age INT DEFAULT NULL
);

DROP TABLE customer