# Create schemas

# Create tables
CREATE TABLE IF NOT EXISTS answer
(
    id INT NOT NULL AUTO_INCREMENT,
    issued DATETIME NOT NULL,
    loaded DATETIME,
    enabled TINYINT(1) NOT NULL,
    task_id INT,
    user_id INT,
    uri VARCHAR(255),
    PRIMARY KEY(id)
);

CREATE TABLE IF NOT EXISTS category
(
    id INT NOT NULL AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    bg_uri VARCHAR(255) NOT NULL,   
    PRIMARY KEY(id)
);
    

CREATE TABLE IF NOT EXISTS user
(
    id INT NOT NULL AUTO_INCREMENT,
    email VARCHAR(60) NOT NULL,
    password VARCHAR(128) NOT NULL,
    enabled TINYINT(1) NOT NULL,
    token VARCHAR(1000)
    create_time DATETIME,
    PRIMARY KEY(id)
);

CREATE TABLE IF NOT EXISTS task
(
    id INT NOT NULL AUTO_INCREMENT,
    uri VARCHAR(255),
    loaded DATETIME,
    enabled TINYINT(1) NOT NULL,
    category_id INT NOT NULL,
    PRIMARY KEY(id)
);


# Create FKs
ALTER TABLE answer
    ADD FOREIGN KEY (task_id)
    REFERENCES task(id)
;
    
ALTER TABLE answer
    ADD FOREIGN KEY (user_id)
    REFERENCES user(id)
;
    
ALTER TABLE task
    ADD FOREIGN KEY (category_id)
    REFERENCES category(id)
;
