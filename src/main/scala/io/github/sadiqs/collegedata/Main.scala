package io.github.sadiqs.collegedata

import fastparse.Parsed
import io.github.sadiqs.collegedata.model._
import io.github.sadiqs.collegedata.parsing.collegeDataParser

import scala.io.Source

object Main extends App {
  val lines = Source.fromResource("alldata.txt").getLines()

  val colleges = lines
    .map(collegeDataParser.parseLine)
    .tapEach(v => if (!v.isSuccess) println(v))
    .collect {
      case Parsed.Success(value: College, index) => value
//      case Parsed.Success(value, index) => value
//      case failure: Parsed.Failure =>
//        println("-" * 100)
//        println(line)
//        println(failure.trace().longAggregateMsg)
//        ???
//    }
    } foreach { c => println(s"${c.code}\t${c.no}\t${c.name}") }

}
