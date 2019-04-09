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

    	
    	ArrayList<Point> boxes = new ArrayList<Point>();
    	int totalBoxDist = 0;
    	int totalAgentDist=0;
    	int shortestAgentDist=0;
    	int closestGoalBox = Integer.MAX_VALUE;
    
    	
    	for(int i=0; i<SearchClient.rows; i++){
    		for(int j=0; j<SearchClient.cols; j++){
    			char chr = n.boxes[i][j];
    			if('A' <= chr && chr <= 'Z'){
    				boxes.add(new Point(i,j));
    			}
    		}
    	}
    	
    	for(Point goal : goalLocations){
    		if( n.boxes[goal.x][goal.y]==SearchClient.goals[goal.x][goal.y]){
    			continue;
    		}
    		int closestBoxDist = Integer.MAX_VALUE;
    		for(Point box: boxes){
    			char chr = n.boxes[box.x][box.y];
    			if(SearchClient.goals[goal.x][goal.y]==Character.toLowerCase(chr)){
    				int currentBoxDist = Math.abs(goal.x-box.x) + Math.abs(goal.y-box.y);
    				if(currentBoxDist<closestBoxDist){
    					closestBoxDist=currentBoxDist;
    					int xAgentDist = Math.abs(n.agentRow-box.x);
    					int yAgentDist = Math.abs(n.agentCol-box.y);
    					if(currentBoxDist>0){
        					shortestAgentDist = xAgentDist + yAgentDist;    						
    					}else{
    						shortestAgentDist=0;
    						break;
    					}
    				}
    			}
        		
    		}
    		/*int sumDist = shortestAgentDist;
    		//sumDist=sumDist*2;
    		if(sumDist <closestGoalBox && sumDist>0){
    			closestGoalBox=sumDist;
    			//System.err.println("sumDist: " + sumDist + " and box dist: " + closestBoxDist + " and agent: " + shortestAgentDist);
    		}*/
    		totalAgentDist = totalAgentDist + shortestAgentDist;
    		totalBoxDist =totalBoxDist +closestBoxDist;// + shortestAgentDist;
    	}
    	//System.err.println(n);
    	//System.err.println("\nTotal h(n): " + (totalBoxDist+closestGoalBox));
    	if(closestGoalBox==Integer.MAX_VALUE){
    		closestGoalBox=0;
    	}
    	return totalBoxDist + totalAgentDist+ closestGoalBox;

//    	
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
