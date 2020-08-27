package io.github.sadiqs.collegedata.parsing

import fastparse.NoWhitespace._
import fastparse._
import io.github.sadiqs.collegedata.model._

object collegeDataParser {

  private[this] object lineParser {

    def string[_: P]: P[String] =
      CharsWhile(c => c != '(' && CharPredicates.isPrintableChar(c)).!
    def collegeCode[_: P]: P[String] =
      CharsWhile(c => c != '(').!
    def word[_: P]: P[String] = CharsWhile(_ != ' ').!

    def number[_: P]: P[Int] = P(CharsWhileIn("0-9", 1).!).map(_.toInt)
    def stringLength[_: P](n: Int): P[String] = AnyChar.rep(n, null, n).!

    def region[_: P]: P[String] = P(("OU" | "AU" | "NL" | "SVU").! ~ &(" "))
    def caste[_: P]: P[String] =
      P(
        "OC" |
          "SC" |
          "ST" |
          "BCA" |
          "BCB" |
          "BCC" |
          "BCD" |
          "BCE"
      ).!

    def sex[_: P]: P[String] = P("F" | "M").!
    def admission[_: P]: P[String] = P(("REG" ~ string).!)

    def college[_: P]: P[College] =
      P(
        "COLL :: " ~
          collegeCode ~
          "(" ~ number ~ ") - " ~
          AnyChar.rep.! ~
          End
      ).map(College.tupled)

    def ignored[_: P]: P[Ignore.type] =
      P(
        "-" | "RANK" | "CRS"
      ).map(_ => Ignore)

    def person[_: P] =
      P(
        number ~ " " ~ stringLength(9) ~ " " ~/ (!region ~ AnyChar).rep.!.map(
          _.trim
        ) ~ region ~ " " ~ caste ~ " " ~ sex ~/
          (" " ~ !admission ~ word).!.rep ~ " " ~/ admission ~ End
      ).map(Person.tupled)

    def parse[_: P]: P[DataLine] = P(person | ignored | college)
  }

  def parseLine(line: String): Parsed[DataLine] =
    parse(line, lineParser.parse(_))
}
