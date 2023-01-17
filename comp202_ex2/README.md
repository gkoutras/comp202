# COMP202 Assignment 2

The purpose of this exercise is the development of familiarity with the use of different binary search trees.

---

## Code Implementation

### Binary Search Tree
In the `BinarySearchTree` class for the implementation of BST with an array (Nx3 two-dimensional array), firstly the stack is initialized with the `initialize` method, which is called in the constructor of the class. The `insert` function first fills the cell of the first row of the array with the `data`, `left` and `right` elements (in initialized form so far) of the first key inserted which is the root key 0. Then `insert` calls the recursive `insertRest` method, which with the help of the stack and the `avail` value makes the appropriate traversal each time, while at the same time updating the left and right fields of the element that has a child of the new key. The `search` method traverses the tree according to the value of the key and returns boolean `true` when it finds it. The method `printRange` accepts the root argument and two values `key1` and `key2` (which are the ends of the range through which the values of the tree they belong should be returned), and traverses the tree until it finds the value that is just after `key1` argument and incrementally prints until it finds the value before the `key2` argument.

### Threaded Binary Search Tree
In the `ThreadedBinarySearchTree` class for the implementation of the threaded BST (with two-dimensional array Νx5), the methodology followed is the same as that of the simple BST of the previous task. The only difference is that in this tree the two new right columns of the table `leftThread` and `rightThread` must be taken into account, which function as boolean fields with values `1` or `0`, signaling the state of the left and right fields of each root (value 0 means that the corresponding field points to the child of the root, while a value of 1 means that the corresponding field points to the root with the immediately smaller or larger value respectively). For these reasons, in `insertRest` appropriate checks are made when traversing the tree, dynamically updating the `leftThread` and `rightThread` fields at each insertion, while in the `search` method the comparisons were modified as there was the possibility of a loop between two roots (e.g. an initial root showing right to its child, which has a larger value, and it points left to the root with the immediately smaller value, which is the aforementioned original parent root). The `printRange` method is not implemented, as mentioned in the *Notes and Remarks* below.

---

## Performance and Documentation of Results

The results are presented through the classes `PerformanceTest` and `ManualPerformanceTest`, in which the only difference is that the first one works with an array of keys that it creates, while the second one imports a file from which it reads the keys, as requested for the correction of the exercise. In them, a sorted field is created and a table is filled with the requested comparisons of the above two trees and this field.

| method | mean number of comparisons (insert) | mean number of comparisons (search) | mean number of comparisons (range search) (K = 100) | mean number of comparisons (range search) (K = 1000) |
|--------|-------|-------|-------|-------|
| BST    | 172   | 82    | 99    | 134   |
| tBST   | 172   | 85    | -     | -     |
| sf     | -     | 18    | 2293  | 22924 |

This table was implemented for N = 104 keys with a range of values from 1 to 106 and not as requested in the exercise due to a stack overflow error that occurred for larger orders from the excessive number of recursive function calls. The results below were obtained for the same range of values from 1 to 106 for different total key numbers N.

### Insert
The results of the comparisons of the two methods had the same mean values, which is also justified in the code, as the implementation increases the counter at almost the same points, so the result was expected. It should be noted however, that although the average number of both cases is the same, the number of comparisons per se has some differences, which can be seen as the number of keys increases with threaded BST performing more comparisons than BST.

### Random Search
The results of the comparisons of the two tree methods had similar mean values. What should be noted however is that, on a logarithmic scale (increasing the number of entered keys), the increase in comparisons for the sorted field appears almost decimally "linear", while for trees it is "exponential".

### Range Search (K = 100)
The results of the comparisons of the tree and the sorted field had large differences in their values which were also proportional to the number N of total keys. What should be noted again, is that, on a logarithmic scale, the increase in comparisons for the sorted field again seems almost decimally "linear", while for the tree it seems to stagnate.

### Range Search (K = 1000)
The results of the comparisons of the tree and the sorted field had large differences in their values which were also proportional to the number N of total keys. What should be noted once more is that, on a logarithmic scale, the increase in comparisons for the sorted field again seems almost decimally "linear", while for the tree it seems to stagnate, while it also seems that there is an increase with respect to the previous case.

---

## Notes and Remarks

- The `printRange` function for threaded BST is not implemented.
- When running the code, no initial argument is taken, so in order for the program to open files (needed in the `ManualPerformanceTest` class), the initial menu option 4 must be selected, which prompts the user to type the path directory of the file to be inserted.
- In performance checks, for total keys greater than 104, a stack overflow occurs and the program terminates, due to an excessive number of recursive calls to some functions. One way to fix it would be to use an `inorder` method to rid the program of the recursive calls that occur in `search` and `printRange`. This is a design flaw in the code and since it has not been fixed it must be reported.
