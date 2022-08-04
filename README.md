# CompaniesDeduplication

CompaniesDeduplication aims at deduplicating a given companies dataset by leveraging the vector space model and the
tf-idf weighting scheme. We are using blocking based on headquarters location to limit the number of comparisons and we
are further filtering the comparisons by leveraging IDF statistic to detect the most important terms on the documents
and perform comparisons only between records that contain these important terms and as a results are most likely to
satisfy the defined similarity thresholds.

## Usage

For performing duplicate detection on a dataset you need to provide to the DuplicateDetection class a CSV that contains your
companies dataset as an input. The CSV needs to be delimited with semicolons and contain at minimun company names. An example of
such CSV can be found under the src/test/resources directory. Make sure you define on your first row the name of each column:
id;name;headquarters;address;integration_id1;integration_id2. The results can be stored either on a CSV file or on a JSON file. 

```java
try {
        DuplicateDetection dedup=new DuplicateDetection("src/test/resources/test_companies.csv");
        dedup.run(1);
        dedup.store(OUTPUT_FORMAT.JSON);
    } catch (Exception ex) {
        throw new Exception(ex);
    }
```

The test_companies.csv contains a dataset with 501 companies and based on the default similarity thresholds, the 
deduplication algorithm finds the following duplicates: 
```json
{
    "task": "deduplication",
    "results": [{
        "headquarters": "Maryland (United States)",
        "address": "7900 HARKINS ROAD, LANHAM, 20706",
        "name": "2U Inc.",
        "id": "3091558",
        "duplicate_candidates": [{
            "score": 0.8,
            "name": "2u Inc.",
            "id": "4227436",
            "integrations": []
        }],
        "integrations": [{}]
    }]
}
```
On the output JSON and CSV the score of each duplicate candidate is also provided. 

The user can set their own similarity thresholds applied before running the deduplication as follows: 

```java
dedup.setNameSimThreshold(0.82); //the default is set to 0.87
dedup.setAddressSimThreshold(0.45);  //the default is set to 0.5
```

We also allow running comparisons in parallel on different threads. You can set the number of threads you would like to 
use when you call the run function of the DuplicateDetection class:

```java
dedup.run(4) // uses 4 threads when running deduplication, for speeding up the deduplication task
```
## License

The library is available as Open Source under the terms of
the [GNU General Public License v3 (GPLv3)](https://www.gnu.org/licenses/gpl-3.0.txt>).