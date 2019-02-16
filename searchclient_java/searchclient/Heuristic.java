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

    public double h(State n) {
    	//int distSum=0;
    	//int shortestAgentDist = Integer.MAX_VALUE;
    	//Point agentLoc = new Point(n.agentRow, n.agentCol);
    	//ArrayList<Point> boxLocations = new ArrayList<Point>();
    	//System.err.println("at state \n"+ n.toString());
    	
    	ArrayList<Point> boxes = new ArrayList<Point>();
    	double closestBoxDist = Integer.MAX_VALUE;
    	double totalBoxDist = 0;
    	double shortestAgentDist=Integer.MAX_VALUE;
    	double closestGoalBox = Integer.MAX_VALUE;
    	//Point boxPoint = new Point();
    	
    	double totalDist = Integer.MAX_VALUE;
    	
    	for(int i=0; i<SearchClient.rows; i++){
    		for(int j=0; j<SearchClient.cols; j++){
    			char chr = n.boxes[i][j];
    			if('A' <= chr && chr <= 'Z'){
    				boxes.add(new Point(i,j));
    			}
    		}
    	}
    	
    	for(Point goal : goalLocations){
    		for(Point box: boxes){
    			char chr = n.boxes[box.x][box.y];
    			if(SearchClient.goals[goal.x][goal.y]==Character.toLowerCase(chr)){
    				double x = Math.abs(goal.x-box.x);
    				double y = Math.abs(goal.y-box.y);
    				double z = x*x+y*y;
    				double currentBoxDist = Math.sqrt(z);
    				
    				double xAgentDist = Math.abs(n.agentRow-box.x);
    				double yAgentDist = Math.abs(n.agentCol-box.y);
    				double currentAgentDist = Math.sqrt(xAgentDist*xAgentDist + yAgentDist*yAgentDist);
    				
    				double currentTotal = currentBoxDist+currentAgentDist;
    				
    				if(currentTotal<totalDist){
    					totalDist=currentTotal;
    				}
    			}
        		
    		}
    		
    	}
    	return totalDist;

//    	for(int i=0; i<SearchClient.rows; i++){
//    		for(int j=0; j<SearchClient.cols; j++){
//    			char chr = n.boxes[i][j];
//    			if('A' <= chr && chr <= 'Z'){
//    				Point boxP = new Point(i,j);
//    				//boxLocations.add(new Point(i,j));
//
//    				for (Point p: goalLocations) {
//    				    if(SearchClient.goals[p.x][p.y]==Character.toLowerCase(chr)){
//    				    	int xDist = Math.abs(p.x-boxP.x);
//    				    	int yDist = Math.abs(p.y-boxP.y);
//    				    	if(xDist!=0 || yDist!=0){
//    				    		int xAgentDist = Math.abs(agentLoc.x-boxP.x);
//    					    	int yAgentDist = Math.abs(agentLoc.y-boxP.y);
//    					    	int agentDist= xAgentDist + yAgentDist;
//    					    	
//    					    	if(agentDist<shortestAgentDist){
//    					    		shortestAgentDist = agentDist;
//    					    	}
//    					    	distSum = distSum + xDist + yDist;
//    				    	}
//    				    }
//    				}
//    			}
//    		}
//    	}
    	//return distSum+shortestAgentDist;
        //throw new NotImplementedException();
    }

    public abstract double f(State n);

    @Override
    public int compare(State n1, State n2) {
    	if (this.f(n1) < this.f(n2)) return -1;
        if (this.f(n1) > this.f(n2)) return 1;
        return 0;
    	
        //return this.f(n1) - this.f(n2);
    }

    public static class AStar extends Heuristic {
        public AStar(State initialState) {
            super(initialState);
        }

        @Override
        public double f(State n) {
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
        public double f(State n) {
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
        public double f(State n) {
            return this.h(n);
        }

        @Override
        public String toString() {
            return "Greedy evaluation";
        }
    }
}
