# CompaniesDeduplication

CompaniesDeduplication aims at deduplicating a given companies dataset by leveraging the vector space model and the
tf-idf weighting scheme. We are using blocking based on headquarters location to limit the number of comparisons and we
are further filtering the comparisons by leveraging IDF statistic to detect the most important terms on the documents
and perform comparisons only between records that contain these important terms and as a results are most likely to
satisfy the defined similarity thresholds.

## Usage

```java
try {
    DuplicateDetection dedup = new DuplicateDetection("src/test/resources/test_companies.csv");
    dedup.run(2);
    dedup.store(OUTPUT_FORMAT.JSON);
} catch (Exception ex) {
    throw new Exception(e);
}
```

License
-------

The library is available as Open Source under the terms of
the `GNU General Public License v3 (GPLv3) <https://www.gnu.org/licenses/gpl-3.0.txt>`_.