package io.github.sadiqs.collegedata.model

sealed trait DataLine

case class Person(
    rank: Int,
    roll: String,
    name: String,
    region: String,
    caste: String,
    sex: String,
    special: Seq[String],
    admission: String
) extends DataLine

case class College(code: String, no: Int, name: String) extends DataLine

case object Ignore extends DataLine

