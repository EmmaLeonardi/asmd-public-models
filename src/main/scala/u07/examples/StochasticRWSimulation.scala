package scala.u07.examples

import u07.examples.DAPGossip.newSimulationTrace
import u07.modelling.{CTMC, SPN}
import u07.modelling.SPN.{SPN, Trn, toCTMC}
import u07.utils.MSet

import java.util.Random
import scala.u07.examples.StochasticRWSimulation.StateRW.*
import scala.u07.examples.StochasticRWSimulation.StateRW


// Specification of my data-type for states
object StochasticRWSimulation extends App:
  enum StateRW:
    case START, READY, READY_READ, READING, READY_WRITE, WRITING, CRITICAL;

    export StateRW.*
    export u07.modelling.CTMCSimulation.*
    export u07.modelling.SPN.*

  val spnRW = SPN[StateRW](
    Trn(MSet(START), m => 1.0,   MSet(READY),  MSet()), //t1
    Trn(MSet(READY), m => 200000.0,  MSet(READY_READ),  MSet()), //t2
    Trn(MSet(READY), m => 100000.0,  MSet(READY_WRITE),  MSet()), //t3
    Trn(MSet(READY_READ, CRITICAL), m => 100000.0,  MSet(READING, CRITICAL),  MSet()), //t4
    Trn(MSet(READY_WRITE, CRITICAL), m => 100000.0,  MSet(WRITING),  MSet(READING)), //t5
    Trn(MSet(READING), m => 0.1 * m(READING),  MSet(START),  MSet()), //t6
    Trn(MSet(WRITING), m => 0.2,  MSet(START, CRITICAL),  MSet()), //t7
)

  println:
    toCTMC(spnRW).newSimulationTrace(MSet(START, CRITICAL),new Random)
      .take(20)
      .toList.mkString("\n")