INSERT INTO stock_locations(id, location, available)
VALUES (100,'05.12.2', false),
       (101, '01.10.1', false),
       (102, '03.15.4', false),
       (103,'06.12.2', false),
       (104, '07.12.1', false),
       (105, '02.15.3', false),
       (106,'08.12.5', true),
       (107, '08.13.1', true),
       (108, '01.11.4', true);

INSERT INTO products (id, name, price, category, description, stock_location_id)
VALUES  (1001, 'APTITLIG', 17.99, 'cooking', 'The chopping board collects meat and fruit juice in the milled groove and prevents it from spilling on to your worktop. You can easily turn the chopping board and use both sides when you prepare food, because it has easy-to-grip slanted edges. Made of bamboo, which is an easy-care, hardwearing natural material that is also gentle on your knives.', 100),
        (1002, 'KAVALKAD', 4.99, 'cooking', 'The pan s low weight makes it easy to handle when filled with food. Made from aluminium, which spreads heat evenly and energy efficiently, and makes it easier to regulate heat so the food does not burn and stick. With Teflon® Classic non-stick coating that makes cooking and cleaning easy. Easy grip handle makes the pan easy to lift.', 101),
        (1003, 'MIXTUR', 4.99, 'cooking', 'Timeless oven dish in clear glass that you can take directly from the oven and put on the table. Dishwasher-, oven- and microwave-safe. Two small handles make it easy to lift and carry – with potholders as protection of course.', 102),
        (1004, 'HEMMABAK', 9.99, 'baking', 'This muffin tin is perfect for muffins, cupcakes, quiches or even bread. Made of durable steel with non-stick coating to make your pastries and food easy to loosen from the tin. The steel distributes the heat evenly, which makes your baking soft and scrumptious on the inside and gives it a nice golden-brown surface.', 103),
        (1005, 'MAGASIN', 4.99, 'baking', 'Helps you roll out the dough when baking buns, bread or creating a tasty pizza base. Simple to clean – just wipe dry with a damp cloth.', 104),
        (1006, 'VARDAGEN', 5.99, 'baking', 'Help you get the right amount of spices, flour or other flavourings when cooking or baking. Take up little storage space since the dimensions fit in each other. Feel free to hang them on a hook over the kitchen worktop so you always have them close at hand.', 105);

INSERT INTO user_datas (id, first_name, last_name, email, phone_number)
VALUES  (200, 'Charles', 'Darwin', 'charles@darwin.com', '06-12345678'),
        (201, 'Albert', 'Einstein', 'albert@einstein.com', '06-87654321'),
        (202, 'Isaac', 'Newton', 'isaac@newton.com', '06-18273645');

INSERT INTO orders (id, paid, total_price, user_data_id)
VALUES  (300, false, 27.97, 200),
        (301, false, 17.99, 201),
        (302, true, 4.99, 202);

INSERT INTO order_product (order_id, product_id)
VALUES  (300, 1001);
INSERT INTO order_product (order_id, product_id)
VALUES  (300, 1002);
INSERT INTO order_product (order_id, product_id)
VALUES  (300, 1003);

INSERT INTO order_product (order_id, product_id)
VALUES  (301, 1005);

INSERT INTO order_product (order_id, product_id)
VALUES  (302, 1002);

INSERT INTO image (file_name, content_type, url)
VALUES ('aptitlig.jpg', 'image/jpeg', 'http://localhost:8090/download/aptitlig.jpg');

UPDATE products
SET file_file_name='aptitlig.jpg' WHERE id=1001;

INSERT INTO roles(role_name)
VALUES ('USER'),
       ('ADMIN');

INSERT INTO users (user_name, password)
VALUES  ('user', '$2a$10$7QFmubcTWlckJ3E5Gt1lUeg7uja0I8eRG3u5QWyIVh0q4LysUwv5K'),
        ('admin', '$2a$10$6d5cTwKlASAG70Nf0UcZTOg/DDDMRx8GqZsNhb.bfEpvcrccSuVAC');

INSERT INTO users_roles (users_user_name, roles_role_name)
VALUES  ('user', 'USER'),
        ('admin', 'ADMIN');