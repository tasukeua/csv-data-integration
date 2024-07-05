# Quarkus CSV Elasticsearch Integration

This project uses Quarkus, the Supersonic Subatomic Java Framework.

If you want to learn more about Quarkus, please visit its website: <https://quarkus.io/>.

This application demonstrates a backend service built with Quarkus for integrating CSV data into Elasticsearch, complete with Swagger API documentation, health checks, and metrics monitoring.

## Features

- Swagger UI: Explore and interact with the API using Swagger UI.
- Health Check: Monitor the service health status at Health UI.
- Micrometer Metrics: View detailed metrics using Micrometer UI.

## Functionality

The program processes CSV files containing data, indexing them into Elasticsearch. It provides endpoints for uploading CSV files and searching for specific data entries.

>[!NOTE]
>By integrating Elasticsearch instead of a traditional relational database, we can enhance the application's scalability and performance for handling large volumes of unstructured data.

## Getting Started

>[!WARNING]
>For advenced users!

### Prerequisites
- Java 11 or higher
- Docker (optional for running Elasticsearch locally)

1. Clone the Repository

```[git clone https://github.com/yourusername/quarkus-csv-elasticsearch.git](https://github.com/tasukeua/csv-data-integration.git)```

2. Start Elasticsearch

If Elasticsearch is not running locally or using Docker:

```docker run -d -p 9200:9200 -e "discovery.type=single-node" docker.elastic.co/elasticsearch/elasticsearch:7.15.0```

3. Build and Run the Quarkus Application

```./mvnw clean compile quarkus:dev```

The application will start at ```http://localhost:8080```

Explore the APIs

Use Swagger UI to interact with API endpoints: [Swagger UI](http://localhost:8080/q/swagger-ui/#/)

Monitor the service health: [Health UI](http://localhost:8080/q/health-ui/)

View metrics: [Micrometer UI](http://localhost:8080/q/dev-ui/configuration-form-editorfilter=quarkus.micrometer)

## Example

>[!NOTE]
>The first line of the document is the names of the columns, the subsequent meanings of these columns

CSV document

```
"Fruits","Vegetables"
"Apple","Tomato"
"Strawberry","Onion"
```

Upload ```http://localhost:8080/api/v1/upload``` file and delimiter (click "body" and "from-data")

Search result ```http://localhost:8080/api/v1/search?field=Fruits&match=Apple```

```
[
    "[Apple, Strawberry]"
]
```

## Related Guides

>[!TIP]
>Use guides for scale!

- SmallRye OpenAPI ([guide](https://quarkus.io/guides/openapi-swaggerui)): Document your REST APIs with OpenAPI - comes
  with Swagger UI
- REST Jackson ([guide](https://quarkus.io/guides/rest#json-serialisation)): Jackson serialization support for Quarkus
  REST. This extension is not compatible with the quarkus-resteasy extension, or any of the extensions that depend on it
- Elasticsearch REST client ([guide](https://quarkus.io/guides/elasticsearch)): Connect to an Elasticsearch cluster
  using the REST low level client
- Micrometer metrics ([guide](https://quarkus.io/guides/micrometer)): Instrument the runtime and your application with
  dimensional metrics using Micrometer.

Made by [tasukeua](https://www.youtube.com/watch?v=dQw4w9WgXcQ)
