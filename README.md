# PokeQuery

## About Our Project
Our goal in creating PokeQuery was to utilize our knowledge of REST APIs and graph algorithms to determine, for a given area in a Pokemon region, which was the best Pokemon which could be caught in that area. For instance, a user could specify that they are looking in the Kanto region, and from there can find out the best Pokemon to catch in Celadon City.
In order to rank Pokemon, we give each Pokemon a "raw score", which is calculated independently of any other Pokemon, based on that Pokemon's base stats, exp, and weight. We then create an N^2 weighted directed network between each Pokemon for a given route, and adjust their raw scores based on their types using BFS to average the effect multiplier for each Pokemon. For instance, if Pokemon A is a fire type, Pokemon B is a grass type, and Pokemon C is an electric type, A has x2 damage against B but x1 damage against C, so its average damage multiplier is x1.5.

## Prerequisite Installations
* [Java](https://www.java.com/en/)
* [Maven](https://maven.apache.org/)

## Execution Instructions
* Unzip PokeQuery.zip
* Navigate to the PokeQuery directory in terminal using the cd command
* simply type java -jar PokeQueryJar.jar
