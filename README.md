# RequestBinJ
Debug HTTP requests

[![Deploy](https://www.herokucdn.com/deploy/button.svg)](https://heroku.com/deploy?template=https://github.com/dmytr0/RequestBinJ)

## Common
Supported methods: 
```
GET, POST, PUT, DELETE
```

## Endpoints

**GET**  
**/** - *web interface with requests*  
  
**GET,POST,PUT,DELETE**  
**/test** - *endpoint for test request with headers, params, body*  
  
**GET**   
**/metrics** - *metrics endpoint*  
*parameters:*
```
    type        | Enum      |   metric type: PER_SECOND, PER_MINUTE, PER_HOUR
    start       | DateTime  |   Start time for metrics. Pattern: 'yyyyMMddHHmmss'. Default 1 hour ago
    finish      | DateTime  |   Finish time for metrics. Pattern: 'yyyyMMddHHmmss'. Default now
    ignoreZero  | Boolean   |   Ignore results with zero count per timeunit

```
*Example response:*
```
  {
	"metricName": "GET_METRIC",
	"rateType": "PER_SECOND",
	"mapResult": {
		"20180829171338": 2,
		"20180829171337": 3,
		"20180829171336": 3
	},
	"avr": 2.6666665
  }
```

**GET**  
**/reset** - *endpoint clear all request and metrics*  

**GET**  
**/api/listrequests** - *get all requests*  

**GET**  
**/api/request/{id}** - *get specific request*

**GET**  
**/response/all** - *get specific request*

```
    {
        "ALL": {
            "answer": "{\"answer\":\"OK\"}",
            "content_type": "application/json",
            "status": 200
        },
        "POST": {
            "status": 400
        }
    }

```

**POST**  
**/response/add/{method}** - *set specific response for method [GET,POST,PUT,DELETE, ALL]*

```
    {
        "answer": "{\"status\":\"Accepted\"}",
        "content_type": "application/json",
        "status": 200
    }

```

**DELETE**  
**/response/delete/{method}** - *delete specific response for method [GET,POST,PUT,DELETE, ALL]*

  
  

