import java.util.ArrayList;
import java.util.HashMap;

public abstract class Minimax<T> {

    int depth;
    T currentState;
    boolean isMaximizer;
    HashMap<T,T> parentMap;

    public Minimax(int depth, T currentState, boolean isMaximizer){
        this.depth = depth;
        this.currentState = currentState;
        this.isMaximizer = isMaximizer;
        this.parentMap = new HashMap<>();
    }

    public abstract boolean isTerminal(T state);
    public abstract int estimate(T state);

    /*TODO in case of maximizer return the states sorted highest to lowest
      TODO and in case of minimizer return the states sorted lowest to highest
      TODO to prune more values earlier.
      TODO [IF PRACTICAL]
    */
    public abstract ArrayList<T> nextStates(T state);

    //TODO print tree in console/GUI
    //TODO return the optimal action or the next optimal state
    private int minimaxHelper(T state, boolean isMaximizer, int depth){
        if(depth == 0 || isTerminal(state)){
            return estimate(state);
        }

        if(isMaximizer){
            int maxValue = Integer.MIN_VALUE;
            for(T nextState: nextStates(state)){
                if(maxValue < minimaxHelper(nextState, false, depth-1)){
                    maxValue = minimaxHelper(nextState, false, depth-1);
                    parentMap.put(state,nextState);
                }
            }
            return maxValue;
        }else{
            int minValue = Integer.MAX_VALUE;
            for(T nextState: nextStates(state)){
                if(minValue > minimaxHelper(nextState, true, depth-1)){
                    minValue = minimaxHelper(nextState, true, depth-1);
                    parentMap.put(state,nextState);
                }
            }
            return minValue;
        }
    }

    public int minimax(){
        return minimaxHelper(currentState, this.isMaximizer, this.depth);
    }

    private int pruningHelper(T state, boolean isMaximizer, int depth, int alpha, int beta){
        //alpha = value assigned to maximizer,
        //beta = value assigned to minimizer

        if(depth == 0 || isTerminal(state)){
            return estimate(state);
        }
        if(isMaximizer){

            int maxVal = Integer.MIN_VALUE;

            for(T nextState: nextStates(state)){
                int val = pruningHelper(nextState, false, depth-1, alpha, beta);
                if(maxVal < val){
                    maxVal = val;
                    parentMap.put(state,nextState);
                }
                alpha = Math.max(alpha, val);

                if(alpha >= beta){break;}
            }
            return maxVal;
        }else{
            int minVal = Integer.MAX_VALUE;
            for(T nextState: nextStates(state)){
                int val = pruningHelper(nextState, true, depth-1, alpha, beta);
                if(minVal > val){
                    minVal = val;
                    parentMap.put(state,nextState);
                }
                beta = Math.min(beta, val);
                if(beta <= alpha){break;}
            }
            return minVal;
        }
    }

    public int minimaxWithPruning(){
        return pruningHelper(
                this.currentState,
                this.isMaximizer,
                this.depth,
                Integer.MIN_VALUE,
                Integer.MAX_VALUE
        );
    }
    // hashmap ( state --> state )
    public abstract void Play();

}

