// Wei Chen - Q Neural Learning Test
// 2017-08-31

import org.scalatest.FunSuite
import com.scalaml.TestData._
import com.scalaml.algorithm.QNeuralLearning

class QNeuralLearningSuite extends FunSuite {

    val learning_rate = 0.1
    val nn_learning_rate = 0.1
    val scale = 1
    val limit = 10000
    val epoch = 100

    test("QNeuralLearning Test : Result 1") { // Case 1
        def simulator(paras: Array[Double], act: Int): (Array[Double], Double, Boolean) = {
            val links = Map(0 -> Array(1, 2),
                1 -> Array(3, 4))
            val scores = Map(2 -> 10.0, 3 -> 0.0, 4 -> 100.0)
            val atloc = paras.zipWithIndex.maxBy(_._1)._2
            val moves = links.getOrElse(atloc, Array[Int]())
            if (moves.size == 0) {
                null
            } else {
                val endloc = moves(act)
                val result = Array(0.0, 0.0, 0.0, 0.0, 0.0)
                result(endloc) = 1.0
                val nextmoves = links.getOrElse(endloc, Array[Int]())
                (result, scores.getOrElse(endloc, 0.0), nextmoves.size == 0)
            }
        }
        // if set nn_lr to 0.1, might fix result to (10.0, 13.0)
        val ql = new QNeuralLearning(Array(5, 4), Array(1.0, 0.0, 0.0, 0.0, 0.0), 2, simulator, 10)
        ql.train(limit, learning_rate, scale, epoch)
        val result = ql.result(epoch)
        assert(result.size == 3)
        assert(result.head.bestAct == 0)
        assert(result(1).bestAct == 1)
        assert(result.last.paras.zipWithIndex.maxBy(_._1)._2 == 4)
    }

    test("QNeuralLearning Test : Result 2") { // Case 2
        def simulator(paras: Array[Double], act: Int): (Array[Double], Double, Boolean) = {
            val links = Map(0 -> Array(1, 2),
                1 -> Array(3, 4))
            val scores = Map(2 -> 10.0, 3 -> 0.0, 4 -> 12.0)
            val atloc = paras.zipWithIndex.maxBy(_._1)._2
            val moves = links.getOrElse(atloc, Array[Int]())
            if (moves.size == 0) {
                null
            } else {
                val endloc = moves(act)
                val result = Array(0.0, 0.0, 0.0, 0.0, 0.0)
                result(endloc) = 1.0
                val nextmoves = links.getOrElse(endloc, Array[Int]())
                (result, scores.getOrElse(endloc, 0.0), nextmoves.size == 0)
            }
        }
        // if set nn_lr to 0.01, might not have enough feedback for first step
        val ql = new QNeuralLearning(Array(5, 4), Array(1.0, 0.0, 0.0, 0.0, 0.0), 2, simulator, 10,
            nn_learning_rate = nn_learning_rate)
        ql.train(limit, learning_rate, scale, epoch)
        val result = ql.result(epoch)
        assert(result.size == 3)
        assert(result.head.bestAct == 0)
        assert(result(1).bestAct == 1)
        assert(result.last.paras.zipWithIndex.maxBy(_._1)._2 == 4)
    }
}
