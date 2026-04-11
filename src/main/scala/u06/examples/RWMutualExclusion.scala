package scala.u06.examples

export u06.modelling.PetriNet
import u06.utils.MSet

object RWMutualExclusion:

  enum PlaceRW:
    case START, READY, READY_READ, READING, READY_WRITE, WRITING, CRITICAL_SECTION
    
  export PlaceRW.*
  export u06.modelling.PetriNet.*
  export u06.modelling.SystemAnalysis.*
  export u06.utils.MSet

  // DSL-like specification of a Petri Net
  def pnRW = PetriNet[PlaceRW](
    MSet(START)~~>MSet(READY),
    MSet(READY)~~>MSet(READY_READ),
    MSet(READY)~~>MSet(READY_WRITE),
    MSet(READY_READ, CRITICAL_SECTION)~~>MSet(READING, CRITICAL_SECTION),
    MSet(READING)~~>MSet(START),
    MSet(READY_WRITE, CRITICAL_SECTION)~~>MSet(WRITING) ^^^ MSet(READING),
    MSet(WRITING)~~>MSet(START, CRITICAL_SECTION)
  ).toSystem

  // DSL-like specification of a Petri Net
  def pnRWLoopless = PetriNet[PlaceRW](
    MSet(START) ~~> MSet(READY),
    MSet(READY) ~~> MSet(READY_READ),
    MSet(READY) ~~> MSet(READY_WRITE),
    MSet(READY_READ, CRITICAL_SECTION) ~~> MSet(READING, CRITICAL_SECTION),
    MSet(READING) ~~> MSet(),
    MSet(READY_WRITE, CRITICAL_SECTION) ~~> MSet(WRITING) ^^^ MSet(READING),
    MSet(WRITING) ~~> MSet(CRITICAL_SECTION)
  ).toSystem

  // DSL-like specification of a Petri Net
  def pnRWReaderPriority = PetriNet[PlaceRW](
    MSet(START)~~>MSet(READY),
    MSet(READY)~~>MSet(READY_READ),
    MSet(READY)~~>MSet(READY_WRITE),
    MSet(READY_READ, CRITICAL_SECTION)~~>MSet(READING, CRITICAL_SECTION),
    MSet(READING)~~>MSet(START),
    MSet(READY_WRITE, CRITICAL_SECTION)~~>MSet(WRITING) ^^^ MSet(READING),
    MSet(WRITING)~~>MSet(START, CRITICAL_SECTION),
    MSet(READY_READ, READY_WRITE, CRITICAL_SECTION)~~>MSet(READING, CRITICAL_SECTION, READY_WRITE)
  ).toSystem

  // DSL-like specification of a Petri Net
  def pnRWWriterPriority = PetriNet[PlaceRW](
    MSet(START) ~~> MSet(READY),
    MSet(READY) ~~> MSet(READY_READ),
    MSet(READY) ~~> MSet(READY_WRITE),
    MSet(READY_READ, CRITICAL_SECTION) ~~> MSet(READING, CRITICAL_SECTION),
    MSet(READING) ~~> MSet(START),
    MSet(READY_WRITE, CRITICAL_SECTION) ~~> MSet(WRITING) ^^^ MSet(READING),
    MSet(WRITING) ~~> MSet(START, CRITICAL_SECTION),
    MSet(READY_READ, READY_WRITE, CRITICAL_SECTION) ~~> MSet(READY_READ, WRITING)
  ).toSystem

@main def mainRWMutualExclusion =
  import RWMutualExclusion.*
  // example usage
  println(pnRW.paths(MSet(START,START, CRITICAL_SECTION), 5).toList.mkString("\n"))
