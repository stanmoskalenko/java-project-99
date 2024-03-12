# Task Manager

<div align="center">

| [![Actions Status](https://github.com/stanmoskalenko/java-project-99/actions/workflows/hexlet-check.yml/badge.svg)](https://github.com/stanmoskalenko/java-project-99/actions) | [![CI](https://github.com/stanmoskalenko/java-project-72/actions/workflows/main.yml/badge.svg)](https://github.com/stanmoskalenko/java-project-72/actions/workflows/main.yml) | [![Maintainability](https://api.codeclimate.com/v1/badges/70ce9a4284a3eaf6a1ad/maintainability)](https://codeclimate.com/github/stanmoskalenko/java-project-99/maintainability) | [![Test Coverage](https://api.codeclimate.com/v1/badges/70ce9a4284a3eaf6a1ad/test_coverage)](https://codeclimate.com/github/stanmoskalenko/java-project-99/test_coverage) |
|---|---|---| ---|

</div>


## Description
The Task Manager is a task management system similar to [Redmine](http://www.redmine.org/). It enables users to assign tasks, designate performers, and modify their statuses. Registration and authentication are required to operate the system.

### Authorization
The project's authorization is implemented through Spring Security, utilizing JWT and RSA.

### API
The project's API supports data filtering.

### Error Tracking
The project incorporates a mechanism that tracks errors occurring in production and alerts about them. This task is handled by the Sentry error collector.

### Technologies
The project is implemented using the following technologies:
- Java
- Spring Boot
- Gradle
- Sentry
- Docker
- GitHub Workflows

After successful CI, the application is deployed on [render.io](https://render.com/). The application also provides OpenAPI documentation at `host:port/swagger-ui/index.html`.

## Usage

### Project Launch

To launch the project, run:

```bash
make run
```

### Build

To build the application, run:

```bash
make build
```

### Install

To install the application, run:

```bash
make install
```

### Lint

To perform linting using Checkstyle, run:

```bash
make lint
```

### Test

To run tests, execute:

```bash
make test
```

### Generate Report

To generate a test report, run:

```bash
make report
```