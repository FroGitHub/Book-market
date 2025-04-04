# Book-store

Writing the book-store project was my task. 
In it, we can register as a user, view a list of books, sort them, or search by categories. 
We can also add a book to the cart and create an order.

**Model diagram:**
![img.png](README_FILES/img.png)

For developing the book-store, I used the following **technologies**:

	Spring Boot	3.4.2
	Spring Web	3.4.2
	Spring Security	3.4.2
	Spring Data JPA	3.4.2
	Swagger 	2.8.4
	Mapstruct	1.6.0.Beta1
	Liquibase	4.27.0
	MySQL		9.2.0
	H2		Default
	Spring Tests	3.4.2
	Docker		1.20.4 (mysql, junit-jupiter)

**Functional**

User capabilities:
1. Register and log in.
2. View all books, sort them by category, or search by title/author/isbn.
3. Add books to the cart, update or remove them.
4. Create an order and track its status.

Admin capabilities:
1. Create, update, or delete book categories and books.
2. Soft deletion is enabled, allowing you to restore deleted items if needed.

**Book-store`s endpoints:**

	Auth (/auth):
            GET: /registration - Register a new user
            GET: /login - Retrieve a token for Bearer authentication

	Book (/books):
            GET: / - Retrieve a list of books
            POST: / - Add a new book (ADMIN only)
            PUT: /{id} - Update a book by ID (ADMIN only)
            DELETE: /{id} - Soft delete a book (ADMIN only)
            GET: /search - Search for books using parameters

	Categories (/categories):
            GET: / - Retrieve a paginated list of categories
            POST: / - Create a new category (ADMIN only)
            PUT: /{id} - Update a category by ID (ADMIN only)
            GET: /{id} - Retrieve a category by ID
            DELETE: /{id} - Soft delete a category (ADMIN only)
            GET: /{id}/books - Retrieve books by category

	Cart (/cart):
            GET: / - Retrieve the authenticated user's cart
            POST: / - Add an item to the cart
            PUT: /item/{cartItemId} - Update the quantity of an item in the cart
            DELETE: /item/{cartItemId} - Soft delete an item from the cart

	Orders (/orders):
            GET: / - Retrieve a paginated list of user orders
            GET: /{id}/items - Retrieve items in a user's order
            GET: /{id}/items/{orderId} - Retrieve a specific item from an order
            POST: / - Create a new order
            PATCH: /{id} - Update order status (ADMIN only)

**Running Book-Store:**

Docker:
1. Install Docker Desktop (if not installed).

2. Configure the .env file with your settings.

3. Open the terminal and run the following commands:

`docker-compose build  # Build the images `

`docker-compose up     # Start the project`
    
`docker-compose down   # Stop the project`

4. After starting the web server, download **book-store.postman_collection.json**(in README_FILES).

5. Import it into Postman to test the functionality.

Running Locally
1. Open application.properties.

2. Set the required database properties:
   * Database URL
   * Username
   * Password

3. Press "run"

**Briefly**, I was interested while writing the book store project. I learned a lot about Spring, project structure, and potential problems I might encounter.
The hardest part was implementing authentication logic and the filter chain.
And here you can see the result:
[Link to video](https://www.loom.com/share/b345a15a218f4375aab35c4e7223edbd)
