package log;

import model.Activity;
import model.DeclareModel;
import model.MixedModel;

import org.deckfour.xes.model.XLog;
import org.deckfour.xes.model.XTrace;

import Automaton.VariableSubstitution;
import translations.PDDLGenerator;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

public class LogFile {
  
  private final ArrayList<Trace> traces;
  
  public LogFile(XLog xLog, DeclareModel declareModel) {
    this.traces = findTraces(xLog, declareModel.getActivities());
  }

  public LogFile(XLog xLog, MixedModel mixedModel) {
    this.traces = findTraces(xLog, mixedModel.activityObjects);
  }
  
  /**
   * Build list of Trace objects from given XLog object
   * @param xlog
   * @param activityMap
   * @return
   */
  public ArrayList<Trace> findTraces(XLog xlog, Map<String, Activity> activityMap) {
    ArrayList<Trace> newTraces = new ArrayList<>();
    if (xlog != null) {
      for (XTrace trace : xlog) {
        newTraces.add(new Trace(trace, activityMap));
      }
    }
    return newTraces;
  }
  
  public ArrayList<String> generateProblems(PDDLGenerator pddlGenerator, Map<String, Integer> assignments, Set<VariableSubstitution> substitutions) {
    ArrayList<String> problems = new ArrayList<>();
    for (Trace trace : traces) {
      problems.add(pddlGenerator.defineProblem(trace.getTrace(), assignments, substitutions));
    }
    return problems;
  }
  
  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder("This log contains " + traces.size() + " traces:\n");
    for (Trace trace : traces) {
      builder.append(trace.toString()).append("\n");
    }
    return builder.toString();
  }
}