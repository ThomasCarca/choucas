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

[
    "502272",
    "72314",
    ...
]
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
### [3] The pinpoint Service

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
```
200 OK

[
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
    "neCoordinates": {
        "lat": 43.3210563659668,
        "lon": -0.3057760000228882
    },
    "swCoordinates": {
        "lat": 43.260074615478516,
        "lon": -0.4187789857387543
    }
}
```


### [5] The sentinel service

The sentinel service will return a list of sentinel images information given 
a bounding box that delimits the perimeter of the sentinel images and a date interval.
The information about the images contains the download url, the preview url, 
the cloud percentage if available, and the date of the shot.

**POST /sentinel/**  
with body :  
```
{
    "neCoordinates": {
        "lat": 51.513824462890625,
        "lon": 7.471989631652832
    },
    "swCoordinates": {
        "lat": 41.64936828613281,
        "lon": -0.8910340070724487
    },
    "startDate": "2016-01-01",
    "completionDate": "2016-01-03"
}
```
will return : 
```
200 OK

[
    {
        "cloud": 0.5714285969734192,
        "date": "2016-01-02",
        "download": "https://peps.cnes.fr/resto/collections/S2/00e550d9-87a6-5c65-8244-09aa710e0b96/download",
        "preview": "https://peps.cnes.fr/quicklook/2016/01/02/S2A/S2A_OPER_PRD_MSIL1C_PDMC_20160107T001924_R049_V20160102T075903_20160102T075903_quicklook.jpg"
    },
    {
        "cloud": 4.538461685180664,
        "date": "2016-01-02",
        "download": "https://peps.cnes.fr/resto/collections/S2/5dfaec17-e7df-5e02-8997-b122390203af/download",
        "preview": "https://peps.cnes.fr/quicklook/2016/01/02/S2A/S2A_OPER_PRD_MSIL1C_PDMC_20160107T003231_R049_V20160102T075903_20160102T075903_quicklook.jpg"
    },
    {
        "cloud": 3.857142925262451,
        "date": "2016-01-02",
        "download": "https://peps.cnes.fr/resto/collections/S2/129b244e-93a0-587f-8c6b-c88617d01e9a/download",
        "preview": "https://peps.cnes.fr/quicklook/2016/01/02/S2A/S2A_OPER_PRD_MSIL1C_PDMC_20160102T145207_R049_V20160102T074835_20160102T074835_quicklook.jpg"
    },
    {
        "cloud": 5.9285712242126465,
        "date": "2016-01-02",
        "download": "https://peps.cnes.fr/resto/collections/S2/1c8bd18c-e8c8-5d7b-aac5-8773f4850b55/download",
        "preview": "https://peps.cnes.fr/quicklook/2016/01/02/S2A/S2A_OPER_PRD_MSIL1C_PDMC_20160102T145948_R049_V20160102T074835_20160102T074835_quicklook.jpg"
    }
]
```

### [6] The tile service

The tile service will take a list of download urls 
(that can be fetched from the sentinel service for instance) 
and will tile each downloaded image. The tiles will then be uploaded to a HDFS.

**POST /tile/**  
with body :  
```
[
    "https://peps.cnes.fr/resto/collections/S2/39b4d215-e74f-533f-8781-9bf599438c8a/download",
    "https://peps.cnes.fr/resto/collections/S2/d4478720-77e5-58ad-81be-72cf38339e55/download",
    "https://peps.cnes.fr/resto/collections/S2/83156d1b-5618-505e-a039-80efee85eb49/download"
]
```
will return : 
```
202 Accepted

{
    "location": "queue/c42c19fd-eb19-4371-b57f-8edf53a40bba"
}
```

Since this service is comprised of long running jobs, the server will automaticaly send back
a 202 Accepted response to the client with a location body.

You can then use the location to get information on the running jobs : 

**GET /queue/:id**  
will return : 
```
200 OK

{
    "jobs": [
        {
            "status": "DOWNLOADING",
            "url": "https://peps.cnes.fr/resto/collections/S2/39b4d215-e74f-533f-8781-9bf599438c8a/download",
            "uuid": "39b4d215-e74f-533f-8781-9bf599438c8a"
        },
        {
            "status": "PENDING",
            "url": "https://peps.cnes.fr/resto/collections/S2/d4478720-77e5-58ad-81be-72cf38339e55/download",
            "uuid": "d4478720-77e5-58ad-81be-72cf38339e55"
        },
        {
            "status": "PENDING",
            "url": "https://peps.cnes.fr/resto/collections/S2/83156d1b-5618-505e-a039-80efee85eb49/download",
            "uuid": "83156d1b-5618-505e-a039-80efee85eb49"
        }
    ],
    "progress": "0/3",
    "uuid": "c42c19fd-eb19-4371-b57f-8edf53a40bba"
}
```

After a while, you'll be able to know when all jobs have been completed :

```
200 OK

{
    "jobs": [
        {
            "status": "COMPLETE",
            "url": "https://peps.cnes.fr/resto/collections/S2/39b4d215-e74f-533f-8781-9bf599438c8a/download",
            "uuid": "39b4d215-e74f-533f-8781-9bf599438c8a"
        },
        {
            "status": "COMPLETE",
            "url": "https://peps.cnes.fr/resto/collections/S2/d4478720-77e5-58ad-81be-72cf38339e55/download",
            "uuid": "d4478720-77e5-58ad-81be-72cf38339e55"
        },
        {
            "status": "COMPLETE",
            "url": "https://peps.cnes.fr/resto/collections/S2/83156d1b-5618-505e-a039-80efee85eb49/download",
            "uuid": "83156d1b-5618-505e-a039-80efee85eb49"
        }
    ],
    "progress": "3/3",
    "uuid": "c42c19fd-eb19-4371-b57f-8edf53a40bba"
}
```
The status can be the following :
- DOWNLOADING
- CONVERTING TO TIFF
- TILING
- SAVING TO THORUS CLOUD
- COMPLETE
- FAILED

## Improvements to be done

### A dedicated backend for the job queue

For now the job queue is only persisted at server level. Should the server restart and the
queue is gone, which is not desired. Some simple persistence layer such as Redis should be implemented.

