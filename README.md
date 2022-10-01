# Order System


### Endpoints

| Endpoints description             | Request | URL                                           | Example value |
|-----------------------------------|---------|-----------------------------------------------|---------------|
| Display all products              | GET     | http://localhost:8090/products                | -             |
| Display all products by category  | GET     | http://localhost:8090/products?category=value | cooking       |
| Add new product                   | POST    | http://localhost:8090/products                | -             |
| Update product by id              | PUT     | http://localhost:8090/products/value          | 1001          |
| Delete product by id              | DELETE  | http://localhost:8090/products/value          | 1001          |
| Assign stock location to product  | PUT     | localhost:8090/products/value/stocklocation   | 1001          |
| Get user by id                    | GET     | http://localhost:8090/users/value             | 1001          |
| Create order                      | POST    | http://localhost:8090/orders                  |               |
| Display order by id               | GET     | http://localhost:8090/orders/value            | 1             |
| Process payment                   | PUT     | http://localhost:8090/orders/value            | 1             |
| Display available stock locations | GET     | http://localhost:8090/stocklocations          |               |
| Create stock location             | POST    | http://localhost:8090/stocklocations          |               |
| Delete stock location             | DELETE  | http://localhost:8090/stocklocations/value    | 1001          |


