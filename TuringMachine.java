import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.HashMap;

public class TuringMachine {
    String tape;
    Map<String, Map<String,List<String>>> transition;
    int head_position = 0;
    String start_state = null;
    String current_state = null;
    String accept_state = "q_accept";
    String reject_state = "q_reject";
    String blank_symbol = "_";

    public TuringMachine(
        String tape, 
        Map<String, Map<String,List<String>>> transition, 
        int head_position, 
        String start_state,
        String accept_state
        ){

        this.tape = tape;
        this.transition = transition;
        this.head_position = head_position;
        this.current_state = start_state;
        this.accept_state = accept_state;

    }
    public static void main(String[] args) throws InterruptedException{
        String tape = "ababaababa";
        Map<String, Map<String,List<String>>> transition = new HashMap<>();
        
        Map<String,List<String>> a = new HashMap<>();
        a.put("a", Arrays.asList("B","_","R"));
        a.put("b", Arrays.asList("E","_","R"));
        a.put("_", Arrays.asList("q_accept","_","L"));
        
        Map<String,List<String>> b = new HashMap<>();
        b.put("a", Arrays.asList("B","a","R"));
        b.put("b", Arrays.asList("B","b","R"));
        b.put("_", Arrays.asList("C","_","L"));
        
        Map<String,List<String>> c = new HashMap<>();
        c.put("a", Arrays.asList("D","_","L"));
        
        Map<String,List<String>> d = new HashMap<>();
        d.put("_", Arrays.asList("A","_","R"));
        d.put("a", Arrays.asList("D","a","L"));
        d.put("b", Arrays.asList("D","b","L"));
        
        Map<String,List<String>> e = new HashMap<>();
        e.put("_", Arrays.asList("F","_","L"));
        e.put("a", Arrays.asList("E","a","R"));
        e.put("b", Arrays.asList("E","b","R"));
        
        Map<String,List<String>> f = new HashMap<>();
        f.put("b", Arrays.asList("G","_","L"));
        
        Map<String,List<String>> g = new HashMap<>();
        g.put("_", Arrays.asList("A","_","R"));
        g.put("a", Arrays.asList("G","a","L"));
        g.put("b", Arrays.asList("G","b","L"));

        transition.put("A", a);
        transition.put("B", b);
        transition.put("C", c);
        transition.put("D", d);
        transition.put("E", e);
        transition.put("F", f);
        transition.put("G", g);
        
        TuringMachine TM = new TuringMachine(tape, transition, 0, "A", "q_accept");
        TM.tapeProcess();

    }

    public List<String> inputProcessing(){
        List<String> inputList = new ArrayList<>(Arrays.asList(this.tape.split("")));
        return inputList;
    }

    public void tapeProcess() throws InterruptedException{
        try{
            List<String> tape = this.inputProcessing();
            
            while (true){
                String symbol = tape.get(this.head_position); // current symbol with tape head

                // new state as key with transition function as value 
                Map<String,List<String>> new_state = this.transition.get(this.current_state);
                List<String> transitionList = new_state.get(symbol);

                this.current_state = transitionList.get(0);
                tape.set(this.head_position, transitionList.get(1));
                if (transitionList.get(2).toUpperCase() == "R"){
                    head_position += 1;
                } else if (transitionList.get(2).toUpperCase() == "L"){
                    head_position -= 1;
                } else{
                    System.out.println("Please input either R for right or L for Left ");
                };
                
                if (head_position >= tape.size()){
                    tape.add(blank_symbol);
                }

                System.out.println(tape);
                System.out.println("Head Position : " + this.head_position);
                System.out.println("Current State : " + this.current_state);
                if (current_state == this.accept_state){
                    System.out.println("Input Accepted\n");
                    break;
                }else if (current_state == reject_state){
                    System.out.println("Input Rejected\n");
                    break;
                }
                else{
                    System.out.println("Running...\n");
                    if (head_position == tape.size() + 1){
                        System.out.println("Your Algorithm cannot Halt...\n");
                        break;
                    }
                }
                // Thread.sleep(300); 
            }
        } catch (NullPointerException e) {
            System.out.println("Input Rejected\n");
        }
    }
}
