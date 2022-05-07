# StokexCMS

[![Tests](https://github.com/alfredats/stokexCMS/actions/workflows/onPush.yml/badge.svg)](https://github.com/alfredats/stokexCMS/actions/workflows/onPush.yml)
[![Coverage](https://visavttppaf-alfred.sgp1.digitaloceanspaces.com/coverage/stokexCMS/jacoco.svg)](https://visavttppaf-alfred.sgp1.digitaloceanspaces.com/coverage/stokexCMS/jacoco/index.html)


[![Release](https://github.com/alfredats/stokexCMS/actions/workflows/versioned.yml/badge.svg)](https://github.com/alfredats/stokexCMS/actions/workflows/versioned.yml)
## Endpoints

### Price
Endpoint: `/price/{ticker}`

**Request**
| Parameter | Type  | Description                                                     | Example |
| --------- | ----- | --------------------------------------------------------------- | ------- |
| ticker    | path  | Ticker symbol that uniquely identifies the stock of interest    | AAPL    |
| interval  | query | Minimum interval between datapoints (5min, 1day, 1week, 1month) | 5min    | 


**Response**
| Parameter      | Type   | Description                                                    |
| -------------- | ------ | -------------------------------------------------------------- |
| symbolInterval | String | Ticker + Interval chose (delimited by ':')                     |
| prices         | Array  | Contains child JSON objects with timestamp & price information |


**Example**


With endpoint: `/price/AAPL?interval=5min`

```json
{
	"symbolInterval":"AAPL:5min",
	"prices":[
		{"timestamp":"2022-05-05T15:55","price":156.80},
		{"timestamp":"2022-05-05T15:50","price":156.05},
		{"timestamp":"2022-05-05T15:45","price":155.06},
		{...}
	]
}
```
