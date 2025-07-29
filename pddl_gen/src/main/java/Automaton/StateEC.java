package Automaton;


public class StateEC {
  public String name;
  public boolean isInitial;
  public boolean isGoal;
  public boolean isFailure;

  public StateEC(String name) {
    this.name = name;
  }
  
  public StateEC(State s) {
	  this.name = s.name;
	  
	  if (s.isFailure) {
		  this.isFailure = true;
	  }
	  if (s.isGoal) {
		  this.isGoal = true;
	  }
	  if (s.isInitial) {
		  this.isInitial = true;
	  }
  }
  
  public StateEC initial() {
    this.isInitial = true;
    return this;
  }
  public StateEC goal() {
    this.isGoal = true;
    return this;
  }
  public StateEC failure() {
    this.isFailure = true;
    return this;
  }

}
