# Order System


### Endpoints

| Endpoints description                                         | Request | URL                                                | Example value                          | Role           |
|---------------------------------------------------------------|---------|----------------------------------------------------|----------------------------------------|----------------|
| Display all products                                          | GET     | http://localhost:8090/products                     | -                                      | ALL            |
| Display all products by category                              | GET     | http://localhost:8090/products?category=value      | cooking (of type String category)      | ALL            |
| Display product by id                                         | GET     | http://localhost:8090/products/value               | 1001  (of type Long id)                | ALL            |
| Add new product                                               | POST    | http://localhost:8090/products                     | -                                      | ADMIN          |
| Update product by id                                          | PUT     | http://localhost:8090/products/value               | 1001 (of type Long id)                 | ADMIN          |
| Delete product by id (delete image if exists)                 | DELETE  | http://localhost:8090/products/value               | 1001 (of type Long id)                 | ADMIN          |
| Assign stock location to product                              | PUT     | http://localhost:8090/products/value/stocklocation | 1001 (of type Long id)                 | ADMIN          |
| Create order                                                  | POST    | http://localhost:8090/orders                       | -                                      | ALL            |
| Display order by id                                           | GET     | http://localhost:8090/orders/value                 | 300 (of type Long id)                  | ADMIN and USER |
| Display unpaid orders                                         | GET     | http://localhost:8090/orders                       | -                                      | ADMIN and USER |
| Process payment                                               | PUT     | http://localhost:8090/orders/value                 | 300 (of type Long id)                  | ADMIN and USER |
| Display available stock locations                             | GET     | http://localhost:8090/stocklocations               | -                                      | ADMIN          |
| Create stock location                                         | POST    | http://localhost:8090/stocklocations               | -                                      | ADMIN          |
| Delete stock location                                         | DELETE  | http://localhost:8090/stocklocations/value         | 100 (of type Long id)                  | ADMIN          |
| Upload image by assigning to product (delete image if exists) | POST    | http://localhost:8090/products/value/image         | 1001 (of type Long id)                 | ADMIN          |
| Download image by fileName                                    | GET     | http://localhost:8090/images/download/value        | aptitlig.jpg (of type String fileName) | ALL            |
| Create a new user                                             | POST    | http://localhost:8090/users                        | -                                      | ALL            |
| Sign in user                                                  | POST    | http://localhost:8090/auth                         | -                                      | ALL            |



