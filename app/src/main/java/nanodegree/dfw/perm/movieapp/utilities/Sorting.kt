package nanodegree.dfw.perm.movieapp.utilities

import nanodegree.dfw.perm.movieapp.data.MoviesData
import java.util.ArrayList
import java.util.HashMap


class Sorting{

    companion object {

        fun mergeSorting(inputArray:DoubleArray):MutableList<Double> {
            return mergeSorting(inputArray,
                    0,
                    inputArray.size - 1).toMutableList()
        }

        fun mergeSorting(inputArray:DoubleArray, start:Int, end:Int): MutableList<Double> {
            if(end <= start) {
                return inputArray.toMutableList()
            }
            var mid = (start + end) / 2
            mergeSorting(inputArray, start, mid)                                     // sort left half
            mergeSorting(inputArray, mid + 1, end)                             // sort right half
            merge(inputArray, start, mid, end)
//        println("mFun-counter: ${mergefun_counter}\t array-size: ${inputArray.size}\t sorted - array: ${inputArray.toMutableList()}")
            return inputArray.toMutableList()
        }

        fun merge(inputArray:DoubleArray, p:Int, q:Int, r:Int) {
            var arrayDTemp = DoubleArray(r - p + 1)                       // last - start + 1
            var leftSlot = p
            var rightSlot = q + 1
            var k = 0
            while (leftSlot <= q && rightSlot <= r) {
                if (inputArray[leftSlot] <= inputArray[rightSlot]) {
                    arrayDTemp[k] = inputArray[leftSlot]
                    leftSlot += 1
                } else {
                    arrayDTemp[k] = inputArray[rightSlot]
                    rightSlot += 1
                }
                k += 1
            }
            if (leftSlot <= q) {
                while (leftSlot <= q) {
                    arrayDTemp[k] = inputArray[leftSlot]
                    leftSlot += 1
                    k += 1
                }
            } else if (rightSlot <= r) {
                while (rightSlot <= r) {
                    arrayDTemp[k] = inputArray[rightSlot]
                    rightSlot += 1
                    k += 1
                }
            }
            var arrayCounter = 0                           // update input-array with sorted elements
            for (iIn in arrayDTemp) {
                inputArray[p + arrayCounter++] = iIn
            }
        }

        //region Description
        //  fun sortHashMap(inputHashMapsList: List<HashMap<Integer, MoviesData>>)
            //     val ReforderedDataList = inputHashMapsList
            //     moviesSortedByPopularity = ArrayList<MoviesData>()

            //      var out: HashMap<Integer, MoviesData>

//        return inputHashMapsList.sortedBy (
//            Comparator { o1: HashMap<Integer, MoviesData>, o2: HashMap<Integer, MoviesData> ->
//                var in1 = o1.get(o1.keys.toList().get(0))!!.popularity
//                var in2 = o2.get(o2.keys.toList().get(0))!!.popularity
//                in1.compareTo(in2)
//            }
//            )
//        }´ƒ´÷
        //            Comparator { o1: HashMap<Integer, MoviesData>, o2: HashMap<Integer, MoviesData> ->

    //    inputHashMapsList.sortedBy {  }


            //  return inputHashMapsList
            //  }
        //endregion
        }

}