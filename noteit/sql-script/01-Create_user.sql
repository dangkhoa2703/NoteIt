-- Drop user first if they exist
DROP USER if exists 'noteit'@'%' ;

-- Now create user with prop privileges
CREATE USER 'noteit'@'%' IDENTIFIED BY 'noteit';

GRANT ALL PRIVILEGES ON * . * TO 'noteit'@'%';
