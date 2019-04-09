# Choucas project API

## Dependencies

## Installation

## API documentation

In this section will be described the different services and endpoints.

### [1] The hike service
The hike service will have 2 functions. The first one will return the list of ids of hikes and the second one 
will return the description for a selected hike.

**GET /hike/**

will return :
```
200 OK

["502272", "72314", "938425",...]
```

**GET /hike/:id**

will return
```
200 OK

"Cette pointe est une valeur sure pour une rando-vol dans cette belle vallée de Sixt. Un des rares sommet 
décollable en parapente à cause de la réserve naturelle.\nAttéro dans les grands champs fauchés vers 
l’école de Sixt.\nItinéraire du topo depuis Le Crot.\nBon sentier, sec."
```

### [2] The annotate service

The annotate service will return a list of URI for each word it finds on dbpedia 
in a given text, using semantics.


**POST /annotate/**  
with body :  
```
{ 
    "text": "Pau is a city in France." 
}  
```
will return :  
```
200 OK

{
    "uris": [
        "http://fr.dbpedia.org/resource/Pau",
        "http://fr.dbpedia.org/resource/France"
    ]
}
```
### [3] Pinpoint Service

Pinpoint service will return a list of latitude and longitude. Each couple of latitude and longitude will be given by a list
of Uri from fr.dbpedia. If uri don't reference a place or if the place don't have latitude and longitude set. It will
return nothing. If none of uris have latitude and longitude, it will return a empty list.

**POST /pinpoint/**
with body :
```
{
    "uris": [
        "http://fr.dbpedia.org/resource/Pau",
        "http://fr.dbpedia.org/resource/France",
        "http://fr.dbpedia.org/resource/Pyrénées-Atlantiques",
        "http://fr.dbpedia.org/resource/Bordée",
        "http://fr.dbpedia.org/resource/Gave_de_Pau",
        "http://fr.dbpedia.org/resource/Océan_Atlantique",
        "http://fr.dbpedia.org/resource/Espagne",
        "http://fr.dbpedia.org/resource/Pyrénées",
        "http://fr.dbpedia.org/resource/Jurançon"
    ]
}
```
will return :
200 OK
```
{
  "type": "coordonne",
  "coord": [
    {
      "lat": 43.301700592041016,
      "lon": -0.3686000108718872
    },
    {
      "lat": 48.86666488647461,
      "lon": 2.3264999389648438
    },
    {
      "lat": 43.546112060546875,
      "lon": -1.1947221755981445
    },
    {
      "lat": 40.43333435058594,
      "lon": -3.700000047683716
    },
    {
      "lat": 43.288299560546875,
      "lon": -0.386944442987442
    }
  ]
}
```

### [4] The box service

The box service will return a bounding box containing all points given in a list.
The bounding box is represented by two points (one at south west and one at north east) 
that delimit the lower left corner and upper right corner.

**POST /box/**  
with body :  
```
[
	{
		"lat": 43.260076,
		"lon": -0.418779
	},
	{
		"lat": 43.321055,
		"lon": -0.313064
	},
	{
		"lat": 43.279068,
		"lon": -0.305776
	}
]
```
will return : 
```
200 OK

{
    "neCoordinate": {
        "lat": 43.3210563659668,
        "lon": -0.3057760000228882
    },
    "swCoordinate": {
        "lat": 43.260074615478516,
        "lon": -0.4187789857387543
    }
}
```


