#!/bin/bash

mysql -u root --password="Mob11l1SON" < init_user_and_database.sql
mysql -u mobiili -D mobiilitiedekerho --password="asd" < init_tables.sql
mysql -u mobiili -D mobiilitiedekerho --password="asd" < init_test_instances.sql
