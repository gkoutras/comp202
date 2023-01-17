# COMP202 Assignment 1

The purpose of this exercise is the development of familiarity with the use of disk files and file processing.

---

## Code Implementation

### Class File

The requested methods were created in the `FileManager` class, and use the `RandomAccessFile` class to open/create `.bin` files. It should be noted that `FileHandle`, since useful information such as the current page, the total number of pages, etc. are stored there, and it controls how the file uses page 0 (more in the Notes and Remarks part), was chosen not to be implemented as a method but as a class instead.

### Information Management in a Serial File

In the `FileMethodManager` class, using the appropriate methods of the first task, the file of the selected management method is created and the buffer with the appropriate records is initialized for each page sequentially using `initializePage` to generate their keys and information (method A) or their keys and indices (method B). After each file is created, there is an option to search serially, during which the keys are checked page-by-page and at the same time the accesses to the disk are counted (that is the times the buffer reads a new page). Keys-info-indices are placed in an array of nodes during initialization, from where they are drawn for comparisons during searches and for the keys-indices files creations.

### Information Management in a Sorted File

Similarly to the previous part, the `FileMethodManager` class creates the file of the new selected management method using the appropriate methods of the first task. In the case that a file (type A or B) already exists and is not created in the current run, all the pages in the buffer are read sequentially so that the arrays of nodes are also updated. Then a simple sort is done on these arrays and a new initialization is done in the new file in the same way as before but with the arrays now sorted. After each file is created, the option is given for a binary search, during which the middle page keys are checked. If the key to be searched is found there, the search ends. If not, the same process is repeated for the middle page of the two halves of the file depending on whether the key being searched for is greater or less than the last or first key of the respective middle page. This is repeated until all pages are checked or the key is found. This process takes advantage of list sorting and, as before, for each page read, the number of memory accesses increases.

---

## Search Performance Results

| method A (serially) | method B (serially) | method C (binarily) | method D (binarily) |
|---------------------|---------------------|---------------------|---------------------|
| 1166 disk accesses  | 341 disk accesses   | 12 disk accesses    | 7 disk accesses     |

Search performance results were as expected. However, it would have been more correct to evaluate the performance of a search algorithm by searching for a number that does not exist in the file, since then all possible entries in the file have been checked (worst case scenario). The results are again as expected, as a serial search in a keys-info file will count 2500 disk accesses (as many as the pages of this file), while a keys-indices file will count 625 accesses (as many as the pages of this file), and accordingly for binary search (complexity of $O(log2(n))$) in a keys-info file will count 11 accesses, while a keys-indices file will count 9 accesses.

---

## Notes and Remarks

- When running the code, no initial argument is taken, so in order for the program to open files, those files must have appropriate names (defined at the beginning of the `Main` class).
- The files created during the run of the code create an extra page, as the first page (page 0) was chosen to write some useful information such as page set and current page, which aided in the whole code implementation process. Thus a file of 2500 pages (from 0 to 2499), when selecting option `1` of the program, is created with 2501 pages (from 1 to 2500). when opening an already existing file by selecting option `2`, `FileHandle` checks whether the first page is a page of such information or whether it is a normal page with keys and constantly updates it if the first case applies.
- In the binary search, the sorting done is a simple sorting of the arrays of nodes based on the key (thanks to the `Comparable` implementation of the `KeyNode` class) instead of external sorting as requested. However, in this exercise the files are small enough to fit in main memory.
