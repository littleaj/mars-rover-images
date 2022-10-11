[![Java CI with Gradle](https://github.com/littleaj/mars-rover-images/actions/workflows/gradle.yml/badge.svg)](https://github.com/littleaj/mars-rover-images/actions/workflows/gradle.yml)

# Mars Rover Images CLI

## Building
This compiles and runs the tests.
```
./gradlew build
```

## Running
```
./gradlew run
```

### Running with Arguments
Replace `<program arguments>` with appropriate options and parameters.
```
./gradlew run --args="<program arguments>"
```

## Configuration
Only `apikey` is required to run the program. This can be provided using CLI parameters or a configuration file. 

If both are provided, the CLI parameters will be used.
### CLI Parameters
Parameters can appear in any order.

| Option            | Effect                         | Default      |
|-------------------|--------------------------------|--------------|
| `YYYY-mm-dd`      | set start date                 | Today's date |
| `--days N`        | return past `N` days of data   | 10           |
| `--limitPerDay N` | return `N` images for each day | 3            |
| `--apikey KEY`    | sets the API key to `KEY`      | _REQUIRED_   |

### Config File
Optionally, the parameters can be provided via a configuration file. Create a file named `config.properties` in the root project folder. Then, add any of the following properties:
```
apikey=MY_API_KEY
days=10
limitPerDay=3
startDate=2022-01-01
```
Then, rebuild the project to include the configuration.

## Log File
`$TEMP/mars-rover-images/*.log`

To change logging configuration, modify `logging.properties` to your liking and rebuild.

## Cache
`$TEMP/mars-rover-images/YYYY-mm-dd`

Each query's response is cached by its `earth_date` parameter. The filename is the date used for the query.

If the response contains an empty list of images, a cache file is not created. This is because I noticed that the images take about a day to become available from the API.

## Potential Improvements
### Configuration Model
The implemented configuration model works fine for a static set of parameters. However, if adding additional parameters it requires a lot more change that I would like. I would prefer a model where adding a parameter also requires only additions to the production and test code.

This could be accomplished by modeling the underlying configuration as `Properties` or other `Map`-like data structure. Then accessing the configuration would not depend on class structure. Parameter validation in this model could be stored in a `Map` of property keys mapped to validator objects.

### Expiring Cache
I did want the cache to expire after some time period. This could be done by checking the last modified time on the file just before reading from it. If the file was too old, it would be deleted and the query would be made again, creating a new file.
