# Image API
This section describes the GraphQL endpoints for creating, retrieving and updating images.

## Get Images

`getImages` API allows clients to request for images that matches the given `ImageSearchFilterInput` criteria.

```graphql
query getImages(
  $filter: ImageSearchFilterInput!
  $pageRequestInput: PaginationInput!
) {
  getImages(searchFilter: $filter, pageRequestInput: $pageRequestInput) {
    totalCount
    edges {
      cursor
      node {
        id
        fileName
        imageData
      }
    }
    pageInfo {
      endCursor
      hasNextPage
    }
  }
}
```

Query Variables
```json
 {
    "filter": {
      "fileNamePortion": "headshot",
      "appCodes": [
        "FABK"
      ]
    },
    "pageRequestInput": {
      "pageNumber": 0,
      "rowPerPage": 10,
      "sortDirection": "ASC",
      "sortField": "fileName"
    }
  }
```

Response
```json
{
  "extensions": {},
  "data": {
    "getImages": {
      "totalCount": 2,
      "edges": [
        {
          "cursor": "Mg==",
          "node": {
            "id": "2",
            "fileName": "headshot(1).png",
            "imageData": ".. <base64 encoded image data>"
          }
        },
        {
          "cursor": "MQ==",
          "node": {
            "id": "1",
            "fileName": "headshot.png",
            "imageData": ".. <base64 encoded image data>"
          }
        }
      ],
      "pageInfo": {
        "endCursor": "MQ==",
        "hasNextPage": false
      }
    }
  }
}
```

An Intellij http request may look as follows:

```http request
# @no-log
POST http://{{host}}:{{port}}/graphql
Content-Type: application/json
Authorization: Bearer {{token}}

{
  "query": "query getImages($filter: ImageSearchFilterInput, $paginationInput: PaginationInput) { getImages(searchFilter: $filter, paginationInput: $paginationInput) { totalCount edges { cursor node { id fileName } } pageInfo { endCursor hasNextPage } } }",
  "variables": {
    "filter": {
      "fileNamePortion": "headshot",
      "appCodes": [
        "FABK"
      ]
    },
    "paginationInput": {
      "pageNumber": 0,
      "rowPerPage": 10,
      "sortDirection": "ASC",
      "sortField": "fileName"
    }
  }
}
```