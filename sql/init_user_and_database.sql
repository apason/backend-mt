#Create user mobiili
CREATE USER mobiili IDENTIFIED BY 'asd';

#Create database mobiilitiedekerho
CREATE DATABASE IF NOT EXISTS mobiilitiedekerho;

#grant all privileges 
GRANT ALL PRIVILEGES ON mobiilitiedekerho.* TO mobiili WITH GRANT OPTION;

#flush
FLUSH PRIVILEGES;
