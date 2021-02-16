# Pizza Jeff
MVP Pizza Jeff!

APP:
- MVVM pattern.
- Register to save username, email and status (Single/Married)
- Created master/detail/confirmation flow to buy pizzas.
- Profile in menu to update data and check all the orders (it shows a coupon banner, if you did an order, for the equal last pizza size and smaller)
- Logout action in profile screen.
- The pizza data is get from repository and saved in local database (Retrofit/Room libraries)
- In pizza details created programmatically buttons, and set purple colour for the best "customer satisfaction score" for your status.
- If you buy a pizza, you can continue purchasing, and if you are Married you are asked if you want buy a Movie.
- Order screen to Buy/Cancel and remove order lines.
- Tests for any function in viewmodels.

Libraries:
- AndroidX lifecycles libraries for MVVM
- Dagger Hilt for DI
- Retrofit to get data from network
- Room to save local database
- Glide to manage images
- JUnit and Mockito for tests


Data model:

![alt text](https://github.com/E7-Company/pizza_jeff/blob/master/Jeff_pizzas.png?raw=true)
