package scala.u06.modelling

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers.*
import u06.utils.MSet

import scala.u06.examples.RWMutualExclusion.PlaceRW.*
import scala.u06.examples.RWMutualExclusion.pnRW

class ReadersWritersSpec extends AnyFunSuite:

  test("Readers Writers for mutual exclusion should properly work with one token"):

    val one_reader = List(MSet(START, CRITICAL_SECTION), MSet(READY, CRITICAL_SECTION), MSet(READY_READ, CRITICAL_SECTION),
      MSet(READING, CRITICAL_SECTION), MSet(START, CRITICAL_SECTION))
    val one_writer = List(MSet(START, CRITICAL_SECTION), MSet(READY, CRITICAL_SECTION), MSet(READY_WRITE, CRITICAL_SECTION),
      MSet(WRITING), MSet(START, CRITICAL_SECTION))

    pnRW.paths(MSet(START, CRITICAL_SECTION),5).toSet should be:
      Set(one_reader, one_writer)


  test("Readers Writers for mutual exclusion should properly work with more than one token"):
    val two_readers = List(MSet(START, START, CRITICAL_SECTION), MSet(READY, START, CRITICAL_SECTION),
      MSet(READY_READ, START, CRITICAL_SECTION), MSet(READY_READ, READY, CRITICAL_SECTION),
      MSet(READY_READ, READY_READ, CRITICAL_SECTION), MSet(READING, READY_READ, CRITICAL_SECTION),
      MSet(READING, READING, CRITICAL_SECTION), MSet(START, READING, CRITICAL_SECTION))

    val two_writers = List(MSet(START, START, CRITICAL_SECTION), MSet(READY, START, CRITICAL_SECTION),
      MSet(READY_WRITE, START, CRITICAL_SECTION), MSet(READY_WRITE, READY, CRITICAL_SECTION),
      MSet(READY_WRITE, READY_WRITE, CRITICAL_SECTION), MSet(WRITING, READY_WRITE),
      MSet(START, READY_WRITE, CRITICAL_SECTION), MSet(START, WRITING))

    val one_reader_one_writer=List(MSet(START, START, CRITICAL_SECTION), MSet(READY, START, CRITICAL_SECTION),
      MSet(READY_WRITE, START, CRITICAL_SECTION), MSet(READY_WRITE, READY, CRITICAL_SECTION),
      MSet(READY_WRITE, READY_READ, CRITICAL_SECTION), MSet(WRITING, READY_READ),
      MSet(START, READY_READ, CRITICAL_SECTION), MSet(START, READING, CRITICAL_SECTION))
    
    val set=pnRW.paths(MSet(START, START, CRITICAL_SECTION), 8).toSet
    set should contain:
      two_readers
    set should contain:
      two_writers
    set should contain:
      one_reader_one_writer
