# Choucas project API

## Dependencies

## Installation

## API documentation

In this section will be described the different services and endpoints.

### The annotate service

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

### The box service

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