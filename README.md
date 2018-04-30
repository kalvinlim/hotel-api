# Hotel Api

## Implementation

This was an interesting problem.  The difficulty was in transacting the data, that is how do we save and query the data in a way that maintains the integrity of the overbooking capacity.  Not wanting to over engineer in the time I had to implement this, I went with MySQL over a noSQL solution.  I felt that we need a transactional database for this kind of problem.
___
**Assumptions:**

I designed the API to support a single hotel as implied by the scenario- this is for one friend's hotel and so a single hotel configuration should exist overall, with infinitely many reservations, limited by capacity per day.  However, we could easily extend it to support multiple hotels by adding a hotel id/unique name column in the data modeling.

Guest 1 checks out today, and then Guest 2 can check in immediately after. Hotels generally have check in and check out times, so we can have two guests overlap reservations in this manner.

____

The first big problem was ensuring that each transaction maintained the overbook limit.  I basically took the guest's reservation request window, and checked that each day within their window had the capacity to book them. If so, the guest gets booked, otherwise an error is returned. The issue I foresee with this is concurrency.  If a date is exactly 1 below capacity, and two transactions attempt to book the room, there could be a chance that both reservations complete.  SQL has ways to prevent this, but I wasn't able to derive a simple solution in time.

The second big problem was checking, for a reservation's date window, that all those dates have capacity.  In doing research for this, there were a lot of fancy, overcomplicated solutions, so I opted for something simple in the interest of time.  I simply queried for all reservations that overlapped the request reservation, then on the server, iterated through all the dates within the reservation window, and built a count using a hashmap.  While this was a O(n^2) solution, I felt that this was moderately reasonable given the assumption that, the largest hotel in the world has ~7,000 rooms, and each guest will stay roughly 1-14 days, so the running time isn't too bad.


## Retrospective

A better implementation for querying might be to build a calendar table containing rows for each date, then as we insert, update the capacity count.

As for concurrency, I think a queue + worker setup to make bookings could potentially be an ideal solution.

---

## Design

Spring Boot backend with mysql database. For local/unit test environment, in memory H2 database is packaged and configured to be the default.

Remote mysql connection available upon request.

## Prerequisites

* Maven
* cURL or Postman

To run:
`mvn spring-boot:run`

## Endpoints

Create initial hotel config:
`(POST) http://localhost:8080/rest/config/create`
* **numRooms**: integer
* **overbookingLevel**: integer, 50% would be 50

  curl -X POST \
  http://localhost:8080/rest/config/create \
  -H 'cache-control: no-cache' \
  -H 'content-type: multipart/form-data; boundary=----WebKitFormBoundary7MA4YWxkTrZu0gW' \
  -F numRooms=200 \
  -F overbookingLevel=50

Update existing hotel config:
`(POST) http://localhost:8080/rest/config/update`

* **numRooms**: integer
* **overbookingLevel**: integer, 50% would be 50

 curl -X POST \
  http://localhost:8080/rest/config/update \
  -H 'cache-control: no-cache' \
  -H 'content-type: multipart/form-data; boundary=----WebKitFormBoundary7MA4YWxkTrZu0gW' \
  -F numRooms=2 \
  -F overbookingLevel=50

Get existing hotel config:
`(GET) http://localhost:8080/rest/config/get`

curl -X GET \
  http://localhost:8080/rest/config/get \
  -H 'cache-control: no-cache' \
  -H 'content-type: multipart/form-data; boundary=----WebKitFormBoundary7MA4YWxkTrZu0gW' \
  -F numRooms=200 \
  -F overbookingLevel=50

Returns error code 500 if hotel config != 1

Attempt to create new hotel reservation:
`(POST) http://localhost:8080/rest/reservation/save`

* **firstName**: String
* **lastName**: String
* **email**: String
* **arrival**: date format (YYYY-MM-dd), 2018-04-29
* **departure**: date format (YYYY-MM-dd), 2018-04-29

  curl -X POST \
  http://localhost:8080/rest/reservation/save \
  -H 'cache-control: no-cache' \
  -H 'content-type: multipart/form-data; boundary=----WebKitFormBoundary7MA4YWxkTrZu0gW' \
  -F arrival=2018-04-29 \
  -F departure=2018-05-03 \
  -F firstName=Joe \
  -F lastName=Bob \
  -F email=mail@mail.com

returns error code 500 if requested dates are at capacity

`(GET) http://localhost:8080/rest/reservation/get/all`

  curl -X GET \
  http://localhost:8080/rest/reservation/get/all \
  -H 'cache-control: no-cache'


## Data Models

HotelConfig- models hotel configuration

* **numRooms**: integer value, ie 100 rooms.
* **overbookingLevel**: integer value with decimal removed- ie 50% overbooking level is represented as 50, not 0.50
* **maxCapacity**: function of numRooms plus percent overbookingLevel (numRooms * (1+(overbookingLevel/100)) ... 100 rooms at 10% overbooking returns 110

Reservation- models each guest's reservation

* **firstName**: First name of guest
* **lastName**: Last name of guest
* **email**: Email of guest
* **arrival**: Guest arrival time formatted as (YYYY-MM-dd)
* **departure**: Guest departure time formatted (as YYY-MM-dd)


