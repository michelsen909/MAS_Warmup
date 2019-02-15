package searchclient;

import java.util.ArrayList;
import java.util.Comparator;
import java.awt.Point;

public abstract class Heuristic implements Comparator<State> {
	public ArrayList<Point> goalLocations = new ArrayList<Point>();
	
    public Heuristic(State initialState) {
        // Here's a chance to pre-process the static parts of the level.
    
    for(int i=0; i<=SearchClient.rows-1; i++){
    	for(int j=0; j<=SearchClient.cols-1; j++){
   			char chr = SearchClient.goals[i][j];
   			if('a' <= chr && chr <= 'z'){
   				goalLocations.add(new Point(i,j));
   			}
   		}
   	}
   }

    public int h(State n) {
    	int distSum=0;
    	int shortestAgentDist = Integer.MAX_VALUE;
    	Point agentLoc = new Point(State.agentRow, State.agentCol);
    	//ArrayList<Point> boxLocations = new ArrayList<Point>();
    	for(int i=0; i<=SearchClient.rows-1; i++){
    		for(int j=0; i<=SearchClient.cols-1; i++){
    			char chr = SearchClient.goals[i][j];
    			if('A' <= chr && chr <= 'Z'){
    				Point boxP = new Point(i,j);
    				//boxLocations.add(new Point(i,j));
    				for (Point p: goalLocations) {
    				    if(SearchClient.goals[p.x][p.y]==Character.toLowerCase(chr)){
    				    	int xDist = Math.abs(p.x-boxP.x);
    				    	int yDist = Math.abs(p.y-boxP.y);
    				    	if(xDist!=0 || yDist!=0){
    				    		int xAgentDist = Math.abs(agentLoc.x-boxP.x);
    					    	int yAgentDist = Math.abs(agentLoc.y-boxP.y);
    					    	int agentDist= xAgentDist + yAgentDist;
    					    	
    					    	if(agentDist<shortestAgentDist){
    					    		shortestAgentDist = agentDist;
    					    	}
    					    	distSum = distSum + xDist + yDist;
    				    	}
    				    }
    				}
    			}
    		}
    	}
    	return distSum+shortestAgentDist;
        //throw new NotImplementedException();
    }

    public abstract int f(State n);

    @Override
    public int compare(State n1, State n2) {
        return this.f(n1) - this.f(n2);
    }

    public static class AStar extends Heuristic {
        public AStar(State initialState) {
            super(initialState);
        }

        @Override
        public int f(State n) {
            return n.g() + this.h(n);
        }

        @Override
        public String toString() {
            return "A* evaluation";
        }
    }

    public static class WeightedAStar extends Heuristic {
        private int W;

        public WeightedAStar(State initialState, int W) {
            super(initialState);
            this.W = W;
        }

        @Override
        public int f(State n) {
            return n.g() + this.W * this.h(n);
        }

        @Override
        public String toString() {
            return String.format("WA*(%d) evaluation", this.W);
        }
    }

    public static class Greedy extends Heuristic {
        public Greedy(State initialState) {
            super(initialState);
        }

        @Override
        public int f(State n) {
            return this.h(n);
        }

        @Override
        public String toString() {
            return "Greedy evaluation";
        }
    }
}
