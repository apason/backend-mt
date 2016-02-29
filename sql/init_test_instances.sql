# Here we create a few test instances to the database. Instances should be created in correct order
# eg. Categories should be created befoce tasks because tasks has reference to category.

# Users and categories does not depend on any other tables so those are created first

# 3 user examples. 2 active and one banned. One active has already a token assigned.
INSERT INTO user 
	(id, email, password, enabled, token, create_time)
	VALUES 
	(1, "matti.meikalainen@gmail.com", "password1", true, "tokeniii", NOW())
;

INSERT INTO user 
	(id, email, password, enabled, create_time)
	VALUES
	(2, "maija.meikalainen@helsinki.fi", "password2", true, "maija.meikalainen@helsinki.fi", NOW())
;

INSERT INTO user
	(id, email, password, enabled, create_time)
	VALUES
	(3, "trolli@peikko.banned", "password3", false, "trolli@peikko.banned", NOW())
;

# 2 tesst categories. (note that background images are not uploaded to server yet.
INSERT INTO category
	(id, name, bg_uri)
	VALUES
	(1, 'test_category', 'test.png')
;

INSERT INTO category
	(id, name, bg_uri)
	VALUES
	(2, 'fysiikka', 'fys.png')
;

# 3 task examples. (note that only the first has a correct uri field) One has default value (not assigned) in field info.
INSERT INTO task
	(id, loaded, uri, category_id, enabled, info)
	VALUES
	(1, NOW(), 'VID_20160201_150600.mp4', 1, true, "tehtävä1")
;

INSERT INTO task
	(id, loaded, uri, category_id, enabled, info)
	VALUES
	(2, NOW(), 'not_working_yet.mp4', 1, false, "tehtävä2")
;

INSERT INTO task
	(id, loaded, uri, category_id, enabled)
	VALUES
	(3, NOW(), 'explosion.mp4', 2, true)
;

# A few test answers for enabled users (note that all instances does not have correct uri
INSERT INTO answer
	(issued, id, loaded, enabled, task_id, user_id, uri)
	VALUES
	(NOW(),1, NOW(), true, 1, 2, 'VID_20160201_150600.mp4')
;

INSERT INTO answer
	(issued, id, loaded, enabled, task_id, user_id, uri)
	VALUES
	(NOW(), 2, NOW(), true, 1, 1, 'asd.mp4')
;

INSERT INTO answer
	(issued, id, loaded, enabled, task_id, user_id, uri)
	VALUES
	(NOW(), 3, NOW(), true, 3, 2, 'asdas.mp4');
;

INSERT INTO answer
        (issued, id, loaded, enabled, task_id, user_id, uri)
        VALUES
        (NOW(), 4, NOW(), false, 1, 2, 'asdasdsa.mp4');
;



